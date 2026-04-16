% Hey there, dear Assessor! This is my original Lab 1 script, I added the fprintf/error checks, and it covers everything required.


% =========================================================================
% Question 1 - Task 1: Basic Signal Processing - Read Audio and Get Properties
%
% This section reads the audio file and extracts its fundamental properties:
% sampling rate, number of samples, and signal duration.
% The results are printed to the Command Window and should be copied into
% the Lab Sheet text box for Q1 Task 1.
% =========================================================================

% Specify the audio file name.
audioFileName = 'Piano - Lizard 4.wav';

% Use the audioread function to read the audio file.
% audioData: Matrix containing audio samples [numberOfSamples, numberOfChannels].
% fs: Sampling frequency in Hertz (Hz).
[audioData, fs] = audioread(audioFileName);

% --- Answering Lab Sheet Questions for Task 1 ---

% Question 1. What is the sampling rate of this signal?
% The variable 'fs' directly contains the sampling rate.
fprintf('---------------------------------------------------\n');
fprintf('Q1 Task 1 Results:\n');
fprintf('---------------------------------------------------\n');
fprintf('1. The sampling rate is: %d Hz\n', fs); % Print sampling rate.

% Question 2. How many samples are there in this signal?
% The number of samples is the number of rows in the audioData matrix.
numberOfSamples = size(audioData, 1); % Get the number of rows.
fprintf('2. The number of samples is: %d\n', numberOfSamples); % Print number of samples.

% Question 3. What is the length (in seconds) of this signal?
% Duration is calculated by dividing the total number of samples by the sampling rate.
signalDuration = numberOfSamples / fs; % Calculate duration.
fprintf('3. The length of the signal is: %.2f seconds\n', signalDuration); % Print duration (formatted to 2 decimal places).
fprintf('---------------------------------------------------\n');

% Optional: Play the audio (as mentioned in Lab Sheet Task 1)
% Uncomment the following line to play the audio. soundsc scales the volume automatically.
soundsc(audioData, fs); % Use soundsc for scaled playback.
% Or use sound(audioData * 0.5, fs); for manual volume control (e.g., half volume).


% =========================================================================
% Proceed to Question 1 - Task 2 code below...
% =========================================================================


% =========================================================================
% Question 1 - Task 2: Plotting Left and Right Channels
%
% This part plots the left and right channels of the audio signal
% on two separate figures, with the time axis in seconds.
% =========================================================================

% --- Prepare data for plotting ---

% Get the number of samples from audioData (calculated in Task 1)
% numberOfSamples = size(audioData, 1); % Already have this from Task 1

% Create the time vector for the horizontal axis.
% It starts at 0, increments by 1/fs for each sample, up to (numberOfSamples-1)/fs.
time_sec = (0:numberOfSamples-1) / fs;

% Extract the left channel (first column of audioData)
leftChannel = audioData(:, 1);

% Extract the right channel (second column of audioData)
rightChannel = audioData(:, 2);

% --- Plotting ---

% Plot the Left Channel (Figure 1)
figure; % *** Create a NEW figure window for the left channel ***
plot(time_sec, leftChannel); % Plot time vs amplitude
title('Audio Signal - Left Channel'); % Set title
xlabel('Time (seconds)'); % Label x-axis
ylabel('Amplitude'); % Label y-axis
grid on; % Optional: Add a grid for better readability

% Plot the Right Channel (Figure 2)
figure; % *** Create another NEW figure window for the right channel ***
plot(time_sec, rightChannel); % Plot time vs amplitude
title('Audio Signal - Right Channel'); % Set title
xlabel('Time (seconds)'); % Label x-axis
ylabel('Amplitude'); % Label y-axis
grid on; % Optional: Add a grid



