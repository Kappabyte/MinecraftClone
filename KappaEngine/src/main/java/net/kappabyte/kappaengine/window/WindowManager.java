package net.kappabyte.kappaengine.window;

import java.util.ArrayList;

import net.kappabyte.kappaengine.util.Log;

import static org.lwjgl.glfw.GLFW.*;

public class WindowManager {

    private static ArrayList<Window> registeredWindows = new ArrayList<>();
    private static ArrayList<Window> markedForRemoval = new ArrayList<>();

    public static boolean update() {
        boolean ret = true;
        for(Window window : registeredWindows) {
            window.update();
            if(glfwWindowShouldClose(window.getHandle())) {
                window.destroyWindow();
                markedForRemoval.add(window);
                ret = false;
            }
        }
        registeredWindows.removeAll(markedForRemoval);
        markedForRemoval.removeAll(markedForRemoval);

        return ret;
    }

    public static void registerWindow(Window e) {
        Log.info("Registered window: " + e.getHandle());
        registeredWindows.add(e);
    }
}
