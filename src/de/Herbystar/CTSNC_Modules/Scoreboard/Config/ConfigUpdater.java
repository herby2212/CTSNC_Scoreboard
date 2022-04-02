package de.Herbystar.CTSNC_Modules.Scoreboard.Config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigUpdater {
	
	public static void updateConfig(File f, YamlConfiguration config) {
		config = YamlConfiguration.loadConfiguration(f);
		config.addDefault("CTSNC.SCOREBOARD.Legacy", false);
		config.options().copyDefaults(true);
		saveConfig(config, f);
	}
	
	public static void saveConfig(YamlConfiguration config, File file) {
		try {
			config.save(file);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
