package be.kdg.programming3.dto;

import be.kdg.programming3.domain.enums.MembershipType;
import java.time.LocalDate;

public class MemberDTO {
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate membershipDate;
    private MembershipType membershipType;
    private String email;
    private String phoneNumber;
    private boolean activeMembership;
    private String profileImageUrl;

    public MemberDTO(){}

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

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    public void setMembershipDate(LocalDate membershipDate) {
        this.membershipDate = membershipDate;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActiveMembership() {
        return activeMembership;
    }

    public void setActiveMembership(boolean activeMembership) {
        this.activeMembership = activeMembership;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
