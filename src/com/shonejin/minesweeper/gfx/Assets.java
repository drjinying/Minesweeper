package com.shonejin.minesweeper.gfx;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.shonejin.minesweeper.game.states.CellStates;

/*
 * Class Assets:
 * 		- Crop icons from the sprite sheet and cache as static buffered images
 * 		- Draw icons to the board
 * */
public class Assets {
	
	// the icons in the sprite sheet are 16 pixels wide
	public static final int width = 16;

	public static BufferedImage[] uncovered;
	public static BufferedImage covered;
	public static BufferedImage mine, flag;
	public static BufferedImage bombMine, wrongFlag;

	public static void init() {
		SpriteSheet sheet = new SpriteSheet(loadImage("res/predatorskin.bmp"));

		uncovered = new BufferedImage[9];
		for (int i = 0; i < uncovered.length; i++) {
			uncovered[i] = sheet.crop(0, i, width);
		}

		covered = sheet.crop(1, 0, width);
		mine = sheet.crop(1, 2, width);
		flag = sheet.crop(1, 3, width);
		wrongFlag = sheet.crop(1, 4, width);
		bombMine = sheet.crop(1, 5, width);
	}

	public static void draw(int row, int col, CellStates state, Graphics g) {
		BufferedImage img = covered;

		switch (state) {
		case COVERED:
			img = covered;
			break;
		case FLAGGED:
			img = flag;
			break;
		case UNC0:
			img = uncovered[0];
			break;
		case UNC1:
			img = uncovered[1];
			break;
		case UNC2:
			img = uncovered[2];
			break;
		case UNC3:
			img = uncovered[3];
			break;
		case UNC4:
			img = uncovered[4];
			break;
		case UNC5:
			img = uncovered[5];
			break;
		case UNC6:
			img = uncovered[6];
			break;
		case UNC7:
			img = uncovered[7];
			break;
		case UNC8:
			img = uncovered[8];
			break;
		case WRONG_FLAG:
			img = wrongFlag;
			break;
		case MINE:
			img = mine;
			break;
		case FIRED_MINE:
			img = bombMine;
			break;
		}

		g.drawImage(img, width * col, width * row, null);
	}

	public static BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(Assets.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
}
