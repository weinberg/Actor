package com.insofar.actor.commands.author;

import java.io.File;
import java.io.IOException;

import com.insofar.actor.ActorPlugin;
import com.insofar.actor.author.EntityActor;
import com.insofar.actor.permissions.PermissionHandler;
import com.insofar.actor.permissions.PermissionNode;

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
		if (!PermissionHandler.has(player, PermissionNode.COMMAND_LOAD_SCENE))
		{
			player.sendMessage("Lack permission: "
					+ PermissionNode.COMMAND_LOAD_SCENE.getNode());
			return true;
		}
		if (args.length != 2)
		{
			player.sendMessage("Error: parameter required: scenename");
			return true;
		}

		String sceneName = args[1];
		File sceneDir = new File(plugin.scenePath+ File.separator + sceneName);

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
				String path = sceneDir+File.separator+actorName;
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

		return true;
	}
}
