package be.kdg.librarysystem.mapper;

import be.kdg.librarysystem.domain.BookCheckout;
import be.kdg.librarysystem.dto.CheckoutDTO;

public class CheckoutMapper {
    public static CheckoutDTO toDTO(BookCheckout checkout) {
        CheckoutDTO dto = new CheckoutDTO();
        dto.setId(checkout.getId());
        dto.setBook(checkout.getBook());
        dto.setMember(checkout.getMember());
        dto.setCheckoutDate(checkout.getCheckoutDate());
        dto.setDueDate(checkout.getDueDate());
        dto.setReturnDate(checkout.getReturnDate());
        dto.setRenewed(checkout.isRenewed());
        dto.setStatus(checkout.getStatus());
        dto.setHandledByLibrarian(checkout.getHandledByLibrarian());
        return dto;
    }

    public static BookCheckout toEntity(CheckoutDTO dto) {
        return new BookCheckout(
                dto.getId(),
                dto.getBook(),
                dto.getMember(),
                dto.getCheckoutDate(),
                dto.getDueDate(),
                dto.getReturnDate(),
                dto.isRenewed(),
                dto.getStatus(),
                dto.getHandledByLibrarian()
        );
    }
}
