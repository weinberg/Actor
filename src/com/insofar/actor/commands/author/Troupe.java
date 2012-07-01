package com.insofar.actor.commands.author;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.insofar.actor.ActorAPI;
import com.insofar.actor.conversations.TroupeAddPrompt;
import com.insofar.actor.permissions.PermissionHandler;
import com.insofar.actor.permissions.PermissionNode;

/**
 * ActorPlugin command to work with troupes
 * 
 * @author Joshua Weinberg
 * 
 */
public class Troupe extends AuthorBaseCommand
{

	public Troupe()
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
	 * Troupe base command. Handles subcommands and help.
	 */
	public boolean execute()
	{
		if (!PermissionHandler.has(player, PermissionNode.COMMAND_TROUPE))
		{
			player.sendMessage("Lack permission: "
					+ PermissionNode.COMMAND_TROUPE.getNode());
			return true;
		}
		
		if (args.length == 1)
		{
			player.sendMessage("/troupe requires subcommand: show|add|remove|record|hire|fire");
			return true;
		}
		
		String subCommand = args[1];
		
		if (subCommand.equals("add"))
		{
			doAdd();
		}
		
		return true;
	}
	
	/*****************************************************************************************
	 * 
	 * SubCommand: Add
	 * 
	 *****************************************************************************************/
	
	/**
	 * Handle subCommand add
	 */
	public boolean doAdd()
	{
		if (args.length!=3)
		{
			player.sendMessage("/troupe add [playername]\n  adds a player to your troupe");
			return true;
		}
		
		Player targetPlayer = Bukkit.getPlayer(args[2]);
		
		if (targetPlayer == null)
		{
			player.sendMessage("Cannot find player "+args[2]);
			return true;
		}
		
		if (ActorAPI.getAuthor(player).getTroupeMembers().contains(targetPlayer))
		{
			player.sendMessage(ChatColor.AQUA + args[2] + ChatColor.WHITE +
					" is already in your troupe.");
			return true;
		}
		
		player.sendMessage("Requesting permission from " +
				ChatColor.AQUA + args[2] + ChatColor.WHITE);
		
		final Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("requestor", player);
		map.put("target", targetPlayer);
		factory.withFirstPrompt(new TroupeAddPrompt(this))
				.withInitialSessionData(map).withLocalEcho(false).buildConversation(targetPlayer)
				.begin();
		return true;
	}
	
	/**
	 * Once the player confirms they are in the troupe, add them to the troupe set.
	 * @param target
	 * @return
	 */
	public void addRequestAccepted(Player target)
	{
		player.sendMessage(ChatColor.AQUA + target.getDisplayName() +
				" added to troupe.");
		ActorAPI.getAuthor(player).getTroupeMembers().add(target);
	}

	/**
	 * If player denies the request...
	 * @param target
	 * @return
	 */
	public void addRequestDenied(Player target)
	{
		player.sendMessage("Troupe add request denied by " +
				ChatColor.AQUA + target.getDisplayName());
	}

}
