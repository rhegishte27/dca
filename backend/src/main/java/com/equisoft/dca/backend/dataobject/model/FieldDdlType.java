package com.equisoft.dca.backend.dataobject.model;

import java.util.Arrays;
import java.util.Objects;

import com.equisoft.dca.backend.common.model.BaseEnum;

import lombok.Getter;

@Getter
public enum FieldDdlType implements BaseEnum<Integer> {

	BIGINT(1, "BIGINT"),
	BINARY(2, "BINARY"),
	BIT(3, "BIT"),
	BLOB(4, "BLOB"),
	CHAR(5, "CHAR"),
	VCHAR(6, "VCHAR"),
	NVCHAR(7, "NVCHAR"),
	NCHAR(8, "NCHAR"),
	TEXT(9, "TEXT"),
	NTEXT(10, "NTEXT"),
	CLOB(11, "CLOB"),
	LONG(12, "LONG"),
	XML(13, "XML"),
	CURSOR(14, "CURSOR"),
	DATE(15, "DATE"),
	DAY(16, "DAY"),
	DATETIME(17, "DATETIME"),
	DBCLOB(18, "DBCLOB"),
	NUMERIC(19, "NUMERIC"),
	FLOAT(20, "FLOAT"),
	HOUR(21, "HOUR"),
	INTEGER(22, "INTEGER"),
	INTERVAL(23, "INTERVAL"),
	IMAGE(24, "IMAGE"),
	MINUTE(25, "MINUTE"),
	MONEY(26, "MONEY"),
	MONTH(27, "MONTH"),
	SECOND(28, "SECOND"),
	SMALL_DATETIME(29, "SMALLDATETIME"),
	SMALLINT(30, "SMALLINT"),
	SMALL_MONEY(31, "SMALLMONEY"),
	SQL_VARIANT(32, "SQL_VARIANT"),
	TIMESTAMP(33, "TIMESTAMP"),
	TINYINT(34, "TINYINT"),
	TABLE(35, "TABLE"),
	TIME(36, "TIME"),
	UNIQUE_ID(37, "UNIQUEID"),
	YEAR(38, "YEAR"),
	UNKNOWN(39, "UNKNOWN"),
	NONE(40, "");

	private final int id;

	private final String name;

	FieldDdlType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public static FieldDdlType from(String name) {
		Objects.requireNonNull(name, "Name cannot be null");

		return Arrays.stream(values())
				.filter(item -> item.getName().equals(name))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Unknown name: " + name));
	}
}
