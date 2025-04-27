package com.sillypantscoder.chess;

import java.awt.Color;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import com.sillypantscoder.chess.bot.ChessBot;
import com.sillypantscoder.chess.game.Board;
import com.sillypantscoder.chess.game.Cell;
import com.sillypantscoder.chess.game.Move;
import com.sillypantscoder.chess.game.Piece;
import com.sillypantscoder.chess.game.Team;
import com.sillypantscoder.utils.Utils;
import com.sillypantscoder.windowlib.Surface;
import com.sillypantscoder.windowlib.Window;

public class ChessWindow extends Window {
	public Board board;
	public Spritesheet spritesheet;
	public Optional<PieceSelection> selectedPiece;
	public double tileSize;
	public int currentTurn;
	public int animationTime;
	public ChessWindow() {
		this.board = Board.generateStandard2Player8x8();
		this.spritesheet = new Spritesheet("pieces");
		this.selectedPiece = Optional.empty();
		this.currentTurn = 0;
		this.animationTime = 0;
	}
	public void open() {
		this.open("Chess", 600, 600);
	}
	public Surface getIcon() {
		return this.spritesheet.getImage(5, 0);
	}
	public Surface frame(int width, int height) {
		Surface s = new Surface(width, height, new Color(255-64, 255-64, 255-64));
		// draw pieces
		tileSize = Math.min(width, height) / 8d;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Cell c = this.board.cells.get(x + ", " + y);
				if (c == null) continue;
				final int drawX = (int)(x * tileSize);
				final int drawY = (int)(y * tileSize);
				final int intTileSize = (int)(Math.ceil(tileSize));
				// draw tile
				Color tileColor = new Color(255-32, 255-32, 255-32);
				if ((x + y) % 2 == 0) tileColor = Color.WHITE;
				if (c.highlighted) tileColor = Color.YELLOW;
				s.drawRect(tileColor, drawX, drawY, intTileSize, intTileSize);
				// selected piece?
				selectedPiece.ifPresent((p) -> {
					if (Utils.bothSameValue(p.piece, c.piece)) {
						// Highlight the selected piece
						s.drawRect(new Color(128, 128, 255), drawX, drawY, intTileSize, intTileSize);
					}
				});
				// check for piece
				c.piece.ifPresent((v) -> {
					// Selected piece?
					selectedPiece.ifPresent((p) -> {
						if (p.piece == v) {
							s.drawRect(new Color(128, 128, 255), drawX, drawY, intTileSize, intTileSize);
						}
					});
					// Draw icon
					Surface icon = v.getIcon(spritesheet);
					icon = icon.resize(intTileSize, intTileSize);
					s.blit(icon, drawX, drawY);
				});
			}
		}
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Cell c = this.board.cells.get(x + ", " + y);
				if (c == null) continue;
				final int drawX = (int)(x * tileSize);
				final int drawY = (int)(y * tileSize);
				// selected piece?
				selectedPiece.ifPresent((p) -> {
					Move m = p.moves.stream().filter((v) -> v.targetLoc == c).findFirst().orElse(null);
					if (m != null) {
						// Highlight possible moves
						s.drawCircle(m.getColor(), drawX + (tileSize / 2), drawY + (tileSize / 2), tileSize / 5);
					}
				});
			}
		}
		// animation
		if (animationTime > 0) {
			animationTime -= 1;
			if (animationTime == 0) {
				// continue animation
				selectedPiece.ifPresentOrElse((v) -> {
					if (v.moves.size() == 0) {
						selectedPiece = Optional.empty();
					}
				}, () -> {
					if (this.board.teams[currentTurn].is_player == false) {
						Team t = this.board.teams[currentTurn];
						makeBotMove(t);
						this.currentTurn += 1;
						animationTime = 10;
					}
					if (currentTurn >= this.board.teams.length) {
						this.currentTurn = 0;
						purgeTeamList();
					}
				});
			}
		}
		// yay
		return s;
	}
	public void purgeTeamList() {
		for (int i = 0; i < board.teams.length; i++) {
			if (this.board.teamHasAnyPieces(board.teams[i])) continue;
			// Remove this team from the list
			Team[] newTeams = new Team[board.teams.length - 1];
			System.arraycopy(board.teams, 0, newTeams, 0, i);
			System.arraycopy(board.teams, i + 1, newTeams, i, board.teams.length - (i + 1));
			board.teams = newTeams;
			// Move to the next team
			i -= 1;
		}
	}
	public void keyDown(String e) {
	}
	public void keyUp(String e) {
	}
	public void mouseMoved(int x, int y) {
	}
	public void mouseDown(int x, int y) {
		int cellX = (int)(Math.floor(x / tileSize));
		int cellY = (int)(Math.floor(y / tileSize));
		// Find cell at this location
		Cell c = this.board.cells.get(cellX + ", " + cellY);
		if (c == null) return;
		// If we have already selected a piece...
		PieceSelection selection = selectedPiece.orElse(null);
		if (selection != null) {
			// AND this is a valid move location...
			Move m = selection.moves.stream().filter((v) -> v.targetLoc == c).findFirst().orElse(null);
			if (m != null) {
				clearHighlights();
				m.execute();
				selectedPiece = Optional.empty();
				currentTurn += 1;
				animationTime = 10;
				// don't re-select the piece
				return;
			}
			// Or maybe we clicked on the selected piece.
			if (Utils.bothSameValue(selection.piece, c.piece)) {
				selectedPiece = Optional.empty();
				return;
			}
		}
		// Get piece at this location
		Piece p = c.piece.orElse(null);
		if (p == null) { selectedPiece = Optional.empty(); return; }
		// Select the piece (if it is ours)
		if (p.team == this.board.teams[this.currentTurn] && p.team.is_player) {
			selectedPiece = Optional.of(new PieceSelection(c));
			animationTime = 10;
		}
	}
	public void mouseUp(int x, int y) {
	}
	public void mouseWheel(int amount) {
	}
	public void makeBotMove(Team team) {
		clearHighlights();
		Move bestMove = ChessBot.getBestMove(board, team);
		if (bestMove == null) return;
		bestMove.execute();
	}
	public void clearHighlights() {
		for (Cell c : this.board.cells.values()) {
			c.highlighted = false;
		}
	}
	public static class PieceSelection {
		public Piece piece;
		public Set<Move> moves;
		public PieceSelection(Cell cell) {
			this.piece = cell.piece.orElseThrow(() -> new NoSuchElementException("A PieceSelection object must be created with a cell containing a piece"));
			this.moves = this.piece.getAllMoves(cell);
		}
	}
}
