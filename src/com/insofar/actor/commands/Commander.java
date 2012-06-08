package com.insofar.actor.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.insofar.actor.ActorPlugin;
import com.insofar.actor.commands.author.Action;
import com.insofar.actor.commands.author.ActionRecord;
import com.insofar.actor.commands.author.Hire;
import com.insofar.actor.commands.author.Cut;
import com.insofar.actor.commands.author.Dub;
import com.insofar.actor.commands.author.Fire;
import com.insofar.actor.commands.author.LoadActor;
import com.insofar.actor.commands.author.LoadScene;
import com.insofar.actor.commands.author.Loop;
import com.insofar.actor.commands.author.Record;
import com.insofar.actor.commands.author.Reset;
import com.insofar.actor.commands.author.SaveActor;
import com.insofar.actor.commands.author.SaveScene;
import com.insofar.actor.commands.author.Visible;
import com.insofar.actor.permissions.PermissionHandler;
import com.insofar.actor.permissions.PermissionNode;

public class Commander implements CommandExecutor
{
	/**
	 * Author sub commands references
	 */
	private Record record;
	private Hire actor;
	private Fire fire;
	private Action action;
	private Reset reset;
	private ActionRecord actionrec;
	private Cut cut;
	private Dub dub;
	private Loop loop;
	private Visible visible;
	private SaveActor saveActor;
	private SaveScene saveScene;
	private LoadActor loadActor;
	private LoadScene loadScene;

	public Commander()
	{
		/**
		 * Initialize author sub commands
		 */
		this.record = new Record();
		this.actor = new Hire();
		this.fire = new Fire();
		this.action = new Action();
		this.reset = new Reset();
		this.actionrec = new ActionRecord();
		this.cut = new Cut();
		this.dub = new Dub();
		this.loop = new Loop();
		this.visible = new Visible();
		this.saveActor = new SaveActor();
		this.saveScene = new SaveScene();
		this.loadActor = new LoadActor();
		this.loadScene = new LoadScene();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args)
	{
		// TODO Auto-generated method stub
		if (args.length == 0)
		{
			// Check if they have "karma" permission
			this.displayHelp(sender);
		}
		else
		{
			final String com = args[0].toLowerCase();
			if (com.equalsIgnoreCase("record"))
			{
				return record.onCommand(sender, command, label, args);
			}
			else if (com.equalsIgnoreCase("create")
					|| com.equalsIgnoreCase("hire"))
			{
				return actor.onCommand(sender, command, label, args);
			}
			else if (com.equalsIgnoreCase("fire"))
			{
				return fire.onCommand(sender, command, label, args);
			}
			else if (com.equalsIgnoreCase("action"))
			{
				return action.onCommand(sender, command, label, args);
			}
			else if (com.equalsIgnoreCase("reset"))
			{
				return reset.onCommand(sender, command, label, args);
			}
			else if (com.equalsIgnoreCase("actionrec"))
			{
				return actionrec.onCommand(sender, command, label, args);
			}
			else if (com.equalsIgnoreCase("cut"))
			{
				return cut.onCommand(sender, command, label, args);
			}
			else if (com.equalsIgnoreCase("dub"))
			{
				return dub.onCommand(sender, command, label, args);
			}
			else if (com.equalsIgnoreCase("loop"))
			{
				return loop.onCommand(sender, command, label, args);
			}
			else if (com.equalsIgnoreCase("visible"))
			{
				return visible.onCommand(sender, command, label, args);
			}
			else if (com.equalsIgnoreCase("saveactor"))
			{
				return saveActor.onCommand(sender, command, label, args);
			}
			else if (com.equalsIgnoreCase("savescene"))
			{
				return saveScene.onCommand(sender, command, label, args);
			}
			else if (com.equalsIgnoreCase("loadactor"))
			{
				return loadActor.onCommand(sender, command, label, args);
			}
			else if (com.equalsIgnoreCase("loadscene"))
			{
				return loadScene.onCommand(sender, command, label, args);
			}
			else
			{
				sender.sendMessage(ActorPlugin.TAG + " Unknown command: " + com);
				return false;
			}
		}
		return true;
	}

