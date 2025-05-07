package com.sillypantscoder.chess.game;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sillypantscoder.utils.SelfAwareMap;

public class Board {
	public SelfAwareMap<Cell> cells;
	public Team[] teams;
	public int stdBoardSize;
	public Board(int stdBoardSize) {
		this.stdBoardSize = stdBoardSize;
		this.cells = new SelfAwareMap<Cell>();
		this.teams = new Team[0];
	}
	public Set<Move> getAllMoves(Team team) {
		HashSet<Move> moves = new HashSet<Move>();
		for (Cell c : this.cells.values()) {
			c.piece.ifPresent((v) -> {
				if (v.team == team) {
					moves.addAll(v.getAllMoves(c));
				}
			});
		}
		return moves;
	}
	public boolean teamHasAnyPieces(Team team) {
		AtomicBoolean teamHasAnyPieces = new AtomicBoolean(false);
		for (Cell c : this.cells.values()) {
			c.piece.ifPresent((v) -> {
				if (v.team == team) {
					teamHasAnyPieces.set(true);
				}
			});
		}
		return teamHasAnyPieces.get();
	}
	public void removeCell(Cell c) {
		this.cells.remove(c.name);
		// remove connections
		for (String dir : c.connections.keySet()) {
			Cell d = c.connections.get(dir);
			for (String dir2 : d.connections.keySet().stream().toList()) {
				if (d.connections.get(dir2).equals(c)) {
					d.connections.remove(dir2);
				}
			}
			for (String dir2 : d.reverseConnections.keySet().stream().toList()) {
				if (d.reverseConnections.get(dir2).equals(c)) {
					d.reverseConnections.remove(dir2);
				}
			}
		}
		for (String dir : c.reverseConnections.keySet()) {
			Cell d = c.reverseConnections.get(dir);
			for (String dir2 : d.connections.keySet().stream().toList()) {
				if (d.connections.get(dir2).equals(c)) {
					d.connections.remove(dir2);
				}
			}
			for (String dir2 : d.reverseConnections.keySet().stream().toList()) {
				if (d.reverseConnections.get(dir2).equals(c)) {
					d.reverseConnections.remove(dir2);
				}
			}
		}
	}
	public boolean isCellAttacked(Cell cell) {
		AtomicBoolean isAttacked = new AtomicBoolean(false);
		for (Cell checkCell : this.cells.items) {
			checkCell.piece.ifPresent((piece) -> {
				Set<Move> moves = piece.getAllMoves(checkCell);
				for (Move m : moves) {
					if (m.targetLoc == cell) isAttacked.set(true);
				}
			});
			if (isAttacked.get()) return true;
		}
		return false;
	}
}
