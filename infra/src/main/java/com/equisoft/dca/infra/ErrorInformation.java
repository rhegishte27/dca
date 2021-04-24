package com.equisoft.dca.infra;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class ErrorInformation {

	private final String message;

	private final String extraInformation;
}
