package be.kdg.librarysystem.service;

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
    /**
     * Cache that maps a domain class to a map of its field names and Field objects.
     *
     * Why this exists:
     * - Reflection (Class#getDeclaredFields) is relatively expensive.
     * - Filtering often happens repeatedly on the same domain type.
     * - Caching avoids scanning fields every time a filter is built.
     *
     * Thread-safe because this service can be used by multiple callers.
     */
    private final Map<Class<?>, Map<String, Field>> domainFieldCache = new ConcurrentHashMap<>();
    private Class<?> activeDomain;

    /**
     * Preloads and caches all declared fields of a given domain class.
     *
     * How it works:
     * - If the requested domain class is different from the previously used one,
     *   the cache is cleared to avoid mixing fields from different entities.
     * - All declared fields are made accessible (setAccessible(true)) so that
     *   private fields can be read using reflection.
     * - Fields are stored in a map keyed by field name for fast lookup.
     *
     * Why it returns a list of field names:
     * - Useful for UI layers (CLI, menu, or form builders) to show
     *   which fields can be filtered.
     *
     * @param domainClass the entity class whose fields should be cached
     * @return a list of field names available for filtering
     */
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

    /**
     * Clears the cached domain fields and resets the active domain.
     *
     * Why this exists:
     * - Allows manual cache invalidation if domain models change
     *   or if memory cleanup is desired.
     */
    public void clearCache() {
        domainFieldCache.clear();
        activeDomain = null;
    }

    /**
     * Builds a predicate that checks whether a single field contains a given value.
     *
     * How it works:
     * - Ensures the domain fields are cached.
     * - Retrieves the Field object for the given field name.
     * - Returns a Predicate that:
     *   - Reads the field value from an entity via reflection
     *   - Converts it to String
     *   - Performs a case-insensitive contains check
     *
     * Why deprecated:
     * - It only supports a single field.
     * - {@link #buildMultiFieldPredicate(Class, List, String)} is more flexible
     *   and better suited for real search functionality.
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

    /**
     * Builds a predicate that checks multiple fields for a "contains" match.
     *
     * How it works:
     * - Ensures fields of the domain class are cached.
     * - Iterates over the provided field names.
     * - For each field:
     *   - Reads its value using reflection
     *   - Converts it to String
     *   - Performs a case-insensitive contains check
     * - Returns true as soon as one field matches.
     *
     * Why this is preferred:
     * - Enables global search functionality (e.g. search across title, author, ISBN).
     * - Avoids building multiple predicates manually.
     *
     * @param domainClass the entity type being filtered
     * @param fieldNames list of fields to search through
     * @param value the value to search for
     */
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

    /**
     * Applies a predicate to a list of items and returns the filtered result.
     *
     * How it works:
     * - Delegates filtering to Java Streams.
     * - Keeps this service focused on predicate construction logic.
     *
     * @param items the list of entities to filter
     * @param predicate the condition to apply
     * @return filtered list
     */
    public <T> List<T> filter(List<T> items, Predicate<T> predicate) {
        return items.stream().filter(predicate).toList();
    }

    /**
     * Builds a predicate based on a field name, operator, and raw string value.
     *
     * Supported operators:
     * - Strings: =, !=, contains, !contains
     * - Comparable types: >, <, >=, <=
     *
     * How it works:
     * - Finds the field using case-insensitive matching.
     * - Reads the field value via reflection.
     * - Converts the raw string input to the field's actual type.
     * - Applies the operator dynamically.
     *
     * Why deprecated:
     * - Complex, error-prone, and mixes parsing with filtering logic.
     * - Multi-field predicates are cleaner and safer.
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

    /**
     * Converts a raw string value into the correct Java type for a field.
     *
     * Supported types:
     * - String
     * - int / Integer
     * - double / Double
     * - boolean / Boolean
     * - LocalDate (ISO-8601)
     * - Enum values
     *
     * Why this exists:
     * - User input is always text.
     * - Reflection gives you field types at runtime.
     * - This method bridges the two safely.
     */
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