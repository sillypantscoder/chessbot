package com.sillypantscoder.chess.game;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.sillypantscoder.utils.SelfAwareMap;

public class Cell implements SelfAwareMap.MapItem {
	public String name;
	public HashMap<String, Cell> connections;
	public HashMap<String, Cell> reverseConnections;
	public Optional<Piece> piece;
	public Direction standardForwardsDirection;
	public boolean highlighted;
	public Cell(String name, Direction standardForwardsDirection) {
		this.name = name;
		this.connections = new HashMap<String, Cell>();
		this.reverseConnections = new HashMap<String, Cell>();
		this.piece = Optional.empty();
		this.standardForwardsDirection = standardForwardsDirection;
		this.highlighted = false;
	}
	public String getName() {
		return name;
	}
	public void updateReverseConnections(Collection<Cell> cells) {
		this.reverseConnections = new HashMap<String, Cell>();
		for (Cell c : cells) {
			for (String dirName : c.connections.keySet()) {
				if (c.connections.get(dirName) == this) {
					this.reverseConnections.put(dirName, c);
				}
			}
		}
	}
	public Set<Direction> getDirections() {
		Set<Direction> directions = new HashSet<Direction>();
		directions.addAll(connections.keySet().stream().map((v) -> new Direction(v, false)).toList());
		directions.addAll(reverseConnections.keySet().stream().map((v) -> new Direction(v, true)).toList());
		return directions;
	}
	// Navigation
	public Cell go(Direction direction) {
		HashMap<String, Cell> map = direction.reversed() ? reverseConnections : connections;
		if (map.containsKey(direction.name())) {
			return map.get(direction.name());
		}
		return null;
	}
	public Cell go(Direction direction, int amount) {
		if (amount == 0) return this;
		if (amount < 0) {
			direction = direction.reverseDirection();
			amount *= -1;
		}
		Cell targetCell = this;
		for (int i = 0; i < amount; i++) {
			targetCell = targetCell.go(direction);
			if (targetCell == null) return null;
		}
		return targetCell;
	}
	public Cell go_diagonal(Direction direction1, Direction direction2) {
		// Direction 1 first...
		Cell loc1 = this.go(direction1);
		if (loc1 == null) return null;
		loc1 = loc1.go(direction2);
		if (loc1 == null) return null;
		// Direction 2 first...
		Cell loc2 = this.go(direction2);
		if (loc2 == null) return null;
		loc2 = loc2.go(direction1);
		if (loc2 == null) return null;
		// Make sure they are the same
		if (loc1 != loc2) return null;
		else return loc1;
	}
}
