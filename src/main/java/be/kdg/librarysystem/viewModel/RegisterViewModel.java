package be.kdg.librarysystem.viewModel;

import be.kdg.librarysystem.domain.enums.MembershipType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterViewModel {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phoneNumber;

    private MembershipType membershipType;

    @NotBlank
    @Size(min = 4, max = 50)
    private String username;

    @NotBlank
    @Size(min = 6)
    private String password;
}