% =========================================================================
% Question 1 - Task 4: Plotting the First Music Note
%
% This section extracts and plots the first music note segment.
% The start and end times need to be manually determined by inspecting
% the full audio waveform plot from Task 2.
% Two separate figures are generated for the left and right channels
% of this first note segment, with time axes in seconds.
% These figures should be copied and pasted into the Lab Sheet document.
% =========================================================================

% --- Define the time segment for the first note ---

% These are the estimated start and end times of the FIRST NOTE in seconds.
firstNote_startTime_sec = 0.55; 
firstNote_endTime_sec = 0.82; 

% Convert time in seconds to sample indices (using 1-based indexing for MATLAB).
% Ensure indices are within the valid bounds of the audio data.
firstNote_startIndex = round(firstNote_startTime_sec * fs) + 1;
firstNote_endIndex = round(firstNote_endTime_sec * fs) + 1;

% Clamp indices to be within the bounds of the audioData array
firstNote_startIndex = max(1, firstNote_startIndex);
firstNote_endIndex = min(size(audioData, 1), firstNote_endIndex); % Use size(audioData, 1) for total samples

% Validate that the end index is after the start index.
if firstNote_startIndex >= firstNote_endIndex
    error('Q1 Task 4: Calculated first note end index (%d) is not after start index (%d). Invalid time boundaries.', firstNote_endIndex, firstNote_startIndex);
end

% --- Extract the first note segment ---
% Select rows from startIndex to endIndex for all columns (channels).
firstNote_audioData = audioData(firstNote_startIndex:firstNote_endIndex, :);

% --- Create the corresponding time vector for the segment ---
% The time points for the extracted segment correspond to the global time
% of the samples from firstNote_startIndex to firstNote_endIndex.
time_sec_firstNote = (firstNote_startIndex-1 : firstNote_endIndex-1) / fs;

% --- Plotting the first note segment ---

% Plot the Left Channel of the first note segment.
figure; % Create a new figure window.
plot(time_sec_firstNote, firstNote_audioData(:, 1)); % Plot time vs amplitude.
title('First Music Note - Left Channel'); % Set plot title.
xlabel('Time (seconds)'); % Set x-axis label.
ylabel('Amplitude'); % Set y-axis label.
grid on; % Add grid for readability.

% Plot the Right Channel of the first note segment.
figure; % Create another new figure window for the right channel.
plot(time_sec_firstNote, firstNote_audioData(:, 2)); % Plot time vs amplitude.
title('First Music Note - Right Channel'); % Set plot title.
xlabel('Time (seconds)'); % Set x-axis label.
ylabel('Amplitude'); % Set y-axis label.
grid on; % Add grid for readability.

% =========================================================================
% Question 1 - Task 5: Frequency Estimation of the First Note using FFT
%
% This section estimates the fundamental frequency of the first music note.
% It performs the Fast Fourier Transform (FFT) on one channel of the
% first note segment extracted in Task 4, calculates the magnitude spectrum,
% and finds the frequency corresponding to the highest peak (excluding DC).
% A plot of the single-sided magnitude spectrum is generated.
% The estimated frequency is printed to the Command Window.
% =========================================================================

% --- Prepare data for FFT ---

% Select one channel of the first note segment for FFT analysis.
% Using the left channel (first column of firstNote_audioData).
firstNote_segment_for_fft = firstNote_audioData(:, 1);

% Get the number of samples in this segment. This length (L) is crucial for FFT calculations.
L = length(firstNote_segment_for_fft);

% --- Perform FFT ---

% Compute the N-point Discrete Fourier Transform (DFT) using the FFT algorithm.
% By default, fft(x) computes the L-point DFT where L is the length of x.
% The result Y is a complex vector of length L.
Y = fft(firstNote_segment_for_fft);

% --- Calculate the Single-Sided Magnitude Spectrum and Frequency Axis ---

% Compute the two-sided spectrum magnitude, normalized by the segment length L.
P2 = abs(Y/L);

