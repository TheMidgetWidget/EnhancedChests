package me.lightlord323dev.enhancedchests.api.file;

import me.lightlord323dev.enhancedchests.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by Luda on 7/19/2020.
 */
public class AbstractFile {

    protected Main main;
    protected File f;
    protected FileConfiguration c;

    public AbstractFile(Main main, String name, boolean yml, boolean createNewFile) {
        this.main = main;

        File dir = new File(main.getDataFolder() + File.separator + "chestdata");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        this.f = new File(main.getDataFolder() + File.separator + "chestdata", name);
        if (!f.exists() && createNewFile) {
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
