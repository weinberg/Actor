package com.insofar.actor.commands.author;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet53BlockChange;

import com.insofar.actor.author.Author;
import com.insofar.actor.author.EntityActor;

/**
 * InfoCraft Plugin command to play a game.
 * 
 * @author Joshua Weinberg
 *
 */
public class Reset extends AuthorBaseCommand {

	public Reset()
	{
		super();
	}
	
	/*************************************************************************
	 * 
	 * BUKKIT COMMAND
	 * 
	*************************************************************************/

	@Override
	/**
	 * rewind one or all actors
	 */
	public boolean execute()
	{
		String actorName = args.length > 0 ? args[0] : "";

		resetAuthor(player);
		
		for (EntityActor actor : plugin.actors)
		{
			if (actor.name.equals(actorName))
			{
				actor.rewind();
			}
		}
		
		return true;
	}
	
	/*************************************************************************
	 * 
	 * API COMMANDS
	 * 
	*************************************************************************/
	
	/**
	 * Reset the author's current recording and give back any blocks used.
	 * @param author
	 */
	public void resetAuthor(Player player)
	{
		Author author = plugin.authors.get(player.getName());
		
		// Rewind the player's current recording and give back any blocks used.
		rewindAuthor(author);
	}

	/**
	 * Rewind the author's current recording. This rewinds the current
	 * recording before it has been assigned to an actor.
	 */
	public void rewindAuthor(Author author)
	{
		MinecraftServer minecraftServer = ((CraftServer)Bukkit.getServer()).getServer();
		
		if (author == null)
			return;
		
		if (author.isRecording)
		{
			author.player.sendMessage("Stopping recording.");
			author.isRecording = false;
		}

		if (author.currentRecording == null ||
				author.currentRecording.rewindPackets.size() == 0)
		{
			// Nothing to do
			return;
		}
		
		World world = author.player.getWorld();

		ArrayList<Packet> rewindPackets = author.currentRecording.getRewindPackets();

		for (Packet p : rewindPackets)
		{
			if (p instanceof Packet53BlockChange)
			{
				// get current block type
				Location location = new Location(world,
						((Packet53BlockChange) p).a,
						((Packet53BlockChange) p).b,
						((Packet53BlockChange) p).c);
				int currType = world.getBlockTypeIdAt(location);
				int currMaterial = world.getBlockAt(location).getData();
				
				// Set the block in the server's world
				if (currType != ((Packet53BlockChange) p).data )
				{
					Block block = world.getBlockAt(location);
					block.setTypeId(((Packet53BlockChange) p).data);
					block.setData((byte)((Packet53BlockChange) p).material);
					minecraftServer.serverConfigurationManager.a(author.player.getName(),p);
				
					if (currType != 0)
					{
						// Do an item drop so player gets the blocks back
						ItemStack is = new ItemStack( currType ,1);
						MaterialData data = new MaterialData(currMaterial);
						is.setData(data);
						author.player.getWorld().dropItemNaturally(author.player.getLocation(), is);
					}
				}
			}
		}

		author.currentRecording.rewind();

	}
}
