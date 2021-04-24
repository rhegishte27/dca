package com.equisoft.dca.backend.common.model;

import java.util.Arrays;
import java.util.Objects;

import org.springframework.util.Assert;

public interface BaseEnum<ID> {

	ID getId();

	static <ID, E extends BaseEnum<ID>> E valueOf(Class<E> enumType, ID id) {
		Objects.requireNonNull(enumType, "EnumType cannot be null");
		Objects.requireNonNull(id, "Id cannot be null");
		Assert.isTrue(enumType.isEnum(), "EnumType must be an enum");

		return Arrays.stream(enumType.getEnumConstants())
				.filter(item -> item.getId().equals(id))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Unknown id: " + id));
	}
}
