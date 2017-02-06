package org.freejava.voicecontrol.impl;

import java.util.List;

import org.freejava.voicecontrol.ISpeechManager;

import com.google.cloud.speech.spi.v1beta1.SpeechClient;
import com.google.cloud.speech.v1beta1.RecognitionAudio;
import com.google.cloud.speech.v1beta1.RecognitionConfig;
import com.google.cloud.speech.v1beta1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1beta1.SpeechRecognitionResult;
import com.google.cloud.speech.v1beta1.SyncRecognizeResponse;
import com.google.protobuf.ByteString;

public class GoogleSpeechManagerImpl implements ISpeechManager {

    /* (non-Javadoc)
     * @see org.freejava.voicecontrol.ISpeechManager#transcript(short[])
     */
    @Override
    public String transcript(short[] dataData) throws Exception {
        byte[] bytes = toLittleEndianByteArray(dataData);

        try (SpeechClient speech = SpeechClient.create()) {

            // Reads the audio file into memory
            ByteString audioBytes = ByteString.copyFrom(bytes);

            // Builds the sync recognize request
            RecognitionConfig config = RecognitionConfig.newBuilder().setEncoding(AudioEncoding.LINEAR16).setSampleRate(16000).build();
            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

            // Performs speech recognition on the audio file
            SyncRecognizeResponse response = speech.syncRecognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            for (SpeechRecognitionResult result : results) {
                List<SpeechRecognitionAlternative> alternatives = result.getAlternativesList();
                for (SpeechRecognitionAlternative alternative : alternatives) {
                    System.out.printf("Transcription: %s%n", alternative.getTranscript());
                    return alternative.getTranscript();

                }
            }
        }

        return null;
    }

    private byte[] toLittleEndianByteArray(short[] data) {
        byte[] bytes = new byte[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            short x = data[i];
            int j = i * 2;
            bytes[j] = (byte) (x & 0xff);
            bytes[j + 1] = (byte) ((x >> 8) & 0xff);
        }
        return bytes;
    }

}
