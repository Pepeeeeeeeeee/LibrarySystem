package be.kdg.librarysystem.service;

import be.kdg.librarysystem.domain.BookCheckout;
import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.domain.enums.MembershipType;
import be.kdg.librarysystem.dto.MemberCheckoutDTO;
import be.kdg.librarysystem.repository.BookCheckoutJpaRepository;
import be.kdg.librarysystem.repository.MemberJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class MemberService {

    private final MemberJpaRepository repository;
    private final BookCheckoutJpaRepository checkoutRepository;

    public MemberService(
            MemberJpaRepository repository,
            BookCheckoutJpaRepository checkoutRepository
    ) {
        this.repository = repository;
        this.checkoutRepository = checkoutRepository;
    }

    public List<Member> getAllMembers() {
        return repository.findAll();
    }

    public Member findByEmail(String email) {
        return repository.findByEmailIgnoreCase(email)
                .orElse(null);
    }

    public Member createMember(
            String firstName,
            String lastName,
            LocalDate membershipDate,
            MembershipType membershipType,
            String email,
            String phoneNumber,
            boolean activeMembership
    ) {

        Member member = new Member();

        member.setFirstName(firstName);
        member.setLastName(lastName);
        member.setMembershipDate(membershipDate);
        member.setMembershipType(membershipType);
        member.setEmail(email);
        member.setPhoneNumber(phoneNumber);
        member.setActiveMembership(activeMembership);

        return repository.save(member);
    }

    public Member update(Member member) {
        return repository.save(member);
    }

    public Member findById(int id) {
        return repository.findById(id)
                .orElse(null);
    }

    public List<MemberCheckoutDTO> findCheckoutDtosByMemberId(
            int memberId
    ) {

        return checkoutRepository
                .findByMemberId(memberId)
                .stream()
                .map(checkout -> new MemberCheckoutDTO(
                        checkout.getBook().getId(),
                        checkout.getBook().getTitle(),
                        checkout.getStatus().name(),
                        checkout.getCheckoutDate().toString(),
                        checkout.getDueDate().toString()
                ))
                .toList();
    }

    @Transactional
    public void updateImage(
            MultipartFile imageFile,
            int userId,
            int memberId
    ) {
        Member member = findById(memberId);

        if (member != null) {
            try {
                String uploadDir = "src/main/resources/static/images/";
                Files.createDirectories(Paths.get(uploadDir));

                String filename = "user_" + userId + "_" + imageFile.getOriginalFilename();
                Path targetPath = Paths.get(uploadDir, filename);

                Files.copy(
                        imageFile.getInputStream(),
                        targetPath,
                        StandardCopyOption.REPLACE_EXISTING
                );

                member.setProfileImageUrl("/images/" + filename);
                repository.save(member);

            } catch (IOException e) {
                throw new RuntimeException(
                        "Failed to store profile image",
                        e
                );
            }
        }
    }
}