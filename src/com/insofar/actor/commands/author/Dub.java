package com.insofar.actor.commands.author;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.World;

import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;

import com.insofar.actor.author.EntityActor;
import com.insofar.actor.author.Recording;
import com.insofar.actor.author.Viewer;

/**
 * ActorPlugin command to duplicate an actor with a translation
 * 
 * @author Joshua Weinberg
 *
 */
public class Dub extends AuthorBaseCommand {

	public Dub()
	{
		super();
	}

	/*****************************************************************************
	 * 
	 * BUKKIT COMMAND
	 * 
	 ******************************************************************************/

	@Override
	/**
	 * Dub command
	 */
	public boolean execute()
	{
		if (args.length != 4)
		{
			player.sendMessage("Error: Usage: dub [actorname|all] x y z");
			return true;
		}

		String actorName = args[0];
		List<EntityActor> newActors = new ArrayList<EntityActor>();

		for (EntityActor actor : plugin.actors)
		{
			if ((actorName.equals("all") || actor.name.equals(actorName)) && actor.hasViewer(player))
			{
				EntityActor newActor = dub(actor, actor.name, player, player.getWorld(),
						Integer.parseInt(args[1]),
						Integer.parseInt(args[2]),
						Integer.parseInt(args[3]));

				newActors.add(newActor);
			}
		}

		plugin.actors.addAll(newActors);

		return true;
	}

	/*****************************************************************************
	 * 
	 * API COMMAND
	 * 
	 ******************************************************************************/
	
	/**
	 * dub an actor
	 */
	public EntityActor dub(EntityActor actor, String newName, Player viewerPlayer, org.bukkit.World world, int x, int y, int z)
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

}
