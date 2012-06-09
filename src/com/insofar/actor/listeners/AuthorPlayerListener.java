package com.insofar.actor.listeners;

import net.minecraft.server.Packet18ArmAnimation;
import net.minecraft.server.Packet34EntityTeleport;
import net.minecraft.server.Packet35EntityHeadRotation;
import net.minecraft.server.Packet3Chat;
import net.minecraft.server.Packet5EntityEquipment;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

import com.insofar.actor.ActorPlugin;
import com.insofar.actor.author.Author;
import com.insofar.actor.author.EntityActor;
import com.insofar.actor.author.Viewer;

public class AuthorPlayerListener implements Listener
{

	public ActorPlugin plugin;

	public AuthorPlayerListener(ActorPlugin instance)
	{
		plugin = instance;
	}

	/**
	 * Author player move listener
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		if (event.isCancelled() && !plugin.getRootConfig().includeCancelledEvents)
		{
			return;
		}
		final Player p = event.getPlayer();

		final Author author = plugin.authors.get(p.getName());
		if (author != null && author.isRecording)
		{
			Packet34EntityTeleport tp = new Packet34EntityTeleport();

			Location to = event.getTo();
			tp.a = p.getEntityId();
			tp.b = floor_double(to.getX() * 32D);
			tp.c = floor_double(to.getY() * 32D);
			tp.d = floor_double(to.getZ() * 32D);
			tp.e = (byte) (int) ((to.getYaw() * 256F) / 360F);
			tp.f = (byte) (int) ((to.getPitch() * 256F) / 360F);
			if (plugin.getRootConfig().debugEvents)
			{
				plugin.getLogger().info(
						"Player move recorded: (" + tp.b + "," + tp.c + ","
								+ tp.d + ")" + " y: " + tp.e + " p: " + tp.f);
			}
			Packet35EntityHeadRotation hr = new Packet35EntityHeadRotation();

			hr.a = p.getEntityId();
			hr.b = tp.e;

			author.currentRecording.recordPacket(tp);
			author.currentRecording.recordPacket(hr);
		}

	}

	/**
	 * Author player
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerPickupItem(PlayerPickupItemEvent event)
	{
		if (event.isCancelled() && !plugin.getRootConfig().includeCancelledEvents)
		{
			return;
		}
		if (plugin.getRootConfig().debugEvents)
		{
			plugin.getLogger().info(
					"Player picked up " + event.getItem().toString());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerChat(PlayerChatEvent event)
	{
		if (event.isCancelled() && !plugin.getRootConfig().includeCancelledEvents)
		{
			return;
		}
		final Player p = event.getPlayer();

		final Author author = plugin.authors.get(p.getName());
		if (author != null && author.isRecording)
		{
			Packet3Chat cp = new Packet3Chat();
			cp.message = event.getMessage();
			author.currentRecording.recordPacket(cp);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.isCancelled() && !plugin.getRootConfig().includeCancelledEvents)
		{
			return;
		}
		if (plugin.getRootConfig().debugEvents)
		{
			plugin.getLogger().info("Player Interact");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		if (event.isCancelled() && !plugin.getRootConfig().includeCancelledEvents)
		{
			return;
		}
		if (plugin.getRootConfig().debugEvents)
		{
			plugin.getLogger().info("Player Interact entity");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerEggThrow(PlayerEggThrowEvent event)
	{
		if (plugin.getRootConfig().debugEvents)
		{
			plugin.getLogger().info("Player Egg Throw");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onItemHeldChange(PlayerItemHeldEvent event)
	{
		final Player p = event.getPlayer();
		final Author author = plugin.authors.get(p.getName());
		if (author != null && author.isRecording)
		{
			Packet5EntityEquipment packet = new Packet5EntityEquipment();
			packet.b = 0;
			packet.c = event.getPlayer().getInventory().getItemInHand()
					.getTypeId();
			packet.d = event.getPlayer().getInventory().getItemInHand()
					.getData().getData();
			if (packet.c == 0)
				packet.c = -1;
			author.currentRecording.recordPacket(packet);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDropItem(PlayerDropItemEvent event)
	{
		if (event.isCancelled() && !plugin.getRootConfig().includeCancelledEvents)
		{
			return;
		}
		if (plugin.getRootConfig().debugEvents)
		{
			plugin.getLogger().info("Player drop item");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event)
	{
		if (event.isCancelled() && !plugin.getRootConfig().includeCancelledEvents)
		{
			return;
		}
		if (plugin.getRootConfig().debugEvents)
		{
			plugin.getLogger().info("Sneak toggle");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerToggleSprint(PlayerToggleSprintEvent event)
	{
		if (event.isCancelled() && !plugin.getRootConfig().includeCancelledEvents)
		{
			return;
		}
		if (plugin.getRootConfig().debugEvents)
		{
			plugin.getLogger().info("Sprint toggle");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerFish(PlayerFishEvent event)
	{
		if (event.isCancelled() && !plugin.getRootConfig().includeCancelledEvents)
		{
			return;
		}
		if (plugin.getRootConfig().debugEvents)
		{
			plugin.getLogger().info("Fish toggle");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerAnimation(PlayerAnimationEvent event)
	{
		if (event.isCancelled() && !plugin.getRootConfig().includeCancelledEvents)
		{
			return;
		}
		if (plugin.getRootConfig().debugEvents)
		{
			plugin.getLogger().info("Player animation");
		}
		final Player p = event.getPlayer();

		final Author author = plugin.authors.get(p.getName());
		if (author != null && author.isRecording)
		{
			// arm swing is the only animation bukkit supports?
			if (event.getAnimationType().equals(PlayerAnimationType.ARM_SWING))
			{
				Packet18ArmAnimation packet = new Packet18ArmAnimation();
				packet.b = 1;
				author.currentRecording.recordPacket(packet);
			}
		}
	}

	/**
	 * Remove the player from the authors list
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		plugin.authors.remove(event.getPlayer().getName());

		for (EntityActor actor : plugin.actors)
		{
			for (Viewer viewer : actor.getViewers())
			{
				if (viewer.player == event.getPlayer())
				{
					actor.getViewers().remove(viewer);
					break;
				}
			}
		}
	}

	public static int floor_double(double d)
	{
		int i = (int) d;
		return d >= i ? i : i - 1;
	}

}
