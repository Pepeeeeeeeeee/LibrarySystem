package be.kdg.librarysystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCheckoutDetailsDTO {

    private boolean checkedOut;

    private String expectedReturnDate;

    private int librarianId;
    private String librarianName;

    private int memberId;
    private String memberName;
}