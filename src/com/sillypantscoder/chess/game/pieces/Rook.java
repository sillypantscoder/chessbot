package com.sillypantscoder.chess.game.pieces;

import java.util.Optional;
import java.util.Set;

import com.sillypantscoder.chess.game.Board;
import com.sillypantscoder.chess.game.Cell;
import com.sillypantscoder.chess.game.Move;
import com.sillypantscoder.chess.game.Piece;

public class Rook extends Piece {
	public Rook() {
		super();
		this.moveSets.add(new OrthogonalSlidingMoveSet());
	}
	public String getName() {
		return "Rook";
	}
	public int[] getIconPos() {
		return new int[] { 4, 0 };
	}
	// Test!
	public static void main(String[] args) {
		// Create board
		Board b = new Board();
		// Add a rook
		Rook rook = new Rook();
		Cell rookLoc = b.cells.get("1, 1");
		rookLoc.piece = Optional.of(rook);
		// Find allowed moves
		Set<Move> moves = rook.moveSets.get(0).getMoves(rookLoc);
		for (Move m : moves) {
			System.out.println(m);
		}
	}
}
