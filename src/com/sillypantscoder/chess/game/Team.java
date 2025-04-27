package com.sillypantscoder.chess.game;

public class Team {
	public String name;
	public int spritesheet_index;
	public Team(String name, int spritesheet_index) {
		this.name = name;
		this.spritesheet_index = spritesheet_index;
	}
	// TODO: make it so pieces on the same team can't capture each other
	// TODO: Finish the Chess_Pieces_Sprite3.svg file
}
