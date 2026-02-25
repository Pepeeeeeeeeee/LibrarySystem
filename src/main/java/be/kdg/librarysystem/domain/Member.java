package be.kdg.librarysystem.domain;

import be.kdg.librarysystem.domain.enums.MembershipType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;
    private String lastName;
    private LocalDate membershipDate;

    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;

    private String email;
    private String phoneNumber;
    private boolean activeMembership;
    private String profileImageUrl;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<BookCheckout> checkouts = new ArrayList<>();

    @OneToOne(mappedBy = "member")
    private User user;

    /**
     * LEGACY constructor — DO NOT USE for new code.
     */
    @Deprecated
    public Member(
            int id,
            String firstName,
            String lastName,
            LocalDate membershipDate,
            MembershipType membershipType,
            String email,
            String phoneNumber,
            boolean activeMembership,
            String profileImageUrl
    ) {
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

    public void addCheckout(BookCheckout checkout) {
        checkouts.add(checkout);
        checkout.setMember(this);
    }
}

