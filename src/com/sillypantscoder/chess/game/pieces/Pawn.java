package com.sillypantscoder.chess.game.pieces;

import com.sillypantscoder.chess.game.Direction;
import com.sillypantscoder.chess.game.Piece;
import com.sillypantscoder.chess.game.Team;

public class Pawn extends Piece {
	public boolean moved;
	public Pawn(Direction mainDirection, Team team) {
		super(team);
		this.moved = false;
		this.moveSets.add(new PawnStandardMoveSet(this, mainDirection));
	}
	public String getName() {
		return "Pawn";
	}
	public int getIconPos() {
		return 5;
	}
	public int getMaterialValue() {
		return 1;
	}
}
