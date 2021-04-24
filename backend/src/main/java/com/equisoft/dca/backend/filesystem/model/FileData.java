package com.equisoft.dca.backend.filesystem.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class FileData {
	private String path;
	private String name;
	private Boolean isDirectory;
	private String parentPath;
	private String content;
	@Default
	private List<String> childrenPathList = new ArrayList<>();
}
