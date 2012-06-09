package com.insofar.actor.commands.author;

import java.io.File;
import java.io.IOException;

import com.insofar.actor.ActorPlugin;
import com.insofar.actor.author.EntityActor;
import com.insofar.actor.permissions.PermissionHandler;
import com.insofar.actor.permissions.PermissionNode;

/**
 * InfoCraft Plugin command to save actor recording to a file
 * 
 * @author Joshua Weinberg
 *
 */
public class SaveActor extends AuthorBaseCommand {

	/*********************************************************************
	 * 
	 * BUKKIT COMMAND
	 * 
	 *********************************************************************/

	@Override
	/**
	 * args(actorName, fileName)
	 * Save actor recording command. Filename is within plugins/Actor/save
	 */
	public boolean execute()
	{
		if (!PermissionHandler.has(player, PermissionNode.COMMAND_SAVE_ACTOR))
		{
			player.sendMessage("Lack permission: "
					+ PermissionNode.COMMAND_SAVE_ACTOR.getNode());
			return true;
		}
		if (args.length != 3)
		{
			player.sendMessage("Error: Two parameters required: actorname filename");
			return true;
		}
		
		String actorName = args[1];
		String fileName = args[2];
		EntityActor actor = null;
		
		for (EntityActor ea : plugin.actors)
		{
			if (ea.hasViewer(player) && (ea.getActorName().equals(actorName) || actorName.equals("")))
			{
				actor=ea;
				break;
			}
		}
		
		if (actor == null)
		{
			player.sendMessage("Error: Cannot find actor '"+actorName+"'");
			return true;
		}

		try
		{
			ActorPlugin.instance.saveActorRecording(actor, plugin.savePath + File.separator + fileName);
		}
		catch (IOException e)
		{
			player.sendMessage("Error: Cannot save to "+fileName);
			return false;
		}
		
		player.sendMessage("Saved to "+fileName);
		return true;
	}
}
