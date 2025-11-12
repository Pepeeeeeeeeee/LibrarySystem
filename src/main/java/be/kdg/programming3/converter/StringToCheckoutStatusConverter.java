package be.kdg.programming3.converter;

import be.kdg.programming3.domain.enums.CheckoutStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCheckoutStatusConverter implements Converter<String, CheckoutStatus> {
    @Override
    public CheckoutStatus convert(String source) {
        if (source == null || source.isBlank()) return null;
        try {
            return CheckoutStatus.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
