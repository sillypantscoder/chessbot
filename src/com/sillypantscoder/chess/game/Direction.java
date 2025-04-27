package com.sillypantscoder.chess.game;

public record Direction(String name, boolean reversed) {
	public Direction reverseDirection() {
		return new Direction(name, !reversed);
	}
}
