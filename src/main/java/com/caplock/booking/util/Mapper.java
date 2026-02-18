package com.caplock.booking.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class Mapper {

    /**  many DAOs -> One Dto**/
    public static <T> T combine(Class<T> targetClass, Object... sources) {
        T target = newInstance(targetClass);
        if (sources == null) return target;

        for (Object src : sources) {
            if (src != null) copyInto(src, target);
        }
        return target;
    }

    /** One DTO -> many DAOs (or generally one source -> many targets). */
    public static Map<Class<?>, Object> split(Object source, Class<?>... targetClasses) {
        Map<Class<?>, Object> result = new LinkedHashMap<>();
        if (targetClasses == null) return result;

        for (Class<?> cls : targetClasses) {
            Object target = newInstanceRaw(cls);
            if (source != null) copyInto(source, target);
            result.put(cls, target);
        }
        return result;
    }

    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = Map.of(
            boolean.class, Boolean.class,
            byte.class, Byte.class,
            short.class, Short.class,
            int.class, Integer.class,
            long.class, Long.class,
            float.class, Float.class,
            double.class, Double.class,
            char.class, Character.class
    );

    private static Class<?> boxed(Class<?> type) {
        return type.isPrimitive() ? PRIMITIVE_TO_WRAPPER.getOrDefault(type, type) : type;
    }

    public static <T> T copyInto(Object source, T target) {
        if (source == null || target == null) return target;

        Map<String, Field> targetFields = allFieldsByName(target.getClass());

        for (Field sf : allFields(source.getClass())) {
            if (Modifier.isStatic(sf.getModifiers())) continue;

            Field tf = targetFields.get(sf.getName());
            if (tf == null) continue;
            if (Modifier.isStatic(tf.getModifiers()) || Modifier.isFinal(tf.getModifiers())) continue;

            try {
                sf.setAccessible(true);
                Object value = sf.get(source);

                tf.setAccessible(true);

                if (value == null) {
                    // don't set null into primitives (would throw)
                    if (!tf.getType().isPrimitive()) tf.set(target, null);
                    continue;
                }

                Class<?> targetType = boxed(tf.getType());

                // direct assign (handles int <- Integer, long <- Long, etc.)
                if (targetType.isAssignableFrom(value.getClass())) {
                    tf.set(target, value);
                    continue;
                }

                // optional: numeric widening (e.g., Integer -> Long)
                if (value instanceof Number n && Number.class.isAssignableFrom(targetType)) {
                    Object converted = convertNumber(n, targetType);
                    if (converted != null) {
                        tf.set(target, converted);
                    }
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to copy field: " + sf.getName(), e);
            }
        }
        return target;
    }

    private static Object convertNumber(Number n, Class<?> targetWrapperType) {
        if (targetWrapperType == Long.class) return n.longValue();
        if (targetWrapperType == Integer.class) return n.intValue();
        if (targetWrapperType == Short.class) return n.shortValue();
        if (targetWrapperType == Byte.class) return n.byteValue();
        if (targetWrapperType == Double.class) return n.doubleValue();
        if (targetWrapperType == Float.class) return n.floatValue();
        return null;
    }

    private static List<Field> allFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = type; c != null && c != Object.class; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

    private static Map<String, Field> allFieldsByName(Class<?> type) {
        Map<String, Field> map = new HashMap<>();
        for (Field f : allFields(type)) {
            map.putIfAbsent(f.getName(), f);
        }
        return map;
    }

    private static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate " + clazz.getName(), e);
        }
    }

    private static Object newInstanceRaw(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate " + clazz.getName(), e);
        }
    }

    public static <T> T splitOne(Object source, Class<T> targetClass) {
        T target = newInstance(targetClass);
        if (source != null) copyInto(source, target);
        return target;
    }
}

