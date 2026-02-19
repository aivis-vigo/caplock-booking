package com.caplock.booking.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class Mapper {

    /** many DAOs -> One Dto **/
    public static <T> T combine(Class<T> targetClass, Object... sources) {
        return combine(targetClass, Map.of(), sources);
    }

    /** many DAOs -> One Dto with aliases **/
    public static <T> T combine(Class<T> targetClass, Map<String, String> aliases, Object... sources) {
        T target = newInstance(targetClass);
        if (sources == null) return target;

        for (Object src : sources) {
            if (src != null) copyInto(src, target, aliases);
        }
        return target;
    }

    /** One DTO -> many DAOs (or generally one source -> many targets). */
    public static Map<Class<?>, Object> split(Object source, Class<?>... targetClasses) {
        return split(source, Map.of(), targetClasses);
    }

    /** One DTO -> many DAOs with aliases. */
    public static Map<Class<?>, Object> split(Object source, Map<String, String> aliases, Class<?>... targetClasses) {
        Map<Class<?>, Object> result = new LinkedHashMap<>();
        if (targetClasses == null) return result;

        for (Class<?> cls : targetClasses) {
            Object target = newInstanceRaw(cls);
            if (source != null) copyInto(source, target, aliases);
            result.put(cls, target);
        }
        return result;
    }

    public static <T> T splitOne(Object source, Class<T> targetClass) {
        return splitOne(source, targetClass, Map.of());
    }

    public static <T> T splitOne(Object source, Class<T> targetClass, Map<String, String> aliases) {
        T target = newInstance(targetClass);
        if (source != null) copyInto(source, target, aliases);
        return target;
    }

    // ------------------ core copy ------------------

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

    /** Copy with aliases (aliases can be empty). */
    public static <T> T copyInto(Object source, T target, Map<String, String> aliases) {
        if (source == null || target == null) return target;

        // NEVER reflect into JDK/framework restricted types (prevents InaccessibleObjectException)
        if (!isSafeToReflect(source.getClass())) return target;

        Map<String, Field> targetFields = allFieldsByName(target.getClass());

        for (Field sf : allFields(source.getClass())) {
            if (Modifier.isStatic(sf.getModifiers())) continue;

            // Special case: flatten nested "eventDto" (EventDetailsDto -> EventDao)
            if ("eventDto".equals(sf.getName())) {
                try {
                    sf.setAccessible(true);
                    Object nested = sf.get(source);
                    if (nested != null && isSafeToReflect(nested.getClass())) {
                        copyInto(nested, target, aliases);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to read nested field: " + sf.getName(), e);
                }
                continue;
            }

            String targetName = (aliases == null || aliases.isEmpty())
                    ? sf.getName()
                    : aliases.getOrDefault(sf.getName(), sf.getName());

            Field tf = targetFields.get(targetName);
            if (tf == null) continue;
            if (Modifier.isStatic(tf.getModifiers()) || Modifier.isFinal(tf.getModifiers())) continue;

            try {
                sf.setAccessible(true);
                Object value = sf.get(source);

                tf.setAccessible(true);

                // don't set null into primitives
                if (value == null) {
                    if (!tf.getType().isPrimitive()) tf.set(target, null);
                    continue;
                }

                Class<?> targetType = boxed(tf.getType());

                // direct assign
                if (targetType.isAssignableFrom(value.getClass())) {
                    tf.set(target, value);
                    continue;
                }

                // numeric widening
                if (value instanceof Number n && Number.class.isAssignableFrom(targetType)) {
                    Object converted = convertNumber(n, targetType);
                    if (converted != null) tf.set(target, converted);
                    continue;
                }

                // Set -> List (useful for seats)
                if (value instanceof Set<?> s && List.class.isAssignableFrom(targetType)) {
                    tf.set(target, new ArrayList<>(s));
                    continue;
                }

                // Enum from String (when source string equals enum constant)
                if (value instanceof String str && tf.getType().isEnum()) {
                    @SuppressWarnings("unchecked")
                    Class<? extends Enum> enumType = (Class<? extends Enum>) tf.getType();
                    try {
                        Object enumVal = Enum.valueOf(enumType, str);
                        tf.set(target, enumVal);
                    } catch (IllegalArgumentException ignored) {
                        // skip if not matching
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

    private static boolean isSafeToReflect(Class<?> c) {
        String p = (c.getPackageName() == null) ? "" : c.getPackageName();

        // block JDK + frameworks (avoid module access issues)
        if (p.startsWith("java.") || p.startsWith("javax.") || p.startsWith("jakarta.")
                || p.startsWith("sun.") || p.startsWith("com.sun.")
                || p.startsWith("org.springframework.")) {
            return false;
        }

        // avoid flattening/reflecting wrappers and simple types
        if (c.isPrimitive() || c.isEnum() || c == String.class
                || Number.class.isAssignableFrom(c)
                || c == Boolean.class || c == Character.class) {
            return false;
        }

        return true;
    }
}
