%% ECE Lab 1 Analysis Script - Question 2

clear; clc; close all;

%% Load Audio Data (same as Q1)
filename = 'Piano - Lizard 4.wav'; 
[y, Fs] = audioread(filename);
numSamples = size(y, 1);
timeVector = (0:numSamples-1) / Fs;
soundsc(y,Fs);
%% ========== Question 2: Advanced Frequency Analysis ==========

% --- Task 1: Estimate Frequencies of All Notes ---
fprintf('Q2 Task 1: Estimating frequencies of all notes...\n');

% IMPORTANT: Define the approximate start and end times for ALL notes
% based on visual inspection of the plots from Q1 Task 2.
% Add/remove/modify rows as needed. Format: [start_time, end_time]
note_times = [
    0.55, 0.75;  % Note 1 (Adjust from Q1 if necessary)
    0.8, 1.0;  % Note 2 (Placeholder - ADJUST BASED ON YOUR PLOT)
    1.1, 1.3 ;  % Note 3 (Placeholder - ADJUST BASED ON YOUR PLOT)
    1.4, 1.6;   % Note 4 (Placeholder - ADJUST BASED ON YOUR PLOT)
    1.65, 1.85;
    1.9, 2.1;
    2.2,2.4;
    2.45,2.65

    % Add more rows if you found more notes...
];

num_notes = size(note_times, 1);
estimated_frequencies = zeros(num_notes, 1);

fprintf('\n--- Estimated Frequencies ---\n');
for i = 1:num_notes
    start_time = note_times(i, 1);
    end_time   = note_times(i, 2);

    % Convert times to sample indices
    start_sample = max(1, round(start_time * Fs) + 1);
    end_sample   = min(numSamples, round(end_time * Fs));

    % Extract segment (using left channel - can change to y(:, 2) for right)
    note_segment = y(start_sample:end_sample, 1);
    N_note = length(note_segment);

    if N_note < 2 % Check if segment is too short
        fprintf('Warning: Note %d segment (%f s - %f s) is too short or empty. Skipping.\n', i, start_time, end_time);
        estimated_frequencies(i) = NaN; % Mark as Not a Number
        continue;
    end

    % Compute FFT
    Y_note = fft(note_segment);
    f_axis_note = (0:N_note-1) * (Fs / N_note);

    % Single-sided spectrum
    P2_note = abs(Y_note / N_note);
    P1_note = P2_note(1:floor(N_note/2)+1);
    P1_note(2:end-1) = 2 * P1_note(2:end-1);
    f_axis_single_note = f_axis_note(1:floor(N_note/2)+1);

    % Find peak frequency
    [~, maxIdx] = max(P1_note);
    estimated_frequencies(i) = f_axis_single_note(maxIdx);

    fprintf('Note %d (%f s - %f s): %.2f Hz\n', i, start_time, end_time, estimated_frequencies(i));
end

disp('Summary of Estimated Frequencies (Hz):');
disp(estimated_frequencies);



% --- Task 3: Impact of Window Duration ---
fprintf('\nQ2 Task 3: Analyzing the Impact of Window Duration\n');
fprintf('--------------------------------------------------\n');

% Choose one note to analyze (e.g., the first note, index 1)
note_to_analyze_idx = 1;
if note_to_analyze_idx > num_notes || isnan(estimated_frequencies(note_to_analyze_idx))
    fprintf('Selected note index %d for analysis is invalid or was skipped. Please choose a valid note.\n', note_to_analyze_idx);
else
    center_time = mean(note_times(note_to_analyze_idx, :)); % Midpoint of the chosen note's window

    % Define different window durations to test (in seconds)
    % IMPORTANT: Adjust these durations based on your note lengths.
    % Should include durations shorter and longer than the original estimate.
    window_durations = [0.05, 0.1, 0.2, 0.3, 0.4, 0.6]; % Example durations

    figure; % Create a figure to hold the spectra for comparison
    hold on;
    plot_colors = lines(length(window_durations)); % Get distinct colors
    legend_entries = cell(length(window_durations), 1);
    peak_freqs_window_test = zeros(length(window_durations), 1);
    freq_resolutions = zeros(length(window_durations), 1);

    fprintf('Analyzing Note %d (Original Window: %.2f s - %.2f s) centered around %.2f s\n', ...
            note_to_analyze_idx, note_times(note_to_analyze_idx, 1), note_times(note_to_analyze_idx, 2), center_time);
    fprintf('%-15s %-10s %-15s %-20s\n', 'Window Dur (s)', 'N Samples', 'Peak Freq (Hz)', 'Freq Res (Fs/N Hz)');

    valid_plot_count = 0;
    for k = 1:length(window_durations)
        duration_k = window_durations(k);
        start_time_k = center_time - duration_k / 2;
        end_time_k   = center_time + duration_k / 2;

        % Convert times to sample indices
        start_sample_k = max(1, round(start_time_k * Fs) + 1);
        end_sample_k   = min(numSamples, round(end_time_k * Fs));

        % Extract segment (using left channel)
        segment_k = y(start_sample_k:end_sample_k, 1);
        N_k = length(segment_k);

        if N_k < 2 % Need at least 2 points for FFT
           fprintf('Window duration %.2f s is too short or outside signal bounds. Skipping.\n', duration_k);
           peak_freqs_window_test(k) = NaN;
           freq_resolutions(k) = NaN;
           legend_entries{k} = sprintf('Window = %.2f s (Skipped)', duration_k);
           continue; % Skip to next duration
        end

        valid_plot_count = valid_plot_count + 1;

        % Compute FFT
        Y_k = fft(segment_k);
        f_axis_k = (0:N_k-1) * (Fs / N_k);

        % Single-sided spectrum
        P2_k = abs(Y_k / N_k);
        P1_k = P2_k(1:floor(N_k/2)+1);
        P1_k(2:end-1) = 2 * P1_k(2:end-1);
        f_axis_single_k = f_axis_k(1:floor(N_k/2)+1);

        % Find peak frequency
        [~, maxIdx_k] = max(P1_k);
        peak_freqs_window_test(k) = f_axis_single_k(maxIdx_k);
        freq_resolutions(k) = Fs / N_k;

        % Plot the spectrum
        plot(f_axis_single_k, P1_k, 'Color', plot_colors(k,:), 'LineWidth', 1.5);
        legend_entries{k} = sprintf('Win=%.2fs (N=%d, Peak=%.1fHz, Res=%.1fHz)', ...
                                 duration_k, N_k, peak_freqs_window_test(k), freq_resolutions(k));

        fprintf('%-15.3f %-10d %-15.2f %-20.2f\n', ...
                duration_k, N_k, peak_freqs_window_test(k), freq_resolutions(k));
    end

    title(sprintf('FFT Spectrum vs. Window Duration (Note %d)', note_to_analyze_idx));
    xlabel('Frequency (Hz)');
    ylabel('|P1(f)|');
    grid on;
    legend(legend_entries(1:valid_plot_count), 'Location', 'best'); % Only include legends for plotted spectra
    % Adjust xlim to zoom in on the relevant frequency range around the expected peak
    valid_peaks = peak_freqs_window_test(~isnan(peak_freqs_window_test));
    if ~isempty(valid_peaks)
        mean_peak = mean(valid_peaks);
        xlim_range = [max(0, mean_peak - 100), mean_peak + 100]; % Adjust range (e.g., +/- 100 Hz) as needed
        xlim(xlim_range);
    end
    hold off;
end

disp('\n--- End of Question 2 Script ---'); 