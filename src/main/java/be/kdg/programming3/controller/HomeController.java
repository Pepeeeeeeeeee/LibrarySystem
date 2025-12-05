package be.kdg.programming3.controller;

import be.kdg.programming3.domain.User;
import be.kdg.programming3.service.AuthService;
import be.kdg.programming3.service.MemberService;
import be.kdg.programming3.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final AuthService authService;
    private final UserService userService;
    private final MemberService memberService;

    public HomeController(AuthService authService, UserService userService, MemberService memberService) {
        this.authService = authService;
        this.userService = userService;
        this.memberService = memberService;
    }

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request, HttpServletResponse response) {
        User user = userService.getUserByToken(authService.getTokensFromCookies(request, response).getValue());

        model.addAttribute("userLoggedIn", authService.checkIsAuth(request) ? user : null);
        model.addAttribute("userPhotoUrl", authService.checkIsAuth(request) ? memberService.findById(user.getId()).getProfileImageUrl() : null);
        return "home";
    }

}
