package be.kdg.librarysystem.domain;

import be.kdg.librarysystem.domain.enums.Department;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "librarians")
@Data
@NoArgsConstructor
public class Librarian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;
    private String lastName;
    private LocalDate hireDate;
    private double salary;

    @Enumerated(EnumType.STRING)
    private Department department;

    private boolean fullTime;
    private String employeePhotoUrl;
    @Column(unique = true)
    private String email;
    private int phoneNumber;

    /**
     * LEGACY constructor — DO NOT USE for new code.
     */
    @Deprecated
    public Librarian(
            int id,
            String firstName,
            String lastName,
            LocalDate hireDate,
            double salary,
            Department department,
            boolean fullTime,
            String employeePhotoUrl,
            String email,
            int phoneNumber
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hireDate = hireDate;
        this.salary = salary;
        this.department = department;
        this.fullTime = fullTime;
        this.employeePhotoUrl = employeePhotoUrl;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}