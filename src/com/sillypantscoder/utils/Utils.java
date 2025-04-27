package com.sillypantscoder.utils;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class Utils {
	/**
	 * Return whether both values are present and equal.
	 */
	public static<T> boolean bothSameValue(Optional<T> a, Optional<T> b) {
		AtomicBoolean ret = new AtomicBoolean(false);
		a.ifPresent((av) -> {
			b.ifPresent((bv) -> {
				if (av.equals(bv)) {
					ret.set(true);
				}
			});
		});
		return ret.get();
	}
	public static<T> boolean bothSameValue(T a, Optional<T> b) {
		AtomicBoolean ret = new AtomicBoolean(false);
		b.ifPresent((bv) -> {
			if (a.equals(bv)) {
				ret.set(true);
			}
		});
		return ret.get();
	}
}
