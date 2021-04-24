package com.equisoft.dca.backend.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.equisoft.dca.backend.user.model.UserSetting;

@Repository
public interface UserSettingRepository extends JpaRepository<UserSetting, Integer> {
	@Modifying
	@Transactional
	@Query("update UserSetting us set us.defaultProject = null where us.defaultProject.id = :projectId")
	void resetProject(@Param("projectId") Integer projectId);

	@Modifying
	@Transactional
	@Query("update UserSetting us set us.defaultSystem = null where us.defaultSystem.id = :systemId")
	void resetSystem(@Param("systemId") Integer systemId);

	@Modifying
	@Transactional
	@Query("update UserSetting us set us.defaultOrganization = null where us.defaultOrganization.id = :organizationId")
	void resetOrganization(@Param("organizationId") Integer organizationId);

	@Modifying
	@Transactional
	@Query("update UserSetting us set us.defaultUser = null where us.defaultUser.id = :userId")
	void resetUser(@Param("userId") Integer userId);

	@Modifying
	@Transactional
	@Query("update UserSetting us set us.defaultLocation = null where us.defaultLocation.id = :locationId")
	void resetLocation(@Param("locationId") Integer locationId);

	@Modifying
	@Transactional
	@Query("update UserSetting us set us.defaultDataObject = null where us.defaultDataObject.id = :dataObjectId")
	void resetDataObject(@Param("dataObjectId") Integer dataObjectId);
}