% Compute the single-sided spectrum P1. For a real-valued input signal,
% the spectrum is symmetric, so we only need the first half (including 0 Hz and Nyquist).
P1 = P2(1:floor(L/2)+1);

% For the single-sided spectrum, the amplitudes of all frequencies except 0 Hz (DC)
% and the Nyquist frequency (if present, i.e., if L is even) are doubled
% to account for the energy from the negative frequencies.
P1(2:end-1) = 2*P1(2:end-1);

% Create the frequency axis 'f' for the single-sided spectrum.
% The frequency resolution is df = fs/L.
% The frequency points are 0, df, 2*df, ..., floor(L/2)*df.
f = (0:floor(L/2)) * (fs/L);

% --- Find the Peak Frequency ---
% The fundamental frequency corresponds to the highest peak in the magnitude spectrum.
% We search for the maximum magnitude in P1, excluding the DC component at f=0 Hz (P1(1)).
[peak_magnitude, peak_index_relative] = max(P1(2:end)); % Find max magnitude and its index in P1 from the 2nd element

% The index returned by max() is relative to P1(2:end).
% The actual index in the full P1 array and the frequency axis 'f' is relative_index + 1.
peak_actual_index = peak_index_relative + 1;

% The estimated frequency is the frequency value from the 'f' axis at the peak index.
estimated_frequency_Hz = f(peak_actual_index);

% --- Plot the Single-Sided Magnitude Spectrum ---
% This plot helps visualize the spectrum and the identified peak.
figure; % Create a new figure window for the spectrum.
plot(f, P1); % Plot magnitude vs frequency.
title('Single-Sided Magnitude Spectrum of First Music Note'); % Set plot title.
xlabel('Frequency (Hz)'); % Set x-axis label.
ylabel('Magnitude'); % Set y-axis label.
grid on; % Add grid.
xlim([0, fs/2]); % Limit x-axis to show frequencies from 0 to the Nyquist frequency.

% Optional: Mark the identified peak on the plot.
hold on; % Keep the current plot for adding markers.
plot(estimated_frequency_Hz, peak_magnitude, 'ro', 'MarkerSize', 8, 'LineWidth', 2); % Plot a red circle marker at the peak.
text(estimated_frequency_Hz, peak_magnitude, sprintf(' %.2f Hz', estimated_frequency_Hz), 'VerticalAlignment','bottom','HorizontalAlignment','left'); % Add text label for the frequency.
hold off; % Release the plot hold.

% --- Display the Estimated Frequency ---
% Print the estimated frequency to the Command Window for easy copying.
fprintf('---------------------------------------------------\n');
fprintf('Q1 Task 5 Result:\n');
fprintf('Estimated fundamental frequency of the first note: %.4f Hz\n', estimated_frequency_Hz);
fprintf('---------------------------------------------------\n');



% =========================================================================
% EBU5376 Digital Signal Processing - LAB 1
% Question 2 - Task 1: Estimate Frequencies of All Music Notes
% =========================================================================
% This section estimates the fundamental frequency for each identified music
% note segment within the audio signal.
% The start and end times for each note must be determined visually
% from the full audio waveform plot generated in Q1 Task 2/3.


% [audioData, fs] = audioread('your_audio_file.wav'); % Replace with your file name

% --- Define note intervals (times in seconds) ---
% Define the start and end times (in seconds) for each music note
 note_intervals_sec = {
     [0.55, 0.82];
     [0.83, 1.08];
     [1.09, 1.36];
     [1.36, 1.62];
     [1.63, 1.90];
     [1.91, 2.11];
     [2.12, 2.43];
     [2.45, 2.70]
 }; 

estimated_frequencies_all_notes = zeros(1, length(note_intervals_sec));

fprintf('\n'); % Add a newline for clarity in the command window
fprintf('---------------------------------------------------\n');
fprintf('Q2 Task 1 Results: Estimating Frequencies for Each Note Segment\n');
fprintf('---------------------------------------------------\n');

