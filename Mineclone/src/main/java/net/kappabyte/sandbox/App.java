package net.kappabyte.sandbox;

import net.kappabyte.kappaengine.KappaEngine;
import net.kappabyte.kappaengine.applicaiton.Application;
import net.kappabyte.kappaengine.scenes.Scene;
import net.kappabyte.kappaengine.util.Log;
import net.kappabyte.kappaengine.util.Log.LogLevel;
import net.kappabyte.kappaengine.window.Window;
import net.kappabyte.kappaengine.window.WindowManager;

/**
 * Hello world!
 */
public final class App extends Application {

    public static void main(String[] args) {
        Log.setLogLevel(LogLevel.DEBUG);
        KappaEngine.init(new App());
    }

    @Override
    public void onStart() {
        Window window = new AppWindow("Mineclone");
        WindowManager.registerWindow(window);
        window.setScene(new Scene());
    }

    @Override
    public void onUpdate() {
    }
}
