package be.kdg.programming3.controller;

import be.kdg.programming3.domain.User;
import be.kdg.programming3.mapper.MemberMapper;
import be.kdg.programming3.domain.Member;
import be.kdg.programming3.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.function.Predicate;

@Controller
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final FilteringService filteringService;
    private static final Logger log = LoggerFactory.getLogger(MemberController.class);
    private static final int PAGE_SIZE = 20;
    private final UserService userService;
    private final AuthService authService;

    public MemberController(MemberService memberService,
                            FilteringService filteringService, UserService userService, AuthService authService) {
        this.memberService = memberService;
        this.filteringService = filteringService;
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping
    public String listMembers(@RequestParam(defaultValue = "1") int page,
                              Model model,
                              @RequestParam(required = false) List<String> fields,
                              @RequestParam(required = false) String query,
                              HttpServletRequest request, HttpServletResponse response) {

        List<Member> allMembers = memberService.getAllMembers();
        int totalMembers = allMembers.size();
        int totalPages = (int) Math.ceil((double) totalMembers / PAGE_SIZE);

        int fromIndex = Math.max(0, (page - 1) * PAGE_SIZE);
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalMembers);

        List<Member> membersPage = allMembers.subList(fromIndex, toIndex);

        User user = userService.getUserByToken(authService.getTokensFromCookies(request, response).getValue());

        model.addAttribute("members", membersPage.stream().map(MemberMapper::toDTO).toList());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalMembers", totalMembers);
        model.addAttribute("memberFields", filteringService.preloadDomainFields(Member.class));
        model.addAttribute("selectedFields", fields);
        model.addAttribute("query", query);
        model.addAttribute("currentUser", user);
        model.addAttribute("userPhotoUrl", user != null ? memberService.findById(user.getId()).getProfileImageUrl() : null);

        log.debug("Listed members page {} of {}", page, totalPages);
        return "members";
    }

    @GetMapping("/filter")
    public String filterMembers(@RequestParam(defaultValue = "email") List<String> fields,
                                @RequestParam String query,
                                Model model) {
        log.debug("Filtering members by field={} query={}", fields, query);
        if (query.isBlank()) return "redirect:/members";

        Predicate<Member> predicate = filteringService.buildMultiFieldPredicate(Member.class, fields, query);
        List<Member> filtered = filteringService.filter(memberService.getAllMembers(), predicate);

        model.addAttribute("members", filtered.stream().map(MemberMapper::toDTO).toList());
        model.addAttribute("memberFields", filteringService.preloadDomainFields(Member.class));
        model.addAttribute("totalMembers", filtered.size());
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);
        model.addAttribute("selectedFields", fields);
        model.addAttribute("query", query);

        return "members";
    }
}
