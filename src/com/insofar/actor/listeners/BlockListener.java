package com.insofar.actor.listeners;

import net.minecraft.server.Packet53BlockChange;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.insofar.actor.ActorPlugin;
import com.insofar.actor.Author;

public class BlockListener implements Listener
{

	public ActorPlugin plugin;

	public BlockListener(ActorPlugin instance)
	{
		// System.out.println("Author Block Listener init");
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockDamage(BlockDamageEvent event)
	{
		if (event.isCancelled()
				&& !plugin.getRootConfig().includeCancelledEvents)
		{
			return;
		}
		if (plugin.getRootConfig().debugEvents)
		{
			plugin.getLogger().info("Block damage");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockIgnite(BlockIgniteEvent event)
	{
		if (event.isCancelled()
				&& !plugin.getRootConfig().includeCancelledEvents)
		{
			return;
		}
		if (plugin.getRootConfig().debugEvents)
		{
			plugin.getLogger().info("Block ignite");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPhysics(BlockPhysicsEvent event)
	{
		if (event.isCancelled()
				&& !plugin.getRootConfig().includeCancelledEvents)
		{
			return;
		}
		if (plugin.getRootConfig().debugEvents)
		{
			plugin.getLogger().info("Block physics");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if (event.isCancelled()
				&& !plugin.getRootConfig().includeCancelledEvents)
		{
			return;
		}
		if (plugin.getRootConfig().debugEvents)
		{
			plugin.getLogger().info("Block place");
		}
		Player p = event.getPlayer();

		Author author = plugin.authors.get(p.getName());
		if (author != null && author.isRecording())
		{
			Packet53BlockChange packet = new Packet53BlockChange();

			int xPosition = event.getBlock().getX();
			int yPosition = event.getBlock().getY();
			int zPosition = event.getBlock().getZ();
			int type = event.getBlock().getTypeId();
			int data = event.getBlock().getData();

			packet.a = xPosition;
			packet.b = yPosition;
			packet.c = zPosition;
			packet.material = type;
			packet.data = data;

			author.getCurrentRecording().recordPacket(packet);
			addRewindForBlockChange(author, xPosition, yPosition, zPosition, 0,
					0);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBurn(BlockBurnEvent event)
	{
		if (event.isCancelled()
				&& !plugin.getRootConfig().includeCancelledEvents)
		{
			return;
		}
		if (plugin.getRootConfig().debugEvents)
		{
			plugin.getLogger().info("Block burn");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event)
	{
		if (event.isCancelled()
				&& !plugin.getRootConfig().includeCancelledEvents)
		{
			return;
		}
		if (plugin.getRootConfig().debugEvents)
		{
			plugin.getLogger().info("Block break");
		}
		final Player p = event.getPlayer();

		final Author author = plugin.authors.get(p.getName());
		if (author != null && author.isRecording())
		{
			// TODO is there a method to use regular bukkit methods to do this?
			Packet53BlockChange packet = new Packet53BlockChange();

			int xPosition = event.getBlock().getX();
			int yPosition = event.getBlock().getY();
			int zPosition = event.getBlock().getZ();
			int type = 0;
			int data = 0;

			packet.a = xPosition;
			packet.b = yPosition;
			packet.c = zPosition;
			packet.material = type;
			packet.data = data;

			author.getCurrentRecording().recordPacket(packet);

			// Rewind packet

			type = event.getBlock().getType().getId();
			data = event.getBlock().getData();

			addRewindForBlockChange(author, xPosition, yPosition, zPosition,
					data, type);
		}
	}

	public void addRewindForBlockChange(Author author, int i, int j, int k,
			int l, int m)
	{
		Packet53BlockChange changeBack = new Packet53BlockChange();

		changeBack.a = i;
		changeBack.b = j;
		changeBack.c = k;
		changeBack.data = l;
		changeBack.material = m;

		author.getCurrentRecording().addRewindPacket(changeBack);
	}

}
