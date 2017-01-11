package com.shonejin.minesweeper.game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseManager implements MouseListener {

	private Game game;

	public MouseManager(Game game) {
		this.game = game;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		boolean isLeft = (e.getButton() == MouseEvent.BUTTON1);
		if (game != null)
			game.onClick(isLeft, e.getX(), e.getY());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
