package br.com.rafaelvieira.shopbeer.service;

import java.util.Optional;

import br.com.rafaelvieira.shopbeer.domain.UserEmployee;
import br.com.rafaelvieira.shopbeer.domain.enums.StatusUserEmployee;
import br.com.rafaelvieira.shopbeer.repository.UserEmployeeRepository;
import br.com.rafaelvieira.shopbeer.service.exception.EmailUserAlreadyRegisteredException;
import br.com.rafaelvieira.shopbeer.service.exception.PasswordRequiredUserException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UserEmployeeService {

    private final UserEmployeeRepository userEmployeeRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEmployeeService(UserEmployeeRepository userEmployeeRepository, PasswordEncoder passwordEncoder) {
        this.userEmployeeRepository = userEmployeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void save(UserEmployee userEmployee) {
        Optional<UserEmployee> existingUser = userEmployeeRepository.findByEmail(userEmployee.getEmail());
        if (existingUser.isPresent() && !existingUser.get().equals(userEmployee)) {
            throw new EmailUserAlreadyRegisteredException("E-mail already registered");
        }

        if (userEmployee.isNew() && StringUtils.hasText(userEmployee.getPassword())) {
            throw new PasswordRequiredUserException("Password is mandatory for new user");
        }

        if (userEmployee.isNew() || !StringUtils.hasText(userEmployee.getPassword())) {
            userEmployee.setPassword(this.passwordEncoder.encode(userEmployee.getPassword()));
        } else if (StringUtils.hasText(userEmployee.getPassword())) {
            userEmployee.setPassword(existingUser.orElseThrow().getPassword());
        }
        userEmployee.setConfirmPassword(userEmployee.getPassword());

        if (!userEmployee.isNew() && userEmployee.getActive() == null) {
            userEmployee.setActive(existingUser.orElseThrow().getActive());
        }

        userEmployeeRepository.save(userEmployee);
    }

    @Transactional
    public void changeStatus(Long[] code, StatusUserEmployee statusUserEmployee) {
        statusUserEmployee.toExecute(code, userEmployeeRepository);
    }
}
