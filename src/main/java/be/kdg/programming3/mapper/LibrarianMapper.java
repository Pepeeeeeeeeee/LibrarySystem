package be.kdg.programming3.mapper;

import be.kdg.programming3.domain.Librarian;
import be.kdg.programming3.dto.LibrarianDTO;
import be.kdg.programming3.viewModel.LibrarianViewModel;

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

    public static Librarian vmToEntity(LibrarianViewModel vm) {
        return new Librarian(
                vm.getId(),
                vm.getFirstName(),
                vm.getLastName(),
                vm.getHireDate(),
                vm.getSalary(),
                vm.getDepartment(),
                vm.isFullTime(),
                vm.getEmployeePhotoUrl(),
                vm.getEmail(),
                vm.getPhoneNumber() != null ? Integer.parseInt(vm.getPhoneNumber()) : 0
        );
    }

}
