package net.javaguides.lms.controller;

import jakarta.servlet.http.HttpSession;
import net.javaguides.lms.entity.User;
import net.javaguides.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        user.setRole("USER"); // default role
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User formUser, HttpSession session, Model model) {
        User user = userRepository.findByUsername(formUser.getUsername());
        if (user != null && user.getPassword().equals(formUser.getPassword())) {
            session.setAttribute("loggedInUser", user);
            if ("ADMIN".equals(user.getRole())) {
                return "redirect:/admin/books";
            } else {
                return "redirect:/user/books";
            }
        }
        model.addAttribute("error", "Invalid credentials");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
