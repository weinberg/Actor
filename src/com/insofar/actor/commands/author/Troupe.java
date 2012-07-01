package com.insofar.actor.commands.author;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.insofar.actor.ActorPlugin;
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
		
		String subCommand = args[2];
		
		if (subCommand.equals("add"))
		{
			doAdd();
		}
		
		return false;
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
			player.sendMessage("/troupe add requires a player name to add");
			return false;
		}
		
		Player targetPlayer = Bukkit.getPlayer(args[2]);
		
		final Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("requstor", player);
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
	public boolean addRequestAccepted(Player target)
	{
		ActorPlugin.getInstance().authors.get(player.getName()).getTroupeMembers().add(target);
		return true;
	}

}
