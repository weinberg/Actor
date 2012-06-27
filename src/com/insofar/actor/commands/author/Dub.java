package com.insofar.actor.commands.author;

import java.util.ArrayList;
import java.util.List;

import com.insofar.actor.ActorAPI;
import com.insofar.actor.EntityActor;
import com.insofar.actor.permissions.PermissionHandler;
import com.insofar.actor.permissions.PermissionNode;

/**
 * ActorPlugin command to duplicate an actor with a translation
 * 
 * @author Joshua Weinberg
 * 
 */
public class Dub extends AuthorBaseCommand
{

	public Dub()
	{
		super();
	}

	/*****************************************************************************
	 * 
	 * BUKKIT COMMAND
	 * 
	 ******************************************************************************/

	@Override
	/**
	 * Dub command
	 */
	public boolean execute()
	{
		if (!PermissionHandler.has(player, PermissionNode.COMMAND_DUB))
		{
			player.sendMessage("Lack permission: "
					+ PermissionNode.COMMAND_DUB.getNode());
			return true;
		}
		if (args.length != 5)
		{
			player.sendMessage("Error: Usage: dub [actorname|all] x y z");
			return true;
		}

		String actorName = args[1];
		List<EntityActor> newActors = new ArrayList<EntityActor>();

		for (EntityActor actor : plugin.actors)
		{
			if ((actorName.equals("all") || actor.getActorName().equals(actorName))
					&& actor.hasViewer(player))
			{
				EntityActor newActor = ActorAPI.dub(actor, actor.getActorName(), player,
						player.getWorld(), Integer.parseInt(args[2]),
						Integer.parseInt(args[3]), Integer.parseInt(args[4]));

				newActors.add(newActor);
			}
		}

		plugin.actors.addAll(newActors);

		return true;
	}
}
