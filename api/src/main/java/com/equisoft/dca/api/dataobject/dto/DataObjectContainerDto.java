package com.equisoft.dca.api.dataobject.dto;

import java.io.Serializable;
import java.util.List;

import com.equisoft.dca.api.location.dto.LocationDto;
import com.equisoft.dca.api.system.dto.SystemDto;

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
public class DataObjectContainerDto implements Serializable {

	private static final long serialVersionUID = -8953994261402137464L;

	@Schema(required = true)
	private SystemDto system;

	@Schema(required = true)
	private DataObjectLocationTypeDto locationType;

	@Schema(required = true)
	private DataObjectTypeDto type;

	@Schema(required = true)
	private LocationDto location;

	private List<DataObjectFileDto> dataObjectFileList;

	private List<DataObjectFileDto> dataObjectFileListChosenInCreateForm;
}
