package com.insofar.actor.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;

import com.insofar.actor.ActorPlugin;

public class RootConfig
{
	// Class variables
	private ActorPlugin plugin;
	public boolean includeCancelledEvents, debugEvents;

	/**
	 * Loads config from yaml file
	 */
	public RootConfig(ActorPlugin plugin)
	{
		this.plugin = plugin;
		// Init config files:
		ConfigurationSection config = plugin.getConfig();
		final Map<String, Object> defaults = new LinkedHashMap<String, Object>();
		defaults.put("record.includeCancelledEvents", false);
		defaults.put("debug.events", false);
		defaults.put("version", plugin.getDescription().getVersion());

		// Insert defaults into config file if they're not present
		for (final Entry<String, Object> e : defaults.entrySet())
		{
			if (!config.contains(e.getKey()))
			{
				config.set(e.getKey(), e.getValue());
			}
		}
		// Save config
		plugin.saveConfig();
		// Load variables
		loadVariables();
		boundsCheck();
	}

	public void reload()
	{
		// Reload
		plugin.reloadConfig();
		loadVariables();
		boundsCheck();
	}

	public void loadVariables()
	{
		// Grab config
		ConfigurationSection config = plugin.getConfig();
		// Load variables from config
		debugEvents = config.getBoolean("debug.events", false);
		includeCancelledEvents = config.getBoolean(
				"record.includeCancelledEvents", false);
	}

	@SuppressWarnings("unused")
	private void boundsCheck()
	{
		// Grab config
		ConfigurationSection config = plugin.getConfig();
		// TODO Bounds check on config options that are user given
	}

	/**
	 * Check if updates are necessary
	 */
	public void checkUpdate()
	{
		// Check if need to update
		ConfigurationSection config = plugin.getConfig();
		if (Double.parseDouble(plugin.getDescription().getVersion()) > Double
				.parseDouble(config.getString("version")))
		{
			// Update to latest version
			plugin.getLogger().info(
					"Updating to v" + plugin.getDescription().getVersion());
			update();
		}
	}

	/**
	 * This method is called to make the appropriate changes, most likely only
	 * necessary for database schema modification, or config structure changes,
	 * for a proper update.
	 */
	@SuppressWarnings("unused")
	private void update()
	{
		// Grab current version
		double ver = Double
				.parseDouble(plugin.getConfig().getString("version"));
		// TODO check the version currently written in config and do anything
		// necessary
		// Update version number in config.yml
		plugin.getConfig().set("version", plugin.getDescription().getVersion());
		plugin.saveConfig();
	}

	public void set(String path, Object o)
	{
		final ConfigurationSection config = plugin.getConfig();
		config.set(path, o);
		plugin.saveConfig();
	}
}
