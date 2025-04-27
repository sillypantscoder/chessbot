package com.sillypantscoder.chess.game.pieces;

import com.sillypantscoder.chess.game.Direction;
import com.sillypantscoder.chess.game.Piece;

public class Pawn extends Piece {
	public boolean moved;
	public Pawn(Direction mainDirection) {
		super();
		this.moved = false;
		this.moveSets.add(new PawnStandardMoveSet(this, mainDirection));
	}
	public String getName() {
		return "Pawn";
	}
	public int[] getIconPos() {
		return new int[] { 5, 0 };
	}
}