% --- Loop through each defined note interval ---
for i = 1:length(note_intervals_sec)
    start_time_sec = note_intervals_sec{i}(1);
    end_time_sec = note_intervals_sec{i}(2);

    % Convert times (in seconds) to sample indices.
    % MATLAB uses 1-based indexing, so add 1 to the start index calculated from time.
    % The end index is inclusive.
    start_idx = round(start_time_sec * fs) + 1;
    end_idx = round(end_time_sec * fs);

    % Ensure sample indices are within the valid range of audioData
    start_idx = max(1, start_idx); % Cannot be less than 1
    end_idx = min(length(audioData), end_idx); % Cannot be more than total samples

    % Extract the audio segment corresponding to the current note interval
    % Assuming stereo data and using the first channel (left channel)
    % Need to handle the case where end_idx might be before start_idx due to rounding
    if start_idx > end_idx
         fprintf('Warning: Calculated indices for note %d (%.4f - %.4f sec) are invalid (start_idx > end_idx). Skipping frequency estimation.\n', ...
                 i, start_time_sec, end_time_sec);
         estimated_frequencies_all_notes(i) = NaN; % Mark as Not a Number
         continue; % Move to the next iteration of the loop
    end

    note_segment = audioData(start_idx:end_idx, 1);

    % --- Perform FFT on the extracted segment ---
    L = length(note_segment); % Number of samples in the current segment

    % If segment is empty
    if L == 0
        fprintf('Warning: Segment for note %d (%.4f - %.4f sec) is empty (L=0). Skipping frequency estimation.\n', ...
                i, start_time_sec, end_time_sec);
        estimated_frequencies_all_notes(i) = NaN; % Mark as Not a Number
        continue; % Move to the next iteration of the loop
    end


    % Compute the Discrete Fourier Transform (DFT) using the Fast Fourier Transform (FFT) algorithm.
    % The result Y is a complex array representing the frequency components.
    Y = fft(note_segment);

    % --- Calculate the Single-Sided Magnitude Spectrum and corresponding Frequency Axis ---
    % The FFT output Y is a two-sided spectrum. We need the single-sided spectrum.
    % P2 contains the magnitude of the two-sided spectrum, normalized by the length L.
    P2 = abs(Y/L);
    % P1 contains the single-sided spectrum.
    % We take the first half of P2 (up to the Nyquist frequency).
    % The magnitudes for frequencies (except DC and Nyquist, if L is even) are doubled
    % to conserve signal energy when moving from a two-sided to a single-sided spectrum representation.
    P1 = P2(1:floor(L/2)+1);
    P1(2:end-1) = 2*P1(2:end-1); % Double the magnitudes for positive frequencies

    % Create the frequency vector 'f' corresponding to the points in P1.
    % Frequencies range from 0 Hz up to the Nyquist frequency (fs/2).
    % The frequency resolution (step size between frequency points) is fs/L.
    f = (0:floor(L/2)) * (fs/L);

    % --- Find the Peak Frequency (excluding the DC component at 0 Hz) ---
    % The fundamental frequency of a pure note corresponds to the highest peak
    % in the magnitude spectrum. We exclude the first element P1(1) as it
    % represents the DC component (0 Hz), which is usually not the note's frequency.
    [peak_magnitude, peak_index_relative] = max(P1(2:end)); % Find max starting from the 2nd element

    if isempty(peak_index_relative) || peak_magnitude == 0
         fprintf('Warning: Could not find a dominant peak for note %d (%.4f - %.4f sec, L=%d). Peak magnitude zero or search failed.\n', ...
                 i, start_time_sec, end_time_sec, L);
         estimated_freq = NaN; % Mark as Not a Number
    else
        % Convert the relative index (within P1(2:end)) back to the actual index in the full P1 array.
        % Since max was called on P1(2:end), the relative index '1' corresponds to P1(2).
        peak_actual_index = peak_index_relative + 1;
        % Get the frequency value from the frequency vector 'f' at the identified peak index.
        estimated_freq = f(peak_actual_index);
    end

    % Store the estimated frequency for the current note
    estimated_frequencies_all_notes(i) = estimated_freq;

    % Display frequency estimate for current note.
    fprintf('Note %d (%.4f - %.4f sec, L=%d): Estimated Frequency = %.4f Hz\n', ...
            i, start_time_sec, end_time_sec, L, estimated_freq);

    % Optional: Plot the spectrum for each note (can clutter the figure window
    % if there are many notes; uncomment for debugging if needed)
    % figure;
    % plot(f, P1);
    % title(sprintf('Spectrum of Note %d (Length %d samples)', i, L));
    % xlabel('Frequency (Hz)');
    % ylabel('Magnitude');
    % xlim([0, fs/2]); % Limit x-axis to Nyquist frequency
    % grid on;
