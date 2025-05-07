package com.sillypantscoder.chess.game.pieces;

import com.sillypantscoder.chess.game.Team;

public class King extends RoyalPiece {
	public King(Team team) {
		super(team);
		this.moveSets.add(new OrthogonalSlidingMoveSet(1));
		this.moveSets.add(new DiagonalSlidingMoveSet(1));
	}
	public String getName() {
		return "King";
	}
	public int getIconPos() {
		return 0;
	}
	public int getMaterialValue() {
		return 4;
	}
	public King duplicate() { return new King(team); }
}
