package com.sillypantscoder.chess.game.pieces;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import com.sillypantscoder.chess.game.Cell;
import com.sillypantscoder.chess.game.Move;
import com.sillypantscoder.chess.game.MoveSet;
import com.sillypantscoder.chess.game.Piece;

public class DiagonalSlidingMoveSet extends MoveSet {
	public int max_length; // -1 for unlimited
	public DiagonalSlidingMoveSet(int max_length) {
		this.max_length = max_length;
	}
	public DiagonalSlidingMoveSet() {
		this.max_length = -1;
	}
	public Set<Move> getMoves(Piece movingPiece, Cell context, String direction1, boolean reversed1, String direction2, boolean reversed2) {
		Set<Move> moves = new HashSet<Move>();
		// Go until we reach a piece or the maximum allowed length
		Cell currentCell = context;
		int max_length = this.max_length == -1 ? 1000 : this.max_length;
		for (int i = 1; i <= max_length; i++) {
			currentCell = currentCell.go_diagonal(direction1, reversed1, direction2, reversed2);
			if (currentCell == null) break;
			// If we hit a piece, allow capture and exit
			if (currentCell.piece.isPresent()) {
				Piece p = currentCell.piece.get();
				Move capture = new Move.CaptureMove(context, movingPiece, currentCell, p);
				moves.add(capture);
				break;
			}
			// Otherwise, regular move here.
			Move move = new Move.JumpMove(context, movingPiece, currentCell);
			moves.add(move);
		}
		// finish
		return moves;
	}
	public Set<Move> getMoves(Cell context) {
		Set<Move> moves = new HashSet<Move>();
		// Get the moving piece
		Piece movingPiece = context.piece.orElseThrow(() -> new NoSuchElementException("There needs to be a piece at the location defined by `context` (required in order to construct a Move object)"));
		// Find all allowed directions
		Set<String> forwardDirections = context.connections.keySet();
		for (String dir : forwardDirections) {
			Set<String> secondaryForwardDirections = context.go(dir, false).connections.keySet();
			for (String dir2 : secondaryForwardDirections) {
				if (dir == dir2) continue;
				moves.addAll(getMoves(movingPiece, context, dir, false, dir2, false));
			}
			Set<String> secondaryBackwardDirections = context.go(dir, false).reverseConnections.keySet();
			for (String dir2 : secondaryBackwardDirections) {
				if (dir == dir2) continue;
				moves.addAll(getMoves(movingPiece, context, dir, false, dir2, true));
			}
		}
		Set<String> backwardDirections = context.reverseConnections.keySet();
		for (String dir : backwardDirections) {
			Set<String> secondaryForwardDirections = context.go(dir, true).connections.keySet();
			for (String dir2 : secondaryForwardDirections) {
				if (dir == dir2) continue;
				moves.addAll(getMoves(movingPiece, context, dir, true, dir2, false));
			}
			Set<String> secondaryBackwardDirections = context.go(dir, true).reverseConnections.keySet();
			for (String dir2 : secondaryBackwardDirections) {
				if (dir == dir2) continue;
				moves.addAll(getMoves(movingPiece, context, dir, true, dir2, true));
			}
		}
		// finish
		return moves;
	}
}
