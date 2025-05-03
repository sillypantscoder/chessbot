package com.sillypantscoder.chess.bot;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.sillypantscoder.chess.game.Board;
import com.sillypantscoder.chess.game.Cell;
import com.sillypantscoder.chess.game.Move;
import com.sillypantscoder.chess.game.Team;
import com.sillypantscoder.chess.game.Move.CaptureMove;

public class ChessBot {
	public static Move getBestMove(Board b, Team team) {
		Set<Move> allMoves = b.getAllMoves(team);
		Move bestMove = null;
		double bestMoveScore = Integer.MIN_VALUE;
		for (Move m : allMoves) {
			double score = getMoveScore(b, team, m);
			if (score > bestMoveScore) {
				bestMove = m;
				bestMoveScore = score;
			}
		}
		return bestMove;
	}
	public static double getMoveScore(Board before, Team team, Move _m) {
		DuplicatedBoard after = new DuplicatedBoard(before);
		Move m = _m.duplicate(after);
		m.execute();
		double score = getThreatScore(after.board, team) - getThreatScore(before, team);
		if (m instanceof Move.CaptureMove capture) {
			score += capture.capturedPiece.getMaterialValue() * 1.75;
		}
		return score;
	}
	public static double getThreatScore(Board b, Team team) {
		AtomicInteger goodValue = new AtomicInteger(0); // Get pieces we are threatening
		AtomicInteger badValue = new AtomicInteger(0); // And pieces that are threatened
		for (Cell c : b.cells.items) {
			c.piece.ifPresent((v) -> {
				if (v.team == team) {
					// find pieces we are threatening
					Set<Move> moves = v.getAllMoves(c);
					for (Move m : moves) {
						if (m instanceof CaptureMove capture) {
							goodValue.addAndGet(capture.capturedPiece.getMaterialValue());
						}
					}
				} else {
					// find pieces that are threatened
					Set<Move> moves = v.getAllMoves(c);
					for (Move m : moves) {
						if (m instanceof CaptureMove capture) {
							if (capture.capturedPiece.team == team) {
								badValue.addAndGet(capture.capturedPiece.getMaterialValue());
							}
						}
					}
				}
			});
		}
		// Add up the results
		return (0.75 * goodValue.get()) - badValue.get();
	}
}
