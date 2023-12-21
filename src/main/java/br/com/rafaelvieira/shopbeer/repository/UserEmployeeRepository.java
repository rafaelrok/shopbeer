package br.com.rafaelvieira.shopbeer.repository;

import br.com.rafaelvieira.shopbeer.domain.UserEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserEmployeeRepository extends JpaRepository<UserEmployee, Long> {

    public Optional<UserEmployee> findByEmail(String email);

    public List<UserEmployee> findByCodeIn(Long[] codes);

}
