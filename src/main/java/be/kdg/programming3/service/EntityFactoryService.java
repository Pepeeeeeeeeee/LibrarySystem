package be.kdg.programming3.service;

import org.springframework.stereotype.Service;
import java.lang.reflect.Constructor;


/**
 * Service responsible for dynamically creating entity instances using reflection.
 *
 * <p>
 * This factory abstracts away direct constructor calls and allows entities to be
 * instantiated at runtime based only on their {@link Class} reference and constructor
 * arguments.
 * </p>
 *
 * <p>
 * It is primarily used to decouple object creation logic from higher-level components
 * (such as presenters or controllers), making the application more flexible and
 * generic when working with different domain types.
 * </p>
 */
@Service
public class EntityFactoryService {

    /**
     * Creates a new instance of the given class using reflection.
     *
     * <p>
     * This method:
     * <ol>
     *     <li>Retrieves the first declared constructor of the given class</li>
     *     <li>Invokes that constructor using the provided arguments</li>
     *     <li>Casts the resulting object to the requested type</li>
     * </ol>
     * </p>
     *
     * <p>
     * The order, number, and types of the {@code args} must exactly match the parameters
     * of the selected constructor, otherwise instantiation will fail at runtime.
     * </p>
     *
     * @param clazz the {@link Class} object representing the entity type to create
     * @param args  the arguments to pass to the entity constructor
     * @param <T>   the type of entity being created
     *
     * @return a new instance of the specified entity type
     *
     * @throws RuntimeException if the constructor cannot be accessed, invoked,
     *                          or if instantiation fails for any reason
     */
    public <T> T createEntity(Class<T> clazz, Object... args) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
            return clazz.cast(constructor.newInstance(args));
        } catch (Exception e) {
            throw new RuntimeException("Could not create entity", e);
        }
    }
}
