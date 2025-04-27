package com.sillypantscoder.chess.game.pieces;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import com.sillypantscoder.chess.game.Cell;
import com.sillypantscoder.chess.game.Direction;
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
	public Set<Move> getMoves(Piece movingPiece, Cell context, Direction direction1, Direction direction2) {
		Set<Move> moves = new HashSet<Move>();
		// Go until we reach a piece or the maximum allowed length
		Cell currentCell = context;
		int max_length = this.max_length == -1 ? 1000 : this.max_length;
		for (int i = 1; i <= max_length; i++) {
			currentCell = currentCell.go_diagonal(direction1, direction2);
			if (currentCell == null) break;
			// If we hit a piece, allow capture and exit
			if (currentCell.piece.isPresent()) {
				Piece p = currentCell.piece.get();
				if (p.team != movingPiece.team) {
					Move capture = new Move.CaptureMove(context, movingPiece, currentCell, p);
					moves.add(capture);
				}
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
		Set<Direction> directions = context.getDirections();
		for (Direction dir : directions) {
			Set<Direction> secondaryForwardDirections = context.go(dir).getDirections();
			for (Direction dir2 : secondaryForwardDirections) {
				if (dir.equals(dir2) || dir.reverseDirection().equals(dir2)) continue;
				moves.addAll(getMoves(movingPiece, context, dir, dir2));
			}
		}
		// finish
		return moves;
	}
}
