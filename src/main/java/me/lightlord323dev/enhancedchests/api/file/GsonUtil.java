package me.lightlord323dev.enhancedchests.api.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.lightlord323dev.enhancedchests.util.GZipUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by Luda on 7/19/2020.
 */
public class GsonUtil {

    private static final Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    public static void saveObject(Object object, File file) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(GZipUtil.compress(GSON.toJson(object)));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T loadObject(TypeToken<T> token, File file) {
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return GSON.fromJson(GZipUtil.decompress(fileContent), token.getType());
        } catch (IOException e) {
            return null;
        }
    }
}
