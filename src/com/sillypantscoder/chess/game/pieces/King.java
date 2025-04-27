package com.sillypantscoder.chess.game.pieces;

import com.sillypantscoder.chess.game.Piece;

public class King extends Piece {
	public King() {
		super();
		this.moveSets.add(new OrthogonalSlidingMoveSet(1));
		this.moveSets.add(new DiagonalSlidingMoveSet(1));
	}
	public String getName() {
		return "King";
	}
	public int[] getIconPos() {
		return new int[] { 0, 0 };
	}
}
