package com.insofar.actor.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import com.insofar.actor.ActorPlugin;
import com.insofar.actor.commands.author.Troupe;

public class TroupeAddPrompt extends BooleanPrompt
{
	private Troupe troupeCommand;
	
	public TroupeAddPrompt(Troupe troupeCommand) {
		super();
		this.troupeCommand = troupeCommand;
	}

	@Override
	public String getPromptText(ConversationContext context)
	{
		return ChatColor.RED + ActorPlugin.TAG + " Allow player "
				+ ChatColor.AQUA + ((Player)(context.getSessionData("requestor"))).getName()
				+ ChatColor.RED + " to record your actions?";
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context,
			boolean valid)
	{
		if (valid)
		{
			context.getForWhom().sendRawMessage(
					ChatColor.YELLOW + ActorPlugin.TAG + " Added to troupe.");
			troupeCommand.addRequestAccepted((Player) context.getForWhom());
		}
		else
		{
			context.getForWhom().sendRawMessage(
					ChatColor.YELLOW + ActorPlugin.TAG + " Recording request denied.");
			troupeCommand.addRequestDenied((Player) context.getForWhom());
		}
		return END_OF_CONVERSATION;
	}
}
