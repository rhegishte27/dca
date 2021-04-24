package com.equisoft.dca.backend.dataobject.model;

import com.equisoft.dca.backend.common.model.BaseEnum;

import lombok.Getter;

@Getter
public enum DataObjectLocationType implements BaseEnum<Integer> {

	MY_COMPUTER(1, "My computer"),
	DCA_SERVER(2, "DCA server"),
	FTP(3, "FTP");

	private final int id;

	private final String name;

	DataObjectLocationType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public Integer getId() {
		return id;
	}
}
