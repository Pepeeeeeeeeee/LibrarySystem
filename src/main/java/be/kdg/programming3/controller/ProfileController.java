package be.kdg.programming3.controller;

import be.kdg.programming3.domain.Member;
import be.kdg.programming3.domain.User;
import be.kdg.programming3.dto.MemberDTO;
import be.kdg.programming3.dto.ProfileFormDTO;
import be.kdg.programming3.mapper.MemberMapper;
import be.kdg.programming3.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final MemberService memberService;
    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);
    private final UserService userService;
    private final AuthService authService;

    public ProfileController(MemberService memberService, UserService userService, AuthService authService) {
        this.memberService = memberService;
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping
    public String showProfile(Model model, HttpServletRequest request, HttpServletResponse response) {
        User user = userService.getUserByToken(authService.getTokensFromCookies(request, response).getValue());

        Member member = memberService.findById(user.getMemberId());

        ProfileFormDTO form = ProfileFormDTO.toProfileForm(member, user);

        model.addAttribute("profile", form);
        model.addAttribute("currentUser", user);
        model.addAttribute("userPhotoUrl", member.getProfileImageUrl());

        model.addAttribute("myCheckouts", memberService.findCheckoutsByMemberId(member.getId()));

        return "profile";
    }

    @GetMapping("/{id}")
    public String showPublicProfile(@PathVariable int id ,Model model){
        Member member = memberService.findById(id);
        User user = userService.getUserByMemberId(member.getId());
        ProfileFormDTO form = ProfileFormDTO.toProfileForm(member, user);

        model.addAttribute("profile", form);
        model.addAttribute("membershipDate", member.getMembershipDate());
        model.addAttribute("membershipType", member.getMembershipType());
        model.addAttribute("currentUser", user);
        model.addAttribute("userPhotoUrl", member.getProfileImageUrl());
        model.addAttribute("myCheckouts", memberService.findCheckoutsByMemberId(member.getId()));

        return "publicProfile";
    }

    @PostMapping("/update")
    public String updateUserData(@ModelAttribute("profile") ProfileFormDTO dto, Model model, HttpServletRequest request, HttpServletResponse response){
        User user = userService.getUserByToken(authService.getTokensFromCookies(request, response).getValue());
        Member member = memberService.findById(user.getMemberId());

        MemberDTO memberDTO = MemberMapper.toDTO(member);
        memberDTO.setFirstName(dto.getFirstName());
        memberDTO.setLastName(dto.getLastName());
        memberDTO.setEmail(dto.getEmail());
        memberDTO.setPhoneNumber(dto.getPhoneNumber());

        memberService.update(MemberMapper.toEntity(memberDTO));
        userService.updateUsername(user.getId(), dto.getUsername());

        return "redirect:/profile";
    }
}
