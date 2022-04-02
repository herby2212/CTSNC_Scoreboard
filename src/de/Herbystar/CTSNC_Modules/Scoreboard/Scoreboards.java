package de.Herbystar.CTSNC_Modules.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.google.common.base.Splitter;

import de.Herbystar.CTSNC.ReplaceString;
import de.Herbystar.CTSNC.Files.Files;

public class Scoreboards {
	
	public Scoreboard board;
	public Objective score;
	public Player player;
	public List<Team> teams = new ArrayList<Team>();
	public HashMap<Team, String> lot = new HashMap<Team, String>();
	public HashMap<Team, String> compare = new HashMap<Team, String>();
	public List<String> content = new ArrayList<String>();
	public List<String> title = new ArrayList<String>();
	public List<String> chlist = new ArrayList<String>();
	public HashMap<Integer, String> scores = new HashMap<Integer, String>();
	
	int index = 15;
	int titleindex = 0;
	int maxchars = 32;
	public String replacer;
	
	public Scoreboards(Player player) {
		Files.config1 = YamlConfiguration.loadConfiguration(Files.f1);
		for(String s : Files.config1.getStringList("CTSNC.SCOREBOARD.Content")) {
			this.content.add(s);
		}
		for(String s : Files.config1.getStringList("CTSNC.SCOREBOARD.Header")) {
			this.title.add(s);
		}
		PlayerReceiveBoard e = new PlayerReceiveBoard(this.player, this.content, this.title, this);
		Bukkit.getPluginManager().callEvent(e);
		if(!e.isCancelled()) {
			lines();
			if(Main.boards.containsKey(this.player)) {
				((Scoreboards)Main.boards.get(this.player)).remove();
			}
			this.titleindex = e.getTitle().size();
			Main.boards.put(player, this);
			Main.allboards.add(this);
			this.player = player;
			this.board = Bukkit.getScoreboardManager().getNewScoreboard();
			this.score = this.board.registerNewObjective("score", "dummy");
			this.score.setDisplaySlot(DisplaySlot.SIDEBAR);
			this.score.setDisplayName((String)e.getTitle().get(0));
			
			for(String s : e.getContent()) {
				Team t = this.board.registerNewTeam("Team:" + this.index);
				String o = s;
				String n = "";
				s = ReplaceString.replaceString(s, this.player);
				this.compare.put(t, s);
				s = this.check(s);
				if(s.length() > 16) {
			        Iterator<String> iterator = this.Splitter(s);
			        String prefix = iterator.next();
			        t.setPrefix(prefix);
			        n = ChatColor.getLastColors(prefix) + iterator.next();
					if(Files.config1.getBoolean("CTSNC.SCOREBOARD.DEBUG") == true) {
						Bukkit.getConsoleSender().sendMessage("n: " + n);
					}

					this.SuffixController(iterator, prefix, s.length(), t);
				} else {
					t.setPrefix(s);
				}
				if(Files.config1.getBoolean("CTSNC.SCOREBOARD.DEBUG") == true) {
					Bukkit.getConsoleSender().sendMessage("Prefix: " + t.getPrefix() + " §fName: " + t.getName() + " §fSuffix: " + t.getSuffix());
				}

				String score = n + (String)this.chlist.get(this.index - 1);
				t.addEntry(score);
				
				this.score.getScore(score).setScore(this.index);
				this.scores.put(this.index, score);
				this.teams.add(t);
				this.lot.put(t, o);
				this.index -= 1;
			}
			player.setScoreboard(this.board);
		}			
	}
	   
	private void lines() {
		this.chlist.add("§1");
		this.chlist.add("§2");
		this.chlist.add("§3");
		this.chlist.add("§4");
		this.chlist.add("§5");
		this.chlist.add("§6");
		this.chlist.add("§7");
		this.chlist.add("§8");
		this.chlist.add("§9");
		this.chlist.add("§a");
		this.chlist.add("§b");
		this.chlist.add("§c");
		this.chlist.add("§d");
		this.chlist.add("§e");
		this.chlist.add("§f");
	}
	
	
	public void remove() {
		/*
		new BukkitRunnable() {

			@Override
			public void run() {
				if(Main.boards.containsKey(player)) {
					Main.boards.remove(player);
					Main.allboards.remove(this);
					player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
				}
			}			
		}.runTask(Main.instance);
		*/
		/*
		 * Both methods work, but will use the first for now!
		 * Working method, just disabled for testing.
		 * 
		 */
		for(Iterator<Player> m = Main.boards.keySet().iterator(); Main.boards.keySet().iterator().hasNext();) {
			Player pt = m.next();
			if(Main.boards.containsKey(this.player) && this.player.equals(pt)) {
				Main.boards.remove(pt);
				Main.allboards.remove(this);
				this.player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
				break;
			}
		}
		
	}
	
