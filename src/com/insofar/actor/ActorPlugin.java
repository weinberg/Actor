package com.insofar.actor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.insofar.actor.commands.Commander;
import com.insofar.actor.config.RootConfig;
import com.insofar.actor.listeners.AuthorBlockListener;
import com.insofar.actor.listeners.AuthorPlayerListener;
import com.insofar.actor.listeners.PlayerListener;
import com.insofar.actor.permissions.PermissionHandler;

/**
 * Actor Plugin for recording and playback of character actions and movement.
 * 
 * @author Joshua Weinberg
 */
public class ActorPlugin extends JavaPlugin
{

	/*****************************************************************************
	 * 
	 * Bukkit plugin properties
	 * 
	 ******************************************************************************/

	//public static ActorPlugin plugin;
	private RootConfig config;
	private PluginDescriptionFile pdfFile;
	private final List<Listener> listeners = new ArrayList<Listener>();
	public String scenePath;
	public String savePath;
	public static final String TAG = "[Actor]";

	/*****************************************************************************
	 * 
	 * Actor plugin properties
	 * 
	 ******************************************************************************/

	/**
	 * Static instance of this plugin for use by other plugins.
	 */
	public static ActorPlugin instance;

	/**
	 * Authors by username. An author is the player recording the actors.
	 */
	public HashMap<String, Author> authors = new HashMap<String, Author>();

	/*****************************************************************************
	 * 
	 * Bukkit Plugin Properties
	 * 
	 ******************************************************************************/

	/**
	 * Actors - this is only for use of the bukkit commands in this plugin. When
	 * using this plugin as a library you need to keep track of your own actors.
	 */
	public ArrayList<EntityActor> actors = new ArrayList<EntityActor>();

	/*****************************************************************************
	 * 
	 * Bukkit Methods
	 * 
	 ******************************************************************************/

	@Override
	/**
	 * Enable
	 */
	public void onEnable()
	{
		pdfFile = this.getDescription();
		if (init())
		{
			getLogger().info(
					pdfFile.getName() + " version " + pdfFile.getVersion()
							+ " is now enabled.");
		}
		else
		{
			getLogger().severe(
					pdfFile.getName() + " version " + pdfFile.getVersion()
							+ " failed to init.");
		}
	}

	@Override
	/**
	 * Disable
	 */
	public void onDisable()
	{
		authors = null;
		getLogger().info(
				pdfFile.getName() + " version " + pdfFile.getVersion()
						+ " is now disabled.");
	}

	/*****************************************************************************
	 * 
	 * Actor Methods
	 * 
	 ******************************************************************************/

	/**
	 * Actor init.
	 */
	private Boolean init()
	{
		// Set up static instance
		instance = this;
		// Setup Config
		config = new RootConfig(this);
		// Setup Permissions
		PermissionHandler.init(this);
		// Setup API
		ActorAPI.init(this);
		// Init data folder
		if (initDataFolder())
		{
			if (config.debugVerbose)
				getLogger().info("Actor: Data folder inited");
		}
		else
		{
			getLogger().severe("Actor: Data folder failed to init");
			return false;
		}

		// Init commands
		if (initCommands())
		{
			if (config.debugVerbose)
				getLogger().info("Actor: Commands inited");
		}
		else
		{
			getLogger().severe("Actor: Commands failed to init");
			return false;
		}

		// Init scheduler
		if (initScheduler())
		{
			if (config.debugVerbose)
				getLogger().info("Actor: Scheduler inited");
		}
		else
		{
			getLogger().severe("Actor: Scheduler failed init");
			return false;
		}

		// Init Listeners
		if (initListeners())
		{
			if (config.debugVerbose)
				getLogger().info("Actor: Listeners inited");
		}
		else
		{
			getLogger().severe("Actor: Listeners failed init");
			return false;
		}

		return true;
	}

