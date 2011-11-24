package com.insofar.actor.commands.author;

import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;

import com.insofar.actor.ActorPlugin;
import com.insofar.actor.author.Author;

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
	protected MinecraftServer minecraftServer;
	
	public AuthorBaseCommand()
	{
		this.plugin = ActorPlugin.instance;
	}

	public AuthorBaseCommand(ActorPlugin plugin)
	{
		this.plugin = plugin;
		minecraftServer = ((CraftServer)Bukkit.getServer()).getServer();
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
	
	protected Author getAuthor(Player p)
	{
		Author author = plugin.authors.get(p.getName());
		
		// Set up an author if needed
		if (author == null)
		{
			author = new Author();
			author.player = p;
			plugin.authors.put(p.getName(), author);
		}
		
		return author;
	}
	
	protected boolean execute()
	{
		return true;
	}
	
}