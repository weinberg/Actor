package com.insofar.actor.commands.author;

import com.insofar.actor.EntityActor;
import com.insofar.actor.permissions.PermissionHandler;
import com.insofar.actor.permissions.PermissionNode;

/**
 * ActorPlugin command to start playback on an actor.
 * 
 * @author Joshua Weinberg
 *
 */
public class Action extends AuthorBaseCommand {

	public Action()
	{
		super();
	}
	
	/********************************************************************
	 * 
	 * BUKKIT COMMAND
	 * 
	 ********************************************************************/

	@Override
	/**
	 * Play one or all actors which this player can view.
	 */
	public boolean execute()
	{
		if (!PermissionHandler.has(player, PermissionNode.COMMAND_ACTION))
		{
			player.sendMessage("Lack permission: "
					+ PermissionNode.COMMAND_ACTION.getNode());
			return true;
		}
		String actorName = args.length > 1 ? args[1] : "";
		
		for (EntityActor actor : plugin.actors)
		{
			if (actor.hasViewer(player) && (actor.getActorName().equals(actorName) || actorName.equals("")))
			{
				actor.setIsPlayback(true);
			}
		}
		
		return true;
	}
}
