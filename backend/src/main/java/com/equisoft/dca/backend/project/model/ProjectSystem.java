package com.equisoft.dca.backend.project.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.equisoft.dca.backend.location.model.Location;
import com.equisoft.dca.backend.project.converter.SystemTypeConverter;
import com.equisoft.dca.backend.system.model.System;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "projectsystems")
public class ProjectSystem implements Serializable {
	private static final long serialVersionUID = 457336819493459238L;

	@Builder.Default
	@EmbeddedId
	private ProjectSystemId id = new ProjectSystemId();

	@NotNull(message = "projectsystem.systemtype.notnull")
	@Convert(converter = SystemTypeConverter.class)
	@Column(name = "systemtype")
	private SystemType systemType;

	@NotNull(message = "projectsystem.isSynchronizationEnable.notnull")
	@Column(name = "syncenabled")
	private Boolean isSynchronizationEnabled;

	@ManyToOne
	@JoinColumn(name = "FK_Locations")
	private Location location;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		ProjectSystem projectSystem = (ProjectSystem) obj;
		return Objects.equals(id, projectSystem.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@Setter
	@Embeddable
	public static class ProjectSystemId implements Serializable {

		private static final long serialVersionUID = 622811346584290115L;

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "fk_projects")
		private Project project;

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "fk_systems")
		private System system;

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			ProjectSystemId that = (ProjectSystemId) o;
			return Objects.equals(getProject(), that.getProject()) &&
					Objects.equals(getSystem(), that.getSystem());
		}

		@Override
		public int hashCode() {
			return Objects.hash(getProject(), getSystem());
		}
	}
}
