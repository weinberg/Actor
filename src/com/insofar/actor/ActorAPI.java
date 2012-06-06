package com.insofar.actor;

import java.util.ArrayList;

import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet29DestroyEntity;
import net.minecraft.server.Packet34EntityTeleport;
import net.minecraft.server.Packet35EntityHeadRotation;
import net.minecraft.server.Packet53BlockChange;
import net.minecraft.server.Packet5EntityEquipment;
import net.minecraft.server.World;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.insofar.actor.author.Author;
import com.insofar.actor.author.EntityActor;
import com.insofar.actor.author.Recording;
import com.insofar.actor.author.Viewer;

public class ActorAPI
{
	private static ActorPlugin plugin;
	private static MinecraftServer minecraftServer;
	
	public static void init(ActorPlugin actor)
	{
		plugin = actor;
		minecraftServer = ((CraftServer)Bukkit.getServer()).getServer();
	}
	
	/**
	 * actor
	 * @param player
	 * Player to take recording from
	 * @return
	 */
	public static EntityActor actor(Player player)
	{
		return actor(player, "Actor", player.getName());
	}

	/**
	 * actor
	 * @param player
	 * Player to take recording from
	 * @param actorName
	 * name to give spawned actor
	 * @return
	 */
	public static EntityActor actor(Player player, String actorName)
	{
		return actor(player, actorName, player.getName());
	}
	
	/**
	 * actor
	 * @param player
	 * Player to take recording from
	 * @param actorName
	 * name to give spawned actor
	 * @param viewerName
	 * name of viewer ("all" for everyone in player's world)
	 * @return
	 */
	public static EntityActor actor(Player player, String actorName, String viewerName)
	{
		Player viewerPlayer;
		org.bukkit.World world;
		
		if (viewerName == "all")
		{
			viewerPlayer = null;
			world = player.getWorld();
		}
		else
		{
			viewerPlayer = plugin.getServer().getPlayer(viewerName);
			if (viewerPlayer == null)
			{
				return null;
			}
			world = viewerPlayer.getWorld();
		}
		
		// Authoring commands must have an author
		Author author = getAuthor(player);

		if (author.currentRecording == null)
		{
			player.sendMessage("ERROR: No recording. You have nothing recorded.");
			return null;
		}

		if (author.isRecording)
		{
			player.sendMessage("ERROR: Recording. You are currently recording. Stop recording first.");
			return null;
		}

		EntityActor result = actor(author.currentRecording, actorName, viewerPlayer, world);
		
		author.currentRecording = null;
		
		if (result != null)
		{
			player.sendMessage("Spawned entity with id = "+result.id+" for player "+viewerName);
			return result;
		}
		else
		{
			return null;
		}

	}
	
	/**
	 * actor
	 * @param recording
	 * Recording to use for the new actor
	 * @param actorName
	 * name to give spawned actor
	 * @param viewerPlayer
	 * viewer who can see this actor (null for everyone in the same world)
	 * @param world
	 * viewer's world
	 * @return The EntityActor which was created.
	 */
	public static EntityActor actor(Recording recording, String actorName, Player viewerPlayer, org.bukkit.World world)
	{
		return actor(recording, actorName, viewerPlayer, world, 0, 0, 0);
	}
	
	public static EntityActor actor(Recording recording, String actorName, Player viewerPlayer, org.bukkit.World world, int x, int y, int z)
	{
		World w = ((CraftWorld) world).getHandle();
		ItemInWorldManager iw = new ItemInWorldManager(w);
		
		// Setup EntityActor
		EntityActor actor = new EntityActor(minecraftServer, w, actorName, iw);
		actor.recording = recording;
		actor.name = actorName;

		// Setup viewer
		if (viewerPlayer == null)
		{
			actor.allPlayersView = true;
		}
		Viewer viewer = new Viewer(viewerPlayer);
		actor.viewers.add(viewer);
		
		// Setup translation
		actor.translateX = x;
		actor.translateY = y;
		actor.translateZ = z;
		
		actor.spawn();

		return actor;
	}
	
	/**
	 * dub an actor
	 */
	public static EntityActor dub(EntityActor actor, String newName,
			Player viewerPlayer, org.bukkit.World world, int x, int y, int z)
	{
		World w = ((CraftWorld) world).getHandle();
		ItemInWorldManager iw = new ItemInWorldManager(w);
		EntityActor newActor = new EntityActor(minecraftServer, w, newName, iw);
		newActor.translateX = actor.translateX + x;
		newActor.translateY = actor.translateY + y;
		newActor.translateZ = actor.translateZ + z;

		if (viewerPlayer == null)
		{
			actor.allPlayersView = true;
		}

		Viewer viewer = new Viewer(viewerPlayer);
		newActor.viewers.add(viewer);

		newActor.recording = new Recording();
		newActor.recording.recordedPackets = actor.recording.recordedPackets;

		newActor.name = newName;

		newActor.spawn();

		return newActor;
	}
	
	/**
	 * actorRemove
	 * @param actor Actor to remove
	 */
	public static boolean actorRemove(EntityActor actor)
	{
		for (Viewer viewer : actor.viewers)
		{
			viewer.sendPacket(new Packet29DestroyEntity(actor.id));
		}
		
		return true;
	}
	
	/**
	 * record command
	 * @param player
	 * @return
	 */
	public static boolean record(Player player)
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
	
	/**
	 * Reset the author's current recording and give back any blocks used.
	 * @param author
	 */
	public static void resetAuthor(Player player)
	{
		Author author = plugin.authors.get(player.getName());
		
		// Rewind the player's current recording and give back any blocks used.
		rewindAuthor(author);
	}

	/**
	 * Rewind the author's current recording. This rewinds the current
	 * recording before it has been assigned to an actor.
	 */
	public static void rewindAuthor(Author author)
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
		
		org.bukkit.World world = author.player.getWorld();

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
	
	/**
	 * stopRecording
	 * 
	 * @param player
	 * @return
	 */
	public static boolean stopRecording(Player player)
	{
		// Authoring commands must have an author
		Author author = getAuthor(player);

		if (author.currentRecording != null)
		{
			author.isRecording = false;
			author.currentRecording.rewind();
			player.sendMessage("Recording stopped.");
		}
		else
		{
			player.sendMessage("You have no recording.");
		}

		return true;
	}
	
	public static Author getAuthor(Player p)
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
}
