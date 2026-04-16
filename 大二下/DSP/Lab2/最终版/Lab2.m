% EBU5376 Digital Signal Processing - Lab 2
% Student Name: Zhaobai Jiang
% BUPT ID: 2023213540


%% Question 1 - Filter Response Analysis

% Clear workspace, command window, and close all figures
clear;
clc;
close all;

% --- Task 1: Identify FIR and IIR filters ---

% Load the filter coefficients from the .mat file.
% This file contains 'a' (denominator) and 'b' (numerator) coefficient vectors
% for four different filters (a1, b1, a2, b2, etc.).
try
    load('filter_coefficients.mat'); 
    disp('Successfully loaded Lab2_coeffs.mat');
catch
    disp('Error: Lab2_coeffs.mat not found. Please ensure the file is in the MATLAB path.');
    return; % Stop execution if file is not found
end

% Display the denominator 'a' coefficients for each filter.
% The structure of the 'a' vector determines if a filter is FIR or IIR.
% - For an FIR filter, the 'a' vector is typically [1] or [a0], meaning
%   the denominator A(z) of the transfer function H(z) = B(z)/A(z) is constant.
%   Effectively, a_k = 0 for k > 0, or length(a) = 1.
% - For an IIR filter, the 'a' vector has non-zero elements for k > 0,
%   indicating feedback and a non-trivial denominator polynomial A(z).
%   This means length(a) > 1 and at least one a_k (k>0) is non-zero.

fprintf('\n--- Denominator Coefficients (''a'' vectors) for Filter Classification ---\n');
fprintf('a1: '); disp(a1);
fprintf('a2: '); disp(a2);
fprintf('a3: '); disp(a3);
fprintf('a4: '); disp(a4);

% Classification based on 'a' vectors (to be done by student based on output):
% Filter 1: If length(a1) == 1 || all(a1(2:end) == 0), it is FIR. Else IIR.
% Filter 2: If length(a2) == 1 || all(a2(2:end) == 0), it is FIR. Else IIR.
% Filter 3: If length(a3) == 1 || all(a3(2:end) == 0), it is FIR. Else IIR.
% Filter 4: If length(a4) == 1 || all(a4(2:end) == 0), it is FIR. Else IIR.

%% --- Task 2: Plot the step response of all filters ---

% Define the number of samples for the step response
N_samples_step = 200; % Adjust if needed to see the full response behavior

% Calculate the step response for each filter
% stepz(b, a, n) calculates the step response for n samples.
% It returns the response y and the time vector t.
[h_step1, t_step1] = stepz(b1, a1, N_samples_step);
[h_step2, t_step2] = stepz(b2, a2, N_samples_step);
[h_step3, t_step3] = stepz(b3, a3, N_samples_step);
[h_step4, t_step4] = stepz(b4, a4, N_samples_step);

% Plot the step responses on the same figure
figure; % Create a new figure window
plot(t_step1, h_step1, 'DisplayName', 'Filter 1 (IIR)');
hold on; % Hold the current plot to add more lines
plot(t_step2, h_step2, 'DisplayName', 'Filter 2 (IIR)');
plot(t_step3, h_step3, 'DisplayName', 'Filter 3 (IIR)');
plot(t_step4, h_step4, 'DisplayName', 'Filter 4 (FIR)');
hold off; % Release the hold

% Add plot details
title('Step Response of Filters');
xlabel('Sample Number (n)');
ylabel('Amplitude');
legend('show', 'Location', 'southeast'); % Display legend
grid on; % Add a grid for better readability

%% --- Task 3: Plot the amplitude response (linear scale) ---

% Define the number of frequency points for freqz
N_freq_points = 1024; % More points give a smoother curve

% Calculate the frequency response for each filter
% freqz(b, a, n_points) returns the complex frequency response H
% and the angular frequency vector w (from 0 to pi).
[H1, w1] = freqz(b1, a1, N_freq_points);
[H2, w2] = freqz(b2, a2, N_freq_points);
[H3, w3] = freqz(b3, a3, N_freq_points);
[H4, w4] = freqz(b4, a4, N_freq_points);

