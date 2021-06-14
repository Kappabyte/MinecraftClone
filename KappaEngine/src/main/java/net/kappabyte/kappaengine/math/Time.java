package net.kappabyte.kappaengine.math;

public class Time {

    static long lastFrame = 0;
    static int fps;
    static long lastFPS;
    static float delta;

    public static long getTime() {
        return System.currentTimeMillis();
    }

    private static void updateDeltaTime() {
        if(lastFrame == 0) lastFrame = getTime();
        long time = getTime();
        delta = (time - lastFrame) / 1000.0f;
        lastFrame = time;
    }

    public static float deltaTime() {
        return delta;
    }

    public static void start() {
        //some startup code
        lastFPS = getTime(); //set lastFPS to current Time
        lastFrame = getTime();
    }

    public static int getFPS() {
        return fps;
    }

    private static void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }

    public static void update() {
        updateDeltaTime();
        updateFPS();
    }
}
