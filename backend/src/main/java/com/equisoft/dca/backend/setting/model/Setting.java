package com.equisoft.dca.backend.setting.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.equisoft.dca.backend.language.converter.LanguageConverter;
import com.equisoft.dca.backend.language.model.Language;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "dcasettings")
public class Setting implements Serializable {

	private static final long serialVersionUID = 1048315686588334192L;

	private static final int PATH_MAX_SIZE = 256;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PK_Settings")
	private Integer id;

	@Convert(converter = LanguageConverter.class)
	@Column(name = "Language")
	@NotNull(message = "{language.notnull}")
	private Language language;

	@Column(name = "tokenduration")
	@NotNull(message = "{setting.tokenduration.notnull}")
	@Min(value = 20, message = "{setting.tokenduration.minmax}")
	@Max(value = 120, message = "{setting.tokenduration.minmax}")
	private Integer tokenDuration;

	@Column(name = "commonfolder")
	@NotBlank(message = "{setting.commonfolder.notblank}")
	@Size(max = PATH_MAX_SIZE, message = "{setting.commonfolder.size}")
	private String commonFolder;

	@Column(name = "defaultimportfolder")
	@NotBlank(message = "{setting.defaultimportfolder.notblank}")
	@Size(max = PATH_MAX_SIZE, message = "{setting.defaultimportfolder.size}")
	private String defaultImportFolder;

	@Column(name = "defaultexportfolder")
	@NotBlank(message = "{setting.defaultexportfolder.notblank}")
	@Size(max = PATH_MAX_SIZE, message = "{setting.defaultexportfolder.size}")
	private String defaultExportFolder;

	@Column(name = "defaultdownloadfolder")
	@NotBlank(message = "{setting.defaultdownloadfolder.notblank}")
	@Size(max = PATH_MAX_SIZE, message = "{setting.defaultdownloadfolder.size}")
	private String defaultDownloadFolder;

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
		Setting setting = (Setting) obj;
		return Objects.equals(id, setting.getId());
	}
}
