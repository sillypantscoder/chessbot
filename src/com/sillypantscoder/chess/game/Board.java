package com.sillypantscoder.chess.game;

import com.sillypantscoder.utils.SelfAwareMap;

public class Board {
	public SelfAwareMap<Cell> cells;
	public Board() {
		this.cells = new SelfAwareMap<Cell>();
		// default 8x8 board
		this.cells.putAll(Board.generateStandard8x8());
	}
	public static SelfAwareMap<Cell> generateStandard8x8() {
		SelfAwareMap<Cell> cells = new SelfAwareMap<Cell>();
		// Create cells
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				Cell c = new Cell(x + ", " + y, new Direction("up", y < 4));
				cells.put(c);
			}
		}
		// Connections
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				String id = x + ", " + y;
				Cell cell = cells.get(id);
				if (x > 0) {
					String leftID = (x - 1) + ", " + y;
					Cell leftCell = cells.get(leftID);
					cell.connections.put("left", leftCell);
				}
				if (y > 0) {
					String upID = x + ", " + (y - 1);
					Cell upCell = cells.get(upID);
					cell.connections.put("up", upCell);
				}
			}
		}
		// Reverse connections
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				String id = x + ", " + y;
				Cell cell = cells.get(id);
				cell.updateReverseConnections(cells.values());
			}
		}
		// finish
		return cells;
	}
}
