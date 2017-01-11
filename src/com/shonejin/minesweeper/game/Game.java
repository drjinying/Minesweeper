package com.shonejin.minesweeper.game;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import com.shonejin.minesweeper.game.states.CellStates;
import com.shonejin.minesweeper.game.states.GameStates;
import com.shonejin.minesweeper.gfx.Assets;


/*
 * Class Game:
 * 		manages all resources of the game (mouse, board, graphics)
 * */
public class Game {

	private Display display;
	private int width, height;
	private int N;
	public String title;

	private boolean finished;
	private Board board;
	private MouseManager mouseManager;
	private BufferStrategy bs;

	public Game(String title, int N, int NMines) {
		this.N = N;
		width = Assets.width * N;
		height = width;
		this.title = title;

		board = new Board(N, NMines);
		mouseManager = new MouseManager(this);

		display = new Display(title, width, height);
		display.getFrame().addMouseListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().createBufferStrategy(2);
		bs = display.getCanvas().getBufferStrategy();

		Assets.init();
	}

	// called when mouse clicks happen (mouse release)
	public void onClick(boolean isLeft, int x, int y) {
		if (finished)
			return;

		int row = y / Assets.width;
		int col = x / Assets.width;
		Graphics g = bs.getDrawGraphics();

		if (isLeft)
			board.uncoverCell(row, col, g);
		else
			board.toggleFlag(row, col, g);

		bs.show();

		GameStates result = board.getGameState();
		
		// when game ends
		if (result != GameStates.ONGOING) {
			finished = true;
			System.out.println("Game ended!");
			String msg = (result == GameStates.LOST ? "!!!!! You Lose !!!!!" : "!!!!! You Won !!!!!");
			display.getFrame().setTitle(msg);
		}

		g.dispose();
	}

	// display the initial covered board when game starts
	public void start() {
		Graphics g = bs.getDrawGraphics();

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				Assets.draw(i, j, CellStates.COVERED, g);
			}
		}
		bs.show();
		g.dispose();
	}
}
