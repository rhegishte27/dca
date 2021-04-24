package com.equisoft.dca.backend.user.service;

import java.util.Optional;

import javax.validation.Valid;

import com.equisoft.dca.backend.user.model.UserSetting;

public interface UserSettingService {

	Optional<UserSetting> findById(Integer id);

	UserSetting save(@Valid UserSetting userSetting);

	void deleteById(Integer id);

	void resetProject(Integer projectId);

	void resetSystem(Integer systemId);

	void resetOrganization(Integer organizationId);

	void resetUser(Integer userId);

	void resetLocation(Integer locationId);

	void resetDataObject(Integer dataObjectId);
}
