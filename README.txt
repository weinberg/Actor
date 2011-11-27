Actor Plugin
------------

Author: 		Joshua Weinberg
Contact: 		jrwap@wwwrl.com
Version: 		0.1 BETA
License:		GNU Lesser General Public License (see LICENSE.txt)			  
Description: 	A library which allows recording and playback of minecraft players.
				While intended primarily as a library for use in other plugins it does
				provide Bukkit commands for those who just want to play around.
				
Notice:			This is a beta release. Use it with caution. 

Installation: 	Build the library and put the resulting plugin jar into plugins on your
				server. 
			  
Command Usage:	When using the plugin to provide commands directly just install the plugin
				and then allow users to access the following commands:			  
				
			record:
			   description: start recording into buffer
			   usage: /record
			   
			stoprec:
			   description: Stop recording
			   usage: /stoprec
			   
			actor:
			   description: Spawn new actor using recording in buffer
			   usage: /actor name
			   
			action:
			   description: playback actor or all actors
			   usage: /action [actorname]
			   
			actionrec:
			   description: playback actor or all actors and record
			   usage: /actionrec [actorname]
			   
			cut:
			   description: Stop actor or all actors
			   usage: /cut [actorname]
		
			reset:
			   description: Rewind actor or all actors
			   usage: /reset [actorname]
			   
			remove:
			   description: Remove an actor
			   usage: /remove [actorname]
			   
			save:
			   description: Save an actor's recording to a file
			   usage: /save actorname filename
			   
			load:
			   description: Spawn an actor with the give recording file
			   usage: /load actorname filename
			   
Library Usage: When using the plugin as a library just add the library as a dependency to
				your project and access the library API like so:
				
				* Record a player's actions
				ActorPlugin.instance.record(player);
				
				* Stop recording a player
				ActorPlugin.instance.stopRecord(player);
				
				* Spawn a new actor using a given player's latest recording
				EntityActor newActor = ActorPlugin.instance.spawnActor(player, actorName);
				
				* etc... The full API is shown in the javadocs for ActorPlugin
			   