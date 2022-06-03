package xyz.colmmurphy.fredo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class DeathCounter {
    private HashMap<String, Integer> deathCount;

    public Integer get(String key) {
        return this.deathCount.get(key);
    }

    public int getOrDefault(String key, int def) {
        Integer value = this.get(key);
        if (value == null) {
            return def;
        }
        return value;
    }

    public int getOrError(String key, String errorMessage) {
        Integer value = this.get(key);
        if (value == null) {
            throw new NullPointerException(errorMessage);
        }
        return value;
    }

    public void put(String key, int value) {
        this.deathCount.put(key, value);
    }

    public void addDeath(String playerName) {
        int currentDeaths = this.getOrDefault(playerName, 0);
        this.put(playerName, currentDeaths + 1);
    }

    public DeathCounter() {
        String j = null;
        try {
            File f = new File("deathcount.json");
            if (!f.exists()) {
                if (f.createNewFile()) {
                    f = new File("deathcount.json");
                }
            }
            StringBuilder sb = new StringBuilder();
            FileInputStream fis = new FileInputStream(f);
            byte[] bytes = fis.readAllBytes();
            for (byte b : bytes) {
                sb.append((char) b);
            }
            j = sb.toString();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (j == null || j.equals("")) {
            j = "{}";
        }
        this.deathCount = jsonToMap(j);
    }

    private HashMap<String, Integer> jsonToMap(String j) {
        try {
            return new ObjectMapper().readValue(j, HashMap.class);
        } catch (JsonProcessingException e) {}
        return null;
    }

    public void writeDeathCountToFile() {
        String j = org.json.JSONStringer.valueToString(this.deathCount);
        File f = new File("deathcount.json");
        try {
            FileOutputStream fos = new FileOutputStream(f);
            byte[] bytes = new byte[j.length()];
            char[] chars = j.toCharArray();
            for(int i = 0; i < chars.length; i++) {
                bytes[i] = (byte) chars[i];
            }
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {}
    }
}
