package br.com.rafaelvieira.shopbeer.domain;

import br.com.rafaelvieira.shopbeer.validation.AttributeConfirmation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AttributeConfirmation(attribute = "password", attributeConfirmation = "confirmPassword"
				, message = "Password confirmation does not match")
@Entity
@Table(name = "user")
@DynamicUpdate
public class UserEmployee implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long code;

	@NotBlank(message = "Name is required")
	private String name;

	@NotBlank(message = "E-mail is required")
	@Email(message = "E-mail invalid")
	@Column(unique = true)
	private String email;

	private String password;
	
	@Transient
	private String confirmPassword;

	private Boolean active;

	@Size(min = 1, message = "Select at least one group")
	@ManyToMany
	@JoinTable(name = "user_group", joinColumns = @JoinColumn(name = "code_user")
				, inverseJoinColumns = @JoinColumn(name = "code_group"))
	@ToString.Exclude
	private List<Group> groups;

	@Column(name = "birth_date")
	private LocalDate birthDate;

	@PreUpdate
	private void preUpdate() {
		this.confirmPassword = password;
	}
	
	public boolean isNew() {
		return code == null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserEmployee other = (UserEmployee) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}
