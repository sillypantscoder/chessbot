package com.sillypantscoder.chess.game.pieces;

import com.sillypantscoder.chess.game.Piece;

public class Knight extends Piece {
	public Knight() {
		super();
		this.moveSets.add(new LeapingMoveSet(2, 1));
	}
	public String getName() {
		return "Knight";
	}
	public int[] getIconPos() {
		return new int[] { 3, 0 };
	}
}
