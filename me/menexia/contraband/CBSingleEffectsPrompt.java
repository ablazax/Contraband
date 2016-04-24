package me.menexia.contraband;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;

public class CBSingleEffectsPrompt extends ValidatingPrompt {

	@Override
	public String getPromptText(ConversationContext context) {
		Conversable sender = context.getForWhom();
		if (context.getSessionData(0).equals(-1)) {
			return ChatColor.RED + "Unrecognized boolean type; " + ChatColor.GREEN + "re-enter input:";
		} else if (context.getSessionData("SINGLE."+1) == null) {
			sender.sendRawMessage(ChatColor.YELLOW + "------------------------------------------------------");
			sender.sendRawMessage(ChatColor.GOLD + "Part 2 of 3: Single Effects");
			sender.sendRawMessage(ChatColor.WHITE + "You will now configure which single effects you want enabled");;
			sender.sendRawMessage(ChatColor.GRAY + "Valid Effects: BLINDNESS, NAUSEA, FLIPPED_SCREEN,");
			sender.sendRawMessage(ChatColor.GRAY + "HASTE, FATIGUE, JUMPBOOST");
			sender.sendRawMessage(ChatColor.YELLOW + "------------------------------------------------------");
			sender.sendRawMessage(ChatColor.GRAY + "Inputs must be boolean: [TRUE|FALSE|YES|NO]");
			return ChatColor.GREEN + "Enable Blindness Effect?:";
		} else if (context.getSessionData("SINGLE."+2).equals(0)) {
			return ChatColor.GREEN + "Enable Nausea Effect?:";
		} else if (context.getSessionData("SINGLE."+3).equals(0)) {
			return ChatColor.GREEN + "Enable Flipped Screen Effect?:";
		} else if (context.getSessionData("SINGLE."+4).equals(0)) {
			return ChatColor.GREEN + "Enable Haste Effect?:";
		} else if (context.getSessionData("SINGLE."+5).equals(0)) {
			return ChatColor.GREEN + "Enable Fatigue Effect?:";
		} else if (context.getSessionData("SINGLE."+6).equals(0)) {
			return ChatColor.GREEN + "Enable Jumpboost Effect?:";
		}
		return null;
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String input) {
		Conversable sender = context.getForWhom();
		input = input.trim().toUpperCase();
		if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no")
				|| input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {
			if (context.getSessionData("SINGLE."+1) == null) {
				context.setSessionData("SINGLE."+1, input);
				context.setSessionData("SINGLE."+2, 0);
				sender.sendRawMessage("Blindness Effect: " + input);
			} else if (context.getSessionData("SINGLE."+2).equals(0)) {
				context.setSessionData("SINGLE."+2, input);
				context.setSessionData("SINGLE."+3, 0);
				sender.sendRawMessage("Nausea Effect: " + input);
			} else if (context.getSessionData("SINGLE."+3).equals(0)) {
				context.setSessionData("SINGLE."+3, input);
				context.setSessionData("SINGLE."+4, 0);
				sender.sendRawMessage("Flipped Screen Effect: " + input);
			} else if (context.getSessionData("SINGLE."+4).equals(0)) {
				context.setSessionData("SINGLE."+4, input);
				context.setSessionData("SINGLE."+5, 0);
				sender.sendRawMessage("Haste Effect: " + input);
			} else if (context.getSessionData("SINGLE."+5).equals(0)) {
				context.setSessionData("SINGLE."+5, input);
				context.setSessionData("SINGLE."+6, 0);
				sender.sendRawMessage("Fatigue Effect: " + input);
			} else if (context.getSessionData("SINGLE."+6).equals(0)) {
				context.setSessionData("SINGLE."+6, input);
				sender.sendRawMessage("Jumpboost Effect: " + input);
				context.setSessionData(0, 0);
				return true;
			}
			context.setSessionData(0, 0);
			return false;
		}
		context.setSessionData(0, -1);
		return false;
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return new CBCombinedEffectsPrompt();
	}

}
