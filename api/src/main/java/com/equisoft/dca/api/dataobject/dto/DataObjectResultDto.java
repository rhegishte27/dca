package com.equisoft.dca.api.dataobject.dto;

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
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class DataObjectResultDto implements Serializable {
	private static final long serialVersionUID = -5740923628010454698L;

	@Schema(required = true)
	private Integer id;

	@Schema(required = true)
	private DataObjectDto dataObject;

	@Schema(required = true)
	private Boolean valid;

	@Schema(required = true)
	private String content;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DataObjectResultDto that = (DataObjectResultDto) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
