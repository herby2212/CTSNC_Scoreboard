package de.Herbystar.CTSNC_Modules.Scoreboard;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import de.Herbystar.CTSNC.Files.Files;

public class Main extends JavaPlugin {
	
	public static BukkitTask scoreboardcontent;
	public static BukkitTask scoreboardtitle;
	
	private int scoreboardcontentinterval = Files.config1.getInt("CTSNC.SCOREBOARD.Scoreboard_Content_Update");
	private int scoreboardtitleinterval = Files.config1.getInt("CTSNC.SCOREBOARD.Scoreboard_Title_Update");
	public static HashMap<Player, Scoreboards> boards = new HashMap<Player, Scoreboards>();
	public static List<Scoreboards> allboards = new ArrayList<Scoreboards>();
	public static Main instance;

	
	public void onEnable() {
		instance = this;
//		ConfigUpdater.updateConfig(Files.f1, Files.config1);
		startScoreboards();
	}
	
	public void onDisable() {
		
	}
	
	public void startScoreboards() {
		Main.scoreboardcontent = new BukkitRunnable() {
			
			@Override
			public void run() {
				try {
					if(Files.config1.getBoolean("CTSNC.SCOREBOARD.Update_Content") == true) {
						for(Scoreboards p : boards.values()) {
							p.updateContent();
						}
					}
				} catch(ConcurrentModificationException ex) {
					if(de.Herbystar.CTSNC.Main.instance.getConfig().getBoolean("CTSNC.Debug") == true) {
						Bukkit.getConsoleSender().sendMessage("§c[§eCTSNC§c] §cScoreboard Warning without Influence. Don't mind!");
						ex.printStackTrace();
					}
				}

			}
		}.runTaskTimerAsynchronously(this, 0, scoreboardcontentinterval);
		
		Main.scoreboardtitle = new BukkitRunnable() {
			
			@Override
			public void run() {
				for(Scoreboards p : boards.values()) {
					p.updateTitle();
				}
			}
		}.runTaskTimerAsynchronously(this, 0, scoreboardtitleinterval);	
	}
}
