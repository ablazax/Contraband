package me.menexia.contraband;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

/**
 * 
 * @author Xavier Luis Ablaza - MeneXia
 *
 */
public class Contraband extends JavaPlugin {
	public Logger logger;
	ConversationFactory factory;
	public List<String> effectList = new ArrayList<String>();
	
	public void onDisable() {
		this.logger.info("Version " + this.getDescription().getVersion() + " disabled!");
	}
	
	public void onEnable() {
		logger = this.getLogger();
		factory = new ConversationFactory(this);
		
		checkConfig();
		
		String suffix = "_EFFECTS";
		ConfigurationSection singleSection = this.getConfig().getConfigurationSection("SINGLE" + suffix);
		ConfigurationSection combinedSection = this.getConfig().getConfigurationSection("COMBINED" + suffix);
		if (singleSection != null) {
			for (String fx : singleSection.getKeys(false)) {
				if (singleSection.getString(fx).equalsIgnoreCase("yes") || singleSection.getString(fx).equalsIgnoreCase("true")) {
					effectList.add(fx);
				}
			}	
		}
		if (combinedSection != null) {
			for (String fx : combinedSection.getKeys(false)) {
				effectList.add(fx);
			}
		}
		new ContrabandListener(this);
		this.logger.info("Version " + this.getDescription().getVersion() + " enabled!");
	}
	
	public int getAmplifier(PotionEffectType type) {
		String effect = type.getName();
		if (effect.equalsIgnoreCase("blindness") || effect.equalsIgnoreCase("confusion")
				|| effect.equalsIgnoreCase("fast_digging") || effect.equalsIgnoreCase("jump")) {
			return 0;
		} else if (effect.equalsIgnoreCase("slow_digging")) {
			return 3;
		} else if (effect.equalsIgnoreCase("slow")) {
			return 17;
		}
		return 9001; // IT'S OVER 9000!
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String zhf, String[] args) {
		if (cmd.getName().equalsIgnoreCase("contraband")) {
			if (sender instanceof ConsoleCommandSender) {
				ConsoleCommandSender conversable = (ConsoleCommandSender)sender;
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("configure")) {
						Conversation conv = factory
								.withFirstPrompt(new CBItemPrompt())
								.withEscapeSequence("/exit")
								.withLocalEcho(false)
								.buildConversation(conversable);
						conv.addConversationAbandonedListener(new CBConversationAbandonedListener(this));
						conv.begin();
						return true;
					}
				}
			}
//			if (args.length == 2) {
//				PotionEffectType type = PotionEffectType.getByName(args[0].toUpperCase());
//				Player p = (Player)sender;
//				p.addPotionEffect(new PotionEffect(type, 1200, Integer.parseInt(args[1])), true);
//				return true;
//			}
//			if (args.length == 2) {
//				if (args[0].equalsIgnoreCase("print") && args[1].equalsIgnoreCase("list")) {
//					for (String s : this.effectList) {
//						sender.sendMessage(s);
//					}
//				}
//			}
		}
		return false;
	}
	
	public void checkConfig() {
		String name = "config.yml";
		File actual = new File(getDataFolder(), name);
		if (!actual.exists()) {
			getDataFolder().mkdir();
			InputStream input = getClass().getResourceAsStream("/defaults/config.yml");
			if (input != null) {
				FileOutputStream output = null;

				try {
					output = new FileOutputStream(actual);
					byte[] buf = new byte[4096]; //[8192]?
					int length = 0;
					while ((length = input.read(buf)) > 0) {
						output.write(buf, 0, length);
					}
					this.logger.info("Default configuration file written: " + name);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (input != null)
							input.close();
					} catch (IOException e) {}

					try {
						if (output != null)
							output.close();
					} catch (IOException e) {}
				}
			}
		}
	}
	
}
