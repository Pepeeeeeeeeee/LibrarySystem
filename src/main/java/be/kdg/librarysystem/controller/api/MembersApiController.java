package be.kdg.librarysystem.controller.api;

import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.dto.MemberCheckoutDTO;
import be.kdg.librarysystem.dto.MemberDTO;
import be.kdg.librarysystem.mapper.MemberMapper;
import be.kdg.librarysystem.service.FilteringService;
import be.kdg.librarysystem.service.MemberService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Predicate;

@RestController
@RequestMapping("/api/members")
public class MembersApiController {

    private final MemberService memberService;
    private final FilteringService filteringService;

    public MembersApiController(
            MemberService memberService,
            FilteringService filteringService
    ) {
        this.memberService = memberService;
        this.filteringService = filteringService;
    }

    @GetMapping
    public ResponseEntity<List<MemberDTO>> getAllMembers() {

        List<MemberDTO> members =
                memberService.getAllMembers()
                        .stream()
                        .map(MemberMapper::toDTO)
                        .toList();

        return ResponseEntity.ok(members);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> getMemberById(
            @PathVariable int id
    ) {

        Member member = memberService.findById(id);

        if (member == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                MemberMapper.toDTO(member)
        );
    }

    @GetMapping("/filter")
    public ResponseEntity<List<MemberDTO>> filterMembers(
            @RequestParam(defaultValue = "email")
            List<String> fields,

            @RequestParam String query
    ) {

        if (query.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Predicate<Member> predicate =
                filteringService.buildMultiFieldPredicate(
                        Member.class,
                        fields,
                        query
                );

        List<MemberDTO> filtered =
                filteringService.filter(
                                memberService.getAllMembers(),
                                predicate
                        )
                        .stream()
                        .map(MemberMapper::toDTO)
                        .toList();

        return ResponseEntity.ok(filtered);
    }

    @GetMapping("/{id}/checkouts")
    public ResponseEntity<List<MemberCheckoutDTO>> getMemberCheckoutsById(
            @PathVariable int id
    ) {

        Member member = memberService.findById(id);

        if (member == null) {
            return ResponseEntity.notFound().build();
        }

        //TODO: create mapper
        return ResponseEntity.ok(
                memberService.findCheckoutDtosByMemberId(member.getId())
        );
    }
}