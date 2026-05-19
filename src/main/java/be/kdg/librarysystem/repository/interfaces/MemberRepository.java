package be.kdg.librarysystem.repository.interfaces;

import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.domain.enums.MembershipType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findByEmailIgnoreCase(String email);
    List<Member> findByMembershipType(MembershipType membershipType);
}
