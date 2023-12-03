package br.com.rafaelvieira.shopbeer.domain;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Embeddable
public class UserGroupId implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "code_user")
	private UserEmployee userEmployee;
	
	@ManyToOne
	@JoinColumn(name = "code_group")
	private Group group;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((userEmployee == null) ? 0 : userEmployee.hashCode());
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
		UserGroupId other = (UserGroupId) obj;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (userEmployee == null) {
			if (other.userEmployee != null)
				return false;
		} else if (!userEmployee.equals(other.userEmployee))
			return false;
		return true;
	}
	
}
