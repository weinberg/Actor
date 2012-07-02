package com.insofar.actor.commands.author;

import org.bukkit.entity.Player;

import com.insofar.actor.ActorAPI;
import com.insofar.actor.Author;
import com.insofar.actor.EntityActor;
import com.insofar.actor.Recording;
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
		// Stop recording on author's personal recording
		Author author = ActorAPI.getAuthor(player);
		if (author.getCurrentRecording() != null)
		{
			author.setRecording(false);
			author.getCurrentRecording().rewind();
			player.sendMessage("Recording stopped.");
		}
		
		// Stop troupe recording
		if (author.isTroupeRecording())
		{
			player.sendMessage("Troupe recording stopped.");
			author.setTroupeRecording(false);
		}
		
		// Rewind troupe member recordings
		for (Player member : author.getTroupeMembers())
		{
			Recording r = author.getTroupRecMap().get(member.getName());
			if (r != null)
				r.rewind();
		}
		
		// Stop playback on all actors
		for (EntityActor actor : plugin.actors)
		{
			if (actor.getOwner() == player)
				actor.setIsPlayback(false);
		}
		
		return true;
	}
	
}
