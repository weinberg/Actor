package com.insofar.actor.author;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.server.Packet;
import net.minecraft.server.Packet53BlockChange;

public class Recording {
	
	// The packets recorded
	public ArrayList<RecordedPacket>recordedPackets = new ArrayList<RecordedPacket>();
	
	// Packets to send to undo the recording
	public ArrayList <Packet>rewindPackets = new ArrayList<Packet>();
	
	// Count of packets which should be sent all at once on spawn to set initial state
	public int jumpstartPacketCount = 0;

	// Index entry for recording or playback
	public int playbackIndex = 0;

	// Last packet time for computing delta
	public long lastTime;

	public Recording()
	{
		recordedPackets = new ArrayList <RecordedPacket>();
	}
	
	public void recordPacket(Packet packet)
	{
		recordPacket(packet, false);
	}

	public void recordPacket(Packet packet, Boolean isJumpstart)
	{
		RecordedPacket np = new RecordedPacket();

		long currTime = System.currentTimeMillis();

		if (recordedPackets.size() == 0 || isJumpstart)
		{
			np.delta = 0;
		}
		else
		{
			np.delta = currTime - lastTime;
		}
		
		if (isJumpstart)
		{
			jumpstartPacketCount++;
		}

		np.packet = packet;
		recordedPackets.add(np);
		lastTime = currTime;
		
		//System.out.println("Recorded packet id = "+np.packet.b());

	}

	public void addRewindPacket(Packet packet)
	{
		if (rewindPackets == null)
		{
			rewindPackets = new ArrayList<Packet>();
		}

		rewindPackets.add(packet);
	}

	public int getSize()
	{
		if (recordedPackets != null)
		{
			return recordedPackets.size();
		}
		else
		{
			return 0;
		}
	}

	public void rewind()
	{
		playbackIndex = 0;
		lastTime = 0;
	}

	public ArrayList <Packet>getRewindPackets()
	{
		ArrayList <Packet>result = new ArrayList <Packet>();

		if (rewindPackets == null || rewindPackets.size() == 0)
		{
			return result;
		}

		for (int i = rewindPackets.size()-1 ; i >= 0; i--)
		{
			Packet packet = (rewindPackets.get(i));

			if (packet instanceof Packet53BlockChange)
			{
				result.add(packet);
			}
		}

		return result;
	}

	public ArrayList <Packet>getNextPlaybackPackets()
	{
		ArrayList <Packet>result = null;
		
		// Look at next packet
		RecordedPacket p = recordedPackets.get(playbackIndex);

		long currTime = System.currentTimeMillis();
		long delta = p.delta;

		if (lastTime == 0)
		{
			lastTime = currTime;
		}

		while (currTime - lastTime >= delta && playbackIndex < recordedPackets.size())
		{
			if (result == null)
			{
				result = new ArrayList <Packet>();
			}

			Packet newPacket = (p.packet);
			
			// add this packet to the result list
			result.add(newPacket);

			// increment last time and index
			lastTime+=delta;
			playbackIndex++;

			if (eof())
			{
				break;
			}

			// look at the next packet
			p = recordedPackets.get(playbackIndex);
			delta = p.delta;
		}

		return result;
	}
	
	/**
	 * Get the jumpstart packets
	 * @return
	 */
	public ArrayList <Packet>getJumpstartPackets()
	{
		ArrayList <Packet> result = new ArrayList<Packet>();
		
		for (int jsi = 0; jsi<jumpstartPacketCount; jsi++)
		{
			result.add(recordedPackets.get(jsi).packet);
		}
		
		playbackIndex = jumpstartPacketCount;
		
		return result;
	}

	public boolean eof()
	{
		return playbackIndex >= recordedPackets.size();
	}

	/**
	 * write
	 * @param dos
	 * @return
	 */
	public boolean write(DataOutputStream dos) throws IOException
	{
		// Save file format is
		// Four bytes of file type id = APR0
		// RecordedPacketsCount(long)
		// JumpstartPacketsCount(int)
		// RecordedPacket 0..n
		// RewindPacketsCount(long)
		// RewindPacket 0..n
//			FileOutputStream fos = new FileOutputStream(fileName);
//			DataOutputStream dos = new DataOutputStream(fos);
			
			dos.writeBytes("APR0");

			long ct = 0;

			if (recordedPackets != null)
			{
				ct = (recordedPackets.size());
			}

			dos.writeLong(ct);
			
			dos.writeInt(jumpstartPacketCount);

			for (int i = 0; i < ct; i++)
			{
				recordedPackets.get(i).write(dos);
			}

			ct = 0;
			if (rewindPackets != null)
			{
				ct = (rewindPackets.size());
			}

			dos.writeLong(ct);
			for (int i = 0; i < ct; i++)
			{
				Packet.a((rewindPackets.get(i)), dos);
			}

		return true;
	}

	/**
	 * read
	 * @param fileName
	 * @return
	 */
	public boolean read(DataInputStream dis) throws IOException
	{
		recordedPackets = new ArrayList <RecordedPacket>();
		rewindPackets = new ArrayList <Packet>();
		playbackIndex = 0;

		try
		{
			
			byte[] id = new byte[4];
			dis.read(id, 0, 4);
			
			String idString = new String(id);
			if (!idString.equals("APR0"))
			{
				throw new IOException("Illegal ID");
			}

			// Recorded packets
			long ct = dis.readLong();
			
			jumpstartPacketCount = dis.readInt();
			
			for (; ct >0; ct--)
			{
				RecordedPacket np = RecordedPacket.read(dis, false);
				recordedPackets.add(np);
			}

			// Rewind packets
			ct = dis.readLong();
			for (; ct >0; ct--)
			{
				Packet rp = Packet.a(dis, false);
				if (rp instanceof Packet)
				{
					rewindPackets.add(rp);
				}
			}

		}
		catch (IOException e)
		{
			System.out.println(e.toString());
			return false;
		}

		return true;
	}


}
