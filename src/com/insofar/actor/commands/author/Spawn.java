package com.insofar.actor.commands.author;

import java.io.IOException;

import com.insofar.actor.ActorPlugin;
import com.insofar.actor.author.EntityActor;

/**
 * InfoCraft Plugin authoring command to spawn an actor from a saved recording
 * 
 * @author Joshua Weinberg
 *
 */
public class Spawn extends AuthorBaseCommand {

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
			newActor = ActorPlugin.instance.spawnActorWithRecording(fileName, actorName, player, player.getWorld());
		}
		catch (IOException e)
		{
			player.sendMessage("Error: Cannot load "+fileName);
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