% Plot the amplitude responses (linear scale) on the same figure
figure; % Create a new figure window
plot(w1/pi, abs(H1), 'LineWidth', 1.5, 'DisplayName', 'Filter 1 (IIR)'); % Normalize frequency to x pi rad/sample
hold on;
plot(w2/pi, abs(H2), 'LineWidth', 1.5, 'DisplayName', 'Filter 2 (IIR)');
plot(w3/pi, abs(H3), 'LineWidth', 1.5, 'DisplayName', 'Filter 3 (IIR)');
plot(w4/pi, abs(H4), 'LineWidth', 1.5, 'DisplayName', 'Filter 4 (FIR)');
hold off;

% Add plot details
title('Amplitude Response of Filters (Linear Scale)');
xlabel('Normalized Frequency (\times\pi rad/sample)');
ylabel('Magnitude |H(e^{j\omega})|');
legend('show', 'Location', 'best');
grid on;
ylim([-0.1 1.2]); % Adjust y-axis if necessary, typically 0 to 1 for normalized filters



%% --- Task 4: Plot the amplitude response (decibel scale) ---

% Ensure H1, H2, H3, H4 and w1, w2, w3, w4 from Task 3 are available.
% We will derive the absolute magnitudes and normalized plot frequencies here.

% Calculate absolute magnitudes from Task 3's H vectors
absH1 = abs(H1); % Assuming H1, H2, H3, H4 are the complex freq responses from freqz
absH2 = abs(H2);
absH3 = abs(H3);
absH4 = abs(H4);

% Create normalized frequency vectors for plotting (if not already globally available in this form)
% Task 3 used w1/pi directly in plot commands, let's define them for clarity here
w_plot1 = w1/pi; % w1, w2, w3, w4 are angular frequencies from freqz (0 to pi)
w_plot2 = w2/pi;
w_plot3 = w3/pi;
w_plot4 = w4/pi;

% Convert magnitudes to dB
% Adding a small epsilon to avoid log10(0) if any magnitude is exactly zero, 
% though freqz output usually doesn't have exact zeros unless specifically designed.
epsilon = 1e-12; % A very small number
absH1_dB = 20*log10(absH1 + epsilon);
absH2_dB = 20*log10(absH2 + epsilon);
absH3_dB = 20*log10(absH3 + epsilon);
absH4_dB = 20*log10(absH4 + epsilon);

% Plot the amplitude responses (dB scale) on the same figure
figure; % Create a new figure for Task 4
plot(w_plot1, absH1_dB, 'LineWidth', 1.5, 'DisplayName', 'Filter 1 (IIR)');
hold on;
plot(w_plot2, absH2_dB, 'LineWidth', 1.5, 'DisplayName', 'Filter 2 (IIR)');
plot(w_plot3, absH3_dB, 'LineWidth', 1.5, 'DisplayName', 'Filter 3 (IIR)');
plot(w_plot4, absH4_dB, 'LineWidth', 1.5, 'DisplayName', 'Filter 4 (FIR)');
hold off;

% Add plot details
title('Amplitude Response of Filters (Decibel Scale)');
xlabel('Normalized Frequency (\times\pi rad/sample)');
ylabel('Magnitude (dB)');
legend('show', 'Location', 'southwest'); 
grid on;
ylim([-100 5]); % This matches your PDF figure's y-axis for consistency

% NOTE: The original fprintf loop for general feature extraction that was here
% (starting with "fprintf('\n--- Amplitude Response Characteristics (dB Scale) ---\n');")
% has been removed as per discussion, to keep the script focused on core requirements.
% The analysis in the lab sheet will refer to observations from the plot,
% possibly aided by the data cursor tool.

%% --- Task 5: Graphical and Programmatic Demonstration of -3dB Cut-off Frequency Estimation ---
% This section graphically illustrates and programmatically estimates the 
% -3dB cut-off frequencies. This supports the explanation in the Lab Sheet for Task 5,
% fulfilling the "may want to include figures or code descriptions" requirement.

