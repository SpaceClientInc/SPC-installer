package de.spc.installer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class MojangProfile {

    private static final SimpleDateFormat TIMEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS'Z'", Locale.GERMANY);

    static {
        TIMEFORMAT.setTimeZone(Calendar.getInstance().getTimeZone());
    }

    private static String getShortUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static JsonObject create() {
        JsonObject object = new JsonObject();
        object.addProperty("created", TIMEFORMAT.format(new Date(System.currentTimeMillis())));
        object.addProperty("lastUsed", TIMEFORMAT.format(new Date(System.currentTimeMillis())));
        object.addProperty("icon", SharedConstants.getIcon());
        object.addProperty("javaArgs", "-Xmx4G -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32M");
        object.addProperty("lastVersionId", "SPC-Closed-Beta");
        object.addProperty("name", "Space Client-beta");
        object.addProperty("type", "normal");
        return object;
    }

    public static void install() {
        File file = new File(SharedConstants.OS.getDirectory(), "launcher_profiles.json");
        if(!file.exists())
            throw new IllegalStateException("Profile File does not exists!");
        try {
            FileReader reader = new FileReader(file);
            JsonObject parsed = JsonParser.parseReader(reader).getAsJsonObject();
            JsonObject profiles = new JsonObject();
            if(parsed.has("profiles")) {
                profiles = parsed.getAsJsonObject("profiles");
                parsed.remove("profiles");
            }
            profiles.add(getShortUUID(), create());
            parsed.add("profiles", profiles);
            Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
            String output = gson.toJson(parsed);

            FileWriter writer = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(output);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
