package br.com.rafaelvieira.shopbeer.security;

import br.com.rafaelvieira.shopbeer.domain.UserEmployee;
import br.com.rafaelvieira.shopbeer.repository.UserEmployeeRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

public class AppUserEmployeeDetailsService implements UserDetailsService {

    private final UserEmployeeRepository userEmployeeRepository;

    public AppUserEmployeeDetailsService(UserEmployeeRepository userEmployeeRepository) {
        this.userEmployeeRepository = userEmployeeRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEmployee> userEmployeeOptional = userEmployeeRepository.perEmailActive(email);
        UserEmployee userEmployee = userEmployeeOptional.orElseThrow(() -> new UsernameNotFoundException("Incorrect username and/or password"));
        return new UserEmployeeSystem(userEmployee, getPermissoes(userEmployee));
    }

    private Collection<? extends GrantedAuthority> getPermissoes(UserEmployee userEmployee) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        List<String> permissoes = userEmployeeRepository.permissions(userEmployee);
        permissoes.forEach(p -> authorities.add(new SimpleGrantedAuthority(p.toUpperCase())));

        return authorities;
    }
}

