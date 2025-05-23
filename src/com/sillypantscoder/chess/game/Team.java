package com.sillypantscoder.chess.game;

import java.util.Optional;

import com.sillypantscoder.chess.bot.ChessBot;

public class Team {
	public String name;
	public int spritesheet_index;
	public InputMode inputMode;
	public Team(String name, int spritesheet_index) {
		this.name = name;
		this.spritesheet_index = spritesheet_index;
		this.inputMode = null;
	}
	public static abstract class InputMode {
		public Team team;
		public InputMode(Team team) {
			this.team = team;
		}
		public abstract Optional<Move> getMove(Board board);
	}
	public static class ThisPlayerMode extends InputMode {
		public ThisPlayerMode(Team team) {
			super(team);
		}
		public Optional<Move> getMove(Board board) { return Optional.empty(); }
	}
	public static class BotMode extends InputMode {
		public int level;
		public BotMode(Team team, int level) {
			super(team);
			this.level = level;
		}
		public Optional<Move> getMove(Board board) {
			return Optional.ofNullable(ChessBot.getBestMove(board, team, level - 1));
		}
	}
}
