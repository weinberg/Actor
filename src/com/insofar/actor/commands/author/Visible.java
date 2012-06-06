package com.insofar.actor.commands.author;

import net.minecraft.server.Packet20NamedEntitySpawn;
import net.minecraft.server.Packet29DestroyEntity;

import com.insofar.actor.author.EntityActor;
import com.insofar.actor.permissions.PermissionHandler;
import com.insofar.actor.permissions.PermissionNode;

/**
 * ActorPlugin command to set visibility on an actor (or "all")
 * 
 * @author Joshua Weinberg
 *
 */
public class Visible extends AuthorBaseCommand {

	public Visible()
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
	 * bukkit command to turn visibility to all or just the author
	 */
	public boolean execute()
	{
		if (!PermissionHandler.has(player, PermissionNode.COMMAND_VISIBLE))
		{
			player.sendMessage("Lack permission: "
					+ PermissionNode.COMMAND_VISIBLE.getNode());
			return true;
		}
		if (args.length != 2)
		{
			player.sendMessage("Error: Usage: /visible [on|off] ActorName. ActorName can be 'all'");
			return true;
		}

		boolean viz = args[0].equals("on") ? true : false;
		String actorName = args[1];
		//TODO switch to Bukkit's built in method of invisibility?
		for (EntityActor actor : plugin.actors)
		{
			if (actor.hasViewer(player) && (actor.name.equals(actorName) || actorName.equals("all")))
			{
				Packet29DestroyEntity d = new Packet29DestroyEntity(actor.id);
				actor.sendPacket(d);
				
				actor.allPlayersView = viz;
				
				// Send spawn packet to the viewers
				Packet20NamedEntitySpawn np = new Packet20NamedEntitySpawn(actor);
				np.a = actor.id;
				actor.sendPacket(np);

				/*
				// Send teleport packet
				Packet34EntityTeleport packet = actor.recording.getJumpstart();
				packet.a = actor.id;
				actor.sendPacketToViewers(packet);
				*/

			}
		}

		return true;
	}

}
