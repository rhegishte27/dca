package com.equisoft.dca.backend.user.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.equisoft.dca.backend.dataobject.model.DataObject;
import com.equisoft.dca.backend.location.model.Location;
import com.equisoft.dca.backend.project.model.Project;
import com.equisoft.dca.backend.system.model.System;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "usersettings")
public class UserSetting implements Serializable {

	private static final long serialVersionUID = -650393460239496258L;

	@Id
	@Column(name = "fk_users")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "fk_users", insertable = false, updatable = false)
	@NotNull(message = "user.notnull")
	private User user;

	@ManyToOne
	@JoinColumn(name = "fk_defaultsystem")
	private System defaultSystem;

	@ManyToOne
	@JoinColumn(name = "fk_defaultproject")
	private Project defaultProject;

	@ManyToOne
	@JoinColumn(name = "fk_defaultorganization")
	private Organization defaultOrganization;

	@ManyToOne
	@JoinColumn(name = "fK_defaultuser")
	private User defaultUser;

	@ManyToOne
	@JoinColumn(name = "fK_defaultlocation")
	private Location defaultLocation;

	@ManyToOne
	@JoinColumn(name = "fK_defaultdataobject")
	private DataObject defaultDataObject;

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		UserSetting userSetting = (UserSetting) obj;
		return Objects.equals(id, userSetting.getId());
	}
}
