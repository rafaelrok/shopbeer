package br.com.rafaelvieira.shopbeer.controller;

import br.com.rafaelvieira.shopbeer.controller.page.PageWrapper;
import br.com.rafaelvieira.shopbeer.domain.UserEmployee;
import br.com.rafaelvieira.shopbeer.domain.enums.StatusUserEmployee;
import br.com.rafaelvieira.shopbeer.repository.GroupsRepository;
import br.com.rafaelvieira.shopbeer.repository.UserEmployeeRepository;
import br.com.rafaelvieira.shopbeer.repository.filter.UserEmployeeFilter;
import br.com.rafaelvieira.shopbeer.repository.query.userEmployee.UserEmployeesQuery;
import br.com.rafaelvieira.shopbeer.service.UserEmployeeService;
import br.com.rafaelvieira.shopbeer.service.exception.EmailUserAlreadyRegisteredException;
import br.com.rafaelvieira.shopbeer.service.exception.PasswordRequiredUserException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserEmployeeController {

    private final UserEmployeeService userEmployeeService;
    private final GroupsRepository groupRepository;
    private final UserEmployeesQuery userEmployeesQuery;

    public UserEmployeeController(UserEmployeeService userEmployeeService, GroupsRepository groupRepository, UserEmployeesQuery userEmployeesQuery) {
        this.userEmployeeService = userEmployeeService;
        this.groupRepository = groupRepository;
        this.userEmployeesQuery = userEmployeesQuery;
    }

    @RequestMapping("/new")
    public ModelAndView newUserEmployee(UserEmployee userEmployee) {
        ModelAndView mv = new ModelAndView("user/register-user");
        mv.addObject("groupsEmployee", groupRepository.findAll());
        return mv;
    }

    @PostMapping({ "/new", "/{d}" })
    public ModelAndView save(@Valid @PathVariable("d") UserEmployee userEmployee, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return newUserEmployee(userEmployee);
        }

        try {
            userEmployeeService.insert(userEmployee);
        } catch (EmailUserAlreadyRegisteredException e) {
            result.rejectValue("email", e.getMessage(), e.getMessage());
            return newUserEmployee(userEmployee);
        } catch (PasswordRequiredUserException e) {
            result.rejectValue("password", e.getMessage(), e.getMessage());
            return newUserEmployee(userEmployee);
        }

        attributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso");
        return new ModelAndView("redirect:/users/new");
    }

    @GetMapping
    public ModelAndView search(UserEmployeeFilter userEmployeeFilter
            , @PageableDefault(size = 3) Pageable pageable, HttpServletRequest httpServletRequest) {
        ModelAndView mv = new ModelAndView("user/search-user");
        mv.addObject("groups", groupRepository.findAll());

        PageWrapper<UserEmployee> pageWrapper = new PageWrapper<>(userEmployeesQuery.filtered(userEmployeeFilter, pageable)
                , httpServletRequest);
        mv.addObject("page", pageWrapper);
        return mv;
    }

    @PutMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public void updateStatus(@RequestParam("code[]") Long[] code, @RequestParam("status") StatusUserEmployee statusUserEmployee) {
        userEmployeeService.changeStatus(code, statusUserEmployee);
    }

    @GetMapping("/{code}")
    public ModelAndView edit(@PathVariable Long code) {
        UserEmployee userEmployee = userEmployeesQuery.searchWithGroups(code);
        ModelAndView mv = newUserEmployee(userEmployee);
        mv.addObject(userEmployee);
        return mv;
    }
}
