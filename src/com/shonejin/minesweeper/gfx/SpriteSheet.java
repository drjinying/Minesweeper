package com.shonejin.minesweeper.gfx;

import java.awt.image.BufferedImage;

public class SpriteSheet {

	private BufferedImage sheet;

	public SpriteSheet(BufferedImage sheet) {
		this.sheet = sheet;
	}

	public BufferedImage crop(int row, int col, int width) {
		int y = width * row;
		int x = width * col;
		return sheet.getSubimage(x, y, width, width);
	}
}
