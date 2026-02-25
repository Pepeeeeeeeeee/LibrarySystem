package be.kdg.librarysystem.mapper;

import be.kdg.librarysystem.domain.Member;
import be.kdg.librarysystem.dto.MemberDTO;

public class MemberMapper {
    public static MemberDTO toDTO(Member member) {
        MemberDTO dto = new MemberDTO();
        dto.setId(member.getId());
        dto.setFirstName(member.getFirstName());
        dto.setLastName(member.getLastName());
        dto.setMembershipDate(member.getMembershipDate());
        dto.setMembershipType(member.getMembershipType());
        dto.setEmail(member.getEmail());
        dto.setPhoneNumber(member.getPhoneNumber());
        dto.setActiveMembership(member.isActiveMembership());
        dto.setProfileImageUrl(member.getProfileImageUrl());
        return dto;
    }

    public static Member toEntity(MemberDTO dto) {
        return new Member(
                dto.getId(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getMembershipDate(),
                dto.getMembershipType(),
                dto.getEmail(),
                dto.getPhoneNumber(),
                dto.isActiveMembership(),
                dto.getProfileImageUrl()
        );
    }
}
