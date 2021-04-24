package com.equisoft.dca.backend.project.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import com.equisoft.dca.backend.project.converter.BackupIntervalConverter;
import com.equisoft.dca.backend.project.converter.BackupKeepIntervalConverter;
import com.equisoft.dca.backend.project.converter.CompilerConverter;
import com.equisoft.dca.backend.project.converter.GeneratedCodeLanguageConverter;
import com.equisoft.dca.backend.project.validator.ValidBackup;
import com.equisoft.dca.backend.project.validator.ValidCompiler;
import com.equisoft.dca.backend.project.validator.ValidProjectSyncSettings;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@ValidBackup(message = "{project.backup.valid}")
@ValidCompiler(message = "{project.compiler.valid}")
@Entity
@Table(name = "Projects")
public class Project implements Serializable {

	private static final long serialVersionUID = -7214939851255394406L;

	private static final int IDENTIFIER_MIN_SIZE = 3;
	private static final int IDENTIFIER_MAX_SIZE = 8;

	private static final int DESCRIPTION_MIN_SIZE = 1;
	private static final int DESCRIPTION_MAX_SIZE = 255;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PK_Projects")
	private Integer id;

	@NotBlank(message = "{identifier.notblank}")
	@Size(min = IDENTIFIER_MIN_SIZE, max = IDENTIFIER_MAX_SIZE, message = "{identifier.size}")
	@Pattern(regexp = "[a-zA-Z][a-zA-Z0-9-]*", message = "{identifier.pattern}")
	@Column(name = "Identifier")
	private String identifier;

	@NotBlank(message = "{description.notblank}")
	@Size(min = DESCRIPTION_MIN_SIZE, max = DESCRIPTION_MAX_SIZE, message = "{description.size}")
	@Column(name = "Description")
	private String description;

	@Convert(converter = GeneratedCodeLanguageConverter.class)
	@Column(name = "generatedcodelanguage")
	private GeneratedCodeLanguage generatedCodeLanguage;

	@Convert(converter = CompilerConverter.class)
	@Column(name = "Compiler")
	private Compiler compiler;

	@Column(name = "syncenabled")
	private Boolean isSynchronizationEnabled;

	@Column(name = "backupsenabled")
	private Boolean isBackupEnabled;

	@Convert(converter = BackupIntervalConverter.class)
	@Column(name = "backupinterval")
	private BackupInterval backupInterval;

	@Column(name = "numbackupintervals")
	private Integer numberOfBackupIntervals;

	@Convert(converter = BackupKeepIntervalConverter.class)
	@Column(name = "backupkeepinterval")
	private BackupKeepInterval backupKeepInterval;

	@Column(name = "numbackupkeepintervals")
	private Integer numberOfBackupKeepIntervals;

	@Column(name = "synchronizeprojectelementsenabled")
	private Boolean isSynchronizeProjectElementsEnabled;

	@Builder.Default
	@ValidProjectSyncSettings
	@OneToMany(mappedBy = "id.project", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<@Valid ProjectSyncSetting> projectSyncSettings = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "id.project", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<@Valid ProjectSystem> systems = new HashSet<>();

	@Column(name = "lastbackuptime")
	private LocalDateTime lastBackup;

	public static class ProjectBuilder {

		public ProjectBuilder identifier(String identifier) {
			this.identifier = StringUtils.toRootUpperCase(identifier);
			return this;
		}

		public ProjectBuilder description(String description) {
			this.description = StringUtils.normalizeSpace(description);
			return this;
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
		Project project = (Project) o;
		return Objects.equals(getId(), project.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
