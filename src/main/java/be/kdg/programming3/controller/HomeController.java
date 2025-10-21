package be.kdg.programming3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        // Simulate a logged-in user
        model.addAttribute("userLoggedIn", false); // change to true to test logged-in state
        model.addAttribute("userPhotoUrl", "/images/default-profile.png");
        return "home"; // your home.html page
    }

//    @GetMapping("/books")
//    public String books(Model model) {
//        // Add the simulated user info for pages with the navbar
//        model.addAttribute("userLoggedIn", false);
//        model.addAttribute("userPhotoUrl", "/images/default-profile.png");
//        return "books";
//    }
//
//    @GetMapping("/members")
//    public String members(Model model) {
//        model.addAttribute("userLoggedIn", false);
//        model.addAttribute("userPhotoUrl", "/images/default-profile.png");
//        return "members";
//    }
//
//    // Repeat for other pages where navbar is used
}
