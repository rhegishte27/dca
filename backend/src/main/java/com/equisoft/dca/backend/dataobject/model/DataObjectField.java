package com.equisoft.dca.backend.dataobject.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.equisoft.dca.backend.dataobject.converter.DataObjectStatusConverter;
import com.equisoft.dca.backend.dataobject.converter.FieldDataTypeConverter;
import com.equisoft.dca.backend.dataobject.converter.FieldDdlTypeConverter;

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
@Table(name = "dataobjectfields")
public class DataObjectField implements Serializable {

	private static final long serialVersionUID = 3751769603634963702L;

	private static final int NAME_MIN_SIZE = 1;
	private static final int NAME_MAX_SIZE = 32;

	private static final int RAWDECLARATION_MAX_SIZE = 256;

	private static final int REDEFINES_MAX_SIZE = 32;

	private static final int ORIGINALNAME_MAX_SIZE = 32;

	private static final int OCCURS_MAX_SIZE = 32;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pk_dataobjectfields")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "fk_dataobjects")
	@NotNull
	private DataObject dataObject;

	@Column(name = "name")
	@NotBlank(message = "{dataobjectfield.name.notblank}")
	@Size(min = NAME_MIN_SIZE, max = NAME_MAX_SIZE, message = "{dataobjectfield.name.size}")
	private String name;

	@Column(name = "ordernumber")
	@NotNull
	private Integer orderNumber;

	@Convert(converter = FieldDataTypeConverter.class)
	@Column(name = "datatype")
	@NotNull
	private FieldDataType dataType;

	@Column(name = "rawdeclaration")
	@Size(max = RAWDECLARATION_MAX_SIZE, message = "{dataobjectfield.rawdeclaration.size}")
	private String rawDeclaration;

	@Convert(converter = FieldDdlTypeConverter.class)
	@Column(name = "ddltype")
	@NotNull
	private FieldDdlType ddlType;

	@Column(name = "level")
	@NotNull
	private Integer level;

	@Column(name = "redefines")
	@Size(max = REDEFINES_MAX_SIZE, message = "{dataobjectfield.redefines.size}")
	private String redefines;

	@Column(name = "size")
	private Integer size;

	@Setter
	@Column(name = "originalname")
	@Size(max = ORIGINALNAME_MAX_SIZE, message = "{dataobjectfield.originalname.size}")
	private String originalName;

	@Setter
	@Column(name = "value")
	private String value;

	@Column(name = "occurs")
	@Size(max = OCCURS_MAX_SIZE, message = "{dataobjectfield.occurs.size}")
	private String occurs;

	@Setter
	@Column(name = "message")
	private String message;

	@Setter
	@Column(name = "status")
	@Convert(converter = DataObjectStatusConverter.class)
	private DataObjectStatus status;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DataObjectField that = (DataObjectField) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
