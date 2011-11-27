package com.insofar.actor.commands.author;

import org.bukkit.entity.Player;

import com.insofar.actor.author.Author;

/**
 * ActorPlugin command to stop recording.
 * 
 * @author Joshua Weinberg
 *
 */
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
