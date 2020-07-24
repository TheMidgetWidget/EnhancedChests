package me.lightlord323dev.enhancedchests.api.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.lightlord323dev.enhancedchests.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Luda on 7/19/2020.
 */
public class AbstractFile {

    protected Main main;
    protected File f;
    protected FileConfiguration c;

    public AbstractFile(Main main, String name, boolean yml) {
        this.main = main;
        this.f = new File(main.getDataFolder(), name);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (yml)
            c = YamlConfiguration.loadConfiguration(f);
    }

    // for yml
    public void saveToYml() {
        try {
            c.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return f;
    }
}
