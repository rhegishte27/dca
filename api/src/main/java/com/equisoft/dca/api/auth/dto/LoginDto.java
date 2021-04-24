package com.equisoft.dca.api.auth.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

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
public class LoginDto implements Serializable {

	private static final long serialVersionUID = 938202096353830619L;

	@NotNull
	@Schema(required = true)
	private String identifier;

	@NotNull
	@Schema(required = true)
	private String password;
}
