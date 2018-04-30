package flagbattle.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import net.md_5.bungee.api.ChatColor;

public class FlagBattle extends JavaPlugin {

	private Scoreboard s;

	@Override
	public void onDisable() {
		// TODO 自動生成されたメソッド・スタブ
		super.onDisable();
	}

	@Override
	public void onEnable() {
		// TODO 自動生成されたメソッド・スタブ
		super.onEnable();

		getCommand("startgame").setExecutor(new StartCommand());
		getCommand("stopgame").setExecutor(new StopCommand());
		getCommand("maketeam").setExecutor(new MakeTeamCommand());
		getCommand("removeteam").setExecutor(new RemoveTeamCommand());
		getCommand("leadteam").setExecutor(new LeadTeamCommand());
		getCommand("addteammember").setExecutor(new AddTeamMemberCommand());
		getCommand("rejectteammember").setExecutor(new RejectTeamMemberCommand());
		getCommand("listteam").setExecutor(new ListTeamCommand());
		getCommand("statusteam").setExecutor(new StatusTeamCommand());

		s = Bukkit.getScoreboardManager().getMainScoreboard();
		registerHealthBar();
	}

	public void registerHealthBar() {
		if (s.getObjective("health") != null) {
			s.getObjective("health").unregister();
		}
		Objective o = s.registerNewObjective("health", "health");
		o.setDisplayName(ChatColor.RED + "♥");
		o.setDisplaySlot(DisplaySlot.BELOW_NAME);
	}
}
