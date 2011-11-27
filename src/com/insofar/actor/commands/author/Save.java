package com.insofar.actor.commands.author;

import java.io.IOException;

import com.insofar.actor.ActorPlugin;
import com.insofar.actor.author.EntityActor;

/**
 * InfoCraft Plugin command to save actor recording to a file
 * 
 * @author Joshua Weinberg
 *
 */
public class Save extends AuthorBaseCommand {

	/*********************************************************************
	 * 
	 * BUKKIT COMMAND
	 * 
	 *********************************************************************/

	@Override
	/**
	 * args(actorName, fileName)
	 * Save actor recording to scene command
	 */
	public boolean execute()
	{
		if (args.length != 3)
		{
			player.sendMessage("Error: Three parameters required: actorname scenename filename");
			return true;
		}
		
		String actorName = args[0];
		String fileName = args[1];
		EntityActor actor = null;
		
		for (EntityActor ea : plugin.actors)
		{
			if (ea.hasViewer(player) && (ea.name.equals(actorName) || actorName.equals("")))
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
			ActorPlugin.instance.saveActorRecording(actor, fileName);
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
