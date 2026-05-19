package be.kdg.librarysystem.controller;

import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.domain.User;
import be.kdg.librarysystem.dto.MemberDTO;
import be.kdg.librarysystem.dto.ProfileFormDTO;
import be.kdg.librarysystem.mapper.MemberMapper;
import be.kdg.librarysystem.security.CustomUserDetails;
import be.kdg.librarysystem.service.MemberService;
import be.kdg.librarysystem.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final MemberService memberService;
    private final UserService userService;

    public ProfileController(
            MemberService memberService,
            UserService userService
    ) {
        this.memberService = memberService;
        this.userService = userService;
    }

    @GetMapping
    public String showProfile(
            Model model,
            @AuthenticationPrincipal
            CustomUserDetails userDetails
    ) {

        User user = userDetails.getDomainUser();

        Member member = user.getMember();

        ProfileFormDTO form = ProfileFormDTO.toProfileForm(member, user);

        model.addAttribute("profile", form);

        model.addAttribute(
                "myCheckouts",
                memberService.findCheckoutDtosByMemberId(
                        member.getId()
                )
        );

        return "profile";
    }

    @GetMapping("/{id}")
    public String showPublicProfile(
            @PathVariable int id,
            Model model
    ) {

        Member member = memberService.findById(id);

        if (member == null) {
            return "redirect:/members";
        }

        User user =
                userService.findByMemberId(member.getId());

        ProfileFormDTO form =
                ProfileFormDTO.toProfileForm(member, user);

        model.addAttribute("profile", form);

        model.addAttribute(
                "membershipDate",
                member.getMembershipDate()
        );

        model.addAttribute(
                "membershipType",
                member.getMembershipType()
        );

        model.addAttribute(
                "myCheckouts",
                memberService.findCheckoutDtosByMemberId(
                        member.getId()
                )
        );

        return "publicProfile";
    }

    @PostMapping("/update")
    public String updateUserData(
            @ModelAttribute("profile")
            ProfileFormDTO dto,

            @AuthenticationPrincipal
            CustomUserDetails userDetails
    ) {

        User user = userDetails.getDomainUser();

        Member member = user.getMember();

        MemberDTO memberDTO =
                MemberMapper.toDTO(member);

        memberDTO.setFirstName(dto.getFirstName());
        memberDTO.setLastName(dto.getLastName());
        memberDTO.setEmail(dto.getEmail());
        memberDTO.setPhoneNumber(dto.getPhoneNumber());

        memberService.update(
                MemberMapper.toEntity(memberDTO)
        );

        userService.updateUsername(
                user.getId(),
                dto.getUsername()
        );

        return "redirect:/profile";
    }

    @PostMapping("/change-password")
    public String updatePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,

            @AuthenticationPrincipal
            CustomUserDetails userDetails
    ) {

        userService.changePassword(
                userDetails.getDomainUser(),
                oldPassword,
                newPassword
        );

        return "redirect:/profile";
    }

    @PostMapping("/update-image")
    public String updateProfileImage(
            @RequestParam("imageFile")
            MultipartFile imageFile,

            @AuthenticationPrincipal
            CustomUserDetails userDetails
    ) {

        if (imageFile.isEmpty()) {
            return "redirect:/profile";
        }

        User user = userDetails.getDomainUser();

        memberService.updateImage(
                imageFile,
                user.getId(),
                user.getMember().getId()
        );

        return "redirect:/profile";
    }
}