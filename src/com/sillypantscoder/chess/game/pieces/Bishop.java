package com.sillypantscoder.chess.game.pieces;

import com.sillypantscoder.chess.game.Piece;

public class Bishop extends Piece {
	public Bishop() {
		super();
		this.moveSets.add(new DiagonalSlidingMoveSet());
	}
	public String getName() {
		return "Bishop";
	}
	public int[] getIconPos() {
		return new int[] { 2, 0 };
	}
}
