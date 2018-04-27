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

public class ListTeamCommand implements CommandExecutor {

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
		if (args.length != 0) {
			player.sendMessage(ChatColor.RED + "コマンドは単体で実行します.  ");
			return false;
		}

		// チームを指定
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getMainScoreboard();

		if (board.getTeams().size() == 0) {
			player.sendMessage(ChatColor.YELLOW + "チームが存在しません. ");
			return false;
		}
		player.sendMessage(ChatColor.GREEN + "チーム リスト");
		for (Team tmp : board.getTeams()) {
			player.sendMessage("   " + tmp.getName() + " : " + tmp.getEntries().size() + "人");
		}

		return true;
	}

}
