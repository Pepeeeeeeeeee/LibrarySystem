package be.kdg.programming3.service;

import org.springframework.stereotype.Service;
import java.lang.reflect.Constructor;

@Service
public class EntityFactoryService {

    public <T> T createEntity(Class<T> clazz, Object... args) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
            return clazz.cast(constructor.newInstance(args));
        } catch (Exception e) {
            throw new RuntimeException("Could not create entity", e);
        }
    }
}
