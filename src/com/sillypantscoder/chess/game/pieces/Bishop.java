package com.sillypantscoder.chess.game.pieces;

import com.sillypantscoder.chess.game.Piece;
import com.sillypantscoder.chess.game.Team;

public class Bishop extends Piece {
	public Bishop(Team team) {
		super(team);
		this.moveSets.add(new DiagonalSlidingMoveSet());
	}
	public String getName() {
		return "Bishop";
	}
	public int getIconPos() {
		return 2;
	}
}
