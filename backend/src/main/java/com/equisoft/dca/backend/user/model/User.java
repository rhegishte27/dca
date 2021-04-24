package com.equisoft.dca.backend.user.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.equisoft.dca.backend.common.OnCreate;
import com.equisoft.dca.backend.common.OnUpdate;
import com.equisoft.dca.backend.language.converter.LanguageConverter;
import com.equisoft.dca.backend.language.model.Language;
import com.equisoft.dca.backend.user.converter.FeatureConverter;
import com.equisoft.dca.backend.user.converter.RoleConverter;
import com.equisoft.dca.backend.user.validator.ConstraintPassword;
import com.equisoft.dca.backend.user.validator.ConstraintPasswordBlankAllowed;
import com.equisoft.dca.backend.user.validator.ValidRoleFeature;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@ValidRoleFeature(message = "{user.rolefeatures.valid}")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name = "Users")
public class User implements Serializable {

	private static final long serialVersionUID = -2724268648588020198L;

	private static final int IDENTIFIER_MIN_SIZE = 6;
	private static final int IDENTIFIER_MAX_SIZE = 8;

	private static final int USER_NAME_MIN_SIZE = 1;
	private static final int USER_NAME_MAX_SIZE = 32;

	private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=*()\"',./])(?=\\S+$).{8,}$";
	private static final int PASSWORD_MIN_SIZE = 8;
	private static final int PASSWORD_MAX_SIZE = 20;

	private static final int PASSWORD_HASH_MAX_SIZE = 255;

	public static final String ADMIN_IDENTIFIER = "Admin";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PK_Users")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "Fk_Organizations")
	@NotNull(message = "{user.organization.notnull}")
	private Organization organization;

	@Convert(converter = RoleConverter.class)
	@Column(name = "role")
	@NotNull(message = "{user.role.notnull}")
	private Role role;

	@Column(name = "Identifier")
	@NotBlank(message = "{identifier.notblank}")
	@Size(min = IDENTIFIER_MIN_SIZE, max = IDENTIFIER_MAX_SIZE, message = "{identifier.size}")
	@Pattern(regexp = "[a-zA-Z][a-zA-Z0-9-]*", message = "{identifier.pattern}")
	private String identifier;

	@Column(name = "username")
	@NotBlank(message = "{name.notblank}")
	@Size(min = USER_NAME_MIN_SIZE, max = USER_NAME_MAX_SIZE, message = "{name.size}")
	private String name;

	@Transient
	@ConstraintPassword(groups = OnCreate.class, minSize = PASSWORD_MIN_SIZE, maxSize = PASSWORD_MAX_SIZE, regex = PASSWORD_PATTERN,
			message = "{user.password.constraint}")
	@ConstraintPasswordBlankAllowed(groups = OnUpdate.class, minSize = PASSWORD_MIN_SIZE, maxSize = PASSWORD_MAX_SIZE, regex = PASSWORD_PATTERN,
			message = "{user.password.constraint}")
	@EqualsAndHashCode.Exclude
	private String password;

	@Column(name = "pwdhash")
	private String passwordHash;

	@Column(name = "emailaddress")
	@NotBlank(message = "{user.email.notblank}")
	@Email(message = "{user.email.email}")
	private String emailAddress;

	@Convert(converter = LanguageConverter.class)
	@Column(name = "Language")
	private Language language;

	@ElementCollection(targetClass = Feature.class)
	@Convert(converter = FeatureConverter.class)
	@JoinTable(name = "usersfeatures", joinColumns = @JoinColumn(name = "FK_Users"))
	@Column(name = "feature")
	private Set<Feature> features;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof User)) {
			return false;
		}
		User user = (User) o;
		return Objects.equals(getId(), user.getId()) &&
				Objects.equals(getIdentifier(), user.getIdentifier());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getIdentifier());
	}
}
