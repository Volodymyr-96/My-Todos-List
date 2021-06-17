package com.khalak.controllers;

import com.khalak.model.User;
import com.khalak.repository.UserRepository;
import com.khalak.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
    private final UserService userService;
    private UserRepository userRepository;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "home"})
    @PreAuthorize("hasAuthority('ADMIN')")
    public String home(Model model) {
        model.addAttribute("users", userService.getAll());
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

//    @PostMapping("/login")
//    public String postLogin(@Validated @ModelAttribute("user") User user, BindingResult result) {
//        if (result.hasErrors()) {
//            return "login";
//        }
//
//        User findUser = userRepository.findByEmail(user.getEmail());
//        return "redirect:/todos/all/users/" + findUser.getId();
//    }

}
