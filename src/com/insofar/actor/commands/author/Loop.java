package com.insofar.actor.commands.author;

import com.insofar.actor.author.EntityActor;
import com.insofar.actor.permissions.PermissionHandler;
import com.insofar.actor.permissions.PermissionNode;

/**
 * ActorPlugin command to set looping on an actor (or "all")
 * 
 * @author Joshua Weinberg
 *
 */
public class Loop extends AuthorBaseCommand {

	public Loop()
	{
		super();
	}

	/*********************************************************************
	 * 
	 * BUKKIT COMMAND
	 * 
	 *********************************************************************/

	@Override
	/**
	 * bukkit command to turn loop on or off for one or "all" actors
	 */
	public boolean execute()
	{
		if (!PermissionHandler.has(player, PermissionNode.COMMAND_LOOP))
		{
			player.sendMessage("Lack permission: "
					+ PermissionNode.COMMAND_LOOP.getNode());
			return true;
		}
		if (args.length != 3)
		{
			player.sendMessage("Error: Usage: /loop [on|off] ActorName. ActorName can be 'all'");
			return true;
		}
		
		boolean loop = args[1].equalsIgnoreCase("on") ? true : false;
		String actorName = args[2];
		
		for (EntityActor actor : plugin.actors)
		{
			if (actor.hasViewer(player) && (actor.name.equals(actorName) || actorName.equals("all")))
			{
				actor.loop = loop;
			}
		}
		
		return true;
	}
	
}
