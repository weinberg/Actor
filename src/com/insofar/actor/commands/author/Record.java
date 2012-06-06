package com.insofar.actor.commands.author;

import net.minecraft.server.Packet34EntityTeleport;
import net.minecraft.server.Packet35EntityHeadRotation;
import net.minecraft.server.Packet5EntityEquipment;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.insofar.actor.author.Author;
import com.insofar.actor.author.Recording;
import com.insofar.actor.permissions.PermissionHandler;
import com.insofar.actor.permissions.PermissionNode;

/**
 * ActorPlugin command to record a player
 * 
 * @author Joshua Weinberg
 *
 */
public class Record extends AuthorBaseCommand {

	public Record()
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
	 * Record command
	 */
	public boolean execute()
	{
		if (!PermissionHandler.has(player, PermissionNode.COMMAND_RECORD))
		{
			player.sendMessage("Lack permission: "
					+ PermissionNode.COMMAND_RECORD.getNode());
			return true;
		}
		// Record the player calling this command
		return record(player);
	}
	
	/*********************************************************************
	 * 
	 * PLUGIN API
	 * 
	 *********************************************************************/
	
	/**
	 * record command
	 * @param player
	 * @return
	 */
	public boolean record(Player player)
	{
		// Authoring commands must have an author
		Author author = getAuthor(player);
		
		if (author.isRecording)
		{
			player.sendMessage("You are already recording.");
			return true;
		}

		// Create new recording
		author.currentRecording = new Recording();
		
		// Setup jumpstart packets on recording

		// 	Packet34EntityTeleport
		Location l = player.getLocation();
		Packet34EntityTeleport tp = new Packet34EntityTeleport();
		tp.a = player.getEntityId(); // Entity id will be replaced on spawn/playback
		tp.b = floor_double(l.getX() * 32D);
		tp.c = floor_double(l.getY() * 32D);
		tp.d = floor_double(l.getZ() * 32D);
		tp.e = (byte)(int)((l.getYaw() * 256F) / 360F);
		tp.f = (byte)(int)((l.getPitch() * 256F) / 360F);
		author.currentRecording.recordPacket(tp,true);
		
		// 	Packet35HeadRotation
		Packet35EntityHeadRotation hr = new Packet35EntityHeadRotation(tp.a, tp.e);
		author.currentRecording.recordPacket(hr,true);

		// Packet5EntityEquipment
		// Should really use five of these on a new spawn for all equipment.
		Packet5EntityEquipment ep = new Packet5EntityEquipment();
		ep.b = 0;
		ep.c = player.getInventory().getItemInHand().getTypeId();
		if (ep.c == 0) ep.c = -1;
		author.currentRecording.recordPacket(ep,true);
		
		author.isRecording = true;
		player.sendMessage("Recording.");
		
		return true;
	}

	public static int floor_double(double d)
	{
		int i = (int)d;
		return d >= i ? i : i - 1;
	}
}
