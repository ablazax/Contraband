package me.menexia.contraband;

import org.bukkit.Material;

public class Utils {
	
	public static Material findMaterial(String input) {
		Material mat = Material.matchMaterial(input);
		if (mat != null) {
			return mat;
		} else {
			if (input.equalsIgnoreCase("brownmushroom")) {
				return Material.BROWN_MUSHROOM;
			} else if (input.equalsIgnoreCase("redmushroom")) {
				return Material.RED_MUSHROOM;
			} else if (input.equalsIgnoreCase("sugarcane")
					|| input.equalsIgnoreCase("reeds") || input.equalsIgnoreCase("reed")) {
				return Material.SUGAR_CANE;
			} else if (input.equalsIgnoreCase("blazepowder")) {
				return Material.BLAZE_POWDER;
			} else if (input.equalsIgnoreCase("blazerod")) {
				return Material.BLAZE_ROD;
			} else if (input.equalsIgnoreCase("brewingstand")) {
				return Material.BREWING_STAND;
			} else if (input.equalsIgnoreCase("brickstairs")) {
				return Material.BRICK_STAIRS;
			} else if (input.equalsIgnoreCase("fermentedspidereye")) {
				return Material.FERMENTED_SPIDER_EYE;
			} else if (input.equalsIgnoreCase("ghasttear")) {
				return Material.GHAST_TEAR;
			} else if (input.equalsIgnoreCase("magmacream")) {
				return Material.MAGMA_CREAM;
			} else if (input.equalsIgnoreCase("rottenflesh")) {
				return Material.ROTTEN_FLESH;
			} else if (input.equalsIgnoreCase("spidereye")) {
				return Material.SPIDER_EYE;
			} else if (input.equalsIgnoreCase("speckledmelon") || input.equalsIgnoreCase("glisteringmelon")) {
				return Material.SPECKLED_MELON;
			}
		}
		return null;
	}

}
