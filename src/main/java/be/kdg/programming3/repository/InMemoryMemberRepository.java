package be.kdg.programming3.repository;

import be.kdg.programming3.domain.Member;
import be.kdg.programming3.domain.enums.MembershipType;
import be.kdg.programming3.repository.interfaces.DataRepository;
import be.kdg.programming3.repository.interfaces.MemberRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@Profile({"web", "console"})
public class InMemoryMemberRepository implements MemberRepository {

    private final DataRepository dataRepository;

    public InMemoryMemberRepository(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public List<Member> findAllMembers() {
        return dataRepository.findAllMembers();
    }

    @Override
    public Member findById(int id) {
        return null;
    }

    @Override
    public Member findByEmail(String email) {
        return dataRepository.findAllMembers().stream().filter(member -> member.getEmail().equals(email)).findFirst().orElse(null);
    }

    @Override
    public int save(String firstName, String lastName, LocalDate membershipDate, MembershipType membershipType, String email, String phoneNumber, boolean activeMembership) {
        Member newMember = new Member(dataRepository.findAllMembers().getLast().getId()+1, firstName, lastName, membershipDate, membershipType, email, phoneNumber, activeMembership, null);
        dataRepository.findAllMembers().add(newMember);
        return 1;
    }

    @Override
    public int update(Member member) {
        return 0;
    }

    @Override
    public int delete(int id) {
        return 0;
    }
}