package com.equisoft.dca.backend.dataobject.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import com.equisoft.dca.backend.dataobject.converter.DataObjectStatusConverter;
import com.equisoft.dca.backend.dataobject.converter.DataObjectTypeConverter;
import com.equisoft.dca.backend.system.model.System;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name = "dataobjects")
public class DataObject implements Serializable {

	private static final long serialVersionUID = -8086450720855681415L;

	private static final int IDENTIFIER_MIN_SIZE = 6;
	private static final int IDENTIFIER_MAX_SIZE = 8;

	private static final int DESCRIPTION_MIN_SIZE = 1;
	private static final int DESCRIPTION_MAX_SIZE = 40;

	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pk_dataobjects")
	private Integer id;

	@Setter
	@Column(name = "Identifier")
	@NotBlank(message = "{identifier.notblank}")
	@Size(min = IDENTIFIER_MIN_SIZE, max = IDENTIFIER_MAX_SIZE, message = "{identifier.size}")
	@Pattern(regexp = "[a-zA-Z][a-zA-Z0-9-]*", message = "{identifier.pattern}")
	private String identifier;

	@Setter
	@Column(name = "Description")
	@NotBlank(message = "{description.notblank}")
	@Size(min = DESCRIPTION_MIN_SIZE, max = DESCRIPTION_MAX_SIZE, message = "{description.size}")
	private String description;

	@Setter
	@Convert(converter = DataObjectStatusConverter.class)
	@Column(name = "status")
	@NotNull
	private DataObjectStatus status;

	@Convert(converter = DataObjectTypeConverter.class)
	@Column(name = "Type")
	@NotNull
	private DataObjectType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_systems")
	@NotNull(message = "{system.notnull}")
	private System system;

	@Builder.Default
	@OneToMany(mappedBy = "dataObject", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DataObjectFile> dataObjectFiles = new ArrayList<>();

	public static class DataObjectBuilder {

		public DataObjectBuilder identifier(String identifier) {
			this.identifier = StringUtils.toRootUpperCase(identifier);
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
		DataObject that = (DataObject) o;
		return Objects.equals(getId(), that.getId()) &&
				Objects.equals(getIdentifier(), that.getIdentifier());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getIdentifier());
	}
}
