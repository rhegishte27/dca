package com.equisoft.dca.backend.user.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
@Table(name = "Organizations")
public class Organization implements Serializable {

	private static final long serialVersionUID = 5777344027766622799L;

	private static final int NAME_SIZE_MIN = 1;
	private static final int NAME_SIZE_MAX = 50;

	private static final int DESCRIPTION_SIZE_MIN = 1;
	private static final int DESCRIPTION_SIZE_MAX = 255;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PK_Organizations")
	private Integer id;

	@Setter
	@Column(name = "Name")
	@NotBlank(message = "{name.notblank}")
	@Size(min = NAME_SIZE_MIN, max = NAME_SIZE_MAX, message = "{name.size}")
	private String name;

	@Setter
	@Column(name = "Description")
	@NotBlank(message = "{description.notblank}")
	@Size(min = DESCRIPTION_SIZE_MIN, max = DESCRIPTION_SIZE_MAX, message = "{description.size}")
	private String description;

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Organization organization = (Organization) obj;
		return Objects.equals(id, organization.getId()) &&
				Objects.equals(name, organization.getName());
	}
}
