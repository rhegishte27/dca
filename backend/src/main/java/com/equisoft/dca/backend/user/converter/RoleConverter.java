package com.equisoft.dca.backend.user.converter;

import javax.persistence.Converter;

import com.equisoft.dca.backend.common.converter.BaseEnumConverter;
import com.equisoft.dca.backend.user.model.Role;

@Converter(autoApply = true)
public class RoleConverter extends BaseEnumConverter<Role, Integer> {

	RoleConverter() {
		super(Role.class);
	}
}
