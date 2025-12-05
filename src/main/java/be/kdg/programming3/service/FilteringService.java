package be.kdg.programming3.service;

import org.springframework.stereotype.Service;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class FilteringService {
    private final Map<Class<?>, Map<String, Field>> domainFieldCache = new ConcurrentHashMap<>();
    private Class<?> activeDomain;

    public List<String> preloadDomainFields(Class<?> domainClass) {
        if (!domainClass.equals(activeDomain)) {
            domainFieldCache.clear();

            Map<String, Field> fieldMap = Arrays.stream(domainClass.getDeclaredFields())
                    .peek(f -> f.setAccessible(true))
                    .collect(Collectors.toMap(Field::getName, f -> f));

            domainFieldCache.put(domainClass, fieldMap);
            activeDomain = domainClass;
        }
        return domainFieldCache.get(domainClass).keySet().stream().toList();
    }

    public void clearCache() {
        domainFieldCache.clear();
        activeDomain = null;
    }

    /**
     * @deprecated Use {@link #buildMultiFieldPredicate(Class, List, String)} instead.
     */
    @Deprecated
    public <T> Predicate<T> buildContainsPredicate(Class<T> domainClass, String fieldName, String value) {
        preloadDomainFields(domainClass);
        Field field = domainFieldCache.get(domainClass).get(fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Unknown field: " + fieldName);
        }

        return entity -> {
            try {
                Object fieldValue = field.get(entity);
                if (fieldValue == null) return false;
                return fieldValue.toString().toLowerCase().contains(value.toLowerCase());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public <T> Predicate<T> buildMultiFieldPredicate(Class<T> domainClass, List<String> fieldNames, String value) {
        preloadDomainFields(domainClass);
        return entity -> {
            for (String fieldName : fieldNames) {
                Field field = domainFieldCache.get(domainClass).get(fieldName);
                if (field == null) continue;
                try {
                    Object fieldValue = field.get(entity);
                    if (fieldValue != null && fieldValue.toString().toLowerCase().contains(value.toLowerCase())) {
                        return true; // match found
                    }
                } catch (IllegalAccessException ignored) {}
            }
            return false;
        };
    }

    public <T> List<T> filter(List<T> items, Predicate<T> predicate) {
        return items.stream().filter(predicate).toList();
    }

    /**
     * @deprecated Use {@link #buildMultiFieldPredicate(Class, List, String)} instead.
     */
    @Deprecated
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
                Object fieldValue = field.get(2);
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