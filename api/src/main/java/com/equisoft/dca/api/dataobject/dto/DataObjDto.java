package com.equisoft.dca.api.dataobject.dto;

import lombok.Getter;
import lombok.Setter;

public enum DataObjDto {
	;

	public enum Request {
		;

		@Getter
		@Setter
		public static final class Create implements Identifier, Description, System, OriginalFileName, Type, Content {
			String identifier;
			String description;
			Integer system;
			String originalFileName;
			Integer type;
			String content;
		}

		@Getter
		@Setter
		public static final class Validate implements Type, OriginalFileName, Content {
			Integer type;
			String originalFileName;
			String content;
		}
	}

	public enum Response {
		;

		@Getter
		@Setter
		public static final class Summary implements Id, Identifier, Description, System, Status {
			Integer id;
			String identifier;
			String description;
			Integer system;
			Integer status;
		}

		@Getter
		@Setter
		public static final class Detailed implements Id, Identifier, Description, User, System, OriginalFileName, Content, Status {
			Integer id;
			String identifier;
			String description;
			String user;
			Integer system;
			String originalFileName;
			String content;
			Integer status;
		}

		@Getter
		@Setter
		public static final class Validate implements OriginalFileName, Status {
			String originalFileName;
			Integer status;
		}
	}

	private interface Id {
		Integer getId();
	}
	private interface Identifier {
		String getIdentifier();
	}
	private interface Description {
		String getDescription();
	}
	private interface Type {
		Integer getType();
	}
	private interface User {
		String getUser();
	}
	private interface System {
		Integer getSystem();
	}
	private interface OriginalFileName {
		String getOriginalFileName();
	}
	private interface Content {
		String getContent();
	}
	private interface Status {
		Integer getStatus();
	}
}
