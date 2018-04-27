package flagbattle.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public class FlagBattle extends JavaPlugin {

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

	}

}
