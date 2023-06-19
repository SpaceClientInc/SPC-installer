package de.spc.installer;

import java.io.File;

public enum EnumOS {

    LINUX(".minecraft"),
    SOLARIS(".minecraft"),
    WINDOWS("AppData" + File.separator + "Roaming" + File.separator + ".minecraft"),
    OSX("Library" + File.separator + "Application Support" + File.separator + "minecraft"),
    UNKOWN(null);

    private final String directory;

    private static EnumOS os;

    public void init() {
        System.out.println("Initailzed EnumOS");
    }

    EnumOS(String directory) {
        this.directory = directory;
    }

    public File getDirectory() {
        return new File(System.getProperty("user.home"), directory);
    }

    public static EnumOS getOS() {
        if (EnumOS.os == null) {
            String osName = System.getProperty("os.name").toLowerCase();
            EnumOS os = EnumOS.UNKOWN;
            if (osName.contains("win"))
                os = EnumOS.WINDOWS;
            else if (osName.contains("mac"))
                os = EnumOS.OSX;
            else if (osName.contains("solaris") || osName.contains("sunos"))
                os = EnumOS.SOLARIS;
            else if (osName.contains("linux") || osName.contains("unix"))
                os = EnumOS.LINUX;
            EnumOS.os = os;
        }
        return EnumOS.os;
    }

}
