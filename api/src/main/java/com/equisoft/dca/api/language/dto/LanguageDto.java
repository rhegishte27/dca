package com.equisoft.dca.api.language.dto;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LanguageDto implements Serializable {

	private static final long serialVersionUID = 6647467709650806410L;

	@Schema(required = true)
	private Integer id;

	@Schema(required = true)
	private String name;

	@Schema(required = true)
	private String code;

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		LanguageDto languageDto = (LanguageDto) obj;
		return Objects.equals(id, languageDto.getId());
	}
}
