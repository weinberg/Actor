package com.insofar.actor;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

public class Author {
	
	// The player
	private Player player;
	
	// Properties for making recordings
	private boolean isRecording;
	private Recording currentRecording;
	
	// Properties for troupes
	/**
	 * List of players in troupe
	 */
	private ArrayList<Player> troupeMembers;
	
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
	public ArrayList<Player> getTroupeMembers() {
		return troupeMembers;
	}
	public void setTroupeMembers(ArrayList<Player> troupeMembers) {
		this.troupeMembers = troupeMembers;
	}
	public HashMap<String, Recording> getTroupeRecording() {
		return troupeRecording;
	}
	public void setTroupeRecording(HashMap<String, Recording> troupeRecording) {
		this.troupeRecording = troupeRecording;
	}

}
