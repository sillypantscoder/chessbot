package com.sillypantscoder.chess.bot;

import java.util.Optional;

import com.sillypantscoder.chess.game.Board;
import com.sillypantscoder.chess.game.Cell;
import com.sillypantscoder.chess.game.Piece;
import com.sillypantscoder.utils.Utils;

public class DuplicatedBoard {
	public Board original;
	public Board board;
	public DuplicatedBoard(Board original) {
		this.original = original;
		this.board = new Board();
		this.board.teams = original.teams;
		for (Cell c : original.cells.items) {
			Cell d = new Cell(c.name, c.standardForwardsDirection);
			board.cells.put(d);
			c.piece.ifPresent((v) -> {
				d.piece = Optional.of(v);
			});
		}
		// Update connections
		for (String name : board.cells.keySet()) {
			Cell originalCell = original.cells.get(name);
			Cell newCell = board.cells.get(name);
			for (String dir : originalCell.connections.keySet()) {
				Cell originalConnectedCell = originalCell.connections.get(dir);
				Cell newConnectedCell = this.board.cells.get(originalConnectedCell.name);
				newCell.connections.put(dir, newConnectedCell);
			}
			for (String dir : originalCell.reverseConnections.keySet()) {
				Cell originalConnectedCell = originalCell.reverseConnections.get(dir);
				Cell newConnectedCell = this.board.cells.get(originalConnectedCell.name);
				newCell.reverseConnections.put(dir, newConnectedCell);
			}
		}
	}
	public Cell getCopy(Cell original) {
		return this.board.cells.get(original.name);
	}
	public Piece getCopy(Piece original) {
		for (Cell c : this.original.cells.items) {
			if (Utils.bothSameValue(original, c.piece)) {
				Cell newCell = getCopy(c);
				return newCell.piece.orElse(null);
			}
		}
		return null;
	}
}
