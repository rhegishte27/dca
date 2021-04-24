package com.equisoft.dca.api.location.dto;

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
public class LocationTypeDto implements Serializable {

	private static final long serialVersionUID = -7000248956181951607L;

	@Schema(required = true)
	private Integer id;

	@Schema(required = true)
	private String name;

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
		LocationTypeDto locationTypeDto = (LocationTypeDto) obj;
		return Objects.equals(id, locationTypeDto.getId());
	}
}
