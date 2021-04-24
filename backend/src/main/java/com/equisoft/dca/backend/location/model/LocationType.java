package com.equisoft.dca.backend.location.model;

import com.equisoft.dca.backend.common.model.BaseEnum;

import lombok.Getter;

@Getter
public enum LocationType implements BaseEnum<Integer> {

	NETWORK(1, "Network"),
	FTP(2, "FTP");

	private final int id;

	private final String name;

	LocationType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public Integer getId() {
		return id;
	}
}
