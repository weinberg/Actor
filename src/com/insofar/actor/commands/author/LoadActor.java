package com.insofar.actor.commands.author;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

import com.insofar.actor.ActorPlugin;
import com.insofar.actor.author.EntityActor;

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
		if (args.length != 2)
		{
			player.sendMessage("Error: Two parameters required: actorname filename");
			return true;
		}
		
		String actorName = args[0];
		String fileName = args[1];
		EntityActor newActor = null;
		
		try
		{
			String path = FilenameUtils.separatorsToSystem(plugin.savePath + "/" + fileName);
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
			plugin.actors.add(newActor);
			player.sendMessage("Spawned.");
			return true;
		}
		
		return false;
	}
}
