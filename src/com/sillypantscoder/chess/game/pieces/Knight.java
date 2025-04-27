package com.sillypantscoder.chess.game.pieces;

import com.sillypantscoder.chess.game.Piece;
import com.sillypantscoder.chess.game.Team;

public class Knight extends Piece {
	public Knight(Team team) {
		super(team);
		this.moveSets.add(new LeapingMoveSet(2, 1));
	}
	public String getName() {
		return "Knight";
	}
	public int getIconPos() {
		return 3;
	}
	public int getMaterialValue() {
		return 3;
	}
}
