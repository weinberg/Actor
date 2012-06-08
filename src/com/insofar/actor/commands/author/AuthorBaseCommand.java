package com.insofar.actor.commands.author;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

import com.insofar.actor.ActorPlugin;

/**
 * Base class for all authoring commands.
 * 
 * @author Joshua Weinberg
 *
 */
public class AuthorBaseCommand implements CommandExecutor {

	protected ActorPlugin plugin;
	protected Player player;
	protected Command cmd;
	protected String label;
	protected CommandSender sender;
	protected String[] args;
	protected ConversationFactory factory;
	
	public AuthorBaseCommand()
	{
		this.plugin = ActorPlugin.instance;
		this.factory = new ConversationFactory(plugin);
	}

	@Override
	/**
	 * Base command
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		this.sender = sender;
		this.cmd = cmd;
		this.label = label;
		this.args = args;
		
		player = (Player)sender;
		
		return execute();
	}
	
	protected boolean execute()
	{
		return true;
	}
	
}