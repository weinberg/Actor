package com.insofar.actor.conversations;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import com.insofar.actor.ActorAPI;
import com.insofar.actor.ActorPlugin;
import com.insofar.actor.EntityActor;

public class FireActorPrompt extends BooleanPrompt
{
	@Override
	public String getPromptText(ConversationContext context)
	{
		return ChatColor.RED + ActorPlugin.TAG + " Fire actor '"
				+ ChatColor.AQUA + context.getSessionData("name")
				+ ChatColor.RED + "'?";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context,
			boolean valid)
	{
		final String actorName = (String) context.getSessionData("name");
		if (valid)
		{
			ArrayList<EntityActor> removeActors = new ArrayList<EntityActor>();
			// Call actorRemove on all actors named args[0]
			for (EntityActor ea : ActorPlugin.instance.actors)
			{
				try
				{
					if ((ea.getActorName().equalsIgnoreCase(actorName) || actorName
							.equalsIgnoreCase("all"))
							&& ea.hasViewer(((Player) context.getForWhom())
									.getName()))
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
				ActorPlugin.instance.actors.removeAll(removeActors);
				context.getForWhom().sendRawMessage(
						ChatColor.GREEN + ActorPlugin.TAG + " Fired actor '"
								+ ChatColor.AQUA + actorName + ChatColor.GREEN
								+ "'");
			}
			else
			{
				context.getForWhom().sendRawMessage(
						ChatColor.YELLOW + ActorPlugin.TAG
								+ " Actor '" + ChatColor.AQUA
								+ actorName + ChatColor.YELLOW + "' not found");
			}
		}
		else
		{
			context.getForWhom().sendRawMessage(
					ChatColor.YELLOW + ActorPlugin.TAG
							+ " Cancelled firing actor '" + ChatColor.AQUA
							+ actorName + ChatColor.YELLOW + "'");
		}
		return END_OF_CONVERSATION;
	}
}
