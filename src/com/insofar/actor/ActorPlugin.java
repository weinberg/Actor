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
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.insofar.actor.author.Author;
import com.insofar.actor.author.EntityActor;
import com.insofar.actor.author.Recording;
import com.insofar.actor.commands.author.Action;
import com.insofar.actor.commands.author.ActionRecord;
import com.insofar.actor.commands.author.Actor;
import com.insofar.actor.commands.author.Cut;
import com.insofar.actor.commands.author.LoadActor;
import com.insofar.actor.commands.author.LoadScene;
import com.insofar.actor.commands.author.Record;
import com.insofar.actor.commands.author.Remove;
import com.insofar.actor.commands.author.Reset;
import com.insofar.actor.commands.author.SaveActor;
import com.insofar.actor.commands.author.SaveScene;
import com.insofar.actor.commands.author.StopRecording;
import com.insofar.actor.listeners.AuthorBlockListener;
import com.insofar.actor.listeners.AuthorPlayerListener;

/**
 * Actor Plugin for recording and playback of character actions and movement.
 * @author Joshua Weinberg
 */
public class ActorPlugin extends JavaPlugin {

	/*****************************************************************************
	 * 
	 * Bukkit plugin properties
	 * 
	 ******************************************************************************/

	public static ActorPlugin plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	private PluginDescriptionFile pdfFile;
	private List<Listener> listeners;
	public String scenePath;
	public String savePath;
	
	/*****************************************************************************
	 * 
	 * Command references
	 * 
	 ******************************************************************************/
	
	private Record recordCommand;
	private StopRecording stopRecordCommand;
	private Action actionCommand;
	private Actor actorCommand;
	private Remove removeCommand;
	private Reset resetCommand;
	private ActionRecord actionRecCommand;
	private Cut cutCommand;
	private SaveActor saveActorCommand;
	private SaveScene saveSceneCommand;
	private LoadActor loadActorCommand;
	private LoadScene loadSceneCommand;
	

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
	 * Actors - this is only for use of the bukkit commands in this plugin.
	 * When using this plugin puely as a library you need to keep track of your own actors.
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
	public void onEnable() {
		pdfFile = this.getDescription();
		if (init())
		{
			logger.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is now enabled.");
		}
		else
		{
			logger.severe(pdfFile.getName() + " version " + pdfFile.getVersion() + " failed to init.");
		}
	}

	@Override
	/**
	 * Disable
	 */
	public void onDisable() {
		authors = null;
		logger.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is now disabled.");
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
		
		// Init data folder
		if (initDataFolder())
		{
			logger.info("Actor: Data folder inited");
		}
		else
		{
			logger.severe("Actor: Data folder failed to init");
			return false;
		}
		
		// Init commands
		if (initCommands())
		{
			logger.info("Actor: Commands inited");
		}
		else
		{
			logger.severe("Actor: Commands failed to init");
			return false;
		}

		// Init scheduler
		if (initScheduler())
		{
			logger.info("Actor: Scheduler inited");
		}
		else
		{
			logger.severe("Actor: Scheduler failed init");
			return false;
		}

		// Init Listeners
		if (initListeners())
		{
			logger.info("Actor: Listeners inited");
		}
		else
		{
			logger.severe("Actor: Listeners failed init");
			return false;
		}

		return true;
	}
	
	/**
	 * Init commands
	 */
	private Boolean initDataFolder()
	{
		File dataFolder = getDataFolder();
		File sceneFolder = new File(dataFolder,"scenes");
		File saveFolder = new File(dataFolder,"save");
		
		dataFolder.mkdirs();
		sceneFolder.mkdirs();
		saveFolder.mkdirs();
		
		scenePath = sceneFolder.getPath();
		savePath = saveFolder.getPath();
		
		System.out.println("Scene dir = "+scenePath);
		System.out.println("Save dir = "+savePath);
		
		return true;
	}

