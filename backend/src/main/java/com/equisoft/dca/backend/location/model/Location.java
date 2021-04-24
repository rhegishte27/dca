package com.equisoft.dca.backend.location.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.equisoft.dca.backend.location.converter.LocationTypeConverter;
import com.equisoft.dca.backend.location.converter.PlatformTypeConverter;
import com.equisoft.dca.backend.location.validator.ValidLocationType;

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
@ValidLocationType(message = "{location.locationtype.valid}")
@Entity
@Table(name = "Locations")
public class Location implements Serializable {

	private static final long serialVersionUID = -114699998881686202L;

	static final int IDENTIFIER_MIN_SIZE = 1;
	static final int IDENTIFIER_MAX_SIZE = 50;

	static final int PATH_MIN_SIZE = 1;
	static final int PATH_MAX_SIZE = 100;

	public static final int SERVERNAME_MIN_SIZE = 1;
	public static final int SERVERNAME_MAX_SIZE = 100;

	public static final int USERNAME_MIN_SIZE = 1;
	public static final int USERNAME_MAX_SIZE = 50;

	public static final int PASSWORD_MIN_SIZE = 1;
	public static final int PASSWORD_MAX_SIZE = 50;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PK_Locations")
	private Integer id;

	@Setter
	@NotBlank(message = "{identifier.notblank}")
	@Size(min = IDENTIFIER_MIN_SIZE, max = IDENTIFIER_MAX_SIZE, message = "{identifier.size}")
	@Column(name = "Identifier")
	private String identifier;

	@Convert(converter = LocationTypeConverter.class)
	@Column(name = "Type")
	private LocationType locationType;

	@NotBlank(message = "{location.path.notblank}")
	@Size(min = PATH_MIN_SIZE, max = PATH_MAX_SIZE, message = "{location.path.size}")
	@Column(name = "Path")
	private String path;

	@Column(name = "servername")
	private String serverName;

	@Column(name = "username")
	private String userName;

	@Column(name = "Password")
	private String password;

	@Convert(converter = PlatformTypeConverter.class)
	@Column(name = "platformtype")
	private PlatformType platformType;

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
		Location location = (Location) obj;
		return Objects.equals(id, location.getId()) &&
				Objects.equals(identifier, location.getIdentifier());
	}
}
