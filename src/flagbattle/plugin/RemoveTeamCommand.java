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

public class RemoveTeamCommand implements CommandExecutor {

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
		if (args.length != 1) {
			player.sendMessage(ChatColor.RED + "コマンドの後にチーム名を入力. \n必要ならばその後にプレイヤー名を連ねてください.");
			return false;
		}

		// チーム削除
		String teamname = args[0];
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getMainScoreboard();
		Team team = null;

		// チームが存在するかの確認
		for (Team tmp : board.getTeams()) {
			if (tmp.getName().equals(teamname)) {
				team = tmp;
				break;
			}
		}
		if (team == null) {
			player.sendMessage(ChatColor.YELLOW + "指定した" + args[0] + "チームは存在しません.");
		} else {
			team.unregister();
			player.sendMessage(ChatColor.GREEN + args[0] + "チームを削除しました. ");
		}

		return true;
	}

}