end

fprintf('---------------------------------------------------\n');
fprintf('Summary of Estimated Frequencies for Q2 Task 1:\n');
% Format the summary string for easy copying into the lab sheet
% Handle potential NaN values in the output string
freq_str_array = arrayfun(@(x) sprintf('%.4f', x), estimated_frequencies_all_notes, 'UniformOutput', false);
% Replace 'NaN' strings with a placeholder if needed, or just leave them as NaN string
fprintf('%s Hz\n', strjoin(freq_str_array, ', '));
fprintf('---------------------------------------------------\n');

% =========================================================================
% EBU5376 Digital Signal Processing - LAB 1
% Question 2 - Task 3: Impact of Window Length on Frequency Analysis
% =========================================================================
% This section investigates how the duration of the analysis window affects
% the estimated peak frequency and the frequency resolution of the spectrum.
% We will use the first note segment (identified in Q1/Q2 Task 1) for this analysis.
% We will analyze segments of different lengths taken from the start of the first note.



fprintf('\n'); % Add a newline for clarity
fprintf('---------------------------------------------------\n');
fprintf('Q2 Task 3 Analysis: Impact of Window Length\n');
fprintf('---------------------------------------------------\n');

% --- Get the full segment for the first note ---
% Use the start and end times determined for the first note in Q2 Task 1.
% Access the first cell of note_intervals_sec.
if exist('note_intervals_sec', 'var') && ~isempty(note_intervals_sec) && length(note_intervals_sec) >= 1
     firstNote_startTime_sec = note_intervals_sec{1}(1);
     firstNote_endTime_sec = note_intervals_sec{1}(2);
else
    % Handle case where note_intervals_sec is not available or invalid
    fprintf('Error: note_intervals_sec not found, is empty, or does not contain a first note interval.\n');
    return;
end

% Convert time boundaries to sample indices
start_idx_full = round(firstNote_startTime_sec * fs) + 1;
end_idx_full = round(firstNote_endTime_sec * fs);

% Ensure sample indices are within the valid range of audioData
start_idx_full = max(1, start_idx_full);
end_idx_full = min(length(audioData), end_idx_full);

% Check for valid interval indices
if start_idx_full > end_idx_full
    fprintf('Error: First note interval indices are invalid (start_idx > end_idx) based on your Q2 Task 1 times.\n');
    fprintf('Cannot perform Q2 Task 3 analysis. Please check the times set in note_intervals_sec{1}.\n');
     return; % Exit this code block
end

% Extract the full segment of the first note (using the first channel for simplicity)
full_first_note_segment = audioData(start_idx_full:end_idx_full, 1);
full_note_length_samples = length(full_first_note_segment);
full_note_duration_sec = full_note_length_samples / fs;

fprintf('Analyzing the first note segment from %.4f s to %.4f s (Duration: %.4f sec, Samples: %d)\n', ...
        firstNote_startTime_sec, firstNote_endTime_sec, full_note_duration_sec, full_note_length_samples);
fprintf('---------------------------------------------------\n');


