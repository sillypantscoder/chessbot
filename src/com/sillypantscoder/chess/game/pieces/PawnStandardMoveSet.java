package com.sillypantscoder.chess.game.pieces;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.sillypantscoder.chess.bot.DuplicatedBoard;
import com.sillypantscoder.chess.game.Cell;
import com.sillypantscoder.chess.game.Direction;
import com.sillypantscoder.chess.game.Move;
import com.sillypantscoder.chess.game.MoveSet;
import com.sillypantscoder.chess.game.Piece;

public class PawnStandardMoveSet extends MoveSet {
	public Pawn pawn;
	public PawnStandardMoveSet(Pawn pawn) {
		this.pawn = pawn;
	}
	public Set<Move> getMoves(Cell context) {
		Set<Move> moves = new HashSet<Move>();
		// Get the moving piece
		Piece movingPiece = context.piece.orElseThrow(() -> new NoSuchElementException("There needs to be a piece at the location defined by `context` (required in order to construct a Move object)"));
		// Go forward and check what is there
		Cell forwards = context.go(pawn.mainDirection);
		if (forwards == null) return moves; // (should have promoted before this happens???)
		if (forwards.piece.isEmpty()) {
			// We can move directly forward.
			moves.add(new PawnForwardMove(context, movingPiece, forwards));
			// Or if this is our first move, we can move 2 spaces.
			if (this.pawn.moved == false) {
				Cell forwards2 = forwards.go(pawn.mainDirection);
				if (forwards2 != null && forwards2.piece.isEmpty()) {
					moves.add(new PawnForwardMove(context, movingPiece, forwards2));
				}
			}
		}
		// Look diagonally
		Set<Direction> diagonalMoves = forwards.getDirections().stream().collect(Collectors.toSet());
		for (Direction dir : diagonalMoves) {
			if (dir.equals(pawn.mainDirection) || dir.equals(pawn.mainDirection.reverseDirection())) continue;
			Cell diagonal = forwards.go(dir);
			diagonal.piece.ifPresent((v) -> {
				if (v.team != movingPiece.team) {
					moves.add(new PawnDiagonalMove(context, movingPiece, diagonal, v));
				}
			});
		}
		// finish
		return moves;
	}
	public static class PawnForwardMove extends Move.JumpMove {
		public Pawn piece;
		public PawnForwardMove(Cell originalLoc, Piece piece, Cell targetLoc) {
			super(originalLoc, piece, targetLoc);
			this.piece = (Pawn)(piece); // dangerous but probably ok
		}
		public void execute() {
			super.execute();
			this.piece.moved = true;
			// Check for promotion
			if (targetLoc.go(piece.mainDirection) == null) {
				targetLoc.piece = Optional.of(new Queen(piece.team));
			}
		}
		public PawnForwardMove duplicate(DuplicatedBoard board) {
			return new PawnForwardMove(board.getCopy(originalLoc), board.getCopy(piece), board.getCopy(targetLoc));
		}
	}
	public static class PawnDiagonalMove extends Move.CaptureMove {
		public Pawn piece;
		public PawnDiagonalMove(Cell originalLoc, Piece piece, Cell targetLoc, Piece capturedPiece) {
			super(originalLoc, piece, targetLoc, capturedPiece);
			this.piece = (Pawn)(piece); // dangerous but probably ok
		}
		public void execute() {
			super.execute();
			this.piece.moved = true;
			// Check for promotion
			if (targetLoc.go(piece.mainDirection) == null) {
				targetLoc.piece = Optional.of(new Queen(piece.team));
			}
		}
		public PawnDiagonalMove duplicate(DuplicatedBoard board) {
			return new PawnDiagonalMove(board.getCopy(originalLoc), board.getCopy(piece), board.getCopy(targetLoc), board.getCopy(capturedPiece));
		}
	}
}
