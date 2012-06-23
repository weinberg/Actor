package com.insofar.actor.commands.author;

import com.insofar.actor.ActorAPI;
import com.insofar.actor.EntityActor;
import com.insofar.actor.permissions.PermissionHandler;
import com.insofar.actor.permissions.PermissionNode;

/**
 * ActorPlugin command to spawn an actor with the current recording
 * 
 * @author Joshua Weinberg
 *
 */
public class Hire extends AuthorBaseCommand {

	public Hire()
	{
		super();
	}

	@Override
	/**
	 * Record command
	 */
	public boolean execute()
	{
		if (!PermissionHandler.has(player, PermissionNode.COMMAND_HIRE))
		{
			player.sendMessage("Lack permission: "
					+ PermissionNode.COMMAND_HIRE.getNode());
			return true;
		}
		EntityActor newActor = null;
		
		if (args.length == 1)
			newActor = ActorAPI.actor(player);
		
		if (args.length == 2)
			newActor = ActorAPI.actor(player, args[1]);
		
		if (args.length == 3)
			newActor = ActorAPI.actor(player, args[1], args[2]);
		
		if (newActor != null)
		{
			plugin.actors.add(newActor);
			return true;
		}
		
		return false;
	}

	/*****************************************************************************
	 * 
	 * Bukkit Plugin Command Methods
	 * 
	 ******************************************************************************/

	
}
