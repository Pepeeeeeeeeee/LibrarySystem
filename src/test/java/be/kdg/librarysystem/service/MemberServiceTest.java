package be.kdg.librarysystem.service;

import be.kdg.librarysystem.domain.Book;
import be.kdg.librarysystem.domain.BookCheckout;
import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.domain.enums.CheckoutStatus;
import be.kdg.librarysystem.domain.enums.Genre;
import be.kdg.librarysystem.domain.enums.MembershipType;
import be.kdg.librarysystem.dto.MemberCheckoutDTO;
import be.kdg.librarysystem.repository.BookCheckoutJpaRepository;
import be.kdg.librarysystem.repository.MemberJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class MemberServiceTest {

    @Mock
    private MemberJpaRepository memberRepository;

    @Mock
    private BookCheckoutJpaRepository checkoutRepository;

    @InjectMocks
    private MemberService memberService;

    private Member member1;
    private Member member2;
    private Book book1;
    private BookCheckout checkout1;

    @BeforeEach
    void setUp() {
        member1 = new Member();
        member1.setId(1);
        member1.setFirstName("John");
        member1.setLastName("Doe");
        member1.setMembershipDate(LocalDate.of(2023, 1, 1));
        member1.setMembershipType(MembershipType.PREMIUM);
        member1.setEmail("john.doe@example.com");
        member1.setPhoneNumber("123456789");
        member1.setActiveMembership(true);
        member1.setProfileImageUrl("http://example.com/john.jpg");

        member2 = new Member();
        member2.setId(2);
        member2.setFirstName("Jane");
        member2.setLastName("Smith");
        member2.setMembershipDate(LocalDate.of(2023, 2, 1));
        member2.setMembershipType(MembershipType.STANDARD);
        member2.setEmail("jane.smith@example.com");
        member2.setPhoneNumber("987654321");
        member2.setActiveMembership(false);
        member2.setProfileImageUrl("http://example.com/jane.jpg");

        book1 = new Book();
        book1.setId(101);
        book1.setTitle("Test Book");
        book1.setAuthor("Test Author");
        book1.setIsbn("1234567890");
        book1.setPublicationDate(LocalDate.of(2020, 1, 1));
        book1.setPages(300);
        book1.setPrice(20.00);
        book1.setGenre(Genre.FICTION);
        book1.setAvailable(true);
        book1.setCoverImageUrl("http://example.com/book.jpg");
        book1.setLocation("Shelf C3");

        checkout1 = new BookCheckout();
        checkout1.setId(1);
        checkout1.setBook(book1);
        checkout1.setMember(member1);
        checkout1.setStatus(CheckoutStatus.CHECKED_OUT);
        checkout1.setCheckoutDate(LocalDate.now());
        checkout1.setDueDate(LocalDate.now().plusDays(14));
    }

    @Test
    void getAllMembers_shouldReturnAllMembers() {
        when(memberRepository.findAll()).thenReturn(Arrays.asList(member1, member2));

        List<Member> members = memberService.getAllMembers();

        assertNotNull(members);
        assertEquals(2, members.size());
        assertTrue(members.contains(member1));
        assertTrue(members.contains(member2));
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void findByEmail_shouldReturnMember_whenMemberExists() {
        when(memberRepository.findByEmailIgnoreCase("john.doe@example.com")).thenReturn(Optional.of(member1));

        Member foundMember = memberService.findByEmail("john.doe@example.com");

        assertNotNull(foundMember);
        assertEquals("john.doe@example.com", foundMember.getEmail());
        verify(memberRepository, times(1)).findByEmailIgnoreCase("john.doe@example.com");
    }

    @Test
    void findByEmail_shouldReturnNull_whenMemberDoesNotExist() {
        when(memberRepository.findByEmailIgnoreCase("nonexistent@example.com")).thenReturn(Optional.empty());

        Member foundMember = memberService.findByEmail("nonexistent@example.com");

        assertNull(foundMember);
        verify(memberRepository, times(1)).findByEmailIgnoreCase("nonexistent@example.com");
    }

    @Test
    void createMember_shouldReturnNewMember() {
        when(memberRepository.save(any(Member.class))).thenReturn(member1);

        Member newMember = memberService.createMember(
                "John", "Doe", LocalDate.of(2023, 1, 1), MembershipType.PREMIUM,
                "john.doe@example.com", "123456789", true
        );

        assertNotNull(newMember);
        assertEquals("John", newMember.getFirstName());
        assertEquals("john.doe@example.com", newMember.getEmail());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void update_shouldReturnUpdatedMember() {
        when(memberRepository.save(any(Member.class))).thenReturn(member1);

        Member updatedMember = memberService.update(member1);

        assertNotNull(updatedMember);
        assertEquals(member1.getFirstName(), updatedMember.getFirstName());
        verify(memberRepository, times(1)).save(member1);
    }

    @Test
    void findById_shouldReturnMember_whenMemberExists() {
        when(memberRepository.findById(1)).thenReturn(Optional.of(member1));

        Member foundMember = memberService.findById(1);

        assertNotNull(foundMember);
        assertEquals(1, foundMember.getId());
        verify(memberRepository, times(1)).findById(1);
    }

    @Test
    void findById_shouldReturnNull_whenMemberDoesNotExist() {
        when(memberRepository.findById(99)).thenReturn(Optional.empty());

        Member foundMember = memberService.findById(99);

        assertNull(foundMember);
        verify(memberRepository, times(1)).findById(99);
    }

    @Test
    void findCheckoutDtosByMemberId_shouldReturnListOfDTOs() {
        when(checkoutRepository.findByMemberId(1)).thenReturn(Arrays.asList(checkout1));

        List<MemberCheckoutDTO> dtos = memberService.findCheckoutDtosByMemberId(1);

        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        assertEquals(book1.getTitle(), dtos.get(0).getBookTitle());
        verify(checkoutRepository, times(1)).findByMemberId(1);
    }

    @Test
    void updateImage_shouldUpdateProfileImageUrl_whenMemberExists() throws IOException {
        MockMultipartFile imageFile = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "some image content".getBytes()
        );
        int userId = 1;
        int memberId = 1;

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member1));
        when(memberRepository.save(any(Member.class))).thenReturn(member1);

        try (var mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.createDirectories(any(Path.class))).thenReturn(null);
            mockedFiles.when(() -> Files.copy(any(InputStream.class), any(Path.class), any(StandardCopyOption.class))).thenReturn(1L);

            memberService.updateImage(imageFile, userId, memberId);

            verify(memberRepository, times(1)).findById(memberId);
            verify(memberRepository, times(1)).save(member1);
            assertEquals("/images/user_1_test.jpg", member1.getProfileImageUrl());
        }
    }

    @Test
    void updateImage_shouldThrowRuntimeException_onIOException() throws IOException {
        MockMultipartFile imageFile = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "some image content".getBytes()
        );
        int userId = 1;
        int memberId = 1;

        lenient().when(memberRepository.findById(memberId)).thenReturn(Optional.of(member1));

        try (var mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.createDirectories(any(Path.class))).thenThrow(new IOException("Test IO Exception"));

            RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
                memberService.updateImage(imageFile, userId, memberId);
            });

            assertTrue(thrown.getMessage().contains("Failed to store profile image"));
            verify(memberRepository, never()).save(any(Member.class));
        }
    }

    @Test
    void updateImage_shouldNotUpdateProfileImageUrl_whenMemberDoesNotExist() throws IOException {
        MockMultipartFile imageFile = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "some image content".getBytes()
        );
        int userId = 1;
        int memberId = 99;

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        try (var mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.createDirectories(any(Path.class))).thenReturn(null);
            mockedFiles.when(() -> Files.copy(any(InputStream.class), any(Path.class), any(StandardCopyOption.class))).thenReturn(1L);

            memberService.updateImage(imageFile, userId, memberId);

            verify(memberRepository, times(1)).findById(memberId);
            verify(memberRepository, never()).save(any(Member.class));
        }
    }
}