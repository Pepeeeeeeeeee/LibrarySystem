package be.kdg.librarysystem.service;

import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.domain.User;
import be.kdg.librarysystem.domain.enums.MembershipType;
import be.kdg.librarysystem.domain.enums.Roles;
import be.kdg.librarysystem.repository.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UserServiceTest {

    @Mock
    private UserJpaRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member();
        member.setId(1);
        member.setFirstName("John");
        member.setLastName("Doe");
        member.setMembershipDate(LocalDate.of(1990, 1, 1));
        member.setEmail("john.doe@example.com");
        member.setPhoneNumber("123456789");
        member.setMembershipType(MembershipType.STANDARD);
        member.setActiveMembership(true);

        user = new User();
        user.setId(1);
        user.setUsername("johndoe");
        user.setPassword("encodedPassword");
        user.setRole(Roles.USER);
        user.setMember(member);
    }

    @Test
    void findByUsername_shouldReturnUser_whenUserExists() {
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));

        User foundUser = userService.findByUsername("johndoe");

        assertNotNull(foundUser);
        assertEquals("johndoe", foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername("johndoe");
    }

    @Test
    void findByUsername_shouldReturnNull_whenUserDoesNotExist() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        User foundUser = userService.findByUsername("nonexistent");

        assertNull(foundUser);
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }

    @Test
    void usernameExists_shouldReturnTrue_whenUsernameExists() {
        when(userRepository.existsByUsername("johndoe")).thenReturn(true);

        boolean exists = userService.usernameExists("johndoe");

        assertTrue(exists);
        verify(userRepository, times(1)).existsByUsername("johndoe");
    }

    @Test
    void usernameExists_shouldReturnFalse_whenUsernameDoesNotExist() {
        when(userRepository.existsByUsername("nonexistent")).thenReturn(false);

        boolean exists = userService.usernameExists("nonexistent");

        assertFalse(exists);
        verify(userRepository, times(1)).existsByUsername("nonexistent");
    }

    @Test
    void createUser_shouldReturnNewUser() {
        String rawPassword = "password123";
        Member newMember = new Member();
        newMember.setId(2);
        newMember.setFirstName("Jane");
        newMember.setLastName("Smith");
        newMember.setMembershipDate(LocalDate.of(1995, 2, 2));
        newMember.setEmail("jane.smith@example.com");
        newMember.setPhoneNumber("987654321");
        newMember.setMembershipType(MembershipType.STANDARD);
        newMember.setActiveMembership(true);

        User newUserEntity = new User();
        newUserEntity.setId(2);
        newUserEntity.setUsername("janesmith");
        newUserEntity.setPassword("encodedPassword");
        newUserEntity.setRole(Roles.USER);
        newUserEntity.setMember(newMember);


        when(passwordEncoder.encode(rawPassword)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUserEntity);

        User createdUser = userService.createUser(newMember, "janesmith", rawPassword);

        assertNotNull(createdUser);
        assertEquals("janesmith", createdUser.getUsername());
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals(Roles.USER, createdUser.getRole());
        assertEquals(newMember, createdUser.getMember());
        verify(passwordEncoder, times(1)).encode(rawPassword);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void findById_shouldReturnUser_whenUserExists() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User foundUser = userService.findById(1);

        assertNotNull(foundUser);
        assertEquals(1, foundUser.getId());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void findById_shouldReturnNull_whenUserDoesNotExist() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        User foundUser = userService.findById(99);

        assertNull(foundUser);
        verify(userRepository, times(1)).findById(99);
    }

    @Test
    void findByMemberId_shouldReturnUser_whenUserExists() {
        when(userRepository.findByMemberId(1)).thenReturn(Optional.of(user));

        User foundUser = userService.findByMemberId(1);

        assertNotNull(foundUser);
        assertEquals(1, foundUser.getMember().getId());
        verify(userRepository, times(1)).findByMemberId(1);
    }

    @Test
    void findByMemberId_shouldReturnNull_whenUserDoesNotExist() {
        when(userRepository.findByMemberId(99)).thenReturn(Optional.empty());

        User foundUser = userService.findByMemberId(99);

        assertNull(foundUser);
        verify(userRepository, times(1)).findByMemberId(99);
    }

    @Test
    void updateUsername_shouldUpdateUsername_whenUserExists() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUsername(1, "newusername");

        assertNotNull(updatedUser);
        assertEquals("newusername", updatedUser.getUsername());
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUsername_shouldReturnNull_whenUserDoesNotExist() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        User updatedUser = userService.updateUsername(99, "newusername");

        assertNull(updatedUser);
        verify(userRepository, times(1)).findById(99);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void changePassword_shouldReturnTrueAndChangePassword_whenOldPasswordMatches() {
        String oldPassword = "oldPassword123";
        String newPassword = "newPassword123";
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        boolean changed = userService.changePassword(user, oldPassword, newPassword);

        assertTrue(changed);
        assertEquals("newEncodedPassword", user.getPassword());
        verify(passwordEncoder, times(1)).matches(oldPassword, "encodedPassword");
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void changePassword_shouldReturnFalseAndNotChangePassword_whenOldPasswordDoesNotMatch() {
        String oldPassword = "wrongPassword";
        String newPassword = "newPassword123";
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(false);

        boolean changed = userService.changePassword(user, oldPassword, newPassword);

        assertFalse(changed);
        assertEquals("encodedPassword", user.getPassword());
        verify(passwordEncoder, times(1)).matches(oldPassword, "encodedPassword");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}
