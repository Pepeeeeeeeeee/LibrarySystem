package be.kdg.librarysystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberCheckoutDTO {

    private int bookId;
    private String bookTitle;

    private String status;

    private String checkoutDate;
    private String dueDate;
}