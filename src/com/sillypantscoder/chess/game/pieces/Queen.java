package com.sillypantscoder.chess.game.pieces;

import com.sillypantscoder.chess.game.Piece;
import com.sillypantscoder.chess.game.Team;

public class Queen extends Piece {
	public Queen(Team team) {
		super(team);
		this.moveSets.add(new OrthogonalSlidingMoveSet());
		this.moveSets.add(new DiagonalSlidingMoveSet());
	}
	public String getName() {
		return "Queen";
	}
	public int getIconPos() {
		return 1;
	}
	public int getMaterialValue() {
		return 9;
	}
}
