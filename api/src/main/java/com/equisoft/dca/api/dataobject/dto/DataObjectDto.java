package com.equisoft.dca.api.dataobject.dto;

import java.io.Serializable;
import java.util.List;
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
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class DataObjectDto implements Serializable {

	private static final long serialVersionUID = 2635283979182083459L;

	@Schema(required = true)
	private Integer id;

	@Schema(required = true)
	private String identifier;

	@Schema(required = true)
	private String description;

	@Schema(required = true)
	private Integer systemId;

	@Schema(required = true)
	private String systemIdentifier;

	@Schema(required = true)
	private DataObjectTypeDto type;

	@Schema(required = true)
	private DataObjStatusDto status;

	private List<DataObjectFileDto> dataObjectFiles;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DataObjectDto that = (DataObjectDto) o;
		return Objects.equals(getId(), that.getId()) &&
				Objects.equals(getIdentifier(), that.getIdentifier());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getIdentifier());
	}
}
