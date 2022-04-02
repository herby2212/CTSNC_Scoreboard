package de.Herbystar.CTSNC_Modules.Scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.Herbystar.CTSNC.ReplaceString;

public class ReplaceStringAsync extends BukkitRunnable {

	String original;
	String replaced;
	Player p;
	
	
	public ReplaceStringAsync(String textToReplaceAsync, Player player) {
		this.original = textToReplaceAsync;
		this.p = player;
	}
	
	@Override
	public void run() {
		this.replaced = ReplaceString.replaceString(this.original, this.p);
	}
	
	public String returnString() {
		return this.replaced;
	}
}
