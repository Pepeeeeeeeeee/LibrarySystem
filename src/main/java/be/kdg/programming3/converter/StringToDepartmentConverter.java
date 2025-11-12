package be.kdg.programming3.converter;

import be.kdg.programming3.domain.enums.Department;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToDepartmentConverter implements Converter<String, Department> {
    @Override
    public Department convert(String source) {
        if (source == null || source.isBlank()) return null;
        try {
            return Department.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
