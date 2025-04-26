package com.sillypantscoder.chess;

import java.net.URL;

import com.sillypantscoder.windowlib.Surface;

public class Spritesheet {
	public Surface sheet;
	public int pixelSize;
	public Spritesheet(String filename) {
		URL sheetURL = this.getClass().getResource("/resources/" + filename + ".png");
		this.sheet = Surface.loadImage(sheetURL);
		this.pixelSize = Math.floorDiv(this.sheet.get_width(), 6);
	}
	public Surface getImage(int x, int y) {
		int pixelX = pixelSize * x;
		int pixelY = pixelSize * y;
		return this.sheet.crop(pixelX, pixelY, pixelSize, pixelSize);
	}
}
