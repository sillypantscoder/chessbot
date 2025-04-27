package com.sillypantscoder.chess.game.pieces;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import com.sillypantscoder.chess.game.Cell;
import com.sillypantscoder.chess.game.Direction;
import com.sillypantscoder.chess.game.Move;
import com.sillypantscoder.chess.game.MoveSet;
import com.sillypantscoder.chess.game.Piece;

public class LeapingMoveSet extends MoveSet {
	public int offsetX;
	public int offsetY;
	public LeapingMoveSet(int offsetX, int offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
	public Set<Move> getMoves(Cell context) {
		// Move in every possible direction by `offsetX`
		ArrayList<CellLine> possibleMovements = new ArrayList<CellLine>();
		for (Direction dir : context.getDirections()) {
			possibleMovements.add(new CellLine(context.go(dir, this.offsetX), dir));
		}
		// Then move in every remaining direction by `offsetY`
		Set<Cell> possibleEndingPoints = new HashSet<Cell>();
		for (CellLine line : possibleMovements) {
			if (line.endCell == null) continue; // no idea how this happened
			// Find all the directions we can go in from here						(Copy the arrays so that removing keys is ok)
			Set<Direction> possibleDirections = line.endCell.getDirections()		.stream().collect(Collectors.toSet());
			// We can't go in the same direction twice
			possibleDirections.remove(line.directionMoved);
			// Go in all possible directions
			for (Direction dir : possibleDirections) {
				Cell endpointCell = line.endCell.go(dir);
				possibleEndingPoints.add(endpointCell);
			}
		}
		// Then convert each target location to a move, and you're done!
		Piece movingPiece = context.piece.orElseThrow(() -> new NoSuchElementException("There needs to be a piece at the location defined by `context` (required in order to construct a Move object)"));
		Set<Move> finalMoves = new HashSet<Move>();
		for (Cell c : possibleEndingPoints) {
			c.piece.ifPresentOrElse((v) -> {
				finalMoves.add(new Move.CaptureMove(context, movingPiece, c, v));
			}, () -> {
				finalMoves.add(new Move.JumpMove(context, movingPiece, c));
			});
		}
		return finalMoves;
	}
	/**
	 * A cell, plus information about the path we took to get to that cell.
	 */
	private static class CellLine {
		public Cell endCell; // The cell that this line ends at
		public Direction directionMoved; // The direction this line goes in
		public CellLine(Cell endCell, Direction directionMoved) {
			this.endCell = endCell;
			this.directionMoved = directionMoved;
		}
	}
}
