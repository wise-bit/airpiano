import numpy as np
import matplotlib.pyplot as plt

from scipy.io.wavfile import write
import simpleaudio as sa

samplerate = 44100 #Frequecy in Hz

def get_wave(freq, duration=0.5):
    '''
    Function takes the "frequecy" and "time_duration" for a wave 
    as the input and returns a "numpy array" of values at all points 
    in time
    '''
    
    amplitude = 4096
    t = np.linspace(0, duration, int(samplerate * duration))
    wave = amplitude * np.sin(2 * np.pi * freq * t)
    
    return wave

# # To get a 1 second long wave of frequency 440Hz
# a_wave = get_wave(440, 1)
# # print(type(a_wave)) # <class 'numpy.ndarray'>

# #wave features
# print(len(a_wave)) # 44100
# print(np.max(a_wave)) # 4096
# print(np.min(a_wave)) # -4096

# plt.plot(a_wave[0:int(44100/440)])
# plt.xlabel('time')
# plt.ylabel('Amplitude')
# plt.show()

def get_piano_notes():
    '''
    Returns a dict object for all the piano 
    note's frequencies
    '''
    # White keys are in Uppercase and black keys (sharps) are in lowercase
    octave = ['C', 'c', 'D', 'd', 'E', 'F', 'f', 'G', 'g', 'A', 'a', 'B'] 
    base_freq = 261.63 #Frequency of Note C4
    
    note_freqs = {octave[i]: base_freq * pow(2,(i/12)) for i in range(len(octave))}        
    note_freqs[''] = 0.0 # silent note
    
    return note_freqs

# note_freqs = get_piano_notes()
# print(note_freqs)

def generate_song(music_notes):
    '''
    Function to concatenate all the waves (notes)
    '''
    note_freqs = get_piano_notes() # Function that we made earlier
    song = [get_wave(note_freqs[note]) for note in music_notes.split('-')]
    song = np.concatenate(song)
    return song

music_notes = 'C-C-G-G-A-A-G--F-F-E-E-D-D-C--G-G-F-F-E-E-D--G-G-F-F-E-E-D--C-C-G-G-A-A-G--F-F-E-E-D-D-C'
data = generate_song(music_notes)

# data = data * (16300/np.max(data)) # Adjusting the Amplitude (Optional)
# write('twinkle-twinkle2.wav', samplerate, data.astype(np.int16))

# play_obj = sa.play_buffer(data.astype(np.int16), 2, 2, 44100)
play_obj = sa.play_buffer(data.astype(np.int16), 2, 2, 44100)
play_obj.wait_done()
