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
	
	if(getVersion() < 8) {
	    Log.fatal("Java 8 or higher is required to run this program. Please upgrade your java version to run this application.");
	    System.exit(-1);
	}
        if(getVersion() < 11) {
            Log.warn("You are using an outdated version on Java. It is recommended to upgrade to Java 11 or higher!");
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
