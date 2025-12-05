package be.kdg.programming3.dto;

import be.kdg.programming3.domain.enums.Department;
import java.time.LocalDate;

public class LibrarianDTO {
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate hireDate;
    private double salary;
    private Department department;
    private boolean fullTime;
    private String employeePhotoUrl;
    private String email;
    private int phoneNumber;

    public LibrarianDTO(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public boolean isFullTime() {
        return fullTime;
    }

    public void setFullTime(boolean fullTime) {
        this.fullTime = fullTime;
    }

    public String getEmployeePhotoUrl() {
        return employeePhotoUrl;
    }

    public void setEmployeePhotoUrl(String employeePhotoUrl) {
        this.employeePhotoUrl = employeePhotoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
