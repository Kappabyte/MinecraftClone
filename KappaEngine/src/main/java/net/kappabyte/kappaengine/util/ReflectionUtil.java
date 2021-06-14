package net.kappabyte.kappaengine.util;

import java.lang.reflect.Field;

public class ReflectionUtil {
    public static void setPrivateFieldValue(Field field, Object object, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
            Log.error("failed to update field for " + object.getClass().getSimpleName() + ".");
            e.printStackTrace();
        }
    }
}
