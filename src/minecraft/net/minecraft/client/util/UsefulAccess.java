package net.minecraft.client.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;

public final class UsefulAccess {
	
	private static final Logger LOGGER = LogManager.getLogger();
	public final Minecraft mcInstance;
	
	public UsefulAccess(Minecraft mcInstance) {
		this.mcInstance = mcInstance;
	}
}
