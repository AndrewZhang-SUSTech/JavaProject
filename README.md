# My Project

___ how to create a map file ___

Create a json file containing a two-dimensional array

[
	[1,1,1,1,1,1,0,0],
	[1,0,0,0,0,1,1,1],
	[1,0,0,0,2,2,0,1],
	[1,0,10,10,10,20,0,1],
	[1,0,0,1,0,2,0,1],
	[1,1,1,1,1,1,1,1]
]

Like this.

The Unit digit number cannot be changed during one game.
1 represents the wall.
0 represents the free space.
2 represents the target location.
The Then digit number can be changed during one game.
Ten digit 1 represents the box.
Ten digit 2 represents the hero/player.
So that 12 represents a box on the target location and 22 represents the player on the target location.

__ Requirements __
1.There should be only one hero/player
2.The amoount of boxes should be same as the targets