fprintf('\n\n--- Task 5: Graphical and Programmatic Estimation of -3dB Cut-off Frequencies ---\n');

% Filter names for clear output and plot legends
filter_names_task5 = {'Filter 1 (IIR)', 'Filter 2 (IIR)', 'Filter 3 (IIR)', 'Filter 4 (FIR)'};
filter_colors = {'b', 'r', 'm', 'g'}; % Assign colors for consistency if needed, or use default plot colors

% Use the dB magnitude responses and normalized frequency vectors from Task 4
H_dB_all_task5 = {absH1_dB, absH2_dB, absH3_dB, absH4_dB}; 
w_plot_all_task5 = {w_plot1, w_plot2, w_plot3, w_plot4}; % These are w/pi

% --- Plot 1: Overall dB response with -3dB points marked ---
figure; % New figure for combined plot with markers
hold on;
cutoff_freq_values_wc_pi = zeros(1, length(filter_names_task5)); % To store wc/pi values

for i = 1:length(filter_names_task5)
    current_H_dB = H_dB_all_task5{i};
    current_w_norm = w_plot_all_task5{i};
    
    % Plot the full response for this filter
    plot(current_w_norm, current_H_dB, 'LineWidth', 1.5, 'DisplayName', sprintf('%s', filter_names_task5{i}));
    
    if isempty(current_H_dB) || isempty(current_w_norm)
        fprintf('  Error: Data for %s is empty. Skipping cut-off estimation.\n', filter_names_task5{i});
        cutoff_freq_values_wc_pi(i) = NaN;
        continue;
    end
    
    dc_gain_dB_task5 = current_H_dB(1);
    target_3dB_level = dc_gain_dB_task5 - 3;
    idx_3dB = find(current_H_dB <= target_3dB_level, 1, 'first');
    
    if ~isempty(idx_3dB) && idx_3dB > 1
        cutoff_freq_norm_task5 = current_w_norm(idx_3dB);
        gain_at_cutoff_task5 = current_H_dB(idx_3dB);
        cutoff_freq_values_wc_pi(i) = cutoff_freq_norm_task5; % Store it
        
        % Mark the -3dB point on the plot
        plot(cutoff_freq_norm_task5, gain_at_cutoff_task5, 'o', ...
             'MarkerFaceColor', 'r', 'MarkerEdgeColor', 'k', 'MarkerSize', 8, ...
             'HandleVisibility', 'off'); % HandleVisibility off so it doesn't appear in legend separately
        % Add a vertical line for the cutoff frequency
        line([cutoff_freq_norm_task5, cutoff_freq_norm_task5], get(gca, 'YLim'), ...
             'Color', 'k', 'LineStyle', ':', 'LineWidth', 1, 'HandleVisibility', 'off');
        text(cutoff_freq_norm_task5 + 0.01, gain_at_cutoff_task5 - 5, ... % Adjust text position
             sprintf('fc=%.3f\\pi', cutoff_freq_norm_task5), 'Color', 'k', 'FontSize', 8);
    else
        cutoff_freq_values_wc_pi(i) = NaN;
    end
end
hold off;
title('Task 5: Overall Amplitude Response (dB) with -3dB Cut-off Points Marked');
xlabel('Normalized Frequency (\times\pi rad/sample)');
ylabel('Magnitude (dB)');
legend('show', 'Location', 'southwest');
grid on;
ylim([-100 5]); % Consistent with Task 4 plot

fprintf('\nProgrammatically Estimated -3dB Cut-off Frequencies (from overall plot marking logic):\n');
for i = 1:length(filter_names_task5)
    if ~isnan(cutoff_freq_values_wc_pi(i))
        fprintf('  %s: %.4f pi rad/sample\n', filter_names_task5{i}, cutoff_freq_values_wc_pi(i));
    else
        fprintf('  %s: Cut-off not found.\n', filter_names_task5{i});
    end
