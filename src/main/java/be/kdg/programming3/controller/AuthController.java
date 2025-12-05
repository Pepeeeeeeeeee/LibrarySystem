package be.kdg.programming3.controller;

import be.kdg.programming3.domain.enums.MembershipType;
import be.kdg.programming3.service.AuthService;
import be.kdg.programming3.service.MemberService;
import be.kdg.programming3.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
public class AuthController {
    private final UserService userService;
    private final MemberService memberService;
    private final AuthService authService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserService userService, AuthService authService, MemberService memberService) {
        this.userService = userService;
        this.authService = authService;
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, @RequestParam String password, Model model, HttpServletResponse response) {
        boolean valid = authService.validateLogin(username, password, response);

        if(valid) {
            return "redirect:/";
        }

        model.addAttribute("error", "Invalid username or password");
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@RequestParam String username, @RequestParam String password, @RequestParam String passwordConf, Model model, HttpServletResponse response) {
        boolean  valid = authService.checkRegisterDataValidity(username, password, passwordConf, response);
        if(valid) {
            return "redirect:/registerMember";
        }
        return "redirect:/register";
    }

    @GetMapping("/registerMember")
    public String showRegisterMember() {
        return "registerMember";
    }

    @PostMapping("/registerMember")
    public String handleRegisterMember(
            @RequestParam("mode") String mode,
            @RequestParam("email") String email,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "membershipType", required = false) MembershipType membershipType,
            HttpServletResponse response,
            HttpServletRequest request
    ) {

        if ("existing".equals(mode)) {
            var member = memberService.findByEmail(email);

            if (member == null) {
                return "redirect:/register?error=notfound";
            }

            System.out.println(member.getId());
            authService.handleRegister(response, request, member.getId());

            return "redirect:/books";
        }

        if ("new".equals(mode)) {
            memberService.save(firstName, lastName, LocalDate.now(), membershipType, email, phone, true);

            authService.handleRegister(response, request, memberService.findByEmail(email).getId());


            return "redirect:/books";
        }

        return "redirect:/register?error=invalid";
    }


    @GetMapping("/logout")
    public String handleLogout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return "/home";
    }
}