	public void updateTitle() {
		if(this.titleindex > this.title.size() - 1) {
			this.titleindex = 0;
		}
		String s = this.title.get(this.titleindex);
		final String buffer = s;
		new BukkitRunnable() {

			@Override
			public void run() {
				replacer = ReplaceString.replaceString(buffer, player);		
			}			
		}.runTask(Main.instance);
		s = replacer;
		if(s == null) {
			return;
		}
		if(s.length() > this.maxchars) {
			s = s.substring(0, this.maxchars);
		}
		this.score.setDisplayName(s);
		this.titleindex += 1;
		
	}
	
	public void updateContent() {
		try {
			int i = 15;
			for(Team t : this.teams) {
				String n = "";
				String s = (String)this.lot.get(t);
				if(Files.config1.getBoolean("CTSNC.SCOREBOARD.DEBUG") == true) {
					Bukkit.getConsoleSender().sendMessage("ContentBeforeReplace: " + s);
				}
				s = ReplaceString.replaceString(s, this.player);
				String compare = (String) this.compare.get(t);
				if(Files.config1.getBoolean("CTSNC.SCOREBOARD.DEBUG") == true) {
					Bukkit.getConsoleSender().sendMessage("ContentAfterReplace: " + s);
					Bukkit.getConsoleSender().sendMessage("CompareString" + compare);
					Bukkit.getConsoleSender().sendMessage("CompareEquals: " + s.equals(compare));
				}
//				if(s.equals(compare)) {
//					continue;
//				}
				if(s.length() > 16) {
			        Iterator<String> iterator = this.Splitter(s);
			        String prefix = iterator.next();
			        t.setPrefix(prefix);
			        n = ChatColor.getLastColors(prefix) + iterator.next();
					if(Files.config1.getBoolean("CTSNC.SCOREBOARD.DEBUG") == true) {
						Bukkit.getConsoleSender().sendMessage("n: " + n);
					}
					this.SuffixController(iterator, prefix, s.length(), t);
					String score = n + (String)this.chlist.get(i - 1);
					this.refreshScores(i, score, t);
				} else {
					t.setPrefix(s);
				}
				if(Files.config1.getBoolean("CTSNC.SCOREBOARD.DEBUG") == true) {
					Bukkit.getConsoleSender().sendMessage(this.player.getName());
					Bukkit.getConsoleSender().sendMessage(t.getEntries().size() + "");
				}
				i -= 1;
				
			}
		} catch(IllegalStateException ex) {
			Bukkit.getConsoleSender().sendMessage("§c[§eCTSNC-Scoreboard§c] §4A Error appeared! (IllegalStateException)");
			Bukkit.getConsoleSender().sendMessage("§c[§eCTSNC-Scoreboard§c] §4Be sure that you have no other Scoreboard plugins installed!");
		}
	}
	
	private void SuffixController(Iterator<String> iterator, String prefix, int length, Team team) {
		if(length > 32) {
			String colorCode = ChatColor.getLastColors(prefix);
			String suffix  = colorCode + iterator.next();
			if(suffix.length() > 16) {
				suffix = suffix.substring(0, 16);
			}
			team.setSuffix(suffix);
		} else {
			team.setSuffix("");
		}
	}
	
	private Iterator<String> Splitter(String s) {
        Iterator<String> iterator = Splitter.fixedLength(16).split(s).iterator();
        return iterator;
	}
	
	private String check(String text) {
		if(text.length() > 48) {
			text = text.substring(0, 47);
		}
		return text;
	}
		
	@SuppressWarnings("unlikely-arg-type")
	private void refreshScores(int scoreCount, String score, Team team) {
		if(this.board.getScores(this.scores.get(scoreCount)).equals(this.score.getScore(score))) {
			return;
		}
		this.board.resetScores(this.scores.get(scoreCount));
		if(!team.getEntries().contains(score)) {
			team.removeEntry(this.scores.get(scoreCount));
			team.addEntry(score);
		}
		this.scores.remove(scoreCount);
		this.score.getScore(score).setScore(scoreCount);
		this.scores.put(scoreCount, score);
	}
}
