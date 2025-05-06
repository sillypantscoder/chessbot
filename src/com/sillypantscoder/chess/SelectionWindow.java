package com.sillypantscoder.chess;

import java.awt.Color;
import java.util.ArrayList;
import java.util.function.Supplier;

import com.sillypantscoder.chess.game.Board;
import com.sillypantscoder.windowlib.Surface;
import com.sillypantscoder.windowlib.Window;

public class SelectionWindow extends Window {
	public ArrayList<BoardGenerator> generators;
	public static record BoardGenerator(String name, Supplier<Board> generator) {}
	public Spritesheet spritesheet = new Spritesheet("pieces");
	public int mouseY = 0;
	public SelectionWindow() {
		generators = new ArrayList<BoardGenerator>();
		generators.add(new BoardGenerator("Standard 8x8 (player vs bot)", Boards::generateStandardPlayerBot8x8));
		generators.add(new BoardGenerator("Standard 8x8 (player vs player)", Boards::generateStandardPlayerPlayer8x8));
		generators.add(new BoardGenerator("Double-sized 16x16 (player vs bot)", Boards::generateDoubleSized));
		generators.add(new BoardGenerator("Horizontally looping (cylindrical) 8x8 (player vs bot)", Boards::generateHorizontallyLooping2Player8x8));
		generators.add(new BoardGenerator("Completely looping (toroidal) 8x8 (player vs bot)", Boards::generateLooping2Player8x8));
	}
	public void open() {
		this.open("Chess - Board Select", 700, 400);
	}
	public Surface getIcon() {
		return this.spritesheet.getImage(5, 0);
	}
	public Surface frame(int width, int height) {
		Surface s = new Surface(width, height, Color.WHITE);
		// Draw title
		Surface title = Surface.renderText(25, "Chess - Board Select", Color.BLACK);
		s.blit(title, 15, 0);
		int y = title.get_height() + 15;
		// Draw items
		for (BoardGenerator gen : generators) {
			String name = gen.name();
			// Find geometry
			int minY = y + 5;
			Surface renderedName = Surface.renderText(20, name, Color.BLACK);
			int maxY = y + 5 + renderedName.get_height();
			// Draw
			s.blit(renderedName, 0, minY);
			// Cursor
			if (mouseY >= minY && mouseY <= maxY) {
				s.invertArea(0, minY, width, renderedName.get_height());
			}
			// Next item
			y += 10 + renderedName.get_height();
		}
		return s;
	}
	public void keyDown(String e) {}
	public void keyUp(String e) {}
	public void mouseMoved(int x, int y) {
		this.mouseY = y;
	}
	public void mouseDown(int x, int y) {}
	public void mouseUp(int mouseX, int mouseY) {
		// Draw title
		Surface title = Surface.renderText(25, "Select Board", Color.BLACK);
		int y = title.get_height() + 15;
		// Draw items
		for (BoardGenerator gen : generators) {
			String name = gen.name();
			// Find geometry
			int minY = y + 5;
			Surface renderedName = Surface.renderText(20, name, Color.BLACK);
			int maxY = y + 5 + renderedName.get_height();
			// Cursor check
			if (mouseY >= minY && mouseY <= maxY) {
				navigate(gen);
			}
			// Next item
			y += 10 + renderedName.get_height();
		}
	}
	public void mouseWheel(int amount) {}
	public void navigate(BoardGenerator gen) {
		ChessWindow win = new ChessWindow(gen.generator().get(), gen.name());
		win.open();
	}
}
