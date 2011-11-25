package com.insofar.actor.commands.author;

import com.insofar.actor.ActorPlugin;

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
		return actionRecord();
	}
	
	/**
	 * Action record command internal
	 * @return
	 */
	public boolean actionRecord()
	{
		if (ActorPlugin.instance.action())
		{
			if (!ActorPlugin.instance.record(player))
			{
				ActorPlugin.instance.cut();
			}
			return true;
		}
		return false;
	}
}
