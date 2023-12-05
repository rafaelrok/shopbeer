package br.com.rafaelvieira.shopbeer.repository.query.userEmployee;

import java.util.List;
import java.util.Optional;

import br.com.rafaelvieira.shopbeer.domain.UserEmployee;
import br.com.rafaelvieira.shopbeer.repository.filter.UserEmployeeFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserEmployeesQuery {

	public Optional<UserEmployee> perEmailActive(String email);
	
	public List<String> permissions(UserEmployee userEmployee);
	
	public Page<UserEmployee> filtered(UserEmployeeFilter filter, Pageable pageable);
	
	public UserEmployee searchWithGroups(Long code);
	
}
