package com.sillypantscoder.chess.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.sillypantscoder.chess.game.pieces.Bishop;
import com.sillypantscoder.chess.game.pieces.King;
import com.sillypantscoder.chess.game.pieces.Knight;
import com.sillypantscoder.chess.game.pieces.Pawn;
import com.sillypantscoder.chess.game.pieces.Queen;
import com.sillypantscoder.chess.game.pieces.Rook;
import com.sillypantscoder.utils.SelfAwareMap;

public class Board {
	public SelfAwareMap<Cell> cells;
	public Team[] teams;
	public Board() {
		this.cells = new SelfAwareMap<Cell>();
		this.teams = new Team[0];
	}
	public static Board generateStandard2Player8x8() {
		Team whiteTeam = new Team("White", 0);
		Team blackTeam = new Team("Black", 1);
		// Generate cells
		SelfAwareMap<Cell> cells = new SelfAwareMap<Cell>();
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				Cell c = new Cell(x + ", " + y, new Direction("up", y < 4));
				cells.put(c);
				// Pieces
				Team team = y < 4 ? blackTeam : whiteTeam;
				if (y == 1 || y == 6) c.piece = Optional.of(new Pawn(c.standardForwardsDirection, team));
				if (y == 0 || y == 7) {
					Piece p = new Rook(team);
					if (x == 1) p = new Knight(team);
					if (x == 2) p = new Bishop(team);
					if (x == 3) p = new Queen(team);
					if (x == 4) p = new King(team);
					if (x == 5) p = new Bishop(team);
					if (x == 6) p = new Knight(team);
					c.piece = Optional.of(p);
				}
			}
		}
		// Connections
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				String id = x + ", " + y;
				Cell cell = cells.get(id);
				if (x > 0) {
					String leftID = (x - 1) + ", " + y;
					Cell leftCell = cells.get(leftID);
					cell.connections.put("left", leftCell);
				}
				if (y > 0) {
					String upID = x + ", " + (y - 1);
					Cell upCell = cells.get(upID);
					cell.connections.put("up", upCell);
				}
			}
		}
		// Reverse connections
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				String id = x + ", " + y;
				Cell cell = cells.get(id);
				cell.updateReverseConnections(cells.values());
			}
		}
		// Finish
		Board b = new Board();
		b.teams = new Team[] { whiteTeam, blackTeam };
		b.cells.putAll(cells);
		return b;
	}
	public static Board generate4Player8x8() {
		Team whiteTeam = new Team("White", 0);
		Team aTeam = new Team("A", 1);
		Team bTeam = new Team("B", 1);
		// Generate cells
		SelfAwareMap<Cell> cells = new SelfAwareMap<Cell>();
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				Cell c = new Cell(x + ", " + y, new Direction("up", y < 4));
				cells.put(c);
				// Pieces
				Team team = y < 4 ? (x < 4 ? aTeam : bTeam) : whiteTeam;
				if (y == 1 || y == 6) c.piece = Optional.of(new Pawn(c.standardForwardsDirection, team));
				if (y == 0 || y == 7) {
					Piece p = new Rook(team);
					if (x == 1) p = new Knight(team);
					if (x == 2) p = new Bishop(team);
					if (x == 3) p = new Queen(team);
					if (x == 4) p = new King(team);
					if (x == 5) p = new Bishop(team);
					if (x == 6) p = new Knight(team);
					c.piece = Optional.of(p);
				}
			}
		}
		// Connections
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				String id = x + ", " + y;
				Cell cell = cells.get(id);
				if (x > 0) {
					String leftID = (x - 1) + ", " + y;
					Cell leftCell = cells.get(leftID);
					cell.connections.put("left", leftCell);
				}
				if (y > 0) {
					String upID = x + ", " + (y - 1);
					Cell upCell = cells.get(upID);
					cell.connections.put("up", upCell);
				}
			}
		}
		// Reverse connections
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				String id = x + ", " + y;
				Cell cell = cells.get(id);
				cell.updateReverseConnections(cells.values());
			}
		}
		// Finish
		Board b = new Board();
		b.teams = new Team[] { whiteTeam, aTeam, bTeam };
		b.cells.putAll(cells);
		return b;
	}
	public static Board generateBillionPlayer8x8() {
		Team whiteTeam = new Team("White", 0);
		ArrayList<Team> additionalTeams = new ArrayList<Team>();
		additionalTeams.add(whiteTeam);
		// Generate cells
		SelfAwareMap<Cell> cells = new SelfAwareMap<Cell>();
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				Cell c = new Cell(x + ", " + y, new Direction("up", y < 4));
				cells.put(c);
				// Pieces
				Team team = y < 4 ? null : whiteTeam;
				if (team == null) {
					team = new Team(c.name, 1);
					additionalTeams.add(team);
				}
				if (y == 1 || y == 6) c.piece = Optional.of(new Pawn(c.standardForwardsDirection, team));
				if (y == 0 || y == 7) {
					Piece p = new Rook(team);
					if (x == 1) p = new Knight(team);
					if (x == 2) p = new Bishop(team);
					if (x == 3) p = new Queen(team);
					if (x == 4) p = new King(team);
					if (x == 5) p = new Bishop(team);
					if (x == 6) p = new Knight(team);
					c.piece = Optional.of(p);
				}
			}
		}
		// Connections
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				String id = x + ", " + y;
				Cell cell = cells.get(id);
				if (x > 0) {
					String leftID = (x - 1) + ", " + y;
					Cell leftCell = cells.get(leftID);
					cell.connections.put("left", leftCell);
				}
				if (y > 0) {
					String upID = x + ", " + (y - 1);
					Cell upCell = cells.get(upID);
					cell.connections.put("up", upCell);
				}
			}
		}
		// Reverse connections
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				String id = x + ", " + y;
				Cell cell = cells.get(id);
				cell.updateReverseConnections(cells.values());
			}
		}
		// Finish
		Board b = new Board();
		b.teams = additionalTeams.toArray(new Team[] { whiteTeam });
		b.cells.putAll(cells);
		return b;
	}
	public Set<Move> getAllMoves(Team team) {
		HashSet<Move> moves = new HashSet<Move>();
		for (Cell c : this.cells.values()) {
			c.piece.ifPresent((v) -> {
				if (v.team == team) {
					moves.addAll(v.getAllMoves(c));
				}
			});
		}
		return moves;
	}
}
