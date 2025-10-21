package be.kdg.programming3.mapper;

import be.kdg.programming3.domain.Librarian;
import be.kdg.programming3.dto.LibrarianDTO;

public class LibrarianMapper {
    public static LibrarianDTO toDTO(Librarian librarian) {
        LibrarianDTO dto = new LibrarianDTO();
        dto.setId(librarian.getId());
        dto.setFirstName(librarian.getFirstName());
        dto.setLastName(librarian.getLastName());
        dto.setHireDate(librarian.getHireDate());
        dto.setSalary(librarian.getSalary());
        dto.setDepartment(librarian.getDepartment());
        dto.setFullTime(librarian.isFullTime());
        dto.setEmployeePhotoUrl(librarian.getEmployeePhotoUrl());
        dto.setEmail(librarian.getEmail());
        dto.setPhoneNumber(librarian.getPhoneNumber());
        return dto;
    }

    public static Librarian toEntity(LibrarianDTO dto) {
        return new Librarian(
                dto.getId(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getHireDate(),
                dto.getSalary(),
                dto.getDepartment(),
                dto.isFullTime(),
                dto.getEmployeePhotoUrl(),
                dto.getEmail(),
                dto.getPhoneNumber()
        );
    }
}
