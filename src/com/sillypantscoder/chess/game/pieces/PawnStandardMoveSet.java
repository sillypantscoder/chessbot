package com.sillypantscoder.chess.game.pieces;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import com.sillypantscoder.chess.game.Cell;
import com.sillypantscoder.chess.game.Move;
import com.sillypantscoder.chess.game.MoveSet;
import com.sillypantscoder.chess.game.Piece;

public class PawnStandardMoveSet extends MoveSet {
	public Pawn pawn;
	public String main_direction;
	public boolean main_direction_reversed;
	public PawnStandardMoveSet(Pawn pawn, String main_direction, boolean main_direction_reversed) {
		this.pawn = pawn;
		this.main_direction = main_direction;
		this.main_direction_reversed = main_direction_reversed;
	}
	public Set<Move> getMoves(Cell context) {
		Set<Move> moves = new HashSet<Move>();
		// Get the moving piece
		Piece movingPiece = context.piece.orElseThrow(() -> new NoSuchElementException("There needs to be a piece at the location defined by `context` (required in order to construct a Move object)"));
		// Go forward and check what is there
		Cell forwards = context.go(main_direction, main_direction_reversed);
		if (forwards == null) return moves; // (??? should have promoted)
		if (forwards.piece.isEmpty()) {
			// We can move directly forward.
			moves.add(new PawnForwardMove(context, movingPiece, forwards));
			// Or if this is our first move, we can move 2 spaces.
			if (this.pawn.moved == false) {
				Cell forwards2 = forwards.go(main_direction, main_direction_reversed);
				if (forwards2 != null) {
					moves.add(new PawnForwardMove(context, movingPiece, forwards2));
				}
			}
		}
		// Look diagonally
		Set<String> diagonalMoves = forwards.connections.keySet().stream().collect(Collectors.toSet());
		for (String dir : diagonalMoves) {
			if (dir == this.main_direction) continue;
			Cell diagonal = forwards.go(dir, false);
			diagonal.piece.ifPresent((v) -> {
				moves.add(new Move.CaptureMove(context, movingPiece, diagonal, v));
			});
		}
		Set<String> diagonalReversedMoves = forwards.reverseConnections.keySet().stream().collect(Collectors.toSet());
		for (String dir : diagonalReversedMoves) {
			if (dir == this.main_direction) continue;
			Cell diagonal = forwards.go(dir, true);
			diagonal.piece.ifPresent((v) -> {
				moves.add(new Move.CaptureMove(context, movingPiece, diagonal, v));
			});
		}
		// finish
		return moves;
	}
	public static class PawnForwardMove extends Move.JumpMove {
		public Pawn piece;
		public PawnForwardMove(Cell originalLoc, Piece piece, Cell targetLoc) {
			super(originalLoc, piece, targetLoc);
			this.piece = (Pawn)(piece);
		}
		public void execute() {
			super.execute();
			this.piece.moved = true;
		}
	}
}