% --- Define window lengths to test (as durations in seconds) ---
% Choose a few different durations, ranging from short to the full note length.
% These durations define how much of the BEGINNING of the first note segment is analyzed.
% IMPORTANT: Adjust these example durations based on the actual duration of your first note.
% Ensure durations are positive, ascending, and the last one is the full note duration.
% Example durations:
test_durations_sec = [0.01, 0.05, 0.1, 0.15,  0.2700]; 

% Initialize arrays to store results
estimated_frequencies_by_window_length = zeros(1, length(test_durations_sec));
frequency_resolutions_Hz = zeros(1, length(test_durations_sec)); % To store fs/L

% --- Prepare figure for plotting spectra at different window lengths ---
figure; % Create a new figure window
set(gcf, 'Name', 'Q2 Task 3: Impact of Window Length on Spectrum Analysis'); % Set figure name
% Use tiledlayout for better subplot arrangement (requires R2018b or later)
% If using an older version, you might use subplot(rows, cols, index) instead
try
    tiledlayout('flow'); % Arranges subplots automatically
    useTiledLayout = true;
catch
    warning('tiledlayout requires MATLAB R2018b or later. Using subplot instead.');
    numPlots = length(test_durations_sec);
    numRows = ceil(sqrt(numPlots));
    numCols = ceil(numPlots / numRows);
    useTiledLayout = false;
end

fprintf('Analyzing spectra for different window durations:\n');

% --- Loop through different window durations ---
for k = 1:length(test_durations_sec)
    current_duration = test_durations_sec(k);

    % Calculate window length in samples
    window_length_samples = round(current_duration * fs);

    % Ensure window length is valid (at least 1 sample and not exceeding the full note length)
    window_length_samples = max(1, min(window_length_samples, full_note_length_samples));

    % Check if calculated window length is 0 (can happen with very short durations and rounding)
    if window_length_samples == 0
         fprintf('Warning: Calculated window length for duration %.4f sec is 0 samples. Skipping analysis.\n', current_duration);
         estimated_frequencies_by_window_length(k) = NaN;
         frequency_resolutions_Hz(k) = NaN;
         continue;
    end

    % Extract a segment of the current window length from the START of the full first note segment
    segment_to_analyze = full_first_note_segment(1:window_length_samples);

    % --- Perform FFT on this shorter segment ---
    L_window = length(segment_to_analyze); % Actual number of samples in the window


    % For this task, let's stick to the standard rectangular window as per typical basic FFT analysis.
    Y_window = fft(segment_to_analyze);

    % --- Calculate Single-Sided Magnitude Spectrum and Frequency Axis ---
    P2_window = abs(Y_window/L_window);
    P1_window = P2_window(1:floor(L_window/2)+1);
    % Double the magnitudes for positive frequencies (except DC and Nyquist if L is even)
    P1_window(2:end-1) = 2*P1_window(2:end-1);

    % Create the frequency vector 'f' corresponding to the points in P1.
    % Frequencies range from 0 Hz up to the Nyquist frequency (fs/2).
    % The frequency resolution (step size between frequency points) is fs/L.
    f_window = (0:floor(L_window/2)) * (fs/L_window); % Frequency vector

    % --- Find the Peak Frequency (excluding the DC component at 0 Hz) ---
    % Find max magnitude in the single-sided spectrum, starting from index 2 (to exclude DC at 0 Hz)
    [peak_magnitude_window, peak_index_relative_window] = max(P1_window(2:end));

    if isempty(peak_index_relative_window) || peak_magnitude_window == 0
         fprintf('Warning: Could not find a dominant peak for window duration %.4f sec (L=%d). Peak magnitude zero or search failed.\n', current_duration, L_window);
         estimated_freq_window = NaN; % Mark as Not a Number
    else
        % Convert the relative index (from P1_window(2:end)) back to the actual index in P1_window and f_window arrays.
        % A relative index of 1 corresponds to an actual index of 2.
        peak_actual_index_window = peak_index_relative_window + 1;
        % Get the frequency value from the frequency vector 'f_window' at the identified peak index.
        estimated_freq_window = f_window(peak_actual_index_window);
    end

    % Store the estimated frequency for the current window duration
    estimated_frequencies_by_window_length(k) = estimated_freq_window;
    % The theoretical frequency resolution is fs/L
    frequency_resolutions_Hz(k) = fs / L_window; % Store resolution

    % --- Plot the Spectrum for this window length ---
    if useTiledLayout
        ax = nexttile; % Move to the next subplot position using tiledlayout
    else
        subplot(numRows, numCols, k); % Use traditional subplot method
        ax = gca; % Get current axes handle
    end

    plot(f_window, P1_window);
    title(sprintf('Dur: %.4fs (L=%d)', current_duration, L_window)); % Short title for subplots
    xlabel('Frequency (Hz)');
    ylabel('Magnitude');
    % Set x-axis limit consistently for comparison (e.g., up to fs/2 or a reasonable range)
    xlim([0, fs/2]);
    % Or set a fixed range if frequencies are known to be in a certain band, e.g., xlim([1000, 2500])
    ylim auto; % Let y-axis scale automatically for each plot
    grid on;
    hold on;
     if ~isnan(estimated_freq_window)
        % Mark the identified peak on the plot
        plot(estimated_freq_window, peak_magnitude_window, 'ro', 'MarkerSize', 6, 'LineWidth', 1.5);
        % Add text label for the peak frequency (adjust position if needed)
        text(estimated_freq_window, peak_magnitude_window, sprintf(' %.2f Hz', estimated_freq_window), ...
             'VerticalAlignment','bottom','HorizontalAlignment','left', 'FontSize', 7, 'Color', 'red'); % Smaller font for subplots
     end
    hold off;

    % Display results for current window duratio    
     fprintf('  Duration: %.4f s, Length (samples): %d, Estimated Frequency: %.4f Hz, Resolution (fs/L): %.4f Hz\n', ...
             current_duration, L_window, estimated_freq_window, frequency_resolutions_Hz(k));
