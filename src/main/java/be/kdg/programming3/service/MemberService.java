package be.kdg.programming3.service;

import be.kdg.programming3.domain.BookCheckout;
import be.kdg.programming3.domain.Member;
import be.kdg.programming3.domain.enums.MembershipType;
import be.kdg.programming3.repository.interfaces.CheckoutRepository;
import be.kdg.programming3.repository.interfaces.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository repo;
    private final CheckoutRepository checkoutRepository;

    public MemberService(MemberRepository repo, CheckoutRepository checkoutRepository) {
        this.repo = repo;
        this.checkoutRepository = checkoutRepository;
    }

    public List<Member> getAllMembers() {
        return repo.findAllMembers();
    }

    public Member findByEmail(String email) {
        return repo.findByEmail(email);
    }

    /**
    @deprecated Use {@link #save(String firstName, String lastName, LocalDate membershipDate, MembershipType membershipType, String email, String phoneNumber, boolean activeMembership)} instead
    */
    public void addMember(Member member){
        repo.save(member.getFirstName(), member.getLastName(), member.getMembershipDate(), member.getMembershipType(), member.getEmail(), member.getPhoneNumber(), member.isActiveMembership());
    }

    public int save(String firstName, String lastName, LocalDate membershipDate, MembershipType membershipType, String email, String phoneNumber, boolean activeMembership) {
        return repo.save(firstName, lastName, membershipDate, membershipType, email, phoneNumber, activeMembership);
    }

    public int update(Member member) {
        return repo.update(member);
    }

    public Member findById(int id) {
        return repo.findById(id);
    }

    public List<Optional<BookCheckout>> findCheckoutsByMemberId(int memberId){
        return checkoutRepository.findByMemberId(memberId);
    }
}
