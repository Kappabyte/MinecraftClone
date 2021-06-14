package net.kappabyte.kappaengine.scenes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joml.Vector3f;

import net.kappabyte.kappaengine.scenes.components.Component;
import net.kappabyte.kappaengine.util.ReflectionUtil;

public class GameObject implements Parent {
    private String name;

    private Transform transform;

    private ArrayList<GameObject> children = new ArrayList<>();

    private Parent parent;

    public GameObject(String name) {
        this.name = name;

        transform = new Transform(new Vector3f(), new Vector3f(), new Vector3f(1,1,1));
        try {
            ReflectionUtil.setPrivateFieldValue(Component.class.getDeclaredField("gameObject"), transform, this);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public Transform getTransform() {
        return transform;
    }

    public String getName() {
        return name;
    }

    private ArrayList<Component> objectComponents = new ArrayList<>();

    public final <T extends Component> T GetComponent(Class<T> clazz) {
        for(Component component : objectComponents) {
            if(clazz.isInstance(component)) {
                return clazz.cast(component);
            }
        }

        return null;
    }

    public final <T extends Component> Collection<T> GetComponents(Class<T> clazz) {
        List<T> valid = new ArrayList<>();
        for(Component component : objectComponents) {
            if(clazz.isInstance(component)) {
                valid.add(clazz.cast(component));
            }
        }

        return valid;
    }

    public final <T extends Component> Collection<T> GetComponentsInChildren(Class<T> clazz) {
        List<T> valid = new ArrayList<>();
        for(Component component : objectComponents) {
            if(clazz.isInstance(component)) {
                valid.add(clazz.cast(component));
            }
        }

        for(GameObject object : children) {
            valid.addAll(object.GetComponentsInChildren(clazz));
        }

        return valid;
    }

    public final Scene getScene() {
        if(parent == null) return null;

        if(parent instanceof Scene) {
            return (Scene) parent;
        }

        if(parent instanceof GameObject) {
            return ((GameObject) parent).getScene();
        }

        return null;
    }

    public boolean addComponent(Component e) {
        try {
            ReflectionUtil.setPrivateFieldValue(Component.class.getDeclaredField("gameObject"), e, this);
        } catch (NoSuchFieldException | SecurityException e1) {
            e1.printStackTrace();
        }
        e.onStart();
        return objectComponents.add(e);
    }

    public boolean removeComponent(Object o) {
        if(o instanceof Component) {
            ((Component)o).onDestroy();
            try {
                ReflectionUtil.setPrivateFieldValue(Component.class.getDeclaredField("gameObject"), o, null);
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
        }
        return objectComponents.remove(o);
    }

    public void setParent(Parent parent) {
        if(this.parent != null) {
            if(parent instanceof GameObject) {
                ((GameObject) this.parent).children.remove(this);
            } else if(parent instanceof Scene) {
                ((Scene) this.parent).gameObjects.remove(this);
            }
        }

        this.parent = parent;
        if(parent instanceof GameObject) {
            ((GameObject) parent).children.add(this);
        } else if(parent instanceof Scene) {
            ((Scene) parent).gameObjects.add(this);
        }
    }

    public void addChild(GameObject child) {
        child.setParent(this);
    }

    public Parent getParent() {
        return parent;
    }

    public GameObject getParentGameObject() {
        if(parent instanceof GameObject) {
            return (GameObject) parent;
        }

        return null;
    }

    public GameObject[] getChildren() {
        GameObject[] children = new GameObject[this.children.size()];
        children = this.children.toArray(children);

        return children;
    }
}
