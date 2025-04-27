package com.sillypantscoder.chess.game;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

import com.sillypantscoder.utils.SelfAwareMap;

public class Cell implements SelfAwareMap.MapItem {
	public String name;
	public HashMap<String, Cell> connections;
	public HashMap<String, Cell> reverseConnections;
	public Optional<Piece> piece;
	public Cell(String name) {
		this.name = name;
		this.connections = new HashMap<String, Cell>();
		this.reverseConnections = new HashMap<String, Cell>();
		this.piece = Optional.empty();
	}
	public String getName() {
		return name;
	}
	public void updateReverseConnections(Collection<Cell> cells) {
		for (Cell c : cells) {
			for (String dirName : c.connections.keySet()) {
				if (c.connections.get(dirName) == this) {
					this.reverseConnections.put(dirName, c);
				}
			}
		}
	}
	// Navigation
	public Cell go(String direction, boolean reversed) {
		HashMap<String, Cell> map = reversed ? reverseConnections : connections;
		if (map.containsKey(direction)) {
			return map.get(direction);
		}
		return null;
	}
	public Cell go(String direction, int amount) {
		if (amount == 0) return this;
		Cell targetCell = this;
		for (int i = 0; i < Math.abs(amount); i++) {
			targetCell = targetCell.go(direction, amount < 0);
			if (targetCell == null) return null;
		}
		return targetCell;
	}
	public Cell go_diagonal(String direction1, boolean reversed1, String direction2, boolean reversed2) {
		// Direction 1 first...
		Cell loc1 = this.go(direction1, reversed1);
		if (loc1 == null) return null;
		loc1 = loc1.go(direction2, reversed2);
		if (loc1 == null) return null;
		// Direction 2 first...
		Cell loc2 = this.go(direction2, reversed2);
		if (loc2 == null) return null;
		loc2 = loc2.go(direction1, reversed1);
		if (loc2 == null) return null;
		// Make sure they are the same
		if (loc1 != loc2) return null;
		else return loc1;
	}
}
