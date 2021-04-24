package com.equisoft.dca.api.setting.dto;

import java.io.Serializable;

import com.equisoft.dca.api.language.dto.LanguageDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SettingDto implements Serializable {

	private static final long serialVersionUID = 1060274304360183175L;

	@Setter
	@Schema(required = true)
	private Integer id;

	@Schema(required = true)
	private LanguageDto language;

	@Schema(required = true)
	private Integer tokenDuration;

	@Schema(required = true)
	private String commonFolder;

	@Schema(required = true)
	private String defaultImportFolder;

	@Schema(required = true)
	private String defaultExportFolder;

	@Schema(required = true)
	private String defaultDownloadFolder;
}
