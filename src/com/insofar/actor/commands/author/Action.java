package com.insofar.actor.commands.author;

import com.insofar.actor.author.EntityActor;

/**
 * ActorPlugin command to start playback on an actor.
 * 
 * @author Joshua Weinberg
 *
 */
public class Action extends AuthorBaseCommand {

	public Action()
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
	 * Play one or all actors which this player can view.
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
		
		return true;
	}
}
