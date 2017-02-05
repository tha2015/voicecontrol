package org.freejava.voicecontrol.impl;

import org.freejava.voicecontrol.ICommandManager;
import org.freejava.voicecontrol.MediaSearchManager;

public class CommandManagerImpl implements Runnable, ICommandManager {
    private String command;

    /* (non-Javadoc)
     * @see org.freejava.voicecontrol.ICommandManager#handleCommand(java.lang.String)
     */
    @Override
    public void handleCommand(String command) {
        if (command == null) return;

        this.command = command.toLowerCase();
        (new Thread(this)).start();

    }

    @Override
    public void run() {
        try {
            String yid = null;
            for (String pre : new String[]{"play"}) {
                if (command.indexOf(pre) != -1) {

                    String name = command.substring(pre.length()).trim();
                    System.out.println("searching " + name);

                    MediaSearchManager h = new YoutubeMediaSearchManagerImpl();
                    yid = h.searchMediaItems(name);
                    if (yid != null) {
                        break;
                    }
                }
            }

            if (yid != null) {
                ProcessBuilder pb = new ProcessBuilder("open", "-a", "vlc", "https://www.youtube.com/watch?v=" + yid);
                System.out.println("https://www.youtube.com/watch?v=" + yid);
                pb.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
