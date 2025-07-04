package net.javaguides.lms.controller;

import jakarta.servlet.http.HttpSession;
import net.javaguides.lms.entity.User;
import net.javaguides.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    // Simulate login by selecting a user from DB
    @GetMapping("/simulate-login")
    public String simulateLogin(HttpSession session) {
        // Use an existing user from your DB (change ID as needed)
        Long fakeUserId = 1L;

        User user = userRepository.findById(fakeUserId).orElse(null);

        if (user != null) {
            // Simulate "admin" or "user" role manually
            if (user.getName().equalsIgnoreCase("admin")) {
                user.setRole("ADMIN");
            } else {
                user.setRole("USER");
            }
            session.setAttribute("loggedInUser", user);
        }

        return "redirect:/ui/books";
    }
}
