package com.equisoft.dca.api.location.dto;

import java.io.Serializable;
import java.util.Objects;

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
public class LocationDto implements Serializable {

	private static final long serialVersionUID = -6833159784687932434L;

	@Setter
	@Schema(required = true)
	private Integer id;

	@Schema(required = true)
	private String identifier;

	@Schema(required = true)
	private LocationTypeDto locationType;

	@Schema(required = true)
	private String path;

	@Schema(required = true)
	private String serverName;

	@Schema(required = true)
	private String userName;

	@Schema(required = true)
	private String password;

	@Schema(required = true)
	private PlatformTypeDto platformType;

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
		LocationDto locationDto = (LocationDto) obj;
		return Objects.equals(id, locationDto.getId()) &&
				Objects.equals(identifier, locationDto.getIdentifier());
	}
}
