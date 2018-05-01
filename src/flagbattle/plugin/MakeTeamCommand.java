package flagbattle.plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;

public class MakeTeamCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String lavel, String[] args) {

		// senderの検証 コンソールからの実行禁止
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "ゲーム内から実行してください.");
			return false;
		}
		Player player = (Player) sender;

		// 実行権限
		if (!(player.hasPermission("some.pointless.permisssion"))) {
			player.sendMessage(ChatColor.RED + "あなたにこのコマンドの実行権限はありません.");
			return false;
		}

		// パラメタ長の検証
		if (args.length < 1) {
			player.sendMessage(ChatColor.RED + "コマンドの後にチーム名を入力. その後チーム色を指定. 必要ならばその後にプレイヤー名を連ねてください. ");
			return false;
		}

		// チーム作成
		String teamname = args[0];
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getMainScoreboard();
		Team team = null;

		// もしチームがすでに存在したら作成しない
		if (board.getTeams().size()>1) {
			player.sendMessage(ChatColor.RED + "チームが2つ存在しているため, チームの新規作成はできません.");
			return true;
		}
		for (Team tmp : board.getTeams()) {
			if (tmp.getName().equals(teamname)) {
				team = tmp;
				player.sendMessage(team.getColor() + args[0] + ChatColor.RED + "チームは存在するため, チームの新規作成は行いませんでした. ");
				break;
			}
		}

		if (team == null) {
			team = board.registerNewTeam(teamname);
			team.setAllowFriendlyFire(true);
			team.setCanSeeFriendlyInvisibles(true);
			player.sendMessage(ChatColor.WHITE + "チームを作成しました. ");
		}

		if (args.length != 1) {
			// プレイヤーをチームに加入
			for (int i=1; i<args.length; i++) {
				Player target = Bukkit.getPlayerExact(args[i]);
				boolean flag_target = false;
				for (Team foo : board.getTeams()) {
					if (foo.hasEntry(args[i])) {
						player.sendMessage("   " + ChatColor.YELLOW + args[i] + "さんは既に" + foo.getColor() + foo.getName() + ChatColor.YELLOW + "チームに加入しています. チームへの加入を見送ります. ");
						flag_target = true;
						break;
					}
				}
				if (flag_target) {
					continue;
				}

				if (target==null) {
					player.sendMessage("   " + ChatColor.YELLOW + args[i] + "さんを認識できませんでした.");
				} else {
					team.addEntry(args[i]);
					player.sendMessage("   " + ChatColor.WHITE + args[i] + "さんを加入させました. ");
				}
			}
		}

		// チーム色を指定
		if (team.getColor().isColor()) {
			return true;
		}
		IChatBaseComponent teamcolor = ChatSerializer
				.a("{\"text\":\"チームカラーを指定してください.\n\", \"color\":\"green\", \"extra\":["
						+ "{\"text\":\"<赤>\", \"color\":\"red\", \"clickEvent\":{\"action\":\"run_command\", \"value\":\"/scoreboard teams option " + teamname + " color red\"}}, "
						+ "{\"text\":\"<青>\", \"color\":\"blue\", \"clickEvent\":{\"action\":\"run_command\", \"value\":\"/scoreboard teams option " + teamname + " color blue\"}}, "
						+ "{\"text\":\"<黄>\", \"color\":\"yellow\", \"clickEvent\":{\"action\":\"run_command\", \"value\":\"/scoreboard teams option " + teamname + " color yellow\"}}, "
						+ "{\"text\":\"<緑>\", \"color\":\"green\", \"clickEvent\":{\"action\":\"run_command\", \"value\":\"/scoreboard teams option " + teamname + " color green\"}} "
						+ "]}");
		PacketPlayOutChat packet = new PacketPlayOutChat(teamcolor);
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
		return true;
	}

}
