package be.kdg.librarysystem.controller.api;

import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.service.MemberService;
import be.kdg.librarysystem.service.UserService;
import be.kdg.librarysystem.viewModel.RegisterViewModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    private final MemberService memberService;
    private final UserService userService;

    public AuthApiController(
            MemberService memberService,
            UserService userService
    ) {
        this.memberService = memberService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterViewModel vm,
            HttpServletRequest request
    ) throws ServletException {

        if (userService.usernameExists(vm.getUsername())) {
            return ResponseEntity.badRequest().build();
        }

        Member member = memberService.createMember(
                vm.getFirstName(),
                vm.getLastName(),
                LocalDate.now(),
                vm.getMembershipType(),
                vm.getEmail(),
                vm.getPhoneNumber(),
                true
        );

        userService.createUser(
                member,
                vm.getUsername(),
                vm.getPassword()
        );

        request.login(
                vm.getUsername(),
                vm.getPassword()
        );

        return ResponseEntity.ok().build();
    }
}