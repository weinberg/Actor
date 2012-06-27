package com.insofar.actor.commands.author;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.insofar.actor.ActorAPI;
import com.insofar.actor.ActorPlugin;
import com.insofar.actor.EntityActor;
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
			if (plugin.getRootConfig().prompt)
			{
				final Map<Object, Object> map = new HashMap<Object, Object>();
				map.put("name", actorName);
				factory.withFirstPrompt(new FireActorPrompt(this))
				.withInitialSessionData(map).withLocalEcho(false).buildConversation(player)
				.begin();
				return true;
			}
			else
			{
				doFire(actorName, player);
			}
		}

		return false;
	}

	/**
	 * Do the firing
	 * @param actorName
	 * @return
	 */
	public boolean doFire(String actorName, Player player)
	{
		ArrayList<EntityActor> removeActors = new ArrayList<EntityActor>();
		// Call actorRemove on all actors named args[0]
		for (EntityActor ea : plugin.actors)
		{
			try
			{
				if ((ea.getActorName().equalsIgnoreCase(actorName) || actorName
						.equalsIgnoreCase("all"))
						&& ea.hasViewer(player.getName()))
				{
					ActorAPI.actorRemove(ea);
					removeActors.add(ea);
				}
			}
			catch (ClassCastException cc)
			{
				// IGNORE
			}
		}
		if (!removeActors.isEmpty())
		{
			// Remove them from the plugin's actors list
			plugin.actors.removeAll(removeActors);
			player.sendRawMessage(
					ChatColor.GREEN + ActorPlugin.TAG + " Fired actor '"
					+ ChatColor.AQUA + actorName + ChatColor.GREEN
					+ "'");
			return true;
		}
		else
		{
			player.sendRawMessage(
					ChatColor.YELLOW + ActorPlugin.TAG
					+ " Actor '" + ChatColor.AQUA
					+ actorName + ChatColor.YELLOW + "' not found");
			return false;
		}

	}
}
