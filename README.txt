Actor Plugin
------------

Author: 		Joshua Weinberg
Contact: 		jrwap@wwwrl.com
Version: 		0.10
License:		GNU General Public License (see LICENSE.txt)			  
Description: 	A library which allows in-game recording and playback of minecraft players.
				While intended primarily as a library for use in other plugins it does
				provide Bukkit commands for those who just want to play around.
				
Notice:			This is a beta release.

Installation: 	Build the library and put the resulting plugin jar into plugins on your
				server. Or use a pre-built jar and put it in your plugins dir. The first
				time it is loaded it will create an Actor folder in your plugins dir where
				all your saved actors and scenes will be created.
			  
Command Usage:	When using the plugin to provide commands directly just install the plugin
				and then allow users to access the following commands:			  
				
   actor record:
      description: start recording into buffer
      usage: /record
      
   actor cut:
      description: Stop recording and playback on all actors
      usage: /cut

   actor hire:
      description: Spawn new actor using recording in buffer
      usage: /actor name
      
   actor dub:
      description: Duplicate one or all actors with a translation
      usage: /dub [name|all] x y z
      
   actor fire:
      description: Remove an actor(s). Name can be "all".
      usage: /fire name
      
   actor action:
      description: playback actor or all actors
      usage: /action [actorname]
      
   actor actionrec:
      description: playback actor or all actors and record
      usage: /actionrec [actorname]
      
   actor loop:
      description: Set an actor (or "all") to loop
      usage: /loop [on|off] actorname

   actor reset:
      description: Rewind actor or all actors
      usage: /reset [actorname]
      
   actor remove:
      description: Remove an actor
      usage: /remove [actorname]
      
   actor troupe:
      description: Work with a troupe of players to record many actors at once.
      usage: /troupe [show|add|remove|record|hire|fire]
      		 		 'add' and 'remove' subcommands require playerName

   actor saveactor:
      description: Save an actor's recording to a file
      usage: /save actorname filename
      
   actor savescene:
      description: Save all actor recordings to dir/actorname
      usage: /savescene scenename
      
   actor loadactor:
      description: Spawn an actor with the give recording file
      usage: /load actorname filename
      
   actor loadscene:
      description: Load and spawn all actor recordings from dir
      usage: /loadscene scenename
			   
Library Usage: When using the plugin as a library just add the library as a dependency to
				your project and access the library API like so:
				
				* Record a player's actions
				ActorPlugin.getInstance().record(player);
				
				* Stop recording a player
				ActorPlugin.getInstance().stopRecord(player);
				
				* Spawn a new actor using a given player's latest recording
				EntityActor newActor = ActorPlugin.getInstance().spawnActor(player, actorName);
				
				* etc... The full API is shown in the javadocs for ActorPlugin
			   
