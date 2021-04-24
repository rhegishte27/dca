package com.equisoft.dca.backend.location.model;

import com.equisoft.dca.backend.common.model.BaseEnum;

import lombok.Getter;

@Getter
public enum PlatformType implements BaseEnum<Integer> {

	WINDOWS_UNIX(1, "Windows/UNIX"),
	MAINFRAME(2, "Mainframe"),
	AS400(3, "AS/400"),
	AS400_2(4, "AS/400-2"),
	VSE_BIM(5, "VSE/BIM");

	private final int id;

	private final String name;

	PlatformType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public Integer getId() {
		return id;
	}
}
