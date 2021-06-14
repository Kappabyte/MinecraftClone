package net.kappabyte.kappaengine;

import static org.lwjgl.glfw.GLFW.glfwGetVersionString;
import static org.lwjgl.glfw.GLFW.glfwInit;

import org.lwjgl.glfw.GLFWErrorCallback;

import net.kappabyte.kappaengine.applicaiton.Application;
import net.kappabyte.kappaengine.math.Time;
import net.kappabyte.kappaengine.util.Log;

public final class KappaEngine {

    private static KappaEngine instance;

    private static final String version = "1.0.0";

    private KappaEngine() {
    }

    /**
     * Initalize KappaEngine
     */
    public static void init(Application app) {
        if(instance != null) {
            throw new IllegalStateException("Already Initalized");
        }

        if(getVersion() < 11) {
            Log.fatal("KappaEngine requires at least Java 11 to run! (Running in legacy mode)");
            System.exit(-1);
        }

        instance = new KappaEngine();

        Time.start();

        Log.info("Initalized KappaEngine!");
        Log.info("Version: " + version);

        Log.debug("Initalizing OpenGL");
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            Log.fatal("Failed To Initalize OpenGL! Does your system support it?");
            Log.stack();
            System.exit(-1);
        }

        Log.info("OpenGL Initalized!");
        Log.info("Version: " + glfwGetVersionString());

        app.start();
    }

    public static KappaEngine getInstance() {
        if(instance == null) {
            throw new IllegalStateException("Not initalized");
        }
        return instance;
    }

    private static int getVersion() {
        String version = System.getProperty("java.version");
        if(version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf(".");
            if(dot != -1) { version = version.substring(0, dot); }
        } return Integer.parseInt(version.split("-")[0]);
    }
}
