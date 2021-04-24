package com.equisoft.dca.api.dataobject.dto;

import java.io.Serializable;
import java.util.Objects;

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
public class DataObjectFileDto implements Serializable {

	private static final long serialVersionUID = 1077267406790677605L;

	private Integer id;

	private String userIdentifier;

	private String originalFileName;

	private String dataObjectContent;

	private String resultContent;

	private DataObjStatusDto status;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DataObjectFileDto that = (DataObjectFileDto) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
