package com.insofar.actor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

public class Author {
	
	// The player
	private Player player;
	
	// Properties for making recordings
	private boolean isRecording;
	private Recording currentRecording;
	
	// Properties for troupes
	/**
	 * Set of players in troupe
	 */
	private Set<Player> troupeMembers = new HashSet<Player>();
	
	/**
	 * PlayerName to Recording map for the troupe
	 */
	private HashMap<String, Recording> troupeRecording = new HashMap<String, Recording>();
	
	/*********************************************************************
	 * Getters and setters
	*********************************************************************/
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public boolean isRecording() {
		return isRecording;
	}
	public void setRecording(boolean isRecording) {
		this.isRecording = isRecording;
	}
	public Recording getCurrentRecording() {
		return currentRecording;
	}
	public void setCurrentRecording(Recording currentRecording) {
		this.currentRecording = currentRecording;
	}
	public Set<Player> getTroupeMembers() {
		return troupeMembers;
	}
	public void setTroupeMembers(Set<Player> troupeMembers) {
		this.troupeMembers = troupeMembers;
	}
	public HashMap<String, Recording> getTroupeRecording() {
		return troupeRecording;
	}
	public void setTroupeRecording(HashMap<String, Recording> troupeRecording) {
		this.troupeRecording = troupeRecording;
	}

}
