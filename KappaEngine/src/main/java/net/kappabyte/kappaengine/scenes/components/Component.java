package net.kappabyte.kappaengine.scenes.components;

import net.kappabyte.kappaengine.scenes.GameObject;
import net.kappabyte.kappaengine.scenes.Scene;
import net.kappabyte.kappaengine.scenes.Transform;
import net.kappabyte.kappaengine.window.Window;

public abstract class Component {

    private GameObject gameObject;

    public Component() {
    }

    public static String getName() {
        return "Component";
    }

    public final GameObject getGameObject() {
        return gameObject;
    }

    public final Transform getTransform() {
        return gameObject.getTransform();
    }

    public final Scene getScene() {
        return gameObject.getScene();
    }

    public final Window getWindow() {
        return gameObject.getScene().getWindow();
    }

    public abstract void onStart();
    public abstract void onUpdate();
    public abstract void onDestroy();
}
