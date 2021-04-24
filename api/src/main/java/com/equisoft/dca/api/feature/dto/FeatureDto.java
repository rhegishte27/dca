package com.equisoft.dca.api.feature.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class FeatureDto implements Serializable {

	private static final long serialVersionUID = -2835252724831212465L;

	@Schema(required = true)
	private Integer id;

	@Schema(required = true)
	private String name;
}
