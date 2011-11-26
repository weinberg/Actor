package com.insofar.actor.commands.author;

import com.insofar.actor.author.EntityActor;

/**
 * InfoCraft Plugin command to play a game.
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
	 * rewind one or all actors
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
		
		return true;
	}
}
