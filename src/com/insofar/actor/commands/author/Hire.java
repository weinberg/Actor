package com.insofar.actor.commands.author;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.insofar.actor.ActorAPI;
import com.insofar.actor.Author;
import com.insofar.actor.EntityActor;
import com.insofar.actor.Recording;
import com.insofar.actor.permissions.PermissionHandler;
import com.insofar.actor.permissions.PermissionNode;

/**
 * ActorPlugin command to spawn an actor with the current recording
 * 
 * @author Joshua Weinberg
 *
 */
public class Hire extends AuthorBaseCommand {

	public Hire()
	{
		super();
	}

	@Override
	/**
	 * Record command
	 */
	public boolean execute()
	{
		if (!PermissionHandler.has(player, PermissionNode.COMMAND_HIRE))
		{
			player.sendMessage("Lack permission: "
					+ PermissionNode.COMMAND_HIRE.getNode());
			return true;
		}
		EntityActor newActor = null;

		if (args.length == 1)
			doHire();

		// Hire 'name' leads to hiring just the player's named actor
		// not the whole troupe.
		if (args.length == 2)
		{
			newActor = ActorAPI.actor(player, args[1]);

			if (newActor != null)
			{
				newActor.setOwner(player);
				plugin.actors.add(newActor);
				return true;
			}
		}

		return false;
	}

	/**
	 * Hire the troupe
	 */
	public void doHire()
	{
		Author author = ActorAPI.getAuthor(player);
		HashMap<String,Recording> recMap = author.getTroupRecMap();
		for (Player member : author.getTroupeMembers())
		{
			Recording r = recMap.get(member.getName());
			if (r == null)
			{
				continue;
			}

			EntityActor newActor = ActorAPI.actor(r, member.getName(), player.getWorld());

			if (newActor != null)
			{
				newActor.setOwner(player);
				plugin.actors.add(newActor);
			}
		}
	}


}
