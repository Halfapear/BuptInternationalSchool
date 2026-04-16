param(
    [string]$RepoPath = ".",
    [int]$WarnMB = 20,
    [int]$HardLimitMB = 100,
    [string]$GitIgnorePath = ".gitignore",
    [string]$ReportPath = "docs/large-files-report.md"
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

$root = (Resolve-Path -LiteralPath $RepoPath).Path
$ignoreFile = Join-Path $root $GitIgnorePath
$reportFile = Join-Path $root $ReportPath

if (-not (Test-Path -LiteralPath $ignoreFile)) {
    New-Item -ItemType File -Path $ignoreFile -Force | Out-Null
}

$files = Get-ChildItem -LiteralPath $root -Recurse -File -Force |
    Where-Object { $_.FullName -notmatch "[\\/]\.git([\\/]|$)" }

$allRows = foreach ($f in $files) {
    $rel = $f.FullName.Substring($root.Length).TrimStart('\').Replace('\', '/')
    [PSCustomObject]@{
        RelativePath = $rel
        SizeMB = [math]::Round($f.Length / 1MB, 2)
    }
}

$warnRows = $allRows | Where-Object { $_.SizeMB -gt $WarnMB } | Sort-Object SizeMB -Descending
$hardRows = $allRows | Where-Object { $_.SizeMB -gt $HardLimitMB } | Sort-Object SizeMB -Descending

$beginMarker = "# BEGIN_LARGE_FILES"
$endMarker = "# END_LARGE_FILES"

$ignoreContent = Get-Content -LiteralPath $ignoreFile -Raw -Encoding UTF8
$managedBlock = @(
    $beginMarker
    ($hardRows | ForEach-Object { $_.RelativePath })
    $endMarker
) -join "`r`n"

if ($ignoreContent -match [regex]::Escape($beginMarker) -and $ignoreContent -match [regex]::Escape($endMarker)) {
    $pattern = "(?s)" + [regex]::Escape($beginMarker) + ".*?" + [regex]::Escape($endMarker)
    $ignoreContent = [regex]::Replace($ignoreContent, $pattern, [System.Text.RegularExpressions.MatchEvaluator]{ param($m) $managedBlock })
} else {
    if (-not $ignoreContent.EndsWith("`r`n")) {
        $ignoreContent += "`r`n"
    }
    $ignoreContent += "`r`n# AUTO-MANAGED: files above $HardLimitMB MB`r`n"
    $ignoreContent += $managedBlock
    $ignoreContent += "`r`n"
}

Set-Content -LiteralPath $ignoreFile -Value $ignoreContent -Encoding UTF8

$reportDir = Split-Path -Parent $reportFile
if (-not (Test-Path -LiteralPath $reportDir)) {
    New-Item -ItemType Directory -Path $reportDir -Force | Out-Null
}

$totalSize = [math]::Round((($allRows | Measure-Object -Property SizeMB -Sum).Sum), 2)
$warnSize = [math]::Round((($warnRows | Measure-Object -Property SizeMB -Sum).Sum), 2)
$hardSize = [math]::Round((($hardRows | Measure-Object -Property SizeMB -Sum).Sum), 2)

$topWarn = $warnRows | Select-Object -First 20
$reportLines = @()
$reportLines += "# Large Files Report"
$reportLines += ""
$reportLines += "Generated: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')"
$reportLines += ""
$reportLines += "- Total files scanned: $($allRows.Count)"
$reportLines += "- Total size (MB): $totalSize"
$reportLines += "- > $WarnMB MB files: $($warnRows.Count) (MB: $warnSize)"
$reportLines += "- > $HardLimitMB MB files: $($hardRows.Count) (MB: $hardSize)"
$reportLines += ""
$reportLines += "## Top files over $WarnMB MB"
$reportLines += ""
$reportLines += "| Size (MB) | Path |"
$reportLines += "|---:|---|"

foreach ($row in $topWarn) {
    $reportLines += "| $($row.SizeMB) | $($row.RelativePath) |"
}

if ($topWarn.Count -eq 0) {
    $reportLines += "| 0 | (none) |"
}

Set-Content -LiteralPath $reportFile -Value ($reportLines -join "`r`n") -Encoding UTF8

Write-Output "Updated $GitIgnorePath with $($hardRows.Count) large-file entries (> $HardLimitMB MB)."
Write-Output "Wrote report to $ReportPath."