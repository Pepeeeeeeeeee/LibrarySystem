package be.kdg.librarysystem.service;

import be.kdg.librarysystem.domain.Book;
import be.kdg.librarysystem.domain.enums.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class FilteringServiceTest {

    @InjectMocks
    private FilteringService filteringService;

    private static class TestEntity {
        private String name;
        private int age;
        private boolean active;
        private LocalDate birthDate;

        public TestEntity(String name, int age, boolean active, LocalDate birthDate) {
            this.name = name;
            this.age = age;
            this.active = active;
            this.birthDate = birthDate;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public boolean isActive() {
            return active;
        }

        public LocalDate getBirthDate() {
            return birthDate;
        }
    }

    private TestEntity entity1;
    private TestEntity entity2;
    private TestEntity entity3;
    private List<TestEntity> testEntities;

    @BeforeEach
    void setUp() {
        filteringService.clearCache();
        entity1 = new TestEntity("Alice", 30, true, LocalDate.of(1993, 5, 15));
        entity2 = new TestEntity("Bob", 25, false, LocalDate.of(1998, 10, 20));
        entity3 = new TestEntity("Charlie", 35, true, LocalDate.of(1988, 1, 1));
        testEntities = Arrays.asList(entity1, entity2, entity3);
    }

    @Test
    void preloadDomainFields_shouldCacheFieldsAndReturnNames() {
        List<String> fieldNames = filteringService.preloadDomainFields(TestEntity.class);

        assertNotNull(fieldNames);
        assertTrue(fieldNames.contains("name"));
        assertTrue(fieldNames.contains("age"));
        assertTrue(fieldNames.contains("active"));
        assertTrue(fieldNames.contains("birthDate"));
        assertEquals(4, fieldNames.size());
    }

    @Test
    void clearCache_shouldClearCachedFields() {
        filteringService.preloadDomainFields(TestEntity.class);
        List<String> fieldNames = filteringService.preloadDomainFields(TestEntity.class);
        assertFalse(fieldNames.isEmpty());

        filteringService.clearCache();

        List<String> newFieldNames = filteringService.preloadDomainFields(TestEntity.class);
        assertFalse(newFieldNames.isEmpty());
    }

    @Test
    void buildContainsPredicate_shouldFilterBySingleFieldCaseInsensitive() {
        Predicate<TestEntity> predicate = filteringService.buildContainsPredicate(TestEntity.class, "name", "ali");
        List<TestEntity> filtered = filteringService.filter(testEntities, predicate);

        assertEquals(1, filtered.size());
        assertEquals(entity1, filtered.get(0));
    }

    @Test
    void buildContainsPredicate_shouldReturnEmptyList_whenNoMatch() {
        Predicate<TestEntity> predicate = filteringService.buildContainsPredicate(TestEntity.class, "name", "xyz");
        List<TestEntity> filtered = filteringService.filter(testEntities, predicate);

        assertTrue(filtered.isEmpty());
    }

    @Test
    void buildContainsPredicate_shouldThrowException_forUnknownField() {
        assertThrows(IllegalArgumentException.class, () -> {
            filteringService.buildContainsPredicate(TestEntity.class, "nonExistentField", "value");
        });
    }

    @Test
    void buildMultiFieldPredicate_shouldFilterByMultipleFields() {
        Predicate<TestEntity> predicate = filteringService.buildMultiFieldPredicate(TestEntity.class, Arrays.asList("name", "age"), "o");
        List<TestEntity> filtered = filteringService.filter(testEntities, predicate);

        assertEquals(1, filtered.size());
        assertFalse(filtered.contains(entity1));
        assertTrue(filtered.contains(entity2));
        assertFalse(filtered.contains(entity3));
    }

    @Test
    void buildMultiFieldPredicate_shouldReturnEmptyList_whenNoMatch() {
        Predicate<TestEntity> predicate = filteringService.buildMultiFieldPredicate(TestEntity.class, Arrays.asList("name", "age"), "xyz");
        List<TestEntity> filtered = filteringService.filter(testEntities, predicate);

        assertTrue(filtered.isEmpty());
    }

    @Test
    void filter_shouldApplyGivenPredicate() {
        Predicate<TestEntity> predicate = entity -> entity.getAge() > 30;
        List<TestEntity> filtered = filteringService.filter(testEntities, predicate);

        assertEquals(1, filtered.size());
        assertEquals(entity3, filtered.get(0));
    }

    @Test
    void buildPredicate_stringEquals() {
        Predicate<TestEntity> predicate = filteringService.buildPredicate(TestEntity.class, "name", "=", "Alice");
        List<TestEntity> filtered = filteringService.filter(testEntities, predicate);
        assertEquals(1, filtered.size());
        assertEquals(entity1, filtered.get(0));
    }

    @Test
    void buildPredicate_stringNotEquals() {
        Predicate<TestEntity> predicate = filteringService.buildPredicate(TestEntity.class, "name", "!=", "Alice");
        List<TestEntity> filtered = filteringService.filter(testEntities, predicate);
        assertEquals(2, filtered.size());
        assertTrue(filtered.contains(entity2));
        assertTrue(filtered.contains(entity3));
    }

    @Test
    void buildPredicate_stringContains() {
        Predicate<TestEntity> predicate = filteringService.buildPredicate(TestEntity.class, "name", "contains", "li");
        List<TestEntity> filtered = filteringService.filter(testEntities, predicate);
        assertEquals(2, filtered.size());
        assertTrue(filtered.contains(entity1));
        assertFalse(filtered.contains(entity2));
        assertTrue(filtered.contains(entity3));
    }

    @Test
    void buildPredicate_stringNotContains() {
        Predicate<TestEntity> predicate = filteringService.buildPredicate(TestEntity.class, "name", "!contains", "li");
        List<TestEntity> filtered = filteringService.filter(testEntities, predicate);
        assertEquals(1, filtered.size());
        assertFalse(filtered.contains(entity1));
        assertTrue(filtered.contains(entity2));
        assertFalse(filtered.contains(entity3));
    }

    @Test
    void buildPredicate_intGreaterThan() {
        Predicate<TestEntity> predicate = filteringService.buildPredicate(TestEntity.class, "age", ">", "30");
        List<TestEntity> filtered = filteringService.filter(testEntities, predicate);
        assertEquals(1, filtered.size());
        assertEquals(entity3, filtered.get(0));
    }

    @Test
    void buildPredicate_intLessThan() {
        Predicate<TestEntity> predicate = filteringService.buildPredicate(TestEntity.class, "age", "<", "30");
        List<TestEntity> filtered = filteringService.filter(testEntities, predicate);
        assertEquals(1, filtered.size());
        assertEquals(entity2, filtered.get(0));
    }

    @Test
    void buildPredicate_intGreaterThanOrEqual() {
        Predicate<TestEntity> predicate = filteringService.buildPredicate(TestEntity.class, "age", ">=", "30");
        List<TestEntity> filtered = filteringService.filter(testEntities, predicate);
        assertEquals(2, filtered.size());
        assertTrue(filtered.contains(entity1));
        assertTrue(filtered.contains(entity3));
    }

    @Test
    void buildPredicate_intLessThanOrEqual() {
        Predicate<TestEntity> predicate = filteringService.buildPredicate(TestEntity.class, "age", "<=", "30");
        List<TestEntity> filtered = filteringService.filter(testEntities, predicate);
        assertEquals(2, filtered.size());
        assertTrue(filtered.contains(entity1));
        assertTrue(filtered.contains(entity2));
    }

    @Test
    void buildPredicate_booleanEqualsTrue() {
        Predicate<TestEntity> predicate = filteringService.buildPredicate(TestEntity.class, "active", "=", "true");
        List<TestEntity> filtered = filteringService.filter(testEntities, predicate);
        assertEquals(2, filtered.size());
        assertTrue(filtered.contains(entity1));
        assertTrue(filtered.contains(entity3));
    }

    @Test
    void buildPredicate_booleanEqualsFalse() {
        Predicate<TestEntity> predicate = filteringService.buildPredicate(TestEntity.class, "active", "=", "false");
        List<TestEntity> filtered = filteringService.filter(testEntities, predicate);
        assertEquals(1, filtered.size());
        assertEquals(entity2, filtered.get(0));
    }

    @Test
    void buildPredicate_localDateGreaterThan() {
        Predicate<TestEntity> predicate = filteringService.buildPredicate(TestEntity.class, "birthDate", ">", "1990-01-01");
        List<TestEntity> filtered = filteringService.filter(testEntities, predicate);
        assertEquals(2, filtered.size());
        assertTrue(filtered.contains(entity1));
        assertTrue(filtered.contains(entity2));
        assertFalse(filtered.contains(entity3));
    }

    @Test
    void buildPredicate_localDateLessThan() {
        Predicate<TestEntity> predicate = filteringService.buildPredicate(TestEntity.class, "birthDate", "<", "1995-01-01");
        List<TestEntity> filtered = filteringService.filter(testEntities, predicate);
        assertEquals(2, filtered.size());
        assertTrue(filtered.contains(entity1));
        assertFalse(filtered.contains(entity2));
        assertTrue(filtered.contains(entity3));
    }

    @Test
    void buildPredicate_unknownField_shouldReturnFalse() {
        Predicate<TestEntity> predicate = filteringService.buildPredicate(TestEntity.class, "nonExistent", "=", "value");
        List<TestEntity> filtered = filteringService.filter(testEntities, predicate);
        assertTrue(filtered.isEmpty());
    }

    @Test
    void buildPredicate_unsupportedOperator_shouldReturnFalse() {
        Predicate<TestEntity> predicate = filteringService.buildPredicate(TestEntity.class, "name", "unknownOp", "Alice");
        List<TestEntity> filtered = filteringService.filter(testEntities, predicate);
        assertTrue(filtered.isEmpty());
    }

    @Test
    void buildPredicate_invalidValueType_shouldThrowException() {
        Predicate<TestEntity> predicate = filteringService.buildPredicate(TestEntity.class, "age", "=", "abc");

        assertThrows(NumberFormatException.class, () -> predicate.test(entity1));
    }

    @Test
    void buildPredicate_fieldValueIsNull_shouldReturnFalse() {
        TestEntity entityWithNullName = new TestEntity(null, 10, true, LocalDate.now());
        List<TestEntity> entities = Arrays.asList(entityWithNullName);
        Predicate<TestEntity> predicate = filteringService.buildPredicate(TestEntity.class, "name", "=", "test");
        List<TestEntity> filtered = filteringService.filter(entities, predicate);
        assertTrue(filtered.isEmpty());
    }

    @Test
    void buildPredicate_withBookClass_shouldWork() {
        Book book1 = new Book();
        book1.setTitle("The Lord of the Rings");
        book1.setAuthor("J.R.R. Tolkien");
        book1.setIsbn("978-0618053267");
        book1.setPublicationDate(LocalDate.of(1954, 1, 1));
        book1.setLocation("Allen & Unwin");
        book1.setGenre(Genre.FICTION);
        book1.setPages(0);
        book1.setPrice(0.0);
        book1.setAvailable(true);
        book1.setCoverImageUrl("");

        Book book2 = new Book();
        book2.setTitle("The Hobbit");
        book2.setAuthor("J.R.R. Tolkien");
        book2.setIsbn("978-0345339683");
        book2.setPublicationDate(LocalDate.of(1937, 1, 1));
        book2.setLocation("George Allen & Unwin");
        book2.setGenre(Genre.FICTION);
        book2.setPages(0);
        book2.setPrice(0.0);
        book2.setAvailable(true);
        book2.setCoverImageUrl("");

        List<Book> books = Arrays.asList(book1, book2);

        Predicate<Book> predicate = filteringService.buildPredicate(Book.class, "title", "contains", "lord");
        List<Book> filtered = filteringService.filter(books, predicate);
        assertEquals(1, filtered.size());
        assertEquals(book1, filtered.get(0));

        predicate = filteringService.buildPredicate(Book.class, "publicationDate", ">", "1940-01-01");
        filtered = filteringService.filter(books, predicate);
        assertEquals(1, filtered.size());
        assertEquals(book1, filtered.get(0));
    }
}