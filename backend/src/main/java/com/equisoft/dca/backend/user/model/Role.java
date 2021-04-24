package com.equisoft.dca.backend.user.model;

import java.util.Set;

import com.equisoft.dca.backend.common.model.BaseEnum;

import lombok.Getter;

import static com.equisoft.dca.backend.user.model.Feature.ORGANIZATIONS_AND_USERS;
import static com.equisoft.dca.backend.user.model.Feature.PROJECT_DATA_MAPS;
import static com.equisoft.dca.backend.user.model.Feature.PROJECT_MANAGEMENT;
import static com.equisoft.dca.backend.user.model.Feature.PROJECT_TABLES;
import static com.equisoft.dca.backend.user.model.Feature.SYSTEM_CODE_ANALYSIS;
import static com.equisoft.dca.backend.user.model.Feature.SYSTEM_DATA_OBJECTS;
import static com.equisoft.dca.backend.user.model.Feature.SYSTEM_MANAGEMENT;
import static com.equisoft.dca.backend.user.model.Feature.SYSTEM_SETTINGS;
import static com.equisoft.dca.backend.user.model.Feature.SYSTEM_TRANSACTIONS;

@Getter
public enum Role implements BaseEnum<Integer> {
	SYSTEM_ADMINISTRATOR(1, "System administrator", 1,
			Set.of(SYSTEM_SETTINGS, ORGANIZATIONS_AND_USERS,
					PROJECT_MANAGEMENT, PROJECT_DATA_MAPS, PROJECT_TABLES,
					SYSTEM_MANAGEMENT, SYSTEM_CODE_ANALYSIS, SYSTEM_DATA_OBJECTS, SYSTEM_TRANSACTIONS),
			Set.of(SYSTEM_SETTINGS, ORGANIZATIONS_AND_USERS,
					PROJECT_MANAGEMENT, PROJECT_DATA_MAPS, PROJECT_TABLES,
					SYSTEM_MANAGEMENT, SYSTEM_CODE_ANALYSIS, SYSTEM_DATA_OBJECTS, SYSTEM_TRANSACTIONS)),
	PROJECT_MANAGER(2, "Project manager", 2,
			Set.of(PROJECT_MANAGEMENT, PROJECT_DATA_MAPS, PROJECT_TABLES,
					SYSTEM_MANAGEMENT, SYSTEM_CODE_ANALYSIS, SYSTEM_DATA_OBJECTS, SYSTEM_TRANSACTIONS),
			Set.of(PROJECT_MANAGEMENT, PROJECT_DATA_MAPS, PROJECT_TABLES,
					SYSTEM_MANAGEMENT, SYSTEM_CODE_ANALYSIS, SYSTEM_DATA_OBJECTS, SYSTEM_TRANSACTIONS)),
	DM_DEVELOPER(3, "DM developer", 3,
			Set.of(PROJECT_MANAGEMENT, PROJECT_DATA_MAPS, PROJECT_TABLES,
					SYSTEM_MANAGEMENT, SYSTEM_CODE_ANALYSIS, SYSTEM_DATA_OBJECTS, SYSTEM_TRANSACTIONS),
			Set.of(SYSTEM_SETTINGS)),
	BUSINESS_ANALYST(4, "Business analyst", 4,
			Set.of(PROJECT_DATA_MAPS, PROJECT_TABLES,
					SYSTEM_DATA_OBJECTS, SYSTEM_TRANSACTIONS),
			Set.of(SYSTEM_SETTINGS)),
	CONVERSION_ANALYST(5, "Conversion analyst", 5,
			Set.of(PROJECT_DATA_MAPS, PROJECT_TABLES),
			Set.of(SYSTEM_SETTINGS));

	private final int id;

	private final String name;

	private final int level;

	private final Set<Feature> defaultFeatures;

	private final Set<Feature> nonEditableFeatures;

	Role(int id, String name, int level, Set<Feature> defaultFeatures, Set<Feature> nonEditableFeatures) {
		this.id = id;
		this.name = name;
		this.level = level;
		this.defaultFeatures = defaultFeatures;
		this.nonEditableFeatures = nonEditableFeatures;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public boolean hasHigherOrEqualsLevel(Role otherRole) {
		return level <= otherRole.getLevel();
	}
}
