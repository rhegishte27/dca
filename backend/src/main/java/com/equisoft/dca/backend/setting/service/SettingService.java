package com.equisoft.dca.backend.setting.service;

import java.util.Optional;

import javax.validation.Valid;

import com.equisoft.dca.backend.setting.model.Setting;

public interface SettingService {

	Setting save(@Valid Setting setting);

	Setting update(@Valid Setting setting);

	Optional<Setting> get();
}
