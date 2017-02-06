package org.freejava.voicecontrol.impl;

public class MusicPlayer {
    private ProcessManager process = new ProcessManager();
    public void play(String yid) throws Exception {
        process.start("/usr/local/bin/mpv", new String[] {"https://www.youtube.com/watch?v=" + yid});
    }
    public void stop() throws Exception {
        process.kill();
    }
}