	/**
	 * Init commands
	 */
	private Boolean initDataFolder()
	{
		final File dataFolder = getDataFolder();
		final File sceneFolder = new File(dataFolder, "scenes");
		final File saveFolder = new File(dataFolder, "save");
		try
		{
			//Create directories
			dataFolder.mkdirs();
			sceneFolder.mkdirs();
			saveFolder.mkdirs();
			//Grab paths
			scenePath = sceneFolder.getPath();
			savePath = saveFolder.getPath();
			//Logging
			if (config.debugVerbose)
			{
				getLogger().info("Scene dir = " + scenePath);
				getLogger().info("Save dir = " + savePath);
			}
		}
		catch (SecurityException s)
		{
			getLogger().severe("Could not create data folders.");
			s.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Init commands
	 */
	private Boolean initCommands()
	{
		// Set up Bukkit commands
		getCommand("actor").setExecutor(new Commander());
		return true;
	}

	/**
	 * Start the bukkit scheduler task
	 * 
	 * @return
	 */
	private Boolean initScheduler()
	{
		return getServer().getScheduler().scheduleSyncRepeatingTask(this,
				new Runnable() {

					@Override
					public void run()
					{

						for (EntityActor actor : actors)
						{
							// Each tick update the actors
							actor.tick();
						}
					}
				}, 1L, 1L) != -1;
	}

	/**
	 * Init listeners
	 * 
	 * @return
	 */
	private Boolean initListeners()
	{
		//Grab plugin manager
		final PluginManager pm = getServer().getPluginManager();
		/* AuthorPlayerListener */
		AuthorPlayerListener al = new AuthorPlayerListener(this);
		pm.registerEvents(al, this);
		listeners.add(al);
		/* AuthorBlockListener */
		AuthorBlockListener abl = new AuthorBlockListener(this);
		pm.registerEvents(abl, this);
		listeners.add(abl);
		/* PlayerListener */
		PlayerListener pl = new PlayerListener(this);
		pm.registerEvents(pl, this);
		listeners.add(pl);

		return true;
	}

	public RootConfig getRootConfig()
	{
		return config;
	}

	/*****************************************************************************
	 * 
	 * Actor Plugin API
	 * 
	 ******************************************************************************/

	/**
	 * Certain actions are not here because you don't need a method to perform
	 * them. When you create an actor you get an EntityActor object back. Use
	 * that to:
	 * 
	 * Action: entityActor.isPlayback = true Cut: entityActor.isPlayback = false
	 * Reset: entityActor.rewind() Loop: entityActor.loop = true Visible:
	 * entityActor.allPlayersView = true;
	 * 
	 */

	/**
	 * Start recording.
	 */
	public boolean record(Player player)
	{
		return ActorAPI.record(player);
	}

	/**
	 * Stop recording.
	 * 
	 * @param player
	 * @return
	 */
	public boolean stopRecord(Player player)
	{
		return ActorAPI.stopRecording(player);
	}

	/**
	 * Spawns an actor using the player's current recording as it's recording.
	 * 
	 * @param player
	 * @return
	 */
	public EntityActor spawnActor(Player player, String actorName)
	{
		return ActorAPI.actor(player, actorName);
	}

	/**
	 * Create an actor with access to all parameters
	 */
	public EntityActor actor(Recording recording, String actorName,
			Player viewerPlayer, org.bukkit.World world, int x, int y, int z)
	{
		return ActorAPI.actor(recording, actorName, world, x, y,
				z);
	}

	/**
	 * Removes an actor
	 * 
	 * @param player
	 * @return
	 */
	public boolean removeActor(EntityActor actor)
	{
		return ActorAPI.actorRemove(actor);
	}

	/**
	 * Reset author's current recording. Returns and blocks used.
	 */
	public boolean resetAuthorRecording(Player player)
	{
		ActorAPI.resetAuthor(player);
		return true;
	}

	/**
	 * Saves the actor's recording in dir using the actor name as the filename
	 * in that dir.
	 * 
	 * @param actor
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	public boolean saveActorRecording(EntityActor actor, String filename)
			throws IOException
	{
		FileOutputStream fos = new FileOutputStream(filename);
		DataOutputStream dos = new DataOutputStream(fos);
		return actor.getRecording().write(dos);
	}

	/**
	 * Save recording in filename.
	 * 
	 * @param recording
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public boolean saveRecording(Recording recording, String filename)
			throws IOException
	{
		FileOutputStream fos = new FileOutputStream(filename);
		DataOutputStream dos = new DataOutputStream(fos);
		return recording.write(dos);
	}

	/**
	 * Spawns an actor with the given recording filename.
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public EntityActor spawnActorWithRecording(String name,
			String recordingPath, Player viewerPlayer, World world)
			throws IOException
	{
		FileInputStream fis = new FileInputStream(recordingPath);
		DataInputStream dis = new DataInputStream(fis);
		Recording recording = new Recording();
		recording.read(dis);
		return ActorAPI.actor(recording, name, world);
	}

}
