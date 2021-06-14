package net.kappabyte.kappaengine.window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.lang.reflect.Field;
import java.nio.IntBuffer;
import java.util.Collection;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryUtil;

import net.kappabyte.kappaengine.input.InputManager;
import net.kappabyte.kappaengine.math.Time;
import net.kappabyte.kappaengine.scenes.GameObject;
import net.kappabyte.kappaengine.scenes.Scene;
import net.kappabyte.kappaengine.scenes.components.Component;
import net.kappabyte.kappaengine.scenes.components.Renderable;
import net.kappabyte.kappaengine.ui.UIRenderable;
import net.kappabyte.kappaengine.util.Log;

public abstract class Window {

    private String title;
    private long handle;

    private Scene scene;

    private InputManager inputManager;

    private GLCapabilities capabilities;

    public Window(String title) {
        this.title = title;

        initalizeWindow();
        inputManager = new InputManager(this);
    }

    int width = 1000;
    int height = 700;
    protected final void initalizeWindow() {
        // Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
		handle = glfwCreateWindow(width, height, title, NULL, NULL);
		if ( handle == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(handle, (handle, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(handle, true); // We will detect this in the rendering loop
		});

        // Make the OpenGL context current
		glfwMakeContextCurrent(handle);
		// Enable v-sync
		glfwSwapInterval(1);

        // glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        // if(glfwRawMouseMotionSupported()) {
        //     glfwSetInputMode(handle, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);
        // }

		// Make the window visible
		glfwShowWindow(handle);
    }

    public final InputManager getInputManager() {
        return inputManager;
    }

    public final void update() {

        //Log.info("Window Update");
        if(capabilities != null) {
            GL.setCapabilities(capabilities);
        } else {
            capabilities = GL.createCapabilities();
            onWindowReady();
        }
        GLFW.glfwMakeContextCurrent(handle);



        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(handle, width, height);

        if (width.get(0) != this.width || height.get(0) != this.height) {
            glViewport(0, 0, width.get(0), height.get(0));
            this.width = width.get(0);
            this.height = height.get(0);
            scene.getActiveCamera().updateProjectionMatrix();
        }

		glClearColor(0.0f, 0.5f, 0.5f, 0.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        if(scene != null) {
            Render();
        }

        for(GameObject object : scene.gameObjects) {
            for(Component component : object.GetComponents(Component.class)) {
                component.onUpdate();
            }
        }


        GL46.glEnable(GL46.GL_DEBUG_OUTPUT);
        GL46.glDebugMessageCallback((source, type, id, severity, length, messagePointer, userParam) -> {
            String message = MemoryUtil.memUTF8(messagePointer);
            if(type == GL46.GL_DEBUG_TYPE_ERROR) {
                Log.fatal("Open GL error: " + message);
                Log.stack();
            } else if(type == GL46.GL_DEBUG_TYPE_PERFORMANCE) {
                Log.debug("Open GL performance: " + message);
            }
        }, 0);

		glfwSwapBuffers(handle); // swap the color buffers

        GLFW.glfwSetErrorCallback((errorCode, handle) -> {
            Log.error("Open Gl Error: " + errorCode);
        });

		// Poll for window events. The key callback above will only be
		// invoked during this call.
		glfwPollEvents();

        Time.update();
    }

    protected abstract void onSceneChange();
    protected abstract void onWindowReady();

    public final int getWidth() {
        return width;
    }

    public final int getHeight() {
        return height;
    }

    public final void Render() {
        if(scene.getActiveCamera() == null) return;
        Collection<Renderable> sceneRenderables = scene.GetComponents(Renderable.class);
        for(Renderable renderable : sceneRenderables) {
            if(renderable.shouldRender()) {
                Log.info("Rendering " + renderable.getGameObject().getName());
                renderable.Render();
            }
        }

        Collection<UIRenderable> uiRenderables = scene.GetComponents(UIRenderable.class);
        for(UIRenderable renderable : uiRenderables) {
            if(renderable.shouldRender()) {
                renderable.Render();
            }
        }

        getInputManager().update();
    }

    public final void closeWindow() {
        Log.info("Window " + handle + " has been marked to be closed!");
        glfwSetWindowShouldClose(handle, true);
    }

    public final void setScene(Scene scene) {
        if(this.scene != null) {
            setSceneWindow(this.scene, null);
        }
        this.scene = scene;
        setSceneWindow(scene, this);
        onSceneChange();
    }

    public final Scene getScene() {
        return scene;
    }

    public final void destroyWindow() {
        Log.debug("Window was destroyed!");
        glfwDestroyWindow(handle);
    }
    public final long getHandle() {
        return handle;
    }

    private void setSceneWindow(Scene scene, Window window) {
        try {
            Field field = scene.getClass().getDeclaredField("window");
            field.setAccessible(true);
            field.set(scene, window);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
