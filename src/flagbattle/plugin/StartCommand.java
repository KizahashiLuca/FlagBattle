package flagbattle.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import net.md_5.bungee.api.ChatColor;

public class StartCommand implements CommandExecutor {

	public static Location[] frame_a = new Location[3];
	public static Location[] frame_b = new Location[3];

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
			player.sendMessage(ChatColor.RED + "コマンドの後に戦闘期間(日) を設定してください.");
			return false;
		}

		// チーム数を確認
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getMainScoreboard();
		if (board.getTeams().size() != 2) {
			player.sendMessage(ChatColor.RED + "チームは2つ無ければ遊べません. ");
			return true;
		}

		List<String> teams = new ArrayList<String>();
		for (Team foo : board.getTeams()) {
			teams.add(foo.getName());
		}

		// 時間設定
		Long term = (24*60*60*1000) * Long.parseLong(args[0], 10);
		FlagBattle.starttime = System.currentTimeMillis();
		FlagBattle.finishtime = FlagBattle.starttime + term;

		// 今いるチャンクに中立地帯を作成
		Location current = player.getLocation();
		Chunk c = player.getLocation().getChunk();
		Location NorthWest = new Location(c.getWorld(), c.getX() << 4 , current.getY(), c.getZ() << 4);

		for (int x=0; x<16; x++) {
			for (int z=0; z<16; z++) {
				for (int y=0; y<(256-NorthWest.getY()); y++) {
					Location emp = NorthWest.clone().add(x, y, z);
					emp.getBlock().setType(Material.AIR);
				}
				for (int y=-4; y<0; y++) {
					Location set = NorthWest.clone().add(x, y, z);
					set.getBlock().setType(Material.BEDROCK);
				}
			}
		}

		// 宣戦布告スペース作成
		for (int i=0; i<2; i++) {
			for (int j=0; j<3; j++) {
				Location frame = NorthWest.clone().add(5*i+5, 0, j+6);
				frame.getBlock().setType(Material.ENDER_PORTAL_FRAME);
				Location observer = frame.clone().add(0, -1, 0);
				observer.getBlock().setType(Material.OBSERVER);
				BlockState ob = observer.getBlock().getState();
				((org.bukkit.material.Observer) ob.getData()).setFacingDirection(BlockFace.UP);
				ob.update();
				Location light = frame.clone().add(0, -3, 0);
				light.getBlock().setType(Material.REDSTONE_LAMP_OFF);
				if(i==0) {
					frame_a[j] = light;
					//player.sendMessage("a    " + frame_a[j].getX() + ", " + frame_a[j].getY() + ", " + frame_a[j].getZ());
				} else {
					frame_b[j] = light;
					//player.sendMessage("b    " + frame_b[j].getX() + ", " + frame_b[j].getY() + ", " + frame_b[j].getZ());
				}
			}
			Location chest = NorthWest.clone().add(5*i+5, 0, 9);
			chest.getBlock().setType(Material.CHEST);
			BlockState cs = (Chest) chest.getBlock().getState();
			if (i==0) {
				((org.bukkit.material.Chest) cs.getData()).setFacingDirection(BlockFace.EAST);
			} else {
				((org.bukkit.material.Chest) cs.getData()).setFacingDirection(BlockFace.WEST);
			}
			Team team = board.getTeam(teams.get(i));
			((Chest) cs).setCustomName( team.getColor() +  teams.get(i) + ChatColor.BLACK + " Team 's Chest " );
			cs.update();

			ItemStack items = (new ItemStack(Material.EYE_OF_ENDER, 1));
			List<String> lore = Arrays.asList("エンドポータルフレームに", "はめて宣戦布告完了" );
			ItemMeta info = items.getItemMeta();
			info.setDisplayName("宣戦布告用アイテム");
			info.setLocalizedName("declaration_item");
			info.setLore(lore);
			items.setItemMeta(info);
			Inventory ch = ((Chest) cs).getInventory();
			for (int k=0; k<3; k++) {
				ch.setItem(12+k, items);
			}
		}
		List<Entity> entlist = c.getWorld().getEntities();
		for (Entity cr : entlist) {
			if (cr instanceof Item && ((Item) cr).getItemStack().getType() == Material.EYE_OF_ENDER) {
				cr.remove();
			}
		}

		return true;
	}

}
