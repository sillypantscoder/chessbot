package com.sillypantscoder.chess;

import java.util.Optional;

import com.sillypantscoder.chess.game.Board;
import com.sillypantscoder.chess.game.Cell;
import com.sillypantscoder.chess.game.Direction;
import com.sillypantscoder.chess.game.Piece;
import com.sillypantscoder.chess.game.Team;
import com.sillypantscoder.chess.game.pieces.Bishop;
import com.sillypantscoder.chess.game.pieces.King;
import com.sillypantscoder.chess.game.pieces.Knight;
import com.sillypantscoder.chess.game.pieces.Pawn;
import com.sillypantscoder.chess.game.pieces.Queen;
import com.sillypantscoder.chess.game.pieces.Rook;
import com.sillypantscoder.utils.SelfAwareMap;

public class Boards {
	@FunctionalInterface
	public static interface CellAndPositionConsumer {
		void accept(Cell c, int x, int y);
	}
	public static SelfAwareMap<Cell> generateGrid(int width, int height, CellAndPositionConsumer forEachCell) {
		// Generate cells
		SelfAwareMap<Cell> cells = new SelfAwareMap<Cell>();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Cell c = new Cell(x + ", " + y, new Direction("up", y < 4));
				cells.put(c);
				forEachCell.accept(c, x, y);
			}
		}
		// Connections
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
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
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				String id = x + ", " + y;
				Cell cell = cells.get(id);
				cell.updateReverseConnections(cells.values());
			}
		}
		return cells;
	}
	public static Board generateStandardPlayerPlayer8x8() {
		Team whiteTeam = new Team("White", 0, true);
		Team blackTeam = new Team("Black", 1, true);
		// Generate cells
		SelfAwareMap<Cell> cells = generateGrid(8, 8, (c, x, y) -> {
			// Pieces
			Team team = y < 4 ? blackTeam : whiteTeam;
			if (y == 1 || y == 6) c.piece = Optional.of(new Pawn(c.standardForwardsDirection, team));
			if (y == 0 || y == 7) {
				Piece p = new Rook(team);
				if (x == 1) p = new Knight(team);
				if (x == 2) p = new Bishop(team);
				if (x == 3) p = new Queen(team);
				if (x == 4) p = new King(team);
				if (x == 5) p = new Bishop(team);
				if (x == 6) p = new Knight(team);
				c.piece = Optional.of(p);
			}
		});
		// Finish
		Board b = new Board(8);
		b.teams = new Team[] { whiteTeam, blackTeam };
		b.cells.putAll(cells);
		return b;
	}
	public static Board generateStandardPlayerBot8x8() {
		Team whiteTeam = new Team("White", 0, true);
		Team blackTeam = new Team("Black", 1, false);
		// Generate cells
		SelfAwareMap<Cell> cells = generateGrid(8, 8, (c, x, y) -> {
			// Pieces
			Team team = y < 4 ? blackTeam : whiteTeam;
			if (y == 1 || y == 6) c.piece = Optional.of(new Pawn(c.standardForwardsDirection, team));
			if (y == 0 || y == 7) {
				Piece p = new Rook(team);
				if (x == 1) p = new Knight(team);
				if (x == 2) p = new Bishop(team);
				if (x == 3) p = new Queen(team);
				if (x == 4) p = new King(team);
				if (x == 5) p = new Bishop(team);
				if (x == 6) p = new Knight(team);
				c.piece = Optional.of(p);
			}
		});
		// Finish
		Board b = new Board(8);
		b.teams = new Team[] { whiteTeam, blackTeam };
		b.cells.putAll(cells);
		return b;
	}
	public static Board generateDoubleSized() {
		Team whiteTeam = new Team("White", 0, true);
		Team blackTeam = new Team("Black", 1, false);
		// Generate cells
		SelfAwareMap<Cell> cells = generateGrid(16, 16, (c, x, y) -> {
			// Pieces
			if (x < 4 || x >= 12) return;
			Team team = y < 8 ? blackTeam : whiteTeam;
			if (y == 1 || y == 14) c.piece = Optional.of(new Pawn(c.standardForwardsDirection, team));
			if (y == 0 || y == 15) {
				Piece p = new Rook(team);
				if (x-4 == 1) p = new Knight(team);
				if (x-4 == 2) p = new Bishop(team);
				if (x-4 == 3) p = new Queen(team);
				if (x-4 == 4) p = new King(team);
				if (x-4 == 5) p = new Bishop(team);
				if (x-4 == 6) p = new Knight(team);
				c.piece = Optional.of(p);
			}
		});
		// Finish
		Board b = new Board(16);
		b.teams = new Team[] { whiteTeam, blackTeam };
		b.cells.putAll(cells);
		return b;
	}
	public static Board generateLooping2Player8x8() {
		Team whiteTeam = new Team("White", 0, true);
		Team blackTeam = new Team("Black", 1, false);
		// Generate cells
		SelfAwareMap<Cell> cells = generateGrid(8, 8, (c, x, y) -> {
			// Pieces
			Team team = y < 4 ? blackTeam : whiteTeam;
			if (y == 2 || y == 5) c.piece = Optional.of(new Pawn(c.standardForwardsDirection, team));
			if (y == 1 || y == 6) {
				Piece p = new Rook(team);
				if (x == 1) p = new Knight(team);
				if (x == 2) p = new Bishop(team);
				if (x == 3) p = new Queen(team);
				if (x == 4) p = new King(team);
				if (x == 5) p = new Bishop(team);
				if (x == 6) p = new Knight(team);
				c.piece = Optional.of(p);
			}
		});
		// Connections
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				String id = x + ", " + y;
				Cell cell = cells.get(id);
				if (x == 0) {
					String leftID = "7, " + y;
					Cell leftCell = cells.get(leftID);
					cell.connections.put("left", leftCell);
				}
				if (y == 0) {
					String upID = x + ", 7";
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
		// Finish
		Board b = new Board(8);
		b.teams = new Team[] { whiteTeam, blackTeam };
		b.cells.putAll(cells);
		return b;
	}
	public static Board generateHorizontallyLooping2Player8x8() {
		Team whiteTeam = new Team("White", 0, true);
		Team blackTeam = new Team("Black", 1, false);
		// Generate cells
		SelfAwareMap<Cell> cells = generateGrid(8, 8, (c, x, y) -> {
			// Pieces
			Team team = y < 4 ? blackTeam : whiteTeam;
			if (y == 1 || y == 6) c.piece = Optional.of(new Pawn(c.standardForwardsDirection, team));
			if (y == 0 || y == 7) {
				Piece p = new Rook(team);
				if (x == 1) p = new Knight(team);
				if (x == 2) p = new Bishop(team);
				if (x == 3) p = new Queen(team);
				if (x == 4) p = new King(team);
				if (x == 5) p = new Bishop(team);
				if (x == 6) p = new Knight(team);
				c.piece = Optional.of(p);
			}
		});
		// Connections
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				String id = x + ", " + y;
				Cell cell = cells.get(id);
				if (x == 0) {
					String leftID = "7, " + y;
					Cell leftCell = cells.get(leftID);
					cell.connections.put("left", leftCell);
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
		// Finish
		Board b = new Board(8);
		b.teams = new Team[] { whiteTeam, blackTeam };
		b.cells.putAll(cells);
		return b;
	}
}
