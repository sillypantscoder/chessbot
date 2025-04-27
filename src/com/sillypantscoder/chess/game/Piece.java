package com.sillypantscoder.chess.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.sillypantscoder.chess.Spritesheet;
import com.sillypantscoder.windowlib.Surface;

public abstract class Piece {
	public Team team;
	public ArrayList<MoveSet> moveSets;
	public Piece(Team team) {
		this.team = team;
		this.moveSets = new ArrayList<MoveSet>();
	}
	public abstract String getName();
	public abstract int getIconPos();
	public String toString() {
		return "Piece { " + this.getName() + ", team: " + team.name + " }";
	}
	public Surface getIcon(Spritesheet sheet) {
		return sheet.getImage(
			getIconPos(), this.team.spritesheet_index
		);
	}
	public Set<Move> getAllMoves(Cell context) {
		Set<Move> moves = new HashSet<Move>();
		for (MoveSet set : this.moveSets) {
			moves.addAll(set.getMoves(context));
		}
		return moves;
	}
	public abstract int getMaterialValue();
}
