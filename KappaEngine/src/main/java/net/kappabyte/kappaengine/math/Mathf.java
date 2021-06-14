package net.kappabyte.kappaengine.math;

public class Mathf {
    public static float clamp(float value, float from, float to) {
        if(value < from) value = from;
        if(value > to) value = to;

        return value;
    }
}
