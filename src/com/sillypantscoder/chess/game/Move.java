package com.sillypantscoder.chess.game;

import java.awt.Color;
import java.util.Optional;

import com.sillypantscoder.chess.bot.DuplicatedBoard;

public abstract class Move {
	public Piece piece;
	public Cell targetLoc;
	public Move(Piece piece, Cell targetLoc) {
		this.piece = piece;
		this.targetLoc = targetLoc;
	}
	public String toString() {
		return "Move { piece: " + this.piece.toString() + ", target location: \"" + targetLoc.name + "\" }";
	}
	public boolean equals(Object other) {
		if (other instanceof Move otherMove) {
			if (this.piece == otherMove.piece && this.targetLoc == otherMove.targetLoc) {
				return true;
			} else return false;
		}
		return false;
	}
	public abstract Color getColor();
	public abstract void execute();
	public abstract Move duplicate(DuplicatedBoard board);
	public static class JumpMove extends Move {
		public Cell originalLoc;
		public JumpMove(Cell originalLoc, Piece piece, Cell targetLoc) {
			super(piece, targetLoc);
			this.originalLoc = originalLoc;
		}
		public String toString() {
			return "Jump" + super.toString();
		}
		public Color getColor() {
			return new Color(128, 128, 255);
		}
		public void execute() {
			this.originalLoc.piece = Optional.empty();
			this.targetLoc.piece = Optional.of(this.piece);
			this.originalLoc.highlighted = true;
			this.targetLoc.highlighted = true;
		}
		public JumpMove duplicate(DuplicatedBoard board) {
			return new JumpMove(board.getCopy(originalLoc), board.getCopy(piece), board.getCopy(targetLoc));
		}
	}
	public static class CaptureMove extends Move {
		public Cell originalLoc;
		public Piece capturedPiece;
		public CaptureMove(Cell originalLoc, Piece piece, Cell targetLoc, Piece capturedPiece) {
			super(piece, targetLoc);
			this.originalLoc = originalLoc;
			this.capturedPiece = capturedPiece;
		}
		public String toString() {
			return "Capture" + super.toString().substring(0, super.toString().length() - 2) + ", captured piece: " + capturedPiece.toString() + " }";
		}
		public boolean equals(Object other) {
			if (other instanceof CaptureMove otherMove) {
				if (this.piece == otherMove.piece && this.targetLoc == otherMove.targetLoc && this.capturedPiece == otherMove.capturedPiece) {
					return true;
				} else return false;
			}
			return false;
		}
		public Color getColor() {
			return new Color(255, 128, 128);
		}
		public void execute() {
			this.originalLoc.piece = Optional.empty();
			this.targetLoc.piece = Optional.of(this.piece);
			this.originalLoc.highlighted = true;
			this.targetLoc.highlighted = true;
		}
		public CaptureMove duplicate(DuplicatedBoard board) {
			return new CaptureMove(board.getCopy(originalLoc), board.getCopy(piece), board.getCopy(targetLoc), board.getCopy(capturedPiece));
		}
	}
}
