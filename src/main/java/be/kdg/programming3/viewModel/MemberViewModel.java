package be.kdg.programming3.viewModel;

import be.kdg.programming3.domain.enums.MembershipType;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class MemberViewModel {
    private int id;

    @NotBlank(message = "{validation.member.firstName.notBlank}")
    private String firstName;

    @NotBlank(message = "{validation.member.lastName.notBlank}")
    private String lastName;

    @NotNull(message = "{validation.member.membershipDate.notNull}")
    @PastOrPresent(message = "{validation.member.membershipDate.pastOrPresent}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate membershipDate;

    @NotNull(message = "{validation.member.membershipType.notNull}")
    private MembershipType membershipType;

    @Email(message = "{validation.member.email.valid}")
    @NotBlank(message = "{validation.member.email.notBlank}")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "{validation.member.phoneNumber.pattern}")
    private String phoneNumber;

    private boolean activeMembership = true;

    @NotBlank(message = "{validation.member.profileImageUrl.notBlank}")
    private String profileImageUrl;
}
