package be.kdg.librarysystem.repository.interfaces;

import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.domain.enums.MembershipType;
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
