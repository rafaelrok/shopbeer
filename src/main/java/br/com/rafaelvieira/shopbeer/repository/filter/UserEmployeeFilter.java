package br.com.rafaelvieira.shopbeer.repository.filter;

import br.com.rafaelvieira.shopbeer.domain.Group;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserEmployeeFilter {
    private String name;
    private String email;
    private List<Group> groups;

}
