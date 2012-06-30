package com.insofar.actor.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.insofar.actor.ActorPlugin;
import com.insofar.actor.EntityActor;

/**
 * Listener for all player events not related to being an author.
 * 
 * @author root
 *
 */
public class PlayerListener implements Listener {

	public ActorPlugin plugin;

	public PlayerListener(ActorPlugin instance)
	{
		plugin = instance;
	}
	
	/**
	 * Player spawn listener
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerLogin(PlayerJoinEvent event)
	{
		for (EntityActor actor : plugin.actors)
		{
			actor.spawn(event.getPlayer());
		}
	}
	
}
