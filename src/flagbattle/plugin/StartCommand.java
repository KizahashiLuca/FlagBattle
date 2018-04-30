package flagbattle.plugin;

import java.util.Arrays;
import java.util.List;

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

import net.md_5.bungee.api.ChatColor;

public class StartCommand implements CommandExecutor {

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
			player.sendMessage(ChatColor.RED + "コマンドは単体で実行します.");
			return false;
		}

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
				Location set = NorthWest.clone().add(x, -1, z);
				set.getBlock().setType(Material.BEDROCK);
			}
		}

		// 宣戦布告スペース作成
		for (int i=0; i<2; i++) {
			for (int j=0; j<3; j++) {
				Location tmp = NorthWest.clone().add(5*i+5, 0, j+6);
				tmp.getBlock().setType(Material.ENDER_PORTAL_FRAME);
			}
			Location chest = NorthWest.clone().add(5*i+5, 0, 9);
			chest.getBlock().setType(Material.CHEST);
			BlockState cs = (Chest) chest.getBlock().getState();
			if (i==0) {
				((org.bukkit.material.Chest) cs.getData()).setFacingDirection(BlockFace.EAST);
			} else {
				((org.bukkit.material.Chest) cs.getData()).setFacingDirection(BlockFace.WEST);
			}
			cs.update();

			ItemStack items = (new ItemStack(Material.EYE_OF_ENDER, 3));
			List<String> lore = Arrays.asList("エンドポータルフレームに", "はめて宣戦布告完了" );
			ItemMeta info = items.getItemMeta();
			info.setDisplayName("宣戦布告用アイテム");
			info.setLocalizedName("declaration_item");
			info.setLore(lore);
			items.setItemMeta(info);
			Inventory ch = ((Chest) cs).getInventory();
			ch.setItem(13, items);
		}
		List<Entity> entlist = c.getWorld().getEntities();
		for (Entity cr : entlist) {
			if (cr instanceof Item && ((Item) cr).getItemStack().getType() == Material.EYE_OF_ENDER) {
				cr.remove();
			}
		}

		//

		return true;
	}

}
