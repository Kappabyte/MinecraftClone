package net.kappabyte.kappaengine.scenes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.kappabyte.kappaengine.scenes.components.Camera;
import net.kappabyte.kappaengine.scenes.components.Component;
import net.kappabyte.kappaengine.util.Log;
import net.kappabyte.kappaengine.util.ReflectionUtil;
import net.kappabyte.kappaengine.window.Window;
public class Scene implements Parent {

    private Camera camera;

    private Window window;

    public ArrayList<GameObject> gameObjects = new ArrayList<>();

    public final <T extends Component> Collection<T> GetComponents(Class<T> clazz) {
        List<T> components = new ArrayList<>();
        for (GameObject object : gameObjects) {
            components.addAll(object.GetComponentsInChildren(clazz));
        }

        return components;
    }

    public final void setActiveCamera(Camera camera) {
        this.camera = camera;
    }

    public final Camera getActiveCamera() {
        return camera;
    }

    public final Window getWindow() {
        return window;
    }

    public void addGameObject(GameObject gameObject) {
        try {
            ReflectionUtil.setPrivateFieldValue(GameObject.class.getDeclaredField("parent"), gameObject, this);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

        gameObjects.add(gameObject);
    }

    public void removeGameObject(Object gameObject) {
        try {
            ReflectionUtil.setPrivateFieldValue(GameObject.class.getDeclaredField("parent"), gameObject, null);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

        gameObjects.remove(gameObject);
    }

    @Override
    public GameObject[] getChildren() {
        GameObject[] children = new GameObject[gameObjects.size()];
        children = gameObjects.toArray(children);

        return children;
    }

    public void printSceneHeirarchy(int depth, GameObject[] objects) {
        for(GameObject object : objects) {
            Log.debug(" ".repeat(depth) + "-" + object.getName());
            printSceneHeirarchy(depth + 1, object.getChildren());
        }
    }
}