end

% Optional: Add an overall title to the figure (requires R2018b+)
if useTiledLayout
    try
        sgtitle('Impact of Window Length on Spectrum Analysis of First Note');
    catch
        % sgtitle not available in older versions, ignore error
    end
else
     % No single title easily added for traditional subplots without manually placing text
end


fprintf('---------------------------------------------------\n');
fprintf('Summary of Q2 Task 3 Analysis:\n');
% Format summary strings for easy copying into the lab sheet
fprintf('Test Durations (s):       %s\n', sprintf('%.4f ', test_durations_sec));
% Handle potential NaN values in summary strings as well for printing
freq_str_summary = arrayfun(@(x) sprintf('%.4f', x), estimated_frequencies_by_window_length, 'UniformOutput', false);
res_str_summary = arrayfun(@(x) sprintf('%.4f', x), frequency_resolutions_Hz, 'UniformOutput', false);

fprintf('Estimated Frequencies (Hz): %s\n', strjoin(freq_str_summary, ', '));
fprintf('Theoretical Resolution (fs/L, Hz):  %s\n', strjoin(res_str_summary, ', '));
fprintf('---------------------------------------------------\n');

% Analysis results (printed summary and generated plots) are used for the Lab Sheet report.

% Final Evaluation (optional): ... (前面的注释和变量定义保持不变) ...
frequencies = [1177.7778, 1052.0000, 833.3333, 788.4615, 700.0000, 625.0000, 590.3226, 528.0000];
fs = 48000;
note_duration = 0.3;
rest_duration = 0.05;
fade_duration = 0.2;

