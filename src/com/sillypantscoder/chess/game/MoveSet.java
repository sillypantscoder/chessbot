package com.sillypantscoder.chess.game;

import java.util.Set;

public abstract class MoveSet {
	public abstract Set<Move> getMoves(Cell context);
}
