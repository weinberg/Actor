package com.insofar.actor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.server.Packet;

public class RecordedPacket {
	
	// Delta from previous packet;
	public long delta;
	
	// Packet having an entityId 
	public Packet packet;
	
    public void write(DataOutputStream dataoutputstream)
        throws IOException
    {
    	dataoutputstream.writeLong(delta);
        Packet.a(packet, dataoutputstream);
    }

    public static RecordedPacket read(DataInputStream datainputstream, boolean flag)
        throws IOException
    {
    	RecordedPacket rp = new RecordedPacket();
    	rp.delta = datainputstream.readLong();
    	rp.packet = (Packet.a(datainputstream, flag));
    	
    	return rp;
    }

}
