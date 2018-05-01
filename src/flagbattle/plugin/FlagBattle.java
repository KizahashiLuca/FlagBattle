package flagbattle.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle.EnumTitleAction;

public class FlagBattle extends JavaPlugin {

	private Scoreboard s;
	public static Long starttime;
	public static Long finishtime;
	public static Player kiz = Bukkit.getPlayer("kizahashiluca");

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
		registerTimeBar();
		if (starttime!=null) {
			declarationRecieved();
			Game();
		}
	}

	// エンドポータルフレームにハマるのを確認したい
	public void declarationRecieved() {
		for( int i = 0; i<3; i++) {
			if (StartCommand.frame_a[i].getBlock().getType().equals(Material.REDSTONE_LAMP_ON)) {
				kiz.sendMessage("A");
				IChatBaseComponent chattitle = ChatSerializer
				.a("{\"text\": \"A 側 宣戦布告\", \"color\":\"red\"}");
				PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, chattitle);
				PacketPlayOutTitle length = new PacketPlayOutTitle(5, 20, 5);
				((CraftPlayer) kiz).getHandle().playerConnection.sendPacket(title);
				((CraftPlayer) kiz).getHandle().playerConnection.sendPacket(length);
			}
		}
		for( int i = 0; i<3; i++) {
			if (StartCommand.frame_a[i].getBlock().getType().equals(Material.REDSTONE_LAMP_ON)) {
				kiz.sendMessage("B");
				IChatBaseComponent chattitle = ChatSerializer
				.a("{\"text\": \"B 側 宣戦布告\", \"color\":\"red\"}");
				PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, chattitle);
				PacketPlayOutTitle length = new PacketPlayOutTitle(5, 20, 5);
				((CraftPlayer) kiz).getHandle().playerConnection.sendPacket(title);
				((CraftPlayer) kiz).getHandle().playerConnection.sendPacket(length);
			}
		}
	}
	public void Game() {
//		Long now = System.currentTimeMillis();
//		if ((finishtime-starttime == (24*60*60*1000) * 5) && (now >= finishtime)) {
//			FinishGame();
//			IChatBaseComponent chattitle = ChatSerializer
//					.a("{\"text\": \"終了！\", \"color\":\"red\"}");
//			PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, chattitle);
//			PacketPlayOutTitle length = new PacketPlayOutTitle(5, 20, 5);
//			for (Player player : Bukkit.getOnlinePlayers()) {
//				((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
//				((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
//			}
//		}else if ((finishtime-starttime != (24*60*60*1000) * 5) && (now >= finishtime)) {
//			FinishGame();
//			IChatBaseComponent chattitle = ChatSerializer
//					.a("{\"text\": \"強制終了！\", \"color\":\"red\"}");
//			PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, chattitle);
//			PacketPlayOutTitle length = new PacketPlayOutTitle(5, 20, 5);
//			for (Player player : Bukkit.getOnlinePlayers()) {
//				((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
//				((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
//			}
//		}
	}

	public void FinishGame() {
		starttime = null;
		finishtime = null;
	}

	public void registerTimeBar() {

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
