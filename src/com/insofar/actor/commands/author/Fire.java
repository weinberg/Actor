package com.insofar.actor.commands.author;

import java.util.HashMap;
import java.util.Map;

import com.insofar.actor.conversations.FireActorPrompt;
import com.insofar.actor.permissions.PermissionHandler;
import com.insofar.actor.permissions.PermissionNode;

/**
 * ActorPlugin command to remove an actor
 * 
 * @author Joshua Weinberg
 * 
 */
public class Fire extends AuthorBaseCommand
{

	public Fire()
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
	 * Remove command. Accepts actorname or "all".
	 */
	public boolean execute()
	{
		if (!PermissionHandler.has(player, PermissionNode.COMMAND_FIRE))
		{
			player.sendMessage("Lack permission: "
					+ PermissionNode.COMMAND_FIRE.getNode());
			return true;
		}

		if (args.length == 2)
		{
			String actorName = args[1];
			final Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("name", actorName);
			factory.withFirstPrompt(new FireActorPrompt())
					.withInitialSessionData(map).withLocalEcho(false).buildConversation(player)
					.begin();
			return true;
		}

		return false;
	}
}
