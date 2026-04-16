%% ECE Lab 1 Analysis Script - Question 1

clear; clc; close all;

%% ========== Question 1: Basic Signal Processing ==========

% --- Task 1: Load Audio and Basic Properties ---
filename = 'Piano - Lizard 4.wav'; % Using the filename provided in the original script
[y, Fs] = audioread(filename);

% 1. Sampling Rate
fprintf('Q1 Task 1.1: Sampling Rate (Fs) = %d Hz\n', Fs);

% 2. Number of Samples
numSamples = size(y, 1);
fprintf('Q1 Task 1.2: Number of Samples = %d\n', numSamples);

% 3. Length (in seconds)
duration = numSamples / Fs;
fprintf('Q1 Task 1.3: Signal Duration = %.2f seconds\n', duration);

% (Optional: Play the audio)
% disp('Playing audio...');
% soundsc(y, Fs);
% disp('Playback finished.');

% --- Task 2: Plot Stereo Channels ---
timeVector = (0:numSamples-1) / Fs; % Time vector in seconds

figure;
plot(timeVector, y(:, 1)); % Left channel
xlabel('Time (seconds)');
ylabel('Amplitude');
title('Left Channel');
grid on;

figure;
plot(timeVector, y(:, 2)); % Right channel
xlabel('Time (seconds)');
ylabel('Amplitude');
title('Right Channel');
grid on;

% --- Task 3: Count Music Notes ---
% IMPORTANT: Visually inspect the plots from Task 2 to count the distinct notes.
% Record your answer in your lab report.
fprintf('Q1 Task 3: Count the number of notes by visually inspecting the plots.\n');

% --- Task 4: Plot First Music Note ---
% IMPORTANT: Adjust these time values based on visual inspection of the plots!
note1_start_time = 0.5; % Approximate start time of the first note (seconds)
note1_end_time   = 0.8; % Approximate end time of the first note (seconds)

% Convert times to sample indices
note1_start_sample = round(note1_start_time * Fs) + 1; % +1 for 1-based indexing
note1_end_sample   = round(note1_end_time * Fs);

% Ensure indices are within bounds
note1_start_sample = max(1, note1_start_sample);
note1_end_sample   = min(numSamples, note1_end_sample);

% Extract the segment for the first note
note1_segment = y(note1_start_sample:note1_end_sample, :);
note1_timeVector = timeVector(note1_start_sample:note1_end_sample);

% Plot the first note segment
figure;
plot(note1_timeVector, note1_segment(:, 1));
xlabel('Time (seconds)');
ylabel('Amplitude');
title('First Note - Left Channel');
grid on;

figure;
plot(note1_timeVector, note1_segment(:, 2));
xlabel('Time (seconds)');
ylabel('Amplitude');
title('First Note - Right Channel');
grid on;

fprintf('Q1 Task 4: Plotted the segment assumed to be the first note (%f s to %f s).\n', note1_start_time, note1_end_time);

% --- Task 5: Estimate Frequency of First Note using FFT ---
fprintf('\nQ1 Task 5: Estimating frequency of the first note...\n');

% Define signals to analyze
signal_left = note1_segment(:, 1);
signal_right = note1_segment(:, 2);
% signal_avg = mean(note1_segment, 2); % Average of left and right -- REMOVED

% --- DEBUG CODE START ---
% Check dimensions before averaging
% disp(['Size of note1_segment: ', num2str(size(note1_segment))]); % Should be [NumSamples, 2]

% Check dimension after averaging
% disp(['Size of signal_avg: ', num2str(size(signal_avg))]); % Should be [NumSamples, 1]

% Plot to compare time-domain signals
% figure;
% plot(note1_timeVector, signal_left, 'b', 'LineWidth', 0.5);
% hold on;
% plot(note1_timeVector, signal_right, 'r', 'LineWidth', 0.5);
% plot(note1_timeVector, signal_avg, 'k--', 'LineWidth', 1.5); % Plot average as dashed black line -- REMOVED
% legend('Left Channel', 'Right Channel'); % Removed 'Average Channel'
% title('Time Domain Comparison of First Note Segment');
% xlabel('Time (seconds)');
% ylabel('Amplitude');
% grid on;
% hold off;
% --- DEBUG CODE END --- REMOVED DEBUG BLOCK

signals_to_process = {signal_left, signal_right}; % Removed signal_avg
channel_names = {'Left Channel', 'Right Channel'}; % Removed 'Average Channel'

% Loop through each signal (Left, Right)
for i = 1:length(signals_to_process)
    current_signal = signals_to_process{i};
    channel_name = channel_names{i};
    N = length(current_signal);

    fprintf('\n--- Analyzing %s ---\n', channel_name);

    if N == 0
        fprintf('Signal segment is empty. Skipping FFT.\n');
        continue;
    end

    % Compute FFT
    Y = fft(current_signal);

    % Compute the frequency axis
    f_axis = (0:N-1) * (Fs / N);

    % Calculate the single-sided amplitude spectrum
    P2 = abs(Y / N); % Two-sided spectrum
    P1 = P2(1:floor(N/2)+1); % Single-sided spectrum
    P1(2:end-1) = 2 * P1(2:end-1);
    f_axis_single = f_axis(1:floor(N/2)+1);

    % Find the peak frequency
    [peak_amplitude, maxIdx] = max(P1);
    estimated_freq = f_axis_single(maxIdx);

    fprintf('Estimated frequency for %s: %.2f Hz\n', channel_name, estimated_freq);

    % Plot the single-sided amplitude spectrum
    figure;
    plot(f_axis_single, P1);
    title(sprintf('Single-Sided Spectrum - First Note (%s)', channel_name));
    xlabel('Frequency (Hz)');
    ylabel('|P1(f)|');
    grid on;
    hold on;
    plot(estimated_freq, peak_amplitude, 'r*', 'MarkerSize', 10); % Mark the peak
    legend('Spectrum', sprintf('Peak at %.2f Hz', estimated_freq));
    if estimated_freq > 0
      xlim([0, estimated_freq * 2]); % Zoom in around the peak frequency
    else
      xlim([0, Fs/2]); % Show full spectrum if peak is at 0 Hz
    end
    hold off;
end

fprintf('\nExplanation Steps (applied to Left and Right channels):\n');
disp('--- End of Question 1 Script ---'); 