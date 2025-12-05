package be.kdg.programming3.domain;

import be.kdg.programming3.domain.enums.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import static java.lang.String.format;

@Data
@AllArgsConstructor
public class Librarian {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final LocalDate hireDate;
    private final double salary;
    private final Department department;
    private final boolean fullTime;
    private final String employeePhotoUrl;
    private final String email;
    private final int phoneNumber;

    @Override
    public String toString() {
        return format("""
            Librarian:
            Id = %d
            First Name = %s
            Last Name = %s
            Hire Date = %s
            Salary = %.2f
            Department = %s
            Full Time = %b
            """,
                id,
                firstName,
                lastName,
                hireDate,
                salary,
                department,
                fullTime
        );
    }
}
