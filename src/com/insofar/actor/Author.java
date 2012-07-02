package com.insofar.actor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

public class Author {
	
	// The player
	private Player player;
	
	// Properties for troupes
	private boolean isRecording;
	/**
	 * Set of players in troupe
	 */
	private Set<Player> troupeMembers;
	
	/**
	 * PlayerName to Recording map for the troupe
	 */
	private HashMap<String, Recording> troupeRecMap = new HashMap<String, Recording>();
	
	/*********************************************************************
	 * Getters and setters
	*********************************************************************/
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public Set<Player> getTroupeMembers() {
		if (troupeMembers == null)
		{
			// Troupe always starts with the player in it
			troupeMembers = new HashSet<Player>();
			troupeMembers.add(player);
		}
		return troupeMembers;
	}
	public void setTroupeMembers(Set<Player> troupeMembers) {
		this.troupeMembers = troupeMembers;
	}
	public HashMap<String, Recording> getTroupRecMap() {
		return troupeRecMap;
	}
	public void setTroupeRecMap(HashMap<String, Recording> troupeRecording) {
		this.troupeRecMap = troupeRecording;
	}
	public boolean isRecording() {
		return isRecording;
	}
	public void setRecording(boolean isRecording) {
		this.isRecording = isRecording;
	}
	
	// "current" recording is the one named after the player.
	// there still can be other recordings in the troupe
	public Recording getCurrentRecording()
	{
		return getTroupRecMap().get(player.getName());
	}
	public void setCurrentRecording(Recording r)
	{
		getTroupRecMap().put(player.getName(), r);
	}
}
