package be.kdg.programming3.controller;

import be.kdg.programming3.domain.User;
import be.kdg.programming3.domain.enums.Flags;
import be.kdg.programming3.service.EntityFactoryService;
import be.kdg.programming3.service.FilteringService;
import be.kdg.programming3.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final UserService userService;
    private final EntityFactoryService entityFactoryService;
    private final FilteringService filteringService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserService userService, EntityFactoryService entityFactoryService, FilteringService filteringService) {
        this.userService = userService;
        this.entityFactoryService = entityFactoryService;
        this.filteringService = filteringService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, @RequestParam String password, Model model) {
        User user = userService.getAllUsers().stream().filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password)).findFirst().orElse(null);

        if(user != null) {
            return "redirect:/";
        }

        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@RequestParam String username, @RequestParam String email, @RequestParam String password, Model model) {
        if (userService.getAllUsers().stream().anyMatch(u -> u.getUsername().equals(username) && u.getEmail().equals(email))) {
            model.addAttribute("error", "Username and or email already exists");
            return "register";
        }

        userService.addUser(new User(userService.getAllUsers().getLast().getId() + 1, username, email, password, Flags.USER));
        return "redirect:/";
    }
}
