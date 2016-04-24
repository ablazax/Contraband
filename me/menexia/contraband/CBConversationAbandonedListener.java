package me.menexia.contraband;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationContext;

public class CBConversationAbandonedListener implements ConversationAbandonedListener {
	private final Contraband plugin;
	public CBConversationAbandonedListener(Contraband plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void conversationAbandoned(ConversationAbandonedEvent e) {
		ConversationContext context = e.getContext();
		Conversable sender = context.getForWhom();
		if (e.gracefulExit()) {
			// Write contraband items list
			int singleEffectsSize = (Integer)context.getSessionData(1);
			List<Integer> myAwesomeList = new ArrayList<Integer>();
			for (int a=0; a<singleEffectsSize; a++) {
				myAwesomeList.add((Integer)context.getSessionData("itemID."+a));
			}
			Integer[] itemsList = new Integer[myAwesomeList.size()];
			itemsList = myAwesomeList.toArray(itemsList);
			plugin.getConfig().set("CONTRABAND_ITEMS", myAwesomeList);
			
			// Write boolean responses of single effects
			String tuna = "SINGLE";
			String base = tuna + "_EFFECTS.";
			plugin.getConfig().set(base + "BLINDNESS", context.getSessionData(tuna + "." + 1));
			plugin.getConfig().set(base + ".CONFUSION", context.getSessionData(tuna + "." + 2));
			plugin.getConfig().set(base + ".SLOW", context.getSessionData(tuna + "." + 3));
			plugin.getConfig().set(base + ".FAST_DIGGING", context.getSessionData(tuna + "." + 4));
			plugin.getConfig().set(base + ".SLOW_DIGGING", context.getSessionData(tuna + "." + 5));
			plugin.getConfig().set(base + ".JUMP", context.getSessionData(tuna + "." + 6));
			
			// Clear current arraylist
			plugin.effectList.clear();
			
			// Save single inputs into arraylist
			for (int a=1; a<7; a++) {
				String input = (String) context.getSessionData(tuna + "." + a);
				if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("true")) {
					String effect = null;
					switch (a) {
					case 1: effect = "BLINDNESS"; break;
					case 2: effect = "CONFUSION"; break;
					case 3: effect = "SLOW"; break;
					case 4: effect = "FAST_DIGGING"; break;
					case 5: effect = "SLOW_DIGGING"; break;
					case 6: effect = "JUMP"; break;
					}
					plugin.effectList.add(effect);
				}
			}
			
			// Write effect combinations and save to arraylist as well
			if (context.getSessionData(2) != null) {
				int combinedEffectsSize = (Integer)context.getSessionData(2)+1;
				for (int a=6; a<combinedEffectsSize; a++) {
					plugin.getConfig().set("COMBINED_EFFECTS."+a, (String[])context.getSessionData("COMBINED."+a));
					plugin.effectList.add(String.valueOf(a));
				}
			} else {
				ConfigurationSection combinedSection = plugin.getConfig().getConfigurationSection("COMBINED_EFFECTS");
				if (combinedSection != null) {
					for (String fx : combinedSection.getKeys(false)) {
						plugin.getConfig().set(fx, null);
					}
				}
			}
			
			plugin.saveConfig();
			plugin.reloadConfig();
			sender.sendRawMessage(ChatColor.GREEN + "Successfully wrote inputs to config.yml.");
			sender.sendRawMessage(ChatColor.YELLOW + "------------------------------------------------------");
			sender.sendRawMessage(ChatColor.GOLD + "Exited Contraband Configuration Interface.");
			sender.sendRawMessage(ChatColor.YELLOW + "------------------------------------------------------");
		} else {
			sender.sendRawMessage(ChatColor.RED + "Exited Contraband Configuration Interface.");
			sender.sendRawMessage(ChatColor.RED + "Type " + ChatColor.WHITE + "/contraband configure" + ChatColor.RED + " to re-enter interface.");
		}

	}

}
