package com.equisoft.dca.backend.system.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.SerializationUtils;

import com.equisoft.dca.backend.dataobject.model.DataObject;

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
@Table(name = "Systems")
public class System implements Serializable {

	private static final long serialVersionUID = 7545761495238605814L;

	private static final int IDENTIFIER_MIN_SIZE = 3;
	private static final int IDENTIFIER_MAX_SIZE = 8;

	private static final int DESCRIPTION_MIN_SIZE = 1;
	private static final int DESCRIPTION_MAX_SIZE = 255;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PK_Systems")
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

	@Column(name = "datadictionary")
	private byte[] dataDictionary;

	@Builder.Default
	@OneToMany(mappedBy = "system", cascade = CascadeType.REMOVE)
	private Set<DataObject> dataObjects = new HashSet<>();

	public byte[] getDataDictionary() {
		return SerializationUtils.clone(dataDictionary);
	}

	public void setDataDictionary(byte[] dataDictionary) {
		this.dataDictionary = SerializationUtils.clone(dataDictionary);
	}

	public static class SystemBuilder {

		public SystemBuilder dataDictionary(byte[] dataDictionary) {
			this.dataDictionary = SerializationUtils.clone(dataDictionary);
			return this;
		}
	}

	@Setter
	@Column(name = "datetimeddupdated")
	private LocalDateTime dataDictionaryDateTimeUpdated;

	@Override
	public int hashCode() {
		return Objects.hash(id, identifier);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		System system = (System) obj;
		return Objects.equals(id, system.getId()) &&
				Objects.equals(identifier, system.getIdentifier());
	}
}
