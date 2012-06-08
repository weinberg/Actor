package com.insofar.actor.commands.author;

import java.util.ArrayList;

import com.insofar.actor.ActorAPI;
import com.insofar.actor.author.EntityActor;
import com.insofar.actor.permissions.PermissionHandler;
import com.insofar.actor.permissions.PermissionNode;

/**
 * ActorPlugin command to remove an actor
 * 
 * @author Joshua Weinberg
 *
 */
public class Fire extends AuthorBaseCommand {

	public Fire()
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
	 * Remove command. Accepts actorname or "all".
	 */
	public boolean execute()
	{
		if (!PermissionHandler.has(player, PermissionNode.COMMAND_FIRE))
		{
			player.sendMessage("Lack permission: "
					+ PermissionNode.COMMAND_FIRE.getNode());
			return true;
		}
		ArrayList<EntityActor> removeActors = new ArrayList<EntityActor>();
		
		if (args.length == 2)
		{
			String actorName = args[1];
			// Call actorRemove on all actors named args[0]
			for (EntityActor ea : plugin.actors)
			{
				if ((ea.name.equals(actorName) || actorName.equals("all")) && ea.hasViewer(player))
				{
					ActorAPI.actorRemove(ea);
					removeActors.add(ea);
				}
			}
			
			// Remove them from the plugin's actors list
			plugin.actors.removeAll(removeActors);
			
			return true;
		}
		
		return false;
	}
}
