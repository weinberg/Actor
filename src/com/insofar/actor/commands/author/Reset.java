package com.insofar.actor.commands.author;

import net.minecraft.server.Packet34EntityTeleport;

import com.insofar.actor.ActorPlugin;
import com.insofar.actor.author.EntityActor;

/**
 * InfoCraft Plugin command to play a game.
 * 
 * @author Joshua Weinberg
 *
 */
public class Reset extends AuthorBaseCommand {

	public Reset(ActorPlugin plugin)
	{
		super(plugin);
	}

	@Override
	/**
	 * rewind one or all actors
	 */
	public boolean execute()
	{
		String actorName = args.length > 0 ? args[0] : "";
		
		for (EntityActor actor : plugin.actors)
		{
			if (actor.name.equals(actor) || actorName.equals(""))
			{
				actor.recording.rewind();
				Packet34EntityTeleport packet = actor.recording.getJumpstart();
				packet.a=actor.id;

				// Send packet to the viewers
				actor.sendPacketToViewers(packet);
			}
		}

		return true;
	}
}
