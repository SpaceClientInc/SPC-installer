package de.spc.installer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

public class Downloader {

    public static Double percentage = 0.0D;

    private static void createDirectory() {
        SharedConstants.CLEINT.mkdirs();
    }

    public static void downloadJar() {
        createDirectory();
        File file = new File(SharedConstants.CLEINT, "SPC-Alpha-1.2.5.jar");
        download("https://cloud.craftsblock.de/s/Zj2Ss7rosqCWGd2/download" , file);
    }

    public static void downloadJson() {
        createDirectory();
        File file = new File(SharedConstants.CLEINT, "SPC-Alpha-1.2.5.json");
        download("https://cloud.craftsblock.de/s/y5wHkHseWi8Bfkr/download", file);
    }

    private static void download(String url, File file) {
        try {
            download(new URL(url), file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void download(URL url, File file) {
        percentage = 0.0D;
        BufferedInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "ytcInstaller/v1.2.5");
            connection.connect();
            inputStream = new BufferedInputStream(connection.getInputStream());
            outputStream = new FileOutputStream(file);
            byte[] data = new byte[1024];
            double sumCount = 0.0D;
            int count;
            int size = connection.getContentLength();
            while ((count = inputStream.read(data, 0, 1024)) != -1) {
                outputStream.write(data, 0, count);
                sumCount += count;
                if(size > 0)
                    percentage = sumCount / size * 100.0D;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            percentage = 100.0D;
            try {
                if(inputStream != null)
                    inputStream.close();
                if(outputStream != null)
                    outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
