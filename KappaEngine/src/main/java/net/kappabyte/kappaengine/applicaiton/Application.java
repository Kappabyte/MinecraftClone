package net.kappabyte.kappaengine.applicaiton;

import net.kappabyte.kappaengine.window.WindowManager;

public abstract class Application {
    public Application() {

    }

    public final void start() {
        onStart();
        while(true) {
            onUpdate();
            if(!WindowManager.update()) {
                break;
            }
        }
    }

    public void onStart() {}

    public void onUpdate() {}
}