	private void displayHelp(CommandSender sender)
	{
		if (PermissionHandler.has(sender, PermissionNode.COMMAND_RECORD))
		{
			sender.sendMessage(ChatColor.GOLD + "/actor record"
					+ ChatColor.WHITE
					+ " : Start recording actions into buffer");
		}
		if (PermissionHandler.has(sender, PermissionNode.COMMAND_CUT))
		{
			sender.sendMessage(ChatColor.GOLD + "/actor cut" + ChatColor.WHITE
					+ " : Stop recording and playback on all actors");
		}
		if (PermissionHandler.has(sender, PermissionNode.COMMAND_HIRE))
		{
			sender.sendMessage(ChatColor.GOLD + "/actor hire <name>"
					+ ChatColor.WHITE
					+ " : Spawn new actor using recording in buffer");
		}
		if (PermissionHandler.has(sender, PermissionNode.COMMAND_DUB))
		{
			sender.sendMessage(ChatColor.GOLD
					+ "/actor dub <name|all> <x> <y> <z>" + ChatColor.WHITE
					+ " : Duplicate actor(s) with a translation");
		}
		if (PermissionHandler.has(sender, PermissionNode.COMMAND_RECORD))
		{
			sender.sendMessage(ChatColor.GOLD + "/actor fire <name|all>"
					+ ChatColor.WHITE + " : Remove an actor(s)");
		}
		if (PermissionHandler.has(sender, PermissionNode.COMMAND_ACTION))
		{
			sender.sendMessage(ChatColor.GOLD + "/actor action <name|all>"
					+ ChatColor.WHITE + " : Playback actor(s)");
		}
		if (PermissionHandler.has(sender, PermissionNode.COMMAND_ACTIONREC))
		{
			sender.sendMessage(ChatColor.GOLD + "/actor actionrec <name|all>"
					+ ChatColor.WHITE + " : Playback actor(s) and record");
		}
		if (PermissionHandler.has(sender, PermissionNode.COMMAND_LOOP))
		{
			sender.sendMessage(ChatColor.GOLD
					+ "/actor loop <on|off> <name|all>" + ChatColor.WHITE
					+ " : Set an actor(s) to loop");
		}
		if (PermissionHandler.has(sender, PermissionNode.COMMAND_VISIBLE))
		{
			sender.sendMessage(ChatColor.GOLD
					+ "/actor visible <on|off> <name|all>"
					+ ChatColor.WHITE
					+ " : Set an actor visible to all other players or just the author");
		}
		if (PermissionHandler.has(sender, PermissionNode.COMMAND_RESET))
		{
			sender.sendMessage(ChatColor.GOLD + "/actor reset <name|all>"
					+ ChatColor.WHITE + " : Rewind actor(s)");
		}
		if (PermissionHandler.has(sender, PermissionNode.COMMAND_SAVE_ACTOR))
		{
			sender.sendMessage(ChatColor.GOLD
					+ "/actor saveactor <name> <file>" + ChatColor.WHITE
					+ " : Save actor's recording to file");
		}
		if (PermissionHandler.has(sender, PermissionNode.COMMAND_SAVE_SCENE))
		{
			sender.sendMessage(ChatColor.GOLD + "/actor savescene <scene>"
					+ ChatColor.WHITE + " : Save all actor recordings to scene");
		}
		if (PermissionHandler.has(sender, PermissionNode.COMMAND_LOAD_ACTOR))
		{
			sender.sendMessage(ChatColor.GOLD
					+ "/actor loadactor <name> <file>" + ChatColor.WHITE
					+ " : Spawn actor from a saved file");
		}
		if (PermissionHandler.has(sender, PermissionNode.COMMAND_LOAD_SCENE))
		{
			sender.sendMessage(ChatColor.GOLD + "/actor loadscene <scene>"
					+ ChatColor.WHITE
					+ " : Load and spawn all actors from a scene");
		}
	}

}
