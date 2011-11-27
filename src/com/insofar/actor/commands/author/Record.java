package com.insofar.actor.commands.author;

import net.minecraft.server.Packet34EntityTeleport;
import net.minecraft.server.Packet5EntityEquipment;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.insofar.actor.author.Author;
import com.insofar.actor.author.Recording;

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

		Location l = player.getLocation();

		// Make a teleport packet to start off
		Packet34EntityTeleport tp = new Packet34EntityTeleport();
		tp.a = player.getEntityId(); // Entity id will be replaced on spawn/playback
		tp.b = floor_double(l.getX() * 32D);
		tp.c = floor_double(l.getY() * 32D);
		tp.d = floor_double(l.getZ() * 32D);
		tp.e = (byte)(int)((l.getYaw() * 256F) / 360F);

		author.currentRecording = new Recording(tp);

		// What entity is holding
		// Should really use five of these on a new spawn for all equipment.
		Packet5EntityEquipment packet = new Packet5EntityEquipment();
		packet.b = 0;
		packet.c = player.getInventory().getItemInHand().getTypeId();
		if (packet.c == 0) packet.c = -1;
		author.currentRecording.recordPacket(packet);

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
