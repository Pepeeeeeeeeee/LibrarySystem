package be.kdg.programming3.service;

import org.springframework.stereotype.Service;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class FilteringService {
    public <T> Predicate<T> buildPredicate(Class<T> clazz, String fieldName, String operator, String rawValue) {
        return item -> {
            try {
                // Case-insensitive field lookup
                Field field = null;
                for (Field f : clazz.getDeclaredFields()) {
                    if (f.getName().equalsIgnoreCase(fieldName)) {
                        field = f;
                        break;
                    }
                }

                // If no matching field found, show available fields
                if (field == null) {
                    System.err.println("No such field: " + fieldName);
                    System.err.println("Available fields: ");
                    for (Field f : clazz.getDeclaredFields()) {
                        System.err.printf(" - %s (%s)%n", f.getName(), f.getType().getSimpleName());
                    }
                    return false;
                }

                // Access field value (java reflection)
                field.setAccessible(true);
                Object fieldValue = field.get(item);
                if (fieldValue == null) return false;

                // Strings have special operators
                if (fieldValue instanceof String stringValue) {
                    String userValue = rawValue.toLowerCase();
                    String actualValue = stringValue.toLowerCase();

                    return switch (operator.toLowerCase()) {
                        case "=" -> actualValue.equals(userValue);
                        case "!=" -> !actualValue.equals(userValue);
                        case "contains" -> actualValue.contains(userValue);
                        case "!contains" -> !actualValue.contains(userValue);
                        default -> {
                            System.err.println("Unsupported operator for String: " + operator);
                            yield false;
                        }
                    };
                }

                // Convert raw input to correct type
                Object typedValue = convertValue(field.getType(), rawValue);

                // Equality checks
                if (operator.equals("=")) return fieldValue.equals(typedValue);
                if (operator.equals("!=")) return !fieldValue.equals(typedValue);

                // Comparable types
                if (fieldValue instanceof Comparable comparableField &&
                        typedValue instanceof Comparable comparableValue) {
                    @SuppressWarnings("unchecked")
                    Comparable<Object> comparable = (Comparable<Object>) comparableField;
                    int comparison = comparable.compareTo(typedValue);

                    return switch (operator) {
                        case ">" -> comparison > 0;
                        case "<" -> comparison < 0;
                        case ">=" -> comparison >= 0;
                        case "<=" -> comparison <= 0;
                        default -> false;
                    };
                }

                return false;

            } catch (Exception e) {
                System.err.println("Filtering failed: " + e.getMessage());
                return false;
            }
        };
    }

    public <T> List<T> filter(List<T> entities, Predicate<T> predicate) {
        return entities.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    private Object convertValue(Class<?> type, String rawValue) {
        if (type == String.class) return rawValue;
        if (type == int.class || type == Integer.class) return Integer.parseInt(rawValue);
        if (type == double.class || type == Double.class) return Double.parseDouble(rawValue);
        if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(rawValue);
        if (type == LocalDate.class) return LocalDate.parse(rawValue); // yyyy-MM-dd
        if (type.isEnum()) return Enum.valueOf((Class<Enum>) type, rawValue.toUpperCase());

        throw new IllegalArgumentException("Unsupported type: " + type);
    }
}