package ru.otus.homework;

import ru.otus.homework.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;


public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;

    private String cacheName;
    private Constructor<T> cacheConstructor;
    private boolean idFieldIsPresented;
    private Field cacheIdField;
    private List<Field> cacheAllFields;
    private List<Field> cacheFieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
        defineClassConstructor(clazz);
    }
    @SuppressWarnings("unchecked")
    private void defineClassConstructor(final Class<T> entityClass) {
        Constructor<?>[] constructors = entityClass.getDeclaredConstructors();
        this.cacheConstructor = (Constructor<T>) stream(constructors)
                .filter(e -> e.getParameterCount() == getFieldsWithoutId().size() + 1)
                .findAny().orElseThrow(() -> new RuntimeException("Can't define correct persistence constructor"));
    }
    @Override
    public String getName() {
        if (cacheName == null) {
            cacheName = clazz.getSimpleName();
        }
        return cacheName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Constructor<T> getConstructor() {
        if (cacheConstructor == null) {
            cacheConstructor = (Constructor<T>) clazz.getConstructors()[0];
        }
        return cacheConstructor;
    }

    // Возвращает ссылку на поле класса помеченную аннотацией @Id.
    // Если такого поля нет в классе, вернется null.
    @Override
    public Field getIdField() {
        if (idFieldIsPresented) {
            return cacheIdField;
        }
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                idFieldIsPresented = true;
                return cacheIdField  = field;
            }
        }
        return null;
    }

    @Override
    public List<Field> getAllFields() {
        if (cacheAllFields == null) {
            cacheAllFields = List.of(clazz.getDeclaredFields());
        }
        return cacheAllFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        if (cacheFieldsWithoutId != null) {
            return cacheFieldsWithoutId;
        }
        cacheFieldsWithoutId = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Id.class)) {
                cacheFieldsWithoutId.add(field);
            }
        }
        return cacheFieldsWithoutId;
    }
}
