package org.freejava.voicecontrol;

public interface ISpeechManager {

    String transcript(String languageCode, short[] audioData) throws Exception;

}