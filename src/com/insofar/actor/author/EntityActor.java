package com.insofar.actor.author;

import java.util.ArrayList;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Packet;
import net.minecraft.server.Packet18ArmAnimation;
import net.minecraft.server.Packet20NamedEntitySpawn;
import net.minecraft.server.Packet33RelEntityMoveLook;
import net.minecraft.server.Packet34EntityTeleport;
import net.minecraft.server.Packet35EntityHeadRotation;
import net.minecraft.server.Packet3Chat;
import net.minecraft.server.Packet53BlockChange;
import net.minecraft.server.Packet5EntityEquipment;
import net.minecraft.server.World;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;

public class EntityActor extends EntityPlayer {

	private String name;
	private Boolean isPlayback = false;
	private int playbackTick = 0;
	private Recording recording;
	private MinecraftServer mcServer;
	private Boolean loop = false;
	private Boolean allPlayersView = false;
	private ArrayList<Viewer> viewers = new ArrayList<Viewer>();
	
	private int translateX = 0, translateY = 0, translateZ = 0;
	private long translateTime;
	

	/**
	 * Constructor
	 * @param minecraftserver
	 * @param world
	 * @param s
	 * @param iteminworldmanager
	 */
	public EntityActor(MinecraftServer minecraftserver, World world, String s,
			ItemInWorldManager iteminworldmanager) {
		super(minecraftserver, world, s, iteminworldmanager);
	}

	public void tick()
	{
		if (isPlayback && recording != null)
		{
			doPlayback();
		}
	}

	private void doPlayback()
	{
		// System.out.print(new StringBuilder("Playback entity: ").append(entityId));
		if (recording.eof())
		{
			if (loop)
			{
				recording.rewind();
			}
			else
			{
				//System.out.print(new StringBuilder(" EOF"));
				isPlayback = false;
				return;
			}
		}

		ArrayList<Packet>packets = recording.getNextPlaybackPackets();

		if (packets != null && packets.size() > 0)
		{
			//System.out.println(new StringBuilder(" #packets=").append(packets.size()).append(": ").toString());
			sendPackets(packets);
		}
	}
	
	/**
	 * Spawn the EntityActor in the world.
	 */
	public void spawn()
	{
		// Send spawn packet to the viewer
		Packet20NamedEntitySpawn np = new Packet20NamedEntitySpawn(this);
		np.a = id;
		sendPacket(np);
		
		// Send jumpstart packets to viewer
		sendPackets(recording.getJumpstartPackets());
	}


	/**
	 * Rewind this actor - sending all rewind packets to viewers and a teleport to the jumpstart
	 */
	public void rewind()
	{
		// Send rewind packets
		for (Packet p : recording.rewindPackets)
		{
			if (p instanceof Packet53BlockChange)
			{
				// Set the block in the server's world so it is in sync with the client
				world.setRawTypeIdAndData(
						((Packet53BlockChange) p).a,
						((Packet53BlockChange) p).b,
						((Packet53BlockChange) p).c,
						((Packet53BlockChange) p).material,
						((Packet53BlockChange) p).data);

				sendPacket(p);
			}
		}

		// Rewind the recording and send the jumpstart packets
		recording.rewind();
		sendPackets(recording.getJumpstartPackets());
	}
	
	/**
	 * Send the given packets to all viewers
	 * @param packets
	 */
	public void sendPackets(ArrayList<Packet> packets)
	{
			for (int i = 0; i < packets.size(); i++)
			{
				// For cloning packets
				Packet newp = null;
				Packet p = packets.get(i);
				
				// System.out.print("Packet: "+p.a());

				//System.out.println(new StringBuilder("   ").append(p.b()).toString());

				if (p instanceof Packet33RelEntityMoveLook)
				{
					// System.out.println(new StringBuilder("   Packet33"));
					// Set the entity for this ghost on this packet
					//((Packet33RelEntityMoveLook)p).entityId = entityId;
					((Packet33RelEntityMoveLook)p).a = id;
				}
				else if (p instanceof Packet34EntityTeleport)
				{
					newp = new Packet34EntityTeleport(
							id,
							((Packet34EntityTeleport) p).b+translateX,
							((Packet34EntityTeleport) p).c+translateY,
							((Packet34EntityTeleport) p).d+translateZ,
							((Packet34EntityTeleport) p).e,
							((Packet34EntityTeleport) p).f);
					
					//((Packet34EntityTeleport)p).a = id;
					setPosition(
							(((Packet34EntityTeleport)newp).b / 32),
							(((Packet34EntityTeleport)newp).c / 32),
							(((Packet34EntityTeleport)newp).d / 32));
					
					//Packet34EntityTeleport tp = (Packet34EntityTeleport)newp;
					
					//System.out.println(" Packet34: (" + tp.b + "," + tp.c + "," + tp.d + ")" + " yaw: "+ tp.e + " pitch: " + tp.f);
					//System.out.println("    yaw: "+((Packet34EntityTeleport)newp).e);
				}
				else if (p instanceof Packet35EntityHeadRotation)
				{
					newp = new Packet35EntityHeadRotation(id, ((Packet35EntityHeadRotation)p).b);
				}
				else if (p instanceof Packet5EntityEquipment)
				{
					((Packet5EntityEquipment)p).a = id;
				}
				else if (p instanceof Packet18ArmAnimation)
				{
					((Packet18ArmAnimation)p).a = id;
				}
				else if (p instanceof Packet3Chat)
				{
					if (((Packet3Chat)p).message.indexOf(ChatColor.WHITE+"<") != 0)
					{
						((Packet3Chat)p).message = ChatColor.WHITE+"<" +
						ChatColor.RED + name + ChatColor.WHITE +
						"> "+((Packet3Chat)p).message;
					}
				}
				else if (p instanceof Packet53BlockChange)
				{
					// Set the block in the server's world so it is in sync with the client
					world.setRawTypeIdAndData(((Packet53BlockChange) p).a,
							((Packet53BlockChange) p).b,
							((Packet53BlockChange) p).c,
							((Packet53BlockChange) p).material,
							((Packet53BlockChange) p).data);
				}
				if (newp != null)
				{
					sendPacket(newp);
				}
				else
				{
					sendPacket(p);
				}
			}
	}

	/**
	 * Send all viewers a packet.
	 * @param p
	 */
	public void sendPacket(Packet p)
	{
		if (allPlayersView)
		{
			//System.out.println("Sending to all ");
			// Send to all in world
			int dimension = world.worldProvider.dimension;
			((CraftServer)Bukkit.getServer()).getServer().serverConfigurationManager.a(p,dimension);
			return;
		}

		// Send packet to the viewer(s)
		for (Viewer viewer : viewers)
		{
			//System.out.println("Sending to viewer "+viewer.player.getDisplayName());
			viewer.sendPacket(p);
		}
	}

	/**
	 * True if player is a viewer of this actor.
	 * @param player
	 */
	public boolean hasViewer(Player player)
	{
		if (allPlayersView)
			return true;

		return hasViewer(player.getName());
	}
	
	public boolean hasViewer(String name)
	{
		for (Viewer viewer : viewers)
		{
			if (viewer.player.getName().equals(name))
				return true;
		}
		return false;
	}
	
	public MinecraftServer getServer()
	{
		return mcServer;
	}

	public String getActorName()
	{
		return name;
	}

	public void setActorName(String name)
	{
		this.name = name;
	}

	public Boolean getIsPlayback()
	{
		return isPlayback;
	}

	public void setIsPlayback(Boolean isPlayback)
	{
		this.isPlayback = isPlayback;
	}

	public int getPlaybackTick()
	{
		return playbackTick;
	}

	public void setPlaybackTick(int playbackTick)
	{
		this.playbackTick = playbackTick;
	}

	public Recording getRecording()
	{
		return recording;
	}

	public void setRecording(Recording recording)
	{
		this.recording = recording;
	}

	public Boolean getLoop()
	{
		return loop;
	}

	public void setLoop(Boolean loop)
	{
		this.loop = loop;
	}

	public Boolean getAllPlayersView()
	{
		return allPlayersView;
	}

	public void setAllPlayersView(Boolean allPlayersView)
	{
		this.allPlayersView = allPlayersView;
	}

	public int getTranslateX()
	{
		return translateX;
	}

	public void setTranslateX(int translateX)
	{
		this.translateX = translateX;
	}

	public int getTranslateY()
	{
		return translateY;
	}

	public void setTranslateY(int translateY)
	{
		this.translateY = translateY;
	}

	public int getTranslateZ()
	{
		return translateZ;
	}

	public void setTranslateZ(int translateZ)
	{
		this.translateZ = translateZ;
	}

	public long getTranslateTime()
	{
		return translateTime;
	}

	public void setTranslateTime(long translateTime)
	{
		this.translateTime = translateTime;
	}

	public ArrayList<Viewer> getViewers()
	{
		return viewers;
	}

	public void setViewers(ArrayList<Viewer> viewers)
	{
		this.viewers = viewers;
	}
}