% --- Preallocate audio_signal array ---
% Calculate the total number of samples needed.
% Total samples = (number of notes * samples per note) + (number of rests * samples per rest)
num_notes = length(frequencies);
samples_per_note = round(note_duration * fs);
samples_per_rest = round(rest_duration * fs);

% If there are notes, there are num_notes - 1 rests between them.
% If only one note, there are 0 rests.
num_rests = max(0, num_notes - 1);

total_samples_estimated = (num_notes * samples_per_note) + (num_rests * samples_per_rest);

% Create a zero array with the estimated total size.
audio_signal = zeros(1, total_samples_estimated);

% Keep track of the current position (index) in the audio_signal array where the next sample should be placed.
current_sample_index = 1;

% --- Generate and place notes and rests in the preallocated array ---
for i = 1:num_notes % Loop through each frequency/note

    % Generate the note waveform (sine wave)
    % Use the actual number of samples based on note_duration
    t = 0:1/fs:note_duration-1/fs;
    note = sin(2*pi*frequencies(i)*t);
    actual_note_samples = length(note); % Get the exact sample count of the generated note

    % Apply a fade-out to the end of the note
    fade_samples = round(fade_duration*fs);
    fade_samples = min(fade_samples, actual_note_samples); % Ensure fade doesn't exceed note length

    if fade_samples > 0
       fade_window_end = linspace(1, 0, fade_samples);
       note(end-fade_samples+1:end) = note(end-fade_samples+1:end) .* fade_window_end;
    end

    % --- Place the generated note into the preallocated audio_signal array ---
    % Calculate the end index for this note in the audio_signal array
    note_end_index = current_sample_index + actual_note_samples - 1;

    % Ensure we don't write beyond the preallocated array size (handle potential slight differences due to rounding)
    % If the preallocated size was slightly off, adjust the placement to fit.
    actual_note_end_index_in_array = min(note_end_index, total_samples_estimated);
    actual_note_samples_to_place = actual_note_end_index_in_array - current_sample_index + 1;

    if actual_note_samples_to_place > 0
        audio_signal(current_sample_index : actual_note_end_index_in_array) = note(1 : actual_note_samples_to_place);
    end


    % Update the current position for the next element
    current_sample_index = actual_note_end_index_in_array + 1;


    % Add a rest (silence) after the note, unless it's the last note
    if i < num_notes
        rest_samples = round(rest_duration*fs);
        actual_rest_samples = rest_samples;

        % Calculate the end index for the rest
        rest_end_index = current_sample_index + actual_rest_samples - 1;

        % Ensure we don't write beyond the preallocated array size
        actual_rest_end_index_in_array = min(rest_end_index, total_samples_estimated);
         actual_rest_samples_to_place = actual_rest_end_index_in_array - current_sample_index + 1;

         if actual_rest_samples_to_place > 0
              % Place zeros into the preallocated array (it's already zeros, so this is technically optional
              % but good practice to show the space is used by the rest)
              audio_signal(current_sample_index : actual_rest_end_index_in_array) = 0; % Redundant, but clear
         end

        % Update the current position for the next element
        current_sample_index = actual_rest_end_index_in_array + 1;
    end
end

% --- Trim the audio_signal array to the actual used size ---
% Due to rounding, the estimated total size might be slightly different from the actual total size.
% Trim the array to the last sample index that was actually written to (or 1 if nothing was written).
last_written_index = current_sample_index - 1;
if last_written_index >= 1
   audio_signal = audio_signal(1:last_written_index);
else
   audio_signal = []; % Handle case where no notes were generated (shouldn't happen if frequencies is not empty)
end


% Normalize the entire synthesized audio signal to prevent clipping during playback
% Only normalize if the signal is not empty and not all zeros
if ~isempty(audio_signal) && max(abs(audio_signal)) > 0
    audio_signal = audio_signal / max(abs(audio_signal));
end


% Play the sequence
% Multiply by a small factor (e.g., 0.2) to control the playback volume
sound(audio_signal*0.2, fs);
