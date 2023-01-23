package com.shonejin.minesweeper.game;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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
		int NMines = board.getNMines();
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
			// Create a new frame to enable restart
			JFrame frame = new JFrame(result == GameStates.LOST ? "!!!!! You Lose !!!!!" : "!!!!! You Won !!!!!"); //Sets title depending on win or loss
			frame.setSize(500, 150);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(true);
			frame.setLocationRelativeTo(display.getFrame());
			
			// Ask user if would like to play again
			JLabel message = new JLabel("Game Lost! Would you like to play again?", SwingConstants.CENTER);
			
			// Create yes or no buttons and panel
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridLayout(1,2));
			JButton yes = new JButton("Yes");
	        JButton no = new JButton("No");
	        
			// If the user selects yes it creates a new game and starts it
	        yes.addActionListener((ActionEvent e) -> {
	        	Game game = new Game("Minesweeper", N, NMines);
	    		game.start();
	    		
	        	display.getFrame().setVisible(false);
	        	frame.setVisible(false);
	        }); 
	        
			// If the user selects no it will close the windows and print "Game ended!" to the console
	        no.addActionListener((ActionEvent e) -> {
				System.out.println("Game ended!");
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	        });
	        
	        buttonPanel.add(yes);
	        buttonPanel.add(no);
	        
			// Create the panel to store the message
	        JPanel popUp = new JPanel();
	        popUp.setLayout(new BorderLayout(20,20));
	        popUp.add(message, BorderLayout.CENTER);
	        popUp.add(buttonPanel, BorderLayout.SOUTH);
	        
	        frame.add(popUp);
	        frame.setVisible(true);
			finished = true;
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
