package br.com.rafaelvieira.shopbeer.domain.enums;

import br.com.rafaelvieira.shopbeer.repository.UserEmployeeRepository;

public enum StatusUserEmployee {

    ACTIVATE {
        @Override
        public void toExecute(Long[] code, UserEmployeeRepository userEmployeeRepository) {
            userEmployeeRepository.findByCodeIn(code).forEach(u -> u.setActive(true));
        }
    },

    DISABLE {
        @Override
        public void toExecute(Long[] code, UserEmployeeRepository userEmployeeRepository) {
            userEmployeeRepository.findByCodeIn(code).forEach(u -> u.setActive(false));
        }
    };

    public abstract void toExecute(Long[] code, UserEmployeeRepository userEmployeeRepository);
}
