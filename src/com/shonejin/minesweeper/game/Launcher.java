package com.shonejin.minesweeper.game;

public class Launcher {

	public static void main(String[] args) {
		// default parameters
		int N = 20, NMines = 50;

		if (args.length > 0) {
			try {
				N = Integer.parseInt(args[0]);
				NMines = Integer.parseInt(args[1]);
			} catch (Exception ex) {
				System.out.println("Invalid parameters!");
				System.out.println("should be: java -jar Minesweeper.jar [Board_width Number_of_Mines]");
				System.exit(-1);
			}
		}
		
		// you can use a greater N if you have a huge display
		if (N < 10 || N > 1000 || NMines < 1 || NMines > N * N) {
			System.out.println("Parameters are not reasonable numbers!");
			System.out.println("should be: java -jar Minesweeper.jar [Board_width Number_of_Mines]");
			System.out.println("\tBoard_width: \t[10, 1000], otherwise your screen won't fit");
			System.out.println("\tNumber_of_Mines: \t[1, Board_width^2]");
			System.exit(-1);
		}

		Game game = new Game("Minesweeper", N, NMines);
		game.start();
	}

}
