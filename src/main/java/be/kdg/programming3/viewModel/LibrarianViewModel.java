package be.kdg.programming3.viewModel;

import be.kdg.programming3.domain.enums.Department;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibrarianViewModel {
    private int id;

    @NotBlank(message = "{validation.librarian.firstName.notBlank}")
    private String firstName;

    @NotBlank(message = "{validation.librarian.lastName.notBlank}")
    private String lastName;

    @NotNull(message = "{validation.librarian.hireDate.notNull}")
    @PastOrPresent(message = "{validation.librarian.hireDate.pastOrPresent}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;

    @Positive(message = "{validation.librarian.salary.positive}")
    private double salary;

    @NotNull(message = "{validation.librarian.department.notNull}")
    private Department department;

    private boolean fullTime;

    @NotBlank(message = "{validation.librarian.employeePhotoUrl.notBlank}")
    private String employeePhotoUrl;

    @Email(message = "{validation.librarian.email.valid}")
    @NotBlank(message = "{validation.librarian.email.notBlank}")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "{validation.librarian.phoneNumber.pattern}")
    private int phoneNumber;
}
