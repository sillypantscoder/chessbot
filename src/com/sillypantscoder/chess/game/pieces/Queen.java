package com.sillypantscoder.chess.game.pieces;

import com.sillypantscoder.chess.game.Piece;

public class Queen extends Piece {
	public Queen() {
		super();
		this.moveSets.add(new OrthogonalSlidingMoveSet());
		this.moveSets.add(new DiagonalSlidingMoveSet());
	}
	public String getName() {
		return "Queen";
	}
	public int[] getIconPos() {
		return new int[] { 1, 0 };
	}
}
