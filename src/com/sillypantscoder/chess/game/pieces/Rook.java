package com.sillypantscoder.chess.game.pieces;

import com.sillypantscoder.chess.game.Piece;
import com.sillypantscoder.chess.game.Team;

public class Rook extends Piece {
	public Rook(Team team) {
		super(team);
		this.moveSets.add(new OrthogonalSlidingMoveSet());
	}
	public String getName() {
		return "Rook";
	}
	public int getIconPos() {
		return 4;
	}
	public int getMaterialValue() {
		return 5;
	}
	public Rook duplicate() { return new Rook(team); }
}
