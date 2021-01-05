# Minesweeper
A Java clone of the Windows Minesweeper  
Author: Ying Jin, Carnegie Mellon University  
https://github.com/drjinying/Minesweeper

![Screenshot](https://github.com/drjinying/Minesweeper/blob/master/Screen%20Shot.png) 

# How to run:
	1. (Double click the jar file)
	2. “java -jar Minesweeper.jar [Board_Width Number_of_Mines]”

	If parameters are not specified, will load default.
	Examples:
		java -jar Minesweeper.jar	(recommended)
		java -jar Minesweeper.jar 20 50

# How to play:
	do the same as what you do on the Microsoft Minesweeper:
	1. left click to uncover a cell
	2. right click to flag/unflag a cell

# Assumptions:
	Java 8
	Tested on Mac OS

# References:
	1. Java Game Programming, learned from here: 
		https://www.youtube.com/playlist?list=PLah6faXAgguMnTBs3JnEJY0shAc18XYQZ
	2. The icon image (skin) is polled out from:
		http://www.minesweeper.info/downloads/MinesweeperX.html

# Test Cases:
	1. Invalid parameters (negative, 0)
	2. Number_of_Mines >= Board_Width * Board_Width
	3. Very big Board_Width
	4. Play the game in different ways (lose, win, ...)
