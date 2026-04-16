frequencies = [1180, 1050, 835, 790, 700, 625, 590, 525]; % replace f1-f8 with your estimations in Q2 task 1
fs = 48000 ;         % set this to the sampling rate of the audio, calculated in Q1 task 1

note_duration = 0.3; 
rest_duration = 0.05; 
fade_duration = 0.2; 

audio_signal = [];
for i = 1:length(frequencies)

    t = 0:1/fs:note_duration-1/fs;
    note = sin(2*pi*frequencies(i)*t);

    fade_samples = round(fade_duration*fs);
    fade_window = ones(1, length(note));
    fade_window(end-fade_samples+1:end) = linspace(1, 0, fade_samples);
    note = note .* fade_window;
    
    audio_signal = [audio_signal, note];
    
    if i < length(frequencies)
        audio_signal = [audio_signal, zeros(1, round(rest_duration*fs))];
    end
end

audio_signal = audio_signal / max(abs(audio_signal));

% Play the sequence
sound(audio_signal*0.2, fs);


