package org.freejava.voicecontrol;

public interface ISpeechManager {

    String transcript(short[] audioData) throws Exception;

}