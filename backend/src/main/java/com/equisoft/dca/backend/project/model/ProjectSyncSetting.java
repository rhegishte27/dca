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
import com.equisoft.dca.backend.project.converter.TypeProjectElementConverter;
import com.equisoft.dca.backend.project.validator.ValidProjectSyncSetting;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@ValidProjectSyncSetting(message = "{projectsyncsetting.location.valid}")
@Entity
@Table(name = "projectsyncsettings")
public class ProjectSyncSetting implements Serializable {
	private static final long serialVersionUID = 7133043107352122230L;

	@Builder.Default
	@EmbeddedId
	private ProjectSyncSettingId id = new ProjectSyncSettingId();

	@Column(name = "syncenabled")
	private Boolean isSyncEnabled;

	@ManyToOne
	@JoinColumn(name = "fk_locations")
	private Location location;

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@Setter
	@Embeddable
	public static class ProjectSyncSettingId implements Serializable {

		private static final long serialVersionUID = -8625718626124634908L;

		@Setter
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "fk_projects")
		private Project project;

		@Convert(converter = TypeProjectElementConverter.class)
		@Column(name = "typeelement")
		@NotNull
		private TypeProjectElement typeProjectElement;

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			ProjectSyncSettingId that = (ProjectSyncSettingId) o;
			return Objects.equals(getProject(), that.getProject()) &&
					getTypeProjectElement() == that.getTypeProjectElement();
		}

		@Override
		public int hashCode() {
			return Objects.hash(getProject(), getTypeProjectElement());
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ProjectSyncSetting that = (ProjectSyncSetting) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
