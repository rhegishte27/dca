package com.equisoft.dca.backend.dataobject.model;

import com.equisoft.dca.backend.common.model.BaseEnum;

import lombok.Getter;

@Getter
public enum DataObjectType implements BaseEnum<Integer> {

	COBOL_COPYBOOK(1, "COBOL Copybook"),
	JAVA_CLASS(2, "Java class"),
	DDL(3, "DDL"),
	XML(4, "XML"),
	MAINFRAME_ASSEMBLER(5, "Mainframe assembler");

	private final int id;

	private final String name;

	DataObjectType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public Integer getId() {
		return id;
	}
}
