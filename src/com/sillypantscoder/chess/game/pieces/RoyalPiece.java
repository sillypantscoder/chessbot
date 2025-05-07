package com.sillypantscoder.chess.game.pieces;

import com.sillypantscoder.chess.game.Piece;
import com.sillypantscoder.chess.game.Team;

public abstract class RoyalPiece extends Piece {
	public RoyalPiece(Team team) {
		super(team);
	}
	public int getPieceValue() {
		return getMaterialValue() + 242;
	}
}
