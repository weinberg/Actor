package com.insofar.actor.commands.author;

import com.insofar.actor.author.EntityActor;

/**
 * InfoCraft Plugin command to play a game.
 * 
 * @author Joshua Weinberg
 *
 */
public class Cut extends AuthorBaseCommand {

	public Cut()
	{
		super();
	}

	@Override
	/**
	 * bukkit command to rewind one or all actors
	 */
	public boolean execute()
	{
		String actorName = args.length > 0 ? args[0] : "";
		
		return cut (actorName);
	}
	
	/**
	 * cut a single actor by name
	 */
	public boolean cut(String actorName)
	{
		for (EntityActor actor : plugin.actors)
		{
			if (actor.name.equals(actor) || actorName.equals(""))
			{
				actor.isPlayback = false;
			}
		}
		
		return false;
	}
}
