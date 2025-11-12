package be.kdg.programming3.converter;

import be.kdg.programming3.domain.enums.Genre;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToGenreConverter implements Converter<String, Genre> {
    @Override
    public Genre convert(String source) {
        try {
            return Genre.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
