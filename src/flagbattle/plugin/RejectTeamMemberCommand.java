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

public class RejectTeamMemberCommand implements CommandExecutor {

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
			player.sendMessage(ChatColor.RED + "コマンドの後にチーム名を入力. \nその後にプレイヤー名を入力");
			return false;
		}

		// チーム作成
		String teamname = args[0];
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getMainScoreboard();
		Team team = board.getTeam(teamname);

		// メンバー除去
		boolean flag_target=false;
		if (team.getEntries().size() == 0) {
			player.sendMessage(ChatColor.YELLOW + args[0] + "チームにメンバーが存在しません. ");
			return false;
		}
		for (String pl : team.getEntries()) {
			if (pl.equals(args[1])) {
				team.removeEntry(args[1]);
				player.sendMessage(ChatColor.GREEN + args[1] + "さんを" + args[0] + "チームから削除しました. ");
				flag_target=true;
				break;
			}
		}
		if (!flag_target) {
			player.sendMessage(ChatColor.YELLOW + args[1] + "さんは" + args[0] + "チームに存在しません. ");
		}

		return true;
	}

}
