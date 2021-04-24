package com.equisoft.dca.backend.configuration.model;

import java.io.Serializable;
import java.util.Objects;

import com.equisoft.dca.backend.language.model.Language;
import com.equisoft.dca.backend.user.model.User;
import com.equisoft.dca.backend.user.model.UserSetting;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Configuration implements Serializable {

	private static final long serialVersionUID = -640844314861268185L;

	private User user;

	private Language language;

	private UserSetting userSetting;

	@Override
	public int hashCode() {
		return Objects.hash(user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Configuration configuration = (Configuration) obj;
		return Objects.equals(user, configuration.getUser());
	}
}
