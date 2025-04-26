package com.sillypantscoder.chess.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.sillypantscoder.chess.Spritesheet;
import com.sillypantscoder.windowlib.Surface;

public abstract class Piece {
	public ArrayList<MoveSet> moveSets;
	public Piece() {
		this.moveSets = new ArrayList<MoveSet>();
	}
	public abstract String getName();
	public abstract int[] getIconPos();
	public String toString() {
		return "Piece { " + this.getName() + " }";
	}
	public Surface getIcon(Spritesheet sheet) {
		int[] pos = getIconPos();
		return sheet.getImage(pos[0], pos[1]);
	}
	public Set<Move> getAllMoves(Cell context) {
		Set<Move> moves = new HashSet<Move>();
		for (MoveSet set : this.moveSets) {
			moves.addAll(set.getMoves(context));
		}
		return moves;
	}
}
