package be.kdg.librarysystem.dto;

import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileFormDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String username;
    private String profileImageUrl;

    public static ProfileFormDTO toProfileForm(Member member, User user) {
        return new ProfileFormDTO(
                member.getFirstName(),
                member.getLastName(),
                member.getEmail(),
                member.getPhoneNumber(),
                user.getUsername(),
                member.getProfileImageUrl()
        );
    }
}
