package com.equisoft.dca.api.system.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.equisoft.dca.api.dataobject.dto.DataObjectDto;

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
public class SystemDto implements Serializable {

	private static final long serialVersionUID = 2537942748337106202L;

	@Setter
	@Schema(required = true)
	private Integer id;

	@Schema(required = true)
	private String identifier;

	@Schema(required = true)
	private String description;

	@Schema(required = true)
	private Set<DataObjectDto> dataObjects = new HashSet<>();

	@Override
	public int hashCode() {
		return Objects.hash(id, identifier);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		SystemDto systemDto = (SystemDto) obj;
		return Objects.equals(id, systemDto.getId()) &&
				Objects.equals(identifier, systemDto.getIdentifier());
	}
}
