package me.menexia.contraband;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

public class CBItemPrompt extends ValidatingPrompt {
	
	@Override
	public String getPromptText(ConversationContext context) {
		Conversable sender = context.getForWhom();
		if (context.getSessionData(0) == null) {
			sender.sendRawMessage(ChatColor.YELLOW + "------------------------------------------------------");
			sender.sendRawMessage(ChatColor.GOLD + "Now entering: Contraband Configuration Interface");
			sender.sendRawMessage(ChatColor.GOLD + "Part 1 of 3: Contraband Items");
			sender.sendRawMessage(ChatColor.RED + "You will not receive public chat until you exit this interface.");
			sender.sendRawMessage(ChatColor.WHITE + "You may type these commands anytime within the interface:");
			sender.sendRawMessage(ChatColor.GRAY + "/exit" + ChatColor.WHITE + " : Exits the interface");
			sender.sendRawMessage(ChatColor.YELLOW + "------------------------------------------------------");
			sender.sendRawMessage(ChatColor.RED + "Item names and item ids are accepted as valid inputs.");
			sender.sendRawMessage(ChatColor.WHITE + "When you are finished entering items, type: " + ChatColor.GREEN + "/done");
			context.setSessionData(0, 0);
		}
		if (context.getSessionData(0).equals(0)) {
			return "Enter contraband item:";
		} else if (context.getSessionData(0).equals(1)) {
			sender.sendRawMessage(ChatColor.RED + "You have not entered any contraband items!");
			sender.sendRawMessage(ChatColor.RED + "If you want to exit, type: " + ChatColor.WHITE + "/exit");
			return ChatColor.GREEN + "If not, enter contraband item:";
		} else {
			return ChatColor.RED + "Unrecognized item; " + ChatColor.GREEN + "re-enter contraband item:";
		}
	}
	
	@Override
	protected boolean isInputValid(ConversationContext context, String input) {
		Conversable sender = context.getForWhom();
		input = input.trim();
		if (input.equalsIgnoreCase("/done")) {
			if (context.getSessionData(1) == null) {
				context.setSessionData(0, 1);
				return false;
			}
			context.setSessionData(0, 0);
			return true;
		}
		
		// Try parsing the input as an integer
		Material mat;
		try {
			int id = Integer.parseInt(input);
			mat = Material.getMaterial(id);
		} catch (NumberFormatException nfe) {
			// Didn't parse? Check it as a name.
			input = input.replaceAll(" ", "_").toUpperCase();
			mat = Utils.findMaterial(input);
		}
		if (mat != null) {
			if (context.getSessionData(1) == null) {
				context.setSessionData(1, 1);
			} else {
				context.setSessionData(1, (Integer)context.getSessionData(1)+1);
			}
			int i = (Integer)context.getSessionData(1)-1;
			context.setSessionData("itemID."+i, mat.getId());
			sender.sendRawMessage(ChatColor.WHITE + "Item recognized as: " + ChatColor.GREEN + mat.toString());
			context.setSessionData(0, 0);
			return true;
		}
		context.setSessionData(0, -1);
		return false;
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		if (input.equalsIgnoreCase("/done")) {
			return new CBSingleEffectsPrompt();
		} else {
			return this;
		}
	}
	
}
