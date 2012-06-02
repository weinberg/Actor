package com.insofar.actor.listeners;

import net.minecraft.server.Packet53BlockChange;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.insofar.actor.ActorPlugin;
import com.insofar.actor.author.Author;

public class AuthorBlockListener implements Listener {

	public ActorPlugin plugin;

	public AuthorBlockListener(ActorPlugin instance)
	{
		plugin = instance;
	}

	@EventHandler
	public void onBlockDamage(BlockDamageEvent event) {
		System.out.println("Block damage");
	}

	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event) {
		System.out.println("Block ignite");
	}

	@EventHandler
	public void onBlockPhysics(BlockPhysicsEvent event) {
		System.out.println("Block physics");
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		System.out.println("Block place");
		Player p = event.getPlayer();

		Author author = plugin.authors.get(p.getName());
		if (author != null && author.isRecording)
		{
			Packet53BlockChange packet = new Packet53BlockChange();

			int xPosition = event.getBlock().getX();
			int yPosition = event.getBlock().getY();
			int zPosition = event.getBlock().getZ();
			int type = event.getBlock().getTypeId();
			int data = event.getBlock().getData();

			packet.a=xPosition;
			packet.b=yPosition;
			packet.c=zPosition;
			packet.material = type;
			packet.data = data;

			author.currentRecording.recordPacket(packet);
			addRewindForBlockChange(author, xPosition, yPosition, zPosition, 0, 0);
		}
	}

	@EventHandler
	public void onBlockBurn(BlockBurnEvent event) {
		System.out.println("Block burn");
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		System.out.println("Block break");
		Player p = event.getPlayer();

		Author author = plugin.authors.get(p.getName());
		if (author != null && author.isRecording)
		{

			Packet53BlockChange packet = new Packet53BlockChange();

			int xPosition = event.getBlock().getX();
			int yPosition = event.getBlock().getY();
			int zPosition = event.getBlock().getZ();
			int type = 0;
			int data = 0;

			packet.a=xPosition;
			packet.b=yPosition;
			packet.c=zPosition;
			packet.material = type;
			packet.data = data;

			author.currentRecording.recordPacket(packet);
			
			// Rewind packet
			
			type = event.getBlock().getType().getId();
			data = event.getBlock().getData();
			
			addRewindForBlockChange(author, xPosition, yPosition, zPosition, data, type);
		}
	}

	public void addRewindForBlockChange(Author author, int i, int j, int k, int l, int m)
	{
		Packet53BlockChange changeBack = new Packet53BlockChange();

		changeBack.a = i;
		changeBack.b = j;
		changeBack.c = k;
		changeBack.data = l;
		changeBack.material = m;

		author.currentRecording.addRewindPacket(changeBack);
	}

}
