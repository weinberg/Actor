package com.insofar.actor.commands.author;

import com.insofar.actor.ActorAPI;
import com.insofar.actor.author.EntityActor;
import com.insofar.actor.permissions.PermissionHandler;
import com.insofar.actor.permissions.PermissionNode;

/**
 * ActorPlugin command to rewind the current recording and actor(s) in the scene.
 * 
 * @author Joshua Weinberg
 *
 */
public class Reset extends AuthorBaseCommand {

	public Reset()
	{
		super();
	}
	
	/*************************************************************************
	 * 
	 * BUKKIT COMMAND
	 * 
	*************************************************************************/

	@Override
	/**
	 * rewind one or all actors
	 */
	public boolean execute()
	{
		if (!PermissionHandler.has(player, PermissionNode.COMMAND_RESET))
		{
			player.sendMessage("Lack permission: "
					+ PermissionNode.COMMAND_RESET.getNode());
			return true;
		}
		String actorName = args.length > 1 ? args[1] : "";

		ActorAPI.resetAuthor(player);
		
		for (EntityActor actor : plugin.actors)
		{
			if (actor.hasViewer(player) && (actor.getActorName().equals(actorName) || actorName.equals("")))
			{
				actor.rewind();
			}
		}
		
		return true;
	}
}
