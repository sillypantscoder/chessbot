package com.sillypantscoder.chess;

import java.awt.Color;
import java.util.function.Function;

import com.sillypantscoder.chess.game.Board;
import com.sillypantscoder.chess.game.Team;
import com.sillypantscoder.chess.game.Team.InputMode;
import com.sillypantscoder.windowlib.Surface;
import com.sillypantscoder.windowlib.Window;

public class GameSetupWindow extends Window {
	public Board board;
	public String name;
	public int teamindex = 0;
	public int lastHeight = 1;
	public int lastWidth = 1;
	public GameSetupWindow(Board board, String name) {
		this.board = board;
		this.name = name;
	}
	public void open() {
		this.open("Chess - Game Setup", 500, 600);
	}
	public Surface getIcon() {
		Surface s = new Surface(16, 16, new Color(0, 0, 0, 0));
		s.drawRect(Color.BLACK, 4, 4, 8, 8);
		return s;
	}
	public Surface frame(int width, int height) {
		this.lastHeight = height;
		this.lastWidth = width;
		Surface s = new Surface(width, height, Color.WHITE);
		// Draw title
		Surface title = Surface.renderText(25, "Chess - Game Setup", Color.BLACK);
		s.blit(title, 15, 0);
		int y = title.get_height() + 5;
		// Draw subtitle
		Surface subtitle = Surface.renderText(25, name, Color.GRAY);
		s.blit(subtitle, 15, y);
		y += subtitle.get_height() + 10;
		// Draw divider
		s.drawRect(Color.BLACK, 0, y, width, 3);
		y += 3;
		// Draw selected team name
		Team selectedTeam = this.board.teams[this.teamindex];
		Surface team_name = Surface.renderText(20, "Team: " + selectedTeam.name, Color.BLACK);
		s.blit(team_name, 15, y);
		y += team_name.get_height() + 5;
		// Draw input mode options
		for (int i = 0; i < GameSetupWindow.options.length; i++) {
			InputModeOption option = GameSetupWindow.options[i];
			// Draw text
			Surface renderedName = Surface.renderText(20, option.name, Color.BLACK);
			s.blit(renderedName, 0, y);
			y += renderedName.get_height();
			// Selected?
			boolean thisOptionSelected = option.checker.apply(selectedTeam.inputMode);
			if (thisOptionSelected) {
				s.invertArea(0, y - renderedName.get_height(), width, renderedName.get_height());
			}
		}
		// Draw back button
		Surface btnLeftText = Surface.renderText(25, teamindex == 0 ? "Cancel" : "Back", Color.WHITE);
		Surface btnLeft = new Surface(width / 2, btnLeftText.get_height() * 2, Color.BLACK);
		btnLeft.blit(btnLeftText, (btnLeft.get_width() - btnLeftText.get_width()) / 2, btnLeftText.get_height() / 2);
		s.blit(btnLeft, 0, height - btnLeft.get_height());
		// Draw forward button
		if (selectedTeam.inputMode != null) {
			Surface btnRightText = Surface.renderText(25, (teamindex == board.teams.length - 1) ? "Finish" : "Next", Color.WHITE);
			Surface btnRight = new Surface(width / 2, btnRightText.get_height() * 2, Color.BLACK);
			btnRight.blit(btnRightText, (btnRight.get_width() - btnRightText.get_width()) / 2, btnRightText.get_height() / 2);
			s.blit(btnRight, width / 2, height - btnRight.get_height());
		}
		// Draw button divider
		s.drawLine(Color.WHITE, width / 2, height - btnLeft.get_height(), width / 2, height, 5);
		// finish
		return s;
	}
	public void keyDown(String e) {}
	public void keyUp(String e) {}
	public void mouseMoved(int x, int y) {}
	public void mouseDown(int x, int y) {
		// Check for buttons
		Surface btnLeftText = Surface.renderText(25, teamindex == 0 ? "Cancel" : "Back", Color.WHITE);
		if (y >= lastHeight - (btnLeftText.get_height() * 2)) {
			// Must have hit one of the buttons
			if (x < lastWidth / 2) {
				// Left button
				if (this.teamindex == 0) {
					this.close();
				} else {
					this.teamindex -= 1;
				}
			} else {
				// Right button
				if (this.teamindex == this.board.teams.length - 1) {
					this.continueToGame();
				} else if (this.board.teams[this.teamindex].inputMode != null) {
					this.teamindex += 1;
				}
			}
			return;
		}
		// Since it's not one of the buttons, go through the normal screen structure, starting from the top
		// Draw title
		Surface title = Surface.renderText(25, "Chess - Game Setup", Color.BLACK);
		y -= title.get_height() + 5;
		// Draw subtitle
		Surface subtitle = Surface.renderText(25, name, Color.GRAY);
		y -= subtitle.get_height() + 10;
		// Draw divider
		y -= 3;
		// Draw selected team name
		Team selectedTeam = this.board.teams[this.teamindex];
		Surface team_name = Surface.renderText(20, "Team: " + selectedTeam.name, Color.BLACK);
		y -= team_name.get_height() + 5;
		// if we have already passed the mouse pos then we didn't click on anything
		if (y < 0) return;
		// Draw input mode options
		for (int i = 0; i < GameSetupWindow.options.length; i++) {
			InputModeOption option = GameSetupWindow.options[i];
			// Draw text
			Surface renderedName = Surface.renderText(20, option.name, Color.BLACK);
			y -= renderedName.get_height();
			// Check for click
			if (y < 0) {
				// === Set the input mode! ===
				selectedTeam.inputMode = option.supplier.apply(selectedTeam);
				return;
			}
		}
	}
	public void mouseUp(int mouseX, int mouseY) {}
	public void mouseWheel(int amount) {}
	public void continueToGame() {
		ChessWindow win = new ChessWindow(board, name);
		win.open();
		this.close();
	}
	// Input Mode Options
	public static InputModeOption[] options = new InputModeOption[] {
		// This Player Mode
		new InputModeOption("Player", (team) -> new Team.ThisPlayerMode(team), (im) -> im instanceof Team.ThisPlayerMode),
		// Bot Mode (level 1)
		new InputModeOption("Bot (level 1)", (team) -> new Team.BotMode(team, 1), (im) -> (im instanceof Team.BotMode botMode && botMode.level == 1)),
		// Bot Mode (level 2)
		new InputModeOption("Bot (level 2)", (team) -> new Team.BotMode(team, 2), (im) -> (im instanceof Team.BotMode botMode && botMode.level == 2)),
		// Bot Mode (level 3)
		new InputModeOption("Bot (level 3) (very slow!)", (team) -> new Team.BotMode(team, 3), (im) -> (im instanceof Team.BotMode botMode && botMode.level == 3))
	};
	public static record InputModeOption(String name, Function<Team, InputMode> supplier, Function<InputMode, Boolean> checker) {}
}
