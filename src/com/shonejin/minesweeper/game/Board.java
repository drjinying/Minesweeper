package com.shonejin.minesweeper.game;

import java.awt.Graphics;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import com.shonejin.minesweeper.game.states.*;
import com.shonejin.minesweeper.gfx.Assets;

/*
 * Class Board:
 * 		manages the underlying data
 * 		and update the data when user clicks the mouse
 * */

public class Board {
	private int N;
	private int NMines;
	private int NCovered;
	private GameStates gameState;

	// save data in separate arrays, instead of an array of objects
	// this increases CPU-Register cache hit so can run faster
	private boolean[][] isMine;
	private int[][] mineCnt;
	private CellStates[][] states;

	// used in putMines() and bfs()
	private final int[] di = new int[] { -1, -1, -1, 0, 1, 1, 1, 0 };
	private final int[] dj = new int[] { -1, 0, 1, 1, 1, 0, -1, -1 };

	private final CellStates[] uncoveredStates = new CellStates[] { 
			CellStates.UNC0, CellStates.UNC1, CellStates.UNC2, CellStates.UNC3, 
			CellStates.UNC4, CellStates.UNC5, CellStates.UNC6, CellStates.UNC7, CellStates.UNC8 
	};

	public Board(int N, int NMines) {
		// parameters should have be checked before here
		// if, somehow, they are still invalid, overwrite them with defaults
		if (N < 10 || N > 1000 || NMines < 1 || NMines > N * N) {
			N = 30;
			NMines = 100;
		}

		this.N = N;
		this.NCovered = N * N;
		this.NMines = NMines;

		isMine = new boolean[N][N];
		mineCnt = new int[N + 2][N + 2];
		states = new CellStates[N][N];

		putMines();

		for (int i = 0; i < N; i++)
			Arrays.fill(states[i], CellStates.COVERED);

		gameState = GameStates.ONGOING;
	}

	// randomly place mines in the board
	// and update "count of mines" of neighboring cells
	private void putMines() {
		Random rand = new Random();
		int mines = NMines;
		while (mines-- > 0) {
			int pos = rand.nextInt(NCovered);
			int x = pos % N;
			int y = pos / N;
			if (isMine[y][x])
				mines++;
			else {
				isMine[y][x] = true;
				for (int d = 0; d < di.length; d++) {
					mineCnt[y + di[d] + 1][x + dj[d] + 1]++;
				}
			}
		}
	}

	// when game stopped, display covered mines, etc.
	private void uncoverAll(Graphics g, boolean won) {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (states[i][j] == CellStates.COVERED && isMine[i][j]) {
					states[i][j] = won ? CellStates.FLAGGED : CellStates.MINE;
					Assets.draw(i, j, states[i][j], g);
				} else if (states[i][j] == CellStates.FLAGGED && !isMine[i][j]) {
					states[i][j] = CellStates.WRONG_FLAG;
					Assets.draw(i, j, states[i][j], g);
				}
			}
		}
	}
	
	// when click on an empty cell (has no mines around)
	// expand the uncovered region automatically
	private void bfs(int row, int col, Graphics g) {
		Queue<Integer> q = new ArrayDeque<>();
		Set<Integer> visited = new HashSet<>();

		NCovered++;
		q.add(row * N + col);
		visited.add(row * N + col);

		while (!q.isEmpty()) {
			int r = q.peek() / N;
			int c = q.poll() % N;

			if (states[r][c] != CellStates.COVERED)
				continue;

			states[r][c] = uncoveredStates[mineCnt[r + 1][c + 1]];
			Assets.draw(r, c, states[r][c], g);
			NCovered--;

			if (states[r][c] != CellStates.UNC0)
				continue;

			for (int i = 0; i < di.length; i++) {
				int _r = r + di[i];
				int _c = c + dj[i];
				int key = _r * N + _c;
				if (_r < 0 || _r >= N || _c < 0 || _c >= N || visited.contains(key))
					continue;
				q.add(key);
				visited.add(key);
			}
		}

		if (NCovered == NMines)
			gameState = GameStates.WON;
	}

	// when user left-clicks on a cell, uncover it
	// the game can end after this
	public boolean uncoverCell(int row, int col, Graphics g) {
		if (states[row][col] != CellStates.COVERED)
			return false;
		if (isMine[row][col]) {
			gameState = GameStates.LOST;
			uncoverAll(g, false);
			states[row][col] = CellStates.FIRED_MINE;
			Assets.draw(row, col, CellStates.FIRED_MINE, g);
		} else {
			NCovered--;

			Assets.draw(row, col, uncoveredStates[mineCnt[row + 1][col + 1]], g);
			if (NCovered == NMines) {
				gameState = GameStates.WON;
				uncoverAll(g, true);
			} else
				bfs(row, col, g);
		}
		return true;
	}

	// when user right-clicks on a cell, we set/remove the flag
	public void toggleFlag(int row, int col, Graphics g) {
		if (states[row][col] == CellStates.COVERED)
			states[row][col] = CellStates.FLAGGED;
		else if (states[row][col] == CellStates.FLAGGED)
			states[row][col] = CellStates.COVERED;

		Assets.draw(row, col, states[row][col], g);
	}

	public GameStates getGameState() {
		return gameState;
	}
}
