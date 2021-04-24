package com.equisoft.dca.api.dataobject.dto;

import lombok.Getter;
import lombok.Setter;

public enum DataObjectStatusDto {
	;

	public enum Response {
		;

		@Getter
		@Setter
		public static final class Detailed implements Id, Name {
			Integer id;
			String name;
		}
	}

	private interface Id {
		Integer getId();
	}
	private interface Name {
		String getName();
	}
}
