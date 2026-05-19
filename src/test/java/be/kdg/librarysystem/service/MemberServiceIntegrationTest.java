package be.kdg.librarysystem.service;

import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.domain.enums.MembershipType;
import be.kdg.librarysystem.repository.MemberJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MemberServiceIntegrationTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    private Member member1;
    private MockedStatic<Files> mockedFiles;

    @BeforeEach
    void setUp() {
        memberJpaRepository.deleteAll();

        member1 = new Member();
        member1.setFirstName("John");
        member1.setLastName("Doe");
        member1.setMembershipDate(LocalDate.of(2023, 1, 1));
        member1.setMembershipType(MembershipType.PREMIUM);
        member1.setEmail("john.doe@example.com");
        member1.setPhoneNumber("123456789");
        member1.setActiveMembership(true);
        member1.setProfileImageUrl("http://example.com/john.jpg");

        mockedFiles = mockStatic(Files.class);
        mockedFiles.when(() -> Files.createDirectories(any(Path.class))).thenReturn(null);
        mockedFiles.when(() -> Files.copy(any(InputStream.class), any(Path.class), any(StandardCopyOption.class))).thenReturn(1L);
    }

    @AfterEach
    void tearDown() {
        if (mockedFiles != null) {
            mockedFiles.close();
        }
    }

    @Test
    void updateImage_shouldUpdateProfileImageUrl_whenMemberExists() throws IOException {
        Member savedMember = memberJpaRepository.save(member1);
        int memberId = savedMember.getId();
        int userId = 1;

        MockMultipartFile imageFile = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "some image content".getBytes()
        );

        memberService.updateImage(imageFile, userId, memberId);

        Optional<Member> updatedMemberOptional = memberJpaRepository.findById(memberId);
        assertTrue(updatedMemberOptional.isPresent());
        Member updatedMember = updatedMemberOptional.get();
        assertEquals("/images/user_1_test.jpg", updatedMember.getProfileImageUrl());

        mockedFiles.verify(() -> Files.createDirectories(any(Path.class)));
        mockedFiles.verify(() -> Files.copy(any(InputStream.class), any(Path.class), any(StandardCopyOption.class)));
    }

    @Test
    void updateImage_shouldThrowRuntimeException_onIOException() throws IOException {
        Member savedMember = memberJpaRepository.save(member1);
        int memberId = savedMember.getId();
        int userId = 1;

        MockMultipartFile imageFile = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "some image content".getBytes()
        );

        // Configure mocked Files.createDirectories to throw IOException
        mockedFiles.when(() -> Files.createDirectories(any(Path.class))).thenThrow(new IOException("Test IO Exception"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            memberService.updateImage(imageFile, userId, memberId);
        });

        assertTrue(thrown.getMessage().contains("Failed to store profile image"));

        Optional<Member> memberAfterAttempt = memberJpaRepository.findById(memberId);
        assertTrue(memberAfterAttempt.isPresent());
        assertEquals("http://example.com/john.jpg", memberAfterAttempt.get().getProfileImageUrl());
    }

    @Test
    void updateImage_shouldNotUpdateProfileImageUrl_whenMemberDoesNotExist() throws IOException {
        int nonExistentMemberId = 999;
        int userId = 1;

        MockMultipartFile imageFile = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "some image content".getBytes()
        );

        memberService.updateImage(imageFile, userId, nonExistentMemberId);

        Optional<Member> memberOptional = memberJpaRepository.findById(nonExistentMemberId);
        assertFalse(memberOptional.isPresent());

        mockedFiles.verify(() -> Files.createDirectories(any(Path.class)), org.mockito.Mockito.never());
        mockedFiles.verify(() -> Files.copy(any(InputStream.class), any(Path.class), any(StandardCopyOption.class)), org.mockito.Mockito.never());
    }
}
