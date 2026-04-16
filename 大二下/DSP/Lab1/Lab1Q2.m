% =========================================================================
% EBU5376 Digital Signal Processing - LAB 1
% Question 2 - Task 1: Estimate Frequencies of All Music Notes
% =========================================================================
% This section estimates the fundamental frequency for each identified music
% note segment within the audio signal.
% The start and end times for each note must be determined visually
% from the full audio waveform plot generated in Q1 Task 2.
% You will need to replace the placeholder intervals below with the actual times
% you observe from the plot.
% We will use one channel for analysis (assuming stereo, typically the first channel).

% NOTE: This code assumes that the audio data and sampling rate (audioData, fs)
% have been loaded and defined previously in your script (e.g., from Q1 Task 1).
% If not, uncomment and run the following lines, making sure 'your_audio_file.wav'
% is the correct filename for the provided audio.
% [audioData, fs] = audioread('your_audio_file.wav'); % Replace with your file name

% --- Define note intervals (times in seconds) ---
% IMPORTANT: Replace these placeholder intervals with the actual start and end times
% you determine by inspecting the waveform plot from Q1 Task 2.
% Look for segments of high amplitude separated by periods of low amplitude (rests).
% The number of intervals should match the number of notes you observe.
% Based on typical examples for this lab and the optional Q9 code structure,
% there are likely 8 distinct notes. Define 8 intervals accordingly.
% Example placeholders (YOU MUST REPLACE THESE WITH YOUR ACTUAL VALUES):
 note_intervals_sec = {
     [0.10, 0.40], % Example: Start at 0.1s, end at 0.4s
     [0.50, 0.80],
     [0.90, 1.20],
     [1.30, 1.60],
     [1.70, 2.00],
     [2.10, 2.40],
     [2.50, 2.80],
     [2.90, 3.20]
 }; % <-- REPLACE THESE EXAMPLE TIMES WITH YOUR OWN VISUALLY DETERMINED TIMES

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
    note_segment = audioData(start_idx:end_idx, 1);

    % --- Perform FFT on the extracted segment ---
    L = length(note_segment); % Number of samples in the current segment

    % Check if the segment is empty (can happen with invalid intervals or rounding)
    if L == 0
        fprintf('Warning: Segment for note %d (%.4f - %.4f sec) is empty. Skipping frequency estimation.\n', ...
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
    [peak_magnitude, peak_index_relative] = max(P1(2:end));

    % Check if a peak was found (e.g., segment wasn't just silence)
    if isempty(peak_index_relative) || peak_magnitude == 0
         fprintf('Warning: Could not find a dominant peak for note %d (%.4f - %.4f sec). Peak magnitude is zero or search failed.\n', ...
                 i, start_time_sec, end_time_sec);
         estimated_freq = NaN; % Mark as Not a Number
    else
        % Convert the relative index (within P1(2:end)) back to the actual index in the full P1 array.
        peak_actual_index = peak_index_relative + 1;
        % Get the frequency value from the frequency vector 'f' at the identified peak index.
        estimated_freq = f(peak_actual_index);
    end

    % Store the estimated frequency for the current note
    estimated_frequencies_all_notes(i) = estimated_freq;

    % Display the result for the current note in the Command Window
    fprintf('Note %d (%.4f - %.4f sec): Estimated Frequency = %.4f Hz\n', ...
            i, start_time_sec, end_time_sec, estimated_freq);

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
freq_str_array = arrayfun(@(x) sprintf('%.4f', x), estimated_frequencies_all_notes, 'UniformOutput', false);
fprintf('%s Hz\n', strjoin(freq_str_array, ', '));
fprintf('---------------------------------------------------\n');

% The variable estimated_frequencies_all_notes now contains the estimated frequencies.
% These values are needed for the optional Final Evaluation section (Q9)
% where they should replace [f1, f2, ..., f8].
% You should manually copy the printed frequencies from the Command Window
% into the Lab Sheet text box for Q2 Task 1.