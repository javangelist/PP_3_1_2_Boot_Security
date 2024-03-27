package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/index")
public class MainController {
    private final UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String index(Model model, @RequestParam(value = "userId", required = false) Integer userId) {
        if (userId != null) {
            model.addAttribute("user", userService.findById(userId).get());
            return "user";
        }
        model.addAttribute("users", userService.findAll());
        return "index";
    }
}
