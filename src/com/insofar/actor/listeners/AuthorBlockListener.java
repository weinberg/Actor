package com.insofar.actor.listeners;

import net.minecraft.server.Packet53BlockChange;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.insofar.actor.ActorPlugin;
import com.insofar.actor.author.Author;

public class AuthorBlockListener extends BlockListener {

	public ActorPlugin plugin;

	public AuthorBlockListener(ActorPlugin instance)
	{
		plugin = instance;
	}

	@Override
	public void onBlockDamage(BlockDamageEvent event) {
		System.out.println("Block damage");
		super.onBlockDamage(event);
	}

	@Override
	public void onBlockIgnite(BlockIgniteEvent event) {
		// TODO Auto-generated method stub
		super.onBlockIgnite(event);
	}

	@Override
	public void onBlockPhysics(BlockPhysicsEvent event) {
		// TODO Auto-generated method stub
		super.onBlockPhysics(event);
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		System.out.println("Block place");
		Player p = event.getPlayer();
		
		Author author = plugin.authors.get(p.getName());
		if (author != null && author.isRecording)
		{
			Packet53BlockChange packet = new Packet53BlockChange();
			
		    int xPosition = event.getBlock().getX();
		    int yPosition = event.getBlock().getY();
		    int zPosition = event.getBlock().getZ();
		    int type = event.getBlock().getTypeId();
		    int data = event.getBlock().getData();
		    
		    packet.a=xPosition;
		    packet.b=yPosition;
		    packet.c=zPosition;
		    packet.material = type;
			packet.data = data;
			
			author.currentRecording.recordPacket(packet);
		}
		
		super.onBlockPlace(event);
	}

	@Override
	public void onBlockBurn(BlockBurnEvent event) {
		// TODO Auto-generated method stub
		super.onBlockBurn(event);
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		System.out.println("Block break");
		Player p = event.getPlayer();
		
		Author author = plugin.authors.get(p.getName());
		if (author != null && author.isRecording)
		{
			
			Packet53BlockChange packet = new Packet53BlockChange();
			
		    int xPosition = event.getBlock().getX();
		    int yPosition = event.getBlock().getY();
		    int zPosition = event.getBlock().getZ();
		    int type = 0;
		    int data = 0;

		    packet.a=xPosition;
		    packet.b=yPosition;
		    packet.c=zPosition;
		    packet.material = type;
			packet.data = data;
			
			author.currentRecording.recordPacket(packet);
		}
		
		super.onBlockBreak(event);
	}
	
	

}
