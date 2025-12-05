package be.kdg.programming3.domain;

import be.kdg.programming3.domain.enums.MembershipType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static java.lang.String.format;

@Data
@RequiredArgsConstructor
public class Member {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final LocalDate membershipDate;
    private final MembershipType membershipType;
    private final String email;
    private final String phoneNumber;
    private final boolean activeMembership;
    private final String profileImageUrl;
    private List<BookCheckout> checkouts = new ArrayList<>();

    public Member addCheckout(BookCheckout checkout) {
        if (!checkouts.contains(checkout)) {
            checkouts.add(checkout);
        }
        return this;
    }

    @Override
    public String toString() {
        return format("""
            Member:
            Id = %d
            First Name = %s
            Last Name = %s
            Membership Date = %s
            Membership Type = %s
            Email = %s
            Phone Number = %s
            Active Membership = %b
            Books Checked Out = %d
            """,
                id,
                firstName,
                lastName,
                membershipDate,
                membershipType,
                email,
                phoneNumber,
                activeMembership,
                checkouts.size()
        );
    }
}
