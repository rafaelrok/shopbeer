package br.com.rafaelvieira.shopbeer.security;

import br.com.rafaelvieira.shopbeer.domain.UserEmployee;
import br.com.rafaelvieira.shopbeer.repository.query.userEmployee.UserEmployeesQuery;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AppUserEmployeeDetailsService implements UserDetailsService {

    private final UserEmployeesQuery userEmployeesQuery;

    public AppUserEmployeeDetailsService(UserEmployeesQuery userEmployeesQuery) {
        this.userEmployeesQuery = userEmployeesQuery;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEmployee> userEmployeeOptional = userEmployeesQuery.perEmailActive(email);
        UserEmployee userEmployee = userEmployeeOptional.orElseThrow(() -> new UsernameNotFoundException("Incorrect username and/or password"));
        return new UserEmployeeSystem(userEmployee, getPermissions(userEmployee));
    }

    private Collection<? extends GrantedAuthority> getPermissions(UserEmployee userEmployee) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        List<String> permissions = userEmployeesQuery.permissions(userEmployee);
        permissions.forEach(p -> authorities.add(new SimpleGrantedAuthority(p.toUpperCase())));

        return authorities;
    }
}

