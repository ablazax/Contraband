package me.menexia.contraband;

import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ContrabandListener implements Listener {
	private final Contraband plugin;
	public ContrabandListener(Contraband plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onRightClick(final PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Player p = e.getPlayer();
			if (p.isSneaking()) {
				if (e.hasItem()) {
					if (plugin.getConfig().getIntegerList("CONTRABAND_ITEMS").contains(e.getMaterial().getId())) {
						if (plugin.effectList.size() == 0) return;
						int random = new Random().nextInt(plugin.effectList.size());
//						plugin.logger.info(String.valueOf(random));
						String effect = plugin.effectList.get(random);
//						plugin.logger.info(effect);
						PotionEffectType type = PotionEffectType.getByName(effect);
						if (type != null) {
//							plugin.logger.info("single");
							p.addPotionEffect(new PotionEffect(type, 1200, plugin.getAmplifier(type)), true);
						} else {
//							plugin.logger.info("group");
							List<String> combinedEffect = plugin.getConfig().getStringList("COMBINED_EFFECTS."+effect);
							for (String s : combinedEffect) {
//								plugin.logger.info(s);
								PotionEffectType awesometype = PotionEffectType.getByName(s);
								p.addPotionEffect(new PotionEffect(awesometype, 1200, plugin.getAmplifier(awesometype)), true);
							}
						}
						if (p.getItemInHand().getAmount() == 1) {
							p.setItemInHand(null);
						} else {
							p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
						}
						p.sendMessage(ChatColor.RED + "You've consumed the drug.");
					}
				}
			}
		}
	}

}
