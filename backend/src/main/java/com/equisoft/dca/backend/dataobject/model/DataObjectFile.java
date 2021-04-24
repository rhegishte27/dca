package com.equisoft.dca.backend.dataobject.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.equisoft.dca.backend.dataobject.converter.DataObjectStatusConverter;
import com.equisoft.dca.backend.user.model.User;

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
@Table(name = "dataobjectfiles")
public class DataObjectFile implements Serializable {

	private static final long serialVersionUID = 4667146243643064755L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pk_dataobjectfiles")
	private Integer id;

	@Column(name = "status")
	@Convert(converter = DataObjectStatusConverter.class)
	@Setter
	@NotNull
	private DataObjectStatus status;

	@Column(name = "originalfilename")
	@NotNull(message = "{dataobject.originalFileName.notnull}")
	private String originalFileName;

	@Setter
	@Column(name = "dataobjectcontent")
	@NotBlank(message = "{dataobject.content.notempty}")
	private String dataObjectContent;

	@Setter
	@Column(name = "resultcontent")
	@NotNull
	private String resultContent;

	@Column(name = "datetimeuploaded")
	@Builder.Default
	@NotNull
	private LocalDateTime dateTimeUploaded = LocalDateTime.now();

	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_users")
	@NotNull(message = "{user.notnull}")
	private User user;

	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_dataobjects")
	private DataObject dataObject;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DataObjectFile that = (DataObjectFile) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
