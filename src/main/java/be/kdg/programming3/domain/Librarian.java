package be.kdg.programming3.domain;

import be.kdg.programming3.domain.enums.Department;
import java.time.LocalDate;
import static java.lang.String.format;

public class Librarian {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final LocalDate hireDate;
    private final String email;
    private final int phoneNumber;
    private final double salary;
    private final Department department;
    private final boolean fullTime;
    private final String employeePhotoUrl;

    public Librarian(int id, String firstName, String lastName, LocalDate hireDate,
                     double salary, Department department, boolean fullTime,
                     String employeePhotoUrl, String email, int phoneNumber) {
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

    public String getEmail() {
        return email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getHireDate() { return hireDate; }
    public double getSalary() { return salary; }
    public Department getDepartment() { return department; }
    public boolean isFullTime() { return fullTime; }
    public String getEmployeePhotoUrl() { return employeePhotoUrl; }

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
