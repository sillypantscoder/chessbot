package com.sillypantscoder.chess.game;

public class Team {
	public String name;
	public int spritesheet_index;
	public boolean is_player;
	public Team(String name, int spritesheet_index, boolean is_player) {
		this.name = name;
		this.spritesheet_index = spritesheet_index;
		this.is_player = is_player;
	}
}
