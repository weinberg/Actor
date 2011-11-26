package com.insofar.actor.commands.author;

import com.insofar.actor.ActorPlugin;
import com.insofar.actor.author.EntityActor;

/**
 * InfoCraft Plugin command to play a game.
 * 
 * @author Joshua Weinberg
 *
 */
public class ActionRecord extends AuthorBaseCommand {

	public ActionRecord()
	{
		super();
	}

	@Override
	/**
	 * Playback all and record
	 */
	public boolean execute()
	{
		String actorName = args.length > 0 ? args[0] : "";
		
		for (EntityActor actor : plugin.actors)
		{
			if (actor.name.equals(actor) || actorName.equals(""))
			{
				actor.isPlayback = true;
			}
		}
		
		ActorPlugin.instance.record(player);
		
		return false;
	}
}
