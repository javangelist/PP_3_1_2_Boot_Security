package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RegistrationService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RegistrationService registrationService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService, RegistrationService registrationService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.registrationService = registrationService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping()
    public String index(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin";
    }

    @GetMapping("/new")
    public String create(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "new";
    }

    @PostMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        registrationService.register(user);
        return "redirect: ";
    }

    @GetMapping("/edit")
    public String editUser(Model model, @RequestParam(value = "userId", required = false) Integer userId) {
        User user = userService.findById(userId).get();
        Set<Role> roles = user.getRoles();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "edit";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @ModelAttribute("roles") Set<Role> roles,
                         @RequestParam(value = "userId", required = false) Integer userId) {
        if(bindingResult.hasErrors()){
            return "edit";
        }
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.update(userId, user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String delete(Model model, @RequestParam(value = "userId", required = false) Integer userId) {
        userService.deleteById(userId);
        return "redirect:";
    }

}
