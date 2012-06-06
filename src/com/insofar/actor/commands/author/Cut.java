package com.insofar.actor.commands.author;

import com.insofar.actor.author.Author;
import com.insofar.actor.author.EntityActor;
import com.insofar.actor.permissions.PermissionHandler;
import com.insofar.actor.permissions.PermissionNode;

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
	 * bukkit command to stop playback and recording 
	 */
	public boolean execute()
	{
		if (!PermissionHandler.has(player, PermissionNode.COMMAND_CUT))
		{
			player.sendMessage("Lack permission: "
					+ PermissionNode.COMMAND_CUT.getNode());
			return true;
		}
		// Stop recording
		Author author = getAuthor(player);
		if (author.currentRecording != null)
		{
			author.isRecording = false;
			author.currentRecording.rewind();
			player.sendMessage("Recording stopped.");
		}
		
		// Stop playback on all actors
		for (EntityActor actor : plugin.actors)
		{
			actor.isPlayback = false;
		}
		
		return true;
	}
	
}
