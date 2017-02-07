package org.freejava.voicecontrol.impl;

import org.freejava.voicecontrol.ICommandManager;
import org.freejava.voicecontrol.MediaSearchManager;

public class CommandManagerImpl implements ICommandManager {

    private MusicPlayer player = new MusicPlayer();


    @Override
    public void handleCommand(String command) {
        if (command == null) return;

        command = command.toLowerCase();

        try {
            if (command.indexOf("stop") != -1 || command.indexOf("t\u1EAFt") != -1) { // stop or ta('t
                player.stop();
            } else {
                String yid = null;
                for (String pre : new String[]{"play", "m\u1EDF b\u00E0i"}) { // play or mo bai
                    if (command.indexOf(pre) != -1) {

                        String name = command.substring(command.lastIndexOf(pre) + pre.length() + 1).trim();
                        System.out.println("searching " + name);

                        MediaSearchManager h = new YoutubeMediaSearchManagerImpl();
                        yid = h.searchMediaItems(name);
                        if (yid != null) {
                            break;
                        }
                    }
                }

                if (yid != null) {
                    player.play(yid);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
