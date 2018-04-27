package flagbattle.plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import net.md_5.bungee.api.ChatColor;

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
			player.sendMessage(ChatColor.RED + "コマンドの後にチーム名を入力. \n必要ならばその後にプレイヤー名を連ねてください.");
			return false;
		}

		// チーム作成
		String teamname = args[0];
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getMainScoreboard();
		Team team = null;

		// もしチームがすでに存在したら作成しない
		for (Team tmp : board.getTeams()) {
			if (tmp.getName().equals(teamname)) {
				team = tmp;
				player.sendMessage(ChatColor.RED + args[0] + "チームは存在するため、チームの新規作成は行いませんでした.");
				break;
			}
		}
		if (team == null) {
			team = board.registerNewTeam(teamname);
			team.setAllowFriendlyFire(true);
			team.setCanSeeFriendlyInvisibles(true);
			player.sendMessage(ChatColor.GREEN + args[0] + "チームを作成しました. ");
		}

		if (args.length == 1) {
			return true;
		}

		// プレイヤーをチームに加入
		for (int i=1; i<args.length; i++) {
			Player target = Bukkit.getPlayerExact(args[i]);
			boolean flag_target = false;
			for (Team foo : board.getTeams()) {
				if (foo.hasEntry(args[i])) {
					player.sendMessage(ChatColor.YELLOW + args[i] + "さんは既に" + foo.getName() + "チームに加入しています. \n" + args[0] + "チームへの加入を見送ります. ");
					flag_target = true;
					break;
				}
			}
			if (flag_target) {
				continue;
			}

			if (target==null) {
				player.sendMessage(ChatColor.YELLOW + args[i] + "さんを認識できませんでした.");
			} else {
				team.addEntry(args[i]);
				player.sendMessage(ChatColor.GREEN + args[i] + "さんを" + args[0] + "チームに加入させました. ");
			}
		}

		return true;
	}

}
