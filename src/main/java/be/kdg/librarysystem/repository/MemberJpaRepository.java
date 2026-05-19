package be.kdg.librarysystem.repository;

import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.domain.enums.MembershipType;
import be.kdg.librarysystem.repository.interfaces.MemberRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberJpaRepository extends JpaRepository<Member, Integer>, MemberRepository {

    @Override
    Optional<Member> findByEmailIgnoreCase(String email);

    @Override
    List<Member> findByMembershipType(MembershipType membershipType);
}
