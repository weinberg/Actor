package com.insofar.actor.commands.author;

import java.io.File;
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
public class LoadScene extends AuthorBaseCommand {

	/*********************************************************************
	 * 
	 * BUKKIT COMMAND
	 * 
	 *********************************************************************/

	@Override
	/**
	 * args(sceneName)
	 * Spawn all actors in scene
	 */
	public boolean execute()
	{
		if (args.length != 1)
		{
			player.sendMessage("Error: parameter required: scenename");
			return true;
		}

		String sceneName = args[0];
		File sceneDir = new File(FilenameUtils.separatorsToSystem(plugin.scenePath+ "/" + sceneName));

		if (sceneDir == null || !sceneDir.isDirectory())
		{
			player.sendMessage("Error: problem reading scene.");
			return true;
		}

		for (String actorName : sceneDir.list())
		{
			EntityActor newActor = null;

			try
			{
				String path = FilenameUtils.separatorsToSystem(sceneDir+"/"+actorName);
				newActor = ActorPlugin.instance.spawnActorWithRecording(actorName, path, player, player.getWorld());
			}
			catch (IOException e)
			{
				player.sendMessage("Error: Error loading "+actorName);
				return false;
			}

			if (newActor != null)
			{
				plugin.actors.add(newActor);
			}
		}

		player.sendMessage("Scene loaded.");

		return false;
	}
}
