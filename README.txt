Actor Plugin
------------

Author: 		Joshua Weinberg
Contact: 		jrwap@wwwrl.com
Version: 		0.1
Description: 	A library which allows recording and playback of minecraft players.
				While intended primarily as a library for use in other plugins it does
				provide Bukkit commands for those who just want to play around.

Installation: 	Build the library and put the resulting plugin jar into plugins on your
				server. 
			  
Commands:			  

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