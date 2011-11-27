package com.insofar.actor.commands.author;

import com.insofar.actor.author.EntityActor;

/**
 * ActorPlugin command to stop playback on an actor.
 * 
 * @author Joshua Weinberg
 *
 */
public class Cut extends AuthorBaseCommand {

	public Cut()
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
	 * bukkit command to stop one or all actors which the player can view
	 */
	public boolean execute()
	{
		String actorName = args.length > 0 ? args[0] : "";
		
		for (EntityActor actor : plugin.actors)
		{
			if (actor.hasViewer(player) && (actor.name.equals(actorName) || actorName.equals("")))
			{
				actor.isPlayback = false;
			}
		}
		
		return true;
	}
	
}
