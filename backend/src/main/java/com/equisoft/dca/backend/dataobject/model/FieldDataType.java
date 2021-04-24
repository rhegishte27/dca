package com.equisoft.dca.backend.dataobject.model;

import java.util.Arrays;
import java.util.Objects;

import com.equisoft.dca.backend.common.model.BaseEnum;

import lombok.Getter;

@Getter
public enum FieldDataType implements BaseEnum<Integer> {

	UNKNOWN(1, "UNKNOWN"),
	ALPHA(2, "ALPHA"),
	INTEGER(3, "INTEGER"),
	FIXED(4, "FIXED"),
	FLOAT(5, "FLOAT"),
	NO_MASK(6, "NO MASK"),
	INDEX(7, "INDEX"),
	LEVEL_88(8, "LEVEL_88"),
	PACKED_DECIMAL(9, "PACKED_DECIMAL"),
	BINARY(10, "BINARY"),
	FIG_CONST_ALPHA(11, "FIG_CONST_ALPHA"),
	FIG_CONST(12, "FIG_CONST"),
	CONSTANT(13, "CONSTANT"),
	NUMERIC(14, "NUMERIC"),
	FIGURATIVE(15, "FIGURATIVE"),
	WORK(16, "WORK"),
	EDIT_TABLE(17, "EDIT-TABLE"),
	STRING_BASED(18, "STRING_BASED"),
	DISPLAY(19, "DISPLAY"),
	PACKED_INT(20, "PACKED_INT"),
	DOUBLE_BYTE(21, "DOUBLE BYTE"),
	INT_TYPE(22, "INT_TYPE"),
	GRAPHIC(23, "GRAPHIC"),
	NATIONAL(24, "NATIONAL"),
	BAD_DISPLAY(25, "BAD_DISPLAY"),
	KEYWORD(26, "KEYWORD"),
	XML_ELEMENT(27, "XML_ELEMENT"),
	JAVA_CLASS(28, "JAVA_CLASS"),
	USER(29, "USER");

	private final int id;

	private final String name;

	FieldDataType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public static FieldDataType from(String name) {
		Objects.requireNonNull(name, "Name cannot be null");

		return Arrays.stream(values())
				.filter(item -> item.getName().equals(name))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Unknown name: " + name));
	}
}
