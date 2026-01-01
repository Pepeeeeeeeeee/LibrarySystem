package be.kdg.programming3.service;

import be.kdg.programming3.domain.BookCheckout;
import be.kdg.programming3.domain.Member;
import be.kdg.programming3.domain.enums.MembershipType;
import be.kdg.programming3.repository.interfaces.CheckoutRepository;
import be.kdg.programming3.repository.interfaces.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    public void save(String firstName, String lastName, LocalDate membershipDate, MembershipType membershipType, String email, String phoneNumber, boolean activeMembership) {
        repo.save(firstName, lastName, membershipDate, membershipType, email, phoneNumber, activeMembership);
    }

    public int update(Member member) {
        return repo.update(member);
    }

    public Member findById(int id) {
        return repo.getById(id);
    }

    public List<Optional<BookCheckout>> findCheckoutsByMemberId(int memberId){
        return checkoutRepository.findByMemberId(memberId);
    }

    public void updateImage(MultipartFile imageFile, int userId, int memberId) {
        try {
            String uploadDir = "src/main/resources/static/images/";
            Files.createDirectories(Paths.get(uploadDir));

            String filename = "user_" + userId + "_" + imageFile.getOriginalFilename();
            Path targetPath = Paths.get(uploadDir, filename);

            Files.copy(imageFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            String imagePathForDb = "/images/" + filename;
            repo.updateImage(memberId, imagePathForDb);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store profile image", e);
        }
    }
}
