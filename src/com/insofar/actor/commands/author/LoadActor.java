package com.insofar.actor.commands.author;

import java.io.File;
import java.io.IOException;

import com.insofar.actor.ActorPlugin;
import com.insofar.actor.EntityActor;
import com.insofar.actor.permissions.PermissionHandler;
import com.insofar.actor.permissions.PermissionNode;

/**
 * InfoCraft Plugin authoring command to spawn an actor from a saved recording
 * 
 * @author Joshua Weinberg
 *
 */
public class LoadActor extends AuthorBaseCommand {

	/*********************************************************************
	 * 
	 * BUKKIT COMMAND
	 * 
	 *********************************************************************/
	
	@Override
	/**
	 * args(actorName, fileName)
	 * Spawn actor with actorname from recording filename
	 */
	public boolean execute()
	{
		if (!PermissionHandler.has(player, PermissionNode.COMMAND_LOAD_ACTOR))
		{
			player.sendMessage("Lack permission: "
					+ PermissionNode.COMMAND_LOAD_ACTOR.getNode());
			return true;
		}
		if (args.length != 3)
		{
			player.sendMessage("Error: Two parameters required: actorname filename");
			return true;
		}
		
		String actorName = args[1];
		String fileName = args[2];
		EntityActor newActor = null;
		
		try
		{
			String path = plugin.savePath + File.separator + fileName;
			System.out.println("Loading actor from path:"+path);
			newActor = ActorPlugin.instance.spawnActorWithRecording(actorName, path, player, player.getWorld());
		}
		catch (IOException e)
		{
			player.sendMessage("Error: Cannot load "+fileName+": ");
			e.printStackTrace();
			return false;
		}
		
		if (newActor != null)
		{
			newActor.setOwner(player);
			plugin.actors.add(newActor);
			player.sendMessage("Spawned.");
			return true;
		}
		
		return false;
	}
}
