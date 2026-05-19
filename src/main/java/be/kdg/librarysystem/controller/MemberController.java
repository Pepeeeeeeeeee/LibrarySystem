package be.kdg.librarysystem.controller;
import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.mapper.MemberMapper;
import be.kdg.librarysystem.service.FilteringService;
import be.kdg.librarysystem.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Predicate;

@Controller
@RequestMapping("/members")
public class MemberController {

    private static final Logger log =
            LoggerFactory.getLogger(MemberController.class);

    private static final int PAGE_SIZE = 20;

    private final MemberService memberService;
    private final FilteringService filteringService;

    public MemberController(
            MemberService memberService,
            FilteringService filteringService
    ) {
        this.memberService = memberService;
        this.filteringService = filteringService;
    }

    @GetMapping
    public String listMembers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false)
            List<String> fields,
            @RequestParam(required = false)
            String query,
            Model model
    ) {

        List<Member> allMembers =
                memberService.getAllMembers();

        int totalMembers = allMembers.size();

        int totalPages =
                (int) Math.ceil(
                        (double) totalMembers / PAGE_SIZE
                );

        int fromIndex =
                Math.max(0, (page - 1) * PAGE_SIZE);

        int toIndex =
                Math.min(
                        fromIndex + PAGE_SIZE,
                        totalMembers
                );

        List<Member> membersPage =
                allMembers.subList(fromIndex, toIndex);

        model.addAttribute(
                "members",
                membersPage.stream()
                        .map(MemberMapper::toDTO)
                        .toList()
        );

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalMembers", totalMembers);

        model.addAttribute(
                "memberFields",
                filteringService.preloadDomainFields(Member.class)
        );

        model.addAttribute("selectedFields", fields);
        model.addAttribute("query", query);

        log.debug(
                "Listed members page {} of {}",
                page,
                totalPages
        );

        return "members";
    }

    @GetMapping("/filter")
    public String filterMembers(
            @RequestParam(defaultValue = "email")
            List<String> fields,

            @RequestParam String query,

            Model model
    ) {

        if (query.isBlank()) {
            return "redirect:/members";
        }

        Predicate<Member> predicate =
                filteringService.buildMultiFieldPredicate(
                        Member.class,
                        fields,
                        query
                );

        List<Member> filtered =
                filteringService.filter(
                        memberService.getAllMembers(),
                        predicate
                );

        model.addAttribute(
                "members",
                filtered.stream()
                        .map(MemberMapper::toDTO)
                        .toList()
        );

        model.addAttribute(
                "memberFields",
                filteringService.preloadDomainFields(Member.class)
        );

        model.addAttribute("totalMembers", filtered.size());

        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);

        model.addAttribute("selectedFields", fields);
        model.addAttribute("query", query);

        return "members";
    }
}