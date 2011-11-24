package com.insofar.actor.author;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Packet;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;

public class Viewer {

	public Player player;
	//public ArrayList<EntityActor> actors = new ArrayList<EntityActor>();
	
	private MinecraftServer minecraftServer;
	
	public Viewer(Player player) {
		this.player = player;
		minecraftServer = ((CraftServer)Bukkit.getServer()).getServer();
	}

	public void sendPacket(Packet packet)
	{
		minecraftServer.serverConfigurationManager.a(player.getName(),packet);
	}
}