end

% --- Plot 2: Individual zoomed-in plots for each filter's -3dB point ---
for i = 1:length(filter_names_task5)
    figure; % New figure for each filter's zoomed plot
    current_H_dB = H_dB_all_task5{i};
    current_w_norm = w_plot_all_task5{i};
    wc_pi = cutoff_freq_values_wc_pi(i); % Get the stored wc/pi

    if isempty(current_H_dB) || isempty(current_w_norm) || isnan(wc_pi)
        title(sprintf('Task 5: %s - Data or Cut-off Not Available', filter_names_task5{i}));
        continue; % Skip if no data or no cutoff
    end
    
    dc_gain_dB_task5 = current_H_dB(1);
    target_3dB_level = dc_gain_dB_task5 - 3;
    idx_3dB = find(current_H_dB <= target_3dB_level, 1, 'first'); % Re-find for safety, or use stored
    gain_at_cutoff_task5 = current_H_dB(idx_3dB);

    plot(current_w_norm, current_H_dB, 'LineWidth', 2, 'DisplayName', 'Amplitude Response');
    hold on;
    
    % Mark the -3dB point
    plot(wc_pi, gain_at_cutoff_task5, 'ro', 'MarkerFaceColor', 'r', 'MarkerSize', 8, ...
         'DisplayName', sprintf('-3dB Point (%.2f dB)', gain_at_cutoff_task5));
    
    % Horizontal line at -3dB target level
    line(get(gca, 'XLim'), [target_3dB_level, target_3dB_level], 'Color', 'k', 'LineStyle', '--', ...
         'DisplayName', sprintf('Target -3dB Level (%.2f dB)',target_3dB_level));
         
    % Vertical line at cutoff frequency
    line([wc_pi, wc_pi], get(gca, 'YLim'), 'Color', 'k', 'LineStyle', ':', ...
         'DisplayName', sprintf('Cut-off Freq. %.3f\\pi', wc_pi));
    
    hold off;
    title(sprintf('Task 5: Zoomed View of -3dB Cut-off for %s', filter_names_task5{i}));
    xlabel('Normalized Frequency (\times\pi rad/sample)');
    ylabel('Magnitude (dB)');
    legend('show', 'Location', 'best');
    grid on;
    
    % Zoom into the relevant area around the cutoff frequency
    % Define a window around the cutoff, e.g., wc +/- 0.1*pi or a fixed small range
    zoom_window_half_width = min(0.1, wc_pi * 0.5); % Adjust as needed
    xlim_lower = max(0, wc_pi - zoom_window_half_width);
    xlim_upper = min(1, wc_pi + zoom_window_half_width);
    if xlim_lower >= xlim_upper % Handle edge case if window is too small/invalid
        xlim_lower = max(0, wc_pi - 0.05);
        xlim_upper = min(1, wc_pi + 0.05);
    end
    xlim([xlim_lower xlim_upper]);
    
    % Adjust YLim to focus on the -3dB region
    ylim_lower_zoom = min(current_H_dB(current_w_norm >= xlim_lower & current_w_norm <= xlim_upper));
    ylim_upper_zoom = max(current_H_dB(current_w_norm >= xlim_lower & current_w_norm <= xlim_upper));
    ylim_padding = 5; % dB padding
    ylim([min(ylim_lower_zoom, target_3dB_level) - ylim_padding, max(ylim_upper_zoom, dc_gain_dB_task5) + ylim_padding/5 ]);


    fprintf('\nFor %s (Zoomed View Analysis):\n', filter_names_task5{i});
    fprintf('  DC Gain: %.2f dB\n', dc_gain_dB_task5);
    fprintf('  Target -3dB level: %.2f dB\n', target_3dB_level);
    fprintf('  Identified point near -3dB: Freq=%.4f pi, Gain=%.2f dB\n', wc_pi, gain_at_cutoff_task5);
end

fprintf('\n--- End of Task 5 Graphical and Programmatic Demonstration ---\n');