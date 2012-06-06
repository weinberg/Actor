package com.insofar.actor.commands.author;

import com.insofar.actor.ActorAPI;
import com.insofar.actor.permissions.PermissionHandler;
import com.insofar.actor.permissions.PermissionNode;

/**
 * ActorPlugin command to record a player
 * 
 * @author Joshua Weinberg
 *
 */
public class Record extends AuthorBaseCommand {

	public Record()
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
	 * Record command
	 */
	public boolean execute()
	{
		if (!PermissionHandler.has(player, PermissionNode.COMMAND_RECORD))
		{
			player.sendMessage("Lack permission: "
					+ PermissionNode.COMMAND_RECORD.getNode());
			return true;
		}
		// Record the player calling this command
		return ActorAPI.record(player);
	}
}
