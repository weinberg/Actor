package com.insofar.actor;

import org.bukkit.entity.Player;

public class Author {
	
	// The player
	private Player player;
	
	// Properties for making recordings
	private boolean isRecording;
	private Recording currentRecording;
	
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

}
