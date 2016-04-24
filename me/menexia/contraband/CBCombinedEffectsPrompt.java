package me.menexia.contraband;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.potion.PotionEffectType;

public class CBCombinedEffectsPrompt extends ValidatingPrompt {

	@Override
	public String getPromptText(ConversationContext context) {
		Conversable sender = context.getForWhom();
		if (context.getSessionData(0).equals(0)) {
			sender.sendRawMessage(ChatColor.YELLOW + "------------------------------------------------------");
			sender.sendRawMessage(ChatColor.GOLD + "Part 3 of 3: Combined Effects");
			sender.sendRawMessage(ChatColor.WHITE + "You will now configure custom, combined effects.");;
			sender.sendRawMessage(ChatColor.GRAY + "Valid Effects: BLINDNESS, NAUSEA, FLIPPED_SCREEN,");
			sender.sendRawMessage(ChatColor.GRAY + "HASTE, FATIGUE, JUMPBOOST");
			sender.sendRawMessage(ChatColor.YELLOW + "------------------------------------------------------");
			sender.sendRawMessage(ChatColor.WHITE + "When you are finished entering your effects,");
			sender.sendRawMessage(ChatColor.WHITE + "or if you don't want to enter any, type: " + ChatColor.GREEN + "/done");
			sender.sendRawMessage(ChatColor.GRAY + "Example input: Blindness, Nausea, Flipped_Screen");
		} else if (context.getSessionData(0).equals(-1)) {
			return ChatColor.RED + "Unrecognized effect type(s); " + ChatColor.GREEN + "re-enter effects:";
		}
		return ChatColor.GREEN + "Enter your effect combination:";
	}
	
	@Override
	protected boolean isInputValid(ConversationContext context, String input) {
		input = input.trim();
		if (input.equalsIgnoreCase("/done")) {
			return true;
		}
		input = input.trim().toUpperCase().replaceAll("JUMPBOOST", "JUMP").replaceAll("NAUSEA", "CONFUSION")
				.replaceAll("FLIPPED_SCREEN", "SLOW").replaceAll("HASTE", "FAST_DIGGING").replaceAll("FATIGUE", "SLOW_DIGGING");
		String[] parts = input.split(", ");
		for (int a=0; a<parts.length; a++) {
			try {
				if (PotionEffectType.getByName(parts[a]).getName().equals(parts[a])) {
					continue;
				} else {
					context.setSessionData(0, -1);
					return false;
				}
			} catch (NullPointerException nfe) {
				context.setSessionData(0, -1);
				return false;
			}
		}
		if (context.getSessionData(2) == null) {
			context.setSessionData(2, 6);
		} else {
			context.setSessionData(2, (Integer)context.getSessionData(2)+1);
		}
		int i = (Integer)context.getSessionData(2);
		context.setSessionData("COMBINED."+i, parts);
		context.setSessionData(0, 1);
		context.getForWhom().sendRawMessage(ChatColor.GREEN + "Combination saved.");
		return true;
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		if (input.equalsIgnoreCase("/done")) {
			return END_OF_CONVERSATION;
		} else {
			return this;
		}
	}

}
