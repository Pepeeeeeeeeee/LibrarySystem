package be.kdg.programming3.domain;

import be.kdg.programming3.domain.enums.MembershipType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static java.lang.String.format;

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

    public Member(int id, String firstName, String lastName, LocalDate membershipDate,
                  MembershipType membershipType, String email, String phoneNumber,
                  boolean activeMembership, String profileImageUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.membershipDate = membershipDate;
        this.membershipType = membershipType;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.activeMembership = activeMembership;
        this.profileImageUrl = profileImageUrl;
    }

    // Getters
    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getMembershipDate() { return membershipDate; }
    public MembershipType getMembershipType() { return membershipType; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public boolean isActiveMembership() { return activeMembership; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public List<BookCheckout> getCheckouts() { return checkouts; }

    // Relationship method (many-to-many)
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
