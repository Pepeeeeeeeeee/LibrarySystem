package be.kdg.programming3.converter;

import be.kdg.programming3.domain.enums.MembershipType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToMembershipTypeConverter implements Converter<String, MembershipType> {
    @Override
    public MembershipType convert(String source) {
        if (source == null || source.isBlank()) return null;
        try {
            return MembershipType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
