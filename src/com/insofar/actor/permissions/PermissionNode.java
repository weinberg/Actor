package com.insofar.actor.permissions;

public enum PermissionNode
{
	COMMAND_RECORD(".command.record"), COMMAND_CUT(".command.cut"), COMMAND_HIRE(
			".command.hire"), COMMAND_DUB(".command.dub"), COMMAND_FIRE(
			".command.fire"), COMMAND_ACTION(".command.action"), COMMAND_ACTIONREC(
			".command.actionrec"), COMMAND_LOOP(".command.loop"), COMMAND_VISIBLE(
			".command.visible"), COMMAND_RESET(".command.reset"), COMMAND_SAVE_ACTOR(
			".command.saveactor"), COMMAND_SAVE_SCENE(".command.savescene"), COMMAND_LOAD_ACTOR(
			".command.loadactor"), COMMAND_LOAD_SCENE(".command.loadscene");

	private static final String prefix = "Actor";
	private String node;

	private PermissionNode(String node)
	{
		this.node = prefix + node;
	}

	public String getNode()
	{
		return node;
	}
}
