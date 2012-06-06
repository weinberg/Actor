package com.insofar.actor.commands.author;

import org.bukkit.entity.Player;

import com.insofar.actor.author.Author;
import com.insofar.actor.permissions.PermissionHandler;
import com.insofar.actor.permissions.PermissionNode;

/**
 * ActorPlugin command to stop recording.
 * 
 * @author Joshua Weinberg
 *
 */
@Deprecated
public class StopRecording extends AuthorBaseCommand {

	public StopRecording()
	{
		super();
	}

	/*************************************************************************
	 * 
	 * BUKKIT COMMAND
	 * 
	 *************************************************************************/

	@Override
	/**
	 * stoprec command
	 */
	public boolean execute()
	{
		if (!PermissionHandler.has(player, PermissionNode.COMMAND_CUT))
		{
			player.sendMessage("Lack permission: "
					+ PermissionNode.COMMAND_CUT.getNode());
			return true;
		}
		return stopRecording(player);
	}
	
	/*************************************************************************
	 * 
	 * API COMMAND
	 * 
	 *************************************************************************/
	
	/**
	 * stopRecording
	 * @param player
	 * @return
	 */
	public boolean stopRecording(Player player)
	{
		// Authoring commands must have an author
		Author author = getAuthor(player);
		
		if (author.currentRecording != null)
		{
			author.isRecording = false;
			author.currentRecording.rewind();
			player.sendMessage("Recording stopped.");
		}
		else
		{
			player.sendMessage("You have no recording.");
		}

		return true;
	}
}
