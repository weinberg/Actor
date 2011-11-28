package com.insofar.actor.commands.author;

import com.insofar.actor.ActorPlugin;
import com.insofar.actor.author.EntityActor;

/**
 * ActorPlugin command to playback actors and record at the same time.
 * 
 * @author Joshua Weinberg
 *
 */
public class ActionRecord extends AuthorBaseCommand {

	public ActionRecord()
	{
		super();
	}

	/********************************************************************
	 * 
	 * BUKKIT COMMAND
	 * 
	 ********************************************************************/

	@Override
	/**
	 * Playback all actors which this player can view and record 
	 */
	public boolean execute()
	{
		String actorName = args.length > 0 ? args[0] : "";
		
		for (EntityActor actor : plugin.actors)
		{
			if (actor.hasViewer(player) && (actor.name.equals(actorName) || actorName.equals("")))
			{
				actor.isPlayback = true;
			}
		}
		
		ActorPlugin.instance.record(player);
		
		return true;
	}
}
