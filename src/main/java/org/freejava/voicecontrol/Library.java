package org.freejava.voicecontrol;

import java.io.File;

public class Library {


    private static String arch() {
        String osArch = System.getProperty("os.arch");
        if (osArch.equals("i386") || osArch.equals("i686"))
            return "x86";
        if (osArch.equals("amd64"))
            return "x86_64";
        if (osArch.equals("IA64N"))
            return "ia64_32";
        if (osArch.equals("IA64W"))
            return "ia64";
        return osArch;
    }

    private static String os() {
        String osName = System.getProperty("os.name");
        if (osName.equals("Linux"))
            return "linux";
        if (osName.equals("AIX"))
            return "aix";
        if (osName.equals("Solaris") || osName.equals("SunOS"))
            return "solaris";
        if (osName.equals("HP-UX"))
            return "hpux";
        if (osName.equals("Mac OS X"))
            return "macosx";
        if (osName.startsWith("Win"))
            return "win32";
        return osName;
    }

    public static void loadLibrary(File basePath, String name) {
        String SEPARATOR = System.getProperty("file.separator");
        System.load(basePath.getAbsolutePath() + SEPARATOR + os() + SEPARATOR + arch() + SEPARATOR + System.mapLibraryName(name));

    }
}
