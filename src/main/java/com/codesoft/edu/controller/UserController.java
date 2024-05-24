package com.codesoft.edu.controller;

import com.codesoft.edu.model.Role;
import com.codesoft.edu.model.User;
import com.codesoft.edu.repository.RoleRepository;
import com.codesoft.edu.repository.UserRepository;
import com.codesoft.edu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {


    public UserRepository userRepository;

    public RoleRepository roleRepository;

    private RoleService roleService;
    @Autowired
    public UserController(UserRepository userRepository, RoleRepository roleRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
    }


    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        return "create-user";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute User user) {
        user.setRole(roleRepository.findAll().get(1));
        userRepository.save(user);
        return "redirect:/home";
    }

    @GetMapping("/{id}/read")
    public String readUser(@PathVariable long id, Model model) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            model.addAttribute("user", optionalUser.get());
            return "user-info";
        } else {
            return "error";
        }
    }

    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable long id, Model model) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            model.addAttribute("user", optionalUser.get());
            List<Role> roles = roleService.getAll();
            model.addAttribute("roles", roles);
            return "update-user";
        } else {
            return "error";
        }
    }

    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable long id, @ModelAttribute User updatedUser) {
        try {
            updatedUser.setId(id);
            userRepository.save(updatedUser);
            return "redirect:/home";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable long id) {
        userRepository.deleteById(id);
        return "redirect:/home";
    }

    @GetMapping("/all")
    public String getAllUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users-list";
    }
}