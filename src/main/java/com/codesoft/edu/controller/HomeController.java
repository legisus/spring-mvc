package com.codesoft.edu.controller;

import com.codesoft.edu.model.User;
import com.codesoft.edu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    public UserRepository userRepository;

    @Autowired
    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        List<User> list = userRepository.findAll();
        model.addAttribute("users", list);
        return "home"; //
    }
}
