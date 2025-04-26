package com.sillypantscoder.chess.game;

import java.awt.Color;

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
	public static class JumpMove extends Move {
		public JumpMove(Piece piece, Cell targetLoc) {
			super(piece, targetLoc);
		}
		public String toString() {
			return "Jump" + super.toString();
		}
		public Color getColor() {
			return new Color(128, 128, 255);
		}
	}
	public static class CaptureMove extends Move {
		public Piece capturedPiece;
		public CaptureMove(Piece piece, Cell targetLoc, Piece capturedPiece) {
			super(piece, targetLoc);
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
	}
}
