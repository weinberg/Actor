package com.insofar.actor.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import com.insofar.actor.ActorPlugin;
import com.insofar.actor.commands.author.Fire;

public class FireActorPrompt extends BooleanPrompt
{
	private Fire fireCommand;
	
	public FireActorPrompt(Fire fireCommand) {
		super();
		this.fireCommand = fireCommand;
	}

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
			fireCommand.doFire(actorName, (Player) context.getForWhom());
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
