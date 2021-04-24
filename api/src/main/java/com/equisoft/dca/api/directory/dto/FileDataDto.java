package com.equisoft.dca.api.directory.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class FileDataDto implements Serializable {

	private static final long serialVersionUID = -9217623093246018271L;

	@Schema(required = true)
	private String path;

	@Schema(required = true)
	private String name;

	@Schema(required = true)
	private Boolean isDirectory;

	@Schema(required = true)
	private String parentPath;

	@Schema(required = true)
	private String content;

	@Schema(required = true)
	private List<String> childrenPathList;
}
