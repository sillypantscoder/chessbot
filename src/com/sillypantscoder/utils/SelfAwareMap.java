package com.sillypantscoder.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SelfAwareMap<T extends SelfAwareMap.MapItem> implements Map<String, T> {
	public static interface MapItem {
		public String getName();
	}
	public ArrayList<T> items;
	public SelfAwareMap() {
		this.items = new ArrayList<T>();
	}
	// Map methods
	public int size() {
		return this.items.size();
	}

	public boolean isEmpty() {
		return this.items.isEmpty();
	}

	public boolean containsKey(Object key) {
		return this.items.stream().anyMatch((v) -> v.getName().equals(key));
	}

	public boolean containsValue(Object value) {
		return this.items.contains(value);
	}

	public T get(Object key) {
		if (!this.containsKey(key)) return null;
		for (int i = 0; i < this.items.size(); i++) {
			if (this.items.get(i).getName().equals(key)) {
				return this.items.get(i);
			}
		}
		return null;
	}

	public T put(String key, T value) {
		// See if this key is already in the map
		for (int i = 0; i < this.items.size(); i++) {
			if (this.items.get(i).getName().equals(key)) {
				// It is, yay
				this.items.set(i, value);
				return value;
			}
		}
		// Nope, add a new key
		this.items.add(value);
		return value;
	}
	public T put(T value) {
		return put(value.getName(), value);
	}

	public T remove(Object key) {
		for (int i = 0; i < this.items.size(); i++) {
			if (this.items.get(i).getName().equals(key)) {
				// It is, yay
				T value = this.items.remove(i);
				return value;
			}
		}
		return null;
	}

	public void putAll(Map<? extends String, ? extends T> var1) {
		for (Entry<? extends String, ? extends T> entry : var1.entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}
	public void putAll(List<? extends T> items) {
		for (T entry : items) {
			this.put(entry);
		}
	}

	public void clear() {
		this.items.clear();
	}

	public Set<String> keySet() {
		return this.items.stream().map((v) -> v.getName()).collect(Collectors.toSet());
	}

	public Collection<T> values() {
		return new ArrayList<T>(this.items);
	}

	public Set<Entry<String, T>> entrySet() {
		SelfAwareMap<T> _map = this; // closure :/
		return this.items.stream().map((v) -> new Entry<String, T>() {
			public String getKey() {
				return v.getName();
			}
			public T getValue() {
				return v;
			}
			public T setValue(T value) {
				return _map.put(value);
			}
		}).collect(Collectors.toSet());
	}

	public boolean equals(Object other) {
		if (other instanceof SelfAwareMap otherMap) {
			return otherMap.items.equals(this.items);
		}
		return false;
	}

	public int hashCode() {
		return this.items.hashCode() + 22;
	}
}
