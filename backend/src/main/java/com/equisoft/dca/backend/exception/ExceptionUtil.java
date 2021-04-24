package com.equisoft.dca.backend.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

public final class ExceptionUtil {

	private ExceptionUtil() {
	}

	public static <K, V> Map<K, V> toMap(Class<K> keyType, Class<V> valueType, Object... entries) {
		if (keyType == null) {
			throw new IllegalArgumentException("Invalid keyType");
		}

		if (valueType == null) {
			throw new IllegalArgumentException("Invalid valueType");
		}

		if (entries.length % 2 == 1) {
			throw new IllegalArgumentException("Invalid entries");
		}
		return IntStream.range(0, entries.length / 2).map(i -> i * 2)
				.collect(LinkedHashMap::new,
						(m, i) -> putToMap(m, keyType, entries[i], valueType, entries[i + 1]),
						Map::putAll);
	}

	private static <K, V> void putToMap(Map<K, V> map, Class<K> keyType, Object key, Class<V> valueType, Object value) {
		K k;
		V v;
		try {
			k = keyType.cast(key);
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("Key type: " + keyType + " and Key: " + key + " does not match");
		}

		try {
			v = valueType.cast(value);
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("Value type: " + valueType + " and Value: " + value + " does not match");
		}

		map.put(k, v);
	}
}