	/**
	 * Init commands
	 */
	private Boolean initCommands()
	{
		// Set up command references
		recordCommand = new Record();
		stopRecordCommand = new StopRecording();
		actionCommand = new Action();
		actionRecCommand = new ActionRecord();
		cutCommand = new Cut();
		resetCommand = new Reset();
		actorCommand = new Actor();
		removeCommand = new Remove();
		saveActorCommand = new SaveActor();
		saveSceneCommand = new SaveScene();
		loadActorCommand = new LoadActor();
		loadSceneCommand = new LoadScene();
		
		// Set up Bukkit commands
		getCommand("record").setExecutor(recordCommand);
		getCommand("stoprec").setExecutor(stopRecordCommand);
		getCommand("actor").setExecutor(actorCommand);
		getCommand("remove").setExecutor(removeCommand);
		getCommand("action").setExecutor(actionCommand);
		getCommand("reset").setExecutor(resetCommand);
		getCommand("actionrec").setExecutor(actionRecCommand);
		getCommand("cut").setExecutor(cutCommand);
		getCommand("saveactor").setExecutor(saveActorCommand);
		getCommand("savescene").setExecutor(saveSceneCommand);
		getCommand("loadactor").setExecutor(loadActorCommand);
		getCommand("loadscene").setExecutor(loadSceneCommand);
		return true;
	}

	/**
	 * Start the bukkit scheduler task
	 * 
	 * @return
	 */
	private Boolean initScheduler()
	{
		return getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {

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
	 * @return
	 */
	private Boolean initListeners()
	{
		// Keep track of these so we can release them on disable.
		listeners = new ArrayList<Listener>();

		PluginManager pm = getServer().getPluginManager();

		/* Authorlistener */
		AuthorPlayerListener al = new AuthorPlayerListener(this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, al, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, al, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_ANIMATION, al, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_DROP_ITEM, al, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_INTERACT, al, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, al, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_ITEM_HELD, al, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_PICKUP_ITEM, al, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_TOGGLE_SNEAK, al, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_CHAT, al, Event.Priority.Normal, this);
		listeners.add(al);

		/* AuthorBlockListener */
		AuthorBlockListener abl = new AuthorBlockListener(this);
		pm.registerEvent(Event.Type.BLOCK_PLACE, abl, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, abl, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_DAMAGE, abl, Event.Priority.Normal, this);
		listeners.add(abl);

		return true;
	}
	
	/*****************************************************************************
	 * 
	 * Actor Plugin API
	 * 
	 ******************************************************************************/
	
	/**
	 * Certain actions are not here because you don't need a method to perform them.
	 * When you create an actor you get an EntityActor object back. Use that to:
	 * 
	 * Action: 	entityActor.isPlayback = true
	 * Cut: 	entityActor.isPlayback = false
	 * Reset:	entityActor.rewind()
	 * Loop:	entityActor.loop = true
	 * 
	 */
	
	/**
	 * Start recording.
	 */
	public boolean record(Player player)
	{
		return recordCommand.record(player);
	}

	/**
	 * Stop recording.
	 * @param player
	 * @return
	 */
	public boolean stopRecord(Player player)
	{
		return stopRecordCommand.stopRecording(player);
	}

	/**
	 * Spawns an actor using the player's current recording as it's recording.
	 * @param player
	 * @return
	 */
	public EntityActor spawnActor(Player player, String actorName)
	{
		return actorCommand.actor(player, actorName);
	}
	
	/**
	 * Removes an actor
	 * @param player
	 * @return
	 */
	public boolean removeActor(EntityActor actor)
	{
		return removeCommand.actorRemove(actor);
	}
	
	/**
	 * Reset author's current recording. Returns and blocks used.
	 */
	public boolean resetAuthorRecording(Player player)
	{
		resetCommand.resetAuthor(player);
		return true;
	}
	
	/**
	 * Saves the actor's recording in dir using the actor name as the filename
	 * in that dir.
	 * @param actor
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	public boolean saveActorRecording(EntityActor actor, String filename) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(filename);
		DataOutputStream dos = new DataOutputStream(fos);
		return actor.recording.write(dos);
	}
	
	/**
	 * Save recording in filename.
	 * @param recording
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public boolean saveRecording(Recording recording, String filename) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(filename);
		DataOutputStream dos = new DataOutputStream(fos);
		return recording.write(dos);
	}
	
	/**
	 * Spawns an actor with the given recording filename.
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public EntityActor spawnActorWithRecording(String name, String recordingPath, Player viewerPlayer, World world) throws IOException
	{
		FileInputStream fis = new FileInputStream(recordingPath);
		DataInputStream dis = new DataInputStream(fis);
		Recording recording = new Recording();
		recording.read(dis);
		return actorCommand.actor(recording, name, viewerPlayer, world);
	}

}
