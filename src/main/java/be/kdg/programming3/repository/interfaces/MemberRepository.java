package be.kdg.programming3.repository.interfaces;

import be.kdg.programming3.domain.Member;
import be.kdg.programming3.domain.enums.MembershipType;
import java.time.LocalDate;
import java.util.List;

public interface MemberRepository {
    List<Member> findAllMembers();
    Member getById(int id);
    Member findByEmail(String email);
    Member save(String firstName, String lastName, LocalDate membershipDate, MembershipType membershipType, String email, String phoneNumber, boolean activeMembership);
    int update(Member member);
    int delete(int id);
    int updateImage(int memberId, String imagePath);
}
