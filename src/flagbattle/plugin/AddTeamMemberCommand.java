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

public class AddTeamMemberCommand implements CommandExecutor {

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
		if (args.length != 2) {
			player.sendMessage(ChatColor.RED + "コマンドの後にチーム名を入力. その後にプレイヤー名を入力. ");
			return false;
		}

		// チーム指定
		String teamname = args[0];
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getMainScoreboard();
		Team team = board.getTeam(teamname);

		// メンバー加入
		Player target = Bukkit.getPlayerExact(args[1]);
		boolean flag_target = false;
		for (Team foo : board.getTeams()) {
			if (foo.hasEntry(args[1])) {
				player.sendMessage(ChatColor.YELLOW + args[1] + "さんは既に" + foo.getColor() + foo.getName() + ChatColor.YELLOW + "チームに加入しています. チームへの加入を見送ります. ");
				flag_target = true;
				break;
			}
		}
		if (flag_target) {
			return true;
		}

		if (target==null) {
			player.sendMessage(ChatColor.YELLOW + args[1] + "さんを認識できませんでした.");
		} else {
			team.addEntry(args[1]);
			player.sendMessage(ChatColor.WHITE + args[1] + "さんを" + team.getColor() + args[0] + ChatColor.WHITE + "チームに加入させました. ");
		}

		return true;
	}

}
