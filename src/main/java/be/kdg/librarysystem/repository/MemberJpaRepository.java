package be.kdg.librarysystem.repository;

import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.domain.enums.MembershipType;
import be.kdg.librarysystem.repository.interfaces.MemberRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MemberJpaRepository extends JpaRepository<Member, Integer>, MemberRepository {

    @Override
    default List<Member> findAllMembers() {
        return findAll();
    }

    @Override
    default Member getById(int id) {
        return findById(id).orElse(null);
    }

    @Override
    default Member findByEmail(String email) {
        return findByEmailIgnoreCase(email).orElse(null);
    }

    List<Member> findByMembershipType(MembershipType type);

    java.util.Optional<Member> findByEmailIgnoreCase(String email);

    @Override
    default Member save(String firstName, String lastName, LocalDate membershipDate,
                     MembershipType membershipType, String email, String phoneNumber, boolean activeMembership) {
        Member m = new Member();
        m.setFirstName(firstName);
        m.setLastName(lastName);
        m.setMembershipDate(membershipDate);
        m.setMembershipType(membershipType);
        m.setEmail(email);
        m.setPhoneNumber(phoneNumber);
        m.setActiveMembership(activeMembership);
        saveAndFlush(m);
        return m;
    }

    @Override
    default int update(Member member) {
        saveAndFlush(member);
        return 1;
    }

    @Override
    default int delete(int id) {
        deleteById(id);
        return 1;
    }

    @Override
    default int updateImage(int memberId, String imagePath){
        return findById(memberId).map(m -> {
            m.setProfileImageUrl(imagePath);
            saveAndFlush(m);
            return 1;
        }).orElse(0);
    }
}
