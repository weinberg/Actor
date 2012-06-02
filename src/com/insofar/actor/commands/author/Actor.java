package com.insofar.actor.commands.author;

import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.Packet20NamedEntitySpawn;
import net.minecraft.server.Packet34EntityTeleport;
import net.minecraft.server.World;

import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;

import com.insofar.actor.author.Author;
import com.insofar.actor.author.EntityActor;
import com.insofar.actor.author.Recording;
import com.insofar.actor.author.Viewer;

/**
 * ActorPlugin command to spawn an actor with the current recording
 * 
 * @author Joshua Weinberg
 *
 */
public class Actor extends AuthorBaseCommand {

	public Actor()
	{
		super();
	}

	@Override
	/**
	 * Record command
	 */
	public boolean execute()
	{
		EntityActor newActor = null;
		
		if (args.length == 0)
			newActor = actor(player);
		
		if (args.length == 1)
			newActor = actor(player, args[0]);
		
		if (args.length == 2)
			newActor = actor(player, args[0], args[1]);
		
		if (newActor != null)
		{
			plugin.actors.add(newActor);
			return true;
		}
		
		return false;
	}

	/*****************************************************************************
	 * 
	 * Bukkit Plugin Command Methods
	 * 
	 ******************************************************************************/

	/**
	 * actor
	 * @param player
	 * Player to take recording from
	 * @return
	 */
	public EntityActor actor(Player player)
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
	public EntityActor actor(Player player, String actorName)
	{
		return actor(player, actorName, player.getName());
	}
	

	/*****************************************************************************
	 * 
	 * Actor Plugin Library Methods
	 * 
	 ******************************************************************************/
	
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
	public EntityActor actor(Player player, String actorName, String viewerName)
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
	public EntityActor actor(Recording recording, String actorName, Player viewerPlayer, org.bukkit.World world)
	{
		return actor(recording, actorName, viewerPlayer, world, 0, 0, 0);
	}
	
	public EntityActor actor(Recording recording, String actorName, Player viewerPlayer, org.bukkit.World world, int x, int y, int z)
	{
		World w = ((CraftWorld) world).getHandle();
		ItemInWorldManager iw = new ItemInWorldManager(w);
		EntityActor actor = new EntityActor(minecraftServer, w, actorName, iw);
		actor.translateX = x;
		actor.translateY = y;
		actor.translateZ = z;

		if (viewerPlayer == null)
		{
			actor.allPlayersView = true;
		}

		Viewer viewer = new Viewer(viewerPlayer);
		actor.viewers.add(viewer);

		actor.recording = recording;
		actor.name = actorName;

		// Send spawn packet to the viewer
		Packet20NamedEntitySpawn np = new Packet20NamedEntitySpawn(actor);
		np.a = actor.id;
		actor.sendPacketToViewers(np);

		// Send teleport packet
		Packet34EntityTeleport packet = actor.recording.getJumpstart();
		packet.a = actor.id;
		packet.b+=x;
		packet.c+=y;
		packet.d+=z;
		actor.sendPacketToViewers(packet);

		return actor;
	}
}
