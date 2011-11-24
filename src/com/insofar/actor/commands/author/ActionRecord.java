package com.insofar.actor.commands.author;

import com.insofar.actor.ActorPlugin;

/**
 * InfoCraft Plugin command to play a game.
 * 
 * @author Joshua Weinberg
 *
 */
public class ActionRecord extends AuthorBaseCommand {

	public ActionRecord(ActorPlugin plugin)
	{
		super(plugin);
	}

	@Override
	/**
	 * Playback all and record
	 */
	public boolean execute()
	{
		if (plugin.getCommand("action").getExecutor().onCommand(sender, cmd, label, args))
		{
			if (!plugin.getCommand("record").getExecutor().onCommand(sender, cmd, label, args))
			{
				plugin.getCommand("cut").getExecutor().onCommand(sender, cmd, label, args);
			}
			return true;
		}
		
		return false;
	}
}
