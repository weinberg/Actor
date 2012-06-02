Actor Plugin
------------

Author: 		Joshua Weinberg
Contact: 		jrwap@wwwrl.com
Version: 		0.4 BETA
License:		GNU General Public License (see LICENSE.txt)			  
Description: 	A library which allows recording and playback of minecraft players.
				While intended primarily as a library for use in other plugins it does
				provide Bukkit commands for those who just want to play around.
				
Notice:			This is a beta release.

Installation: 	Build the library and put the resulting plugin jar into plugins on your
				server. Or use a pre-built jar and put it in your plugins dir. The first
				time it is loaded it will create an Actor folder in your plugins dir where
				all your saved actors and scenes will be created.
			  
Command Usage:	When using the plugin to provide commands directly just install the plugin
				and then allow users to access the following commands:			  
				
   record:
      description: start recording into buffer
      usage: /record
      
   cut:
      description: Stop recording and playback on all actors
      usage: /cut

   actor:
      description: Spawn new actor using recording in buffer
      usage: /actor name
      
   dub:
      description: Duplicate one or all actors with a translation
      usage: /dub [name|all] x y z
      
   fire:
      description: Remove an actor(s). Name can be "all".
      usage: /fire name
      
   action:
      description: playback actor or all actors
      usage: /action [actorname]
      
   actionrec:
      description: playback actor or all actors and record
      usage: /actionrec [actorname]
      
   loop:
      description: Set an actor (or "all") to loop
      usage: /loop [on|off] actorname

   visible:
      description: Set an actor visible to all other players or just the author.
      usage: /visible [on|off] actorname

   reset:
      description: Rewind actor or all actors
      usage: /reset [actorname]
      
   remove:
      description: Remove an actor
      usage: /remove [actorname]
      
   saveactor:
      description: Save an actor's recording to a file
      usage: /save actorname filename
      
   savescene:
      description: Save all actor recordings to dir/actorname
      usage: /savescene scenename
      
   loadactor:
      description: Spawn an actor with the give recording file
      usage: /load actorname filename
      
   loadscene:
      description: Load and spawn all actor recordings from dir
      usage: /loadscene scenename
			   
Library Usage: When using the plugin as a library just add the library as a dependency to
				your project and access the library API like so:
				
				* Record a player's actions
				ActorPlugin.instance.record(player);
				
				* Stop recording a player
				ActorPlugin.instance.stopRecord(player);
				
				* Spawn a new actor using a given player's latest recording
				EntityActor newActor = ActorPlugin.instance.spawnActor(player, actorName);
				
				* etc... The full API is shown in the javadocs for ActorPlugin
			   
