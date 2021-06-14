package net.kappabyte.sandbox;

import net.kappabyte.kappaengine.physics.Rigidbody;
import net.kappabyte.kappaengine.scenes.GameObject;
import net.kappabyte.kappaengine.scenes.Scene;
import net.kappabyte.kappaengine.scenes.components.Camera;
import net.kappabyte.kappaengine.window.Window;
import net.kappabyte.sandbox.components.CharacterController;
import net.kappabyte.sandbox.terrain.World;

import org.joml.Vector3f;

public class AppWindow extends Window {

    Scene game;

    public AppWindow(String title) {
        super(title);
    }

    @Override
    protected void onSceneChange() {
    }

    @Override
    protected void onWindowReady() {
        game = new Scene();
        setScene(game);

        GameObject world = new GameObject("World");
        World worldComponent = new World();
        world.addComponent(worldComponent);
        for(int x = 0; x < 5; x++) {
            for(int y = 0; y < 5; y++) {
                worldComponent.spawnChunk(x, y);
            }
        }
        game.addGameObject(world);

        GameObject player = new GameObject("Player");
        GameObject camera = new GameObject("Camera");
        player.addChild(camera);
        camera.addComponent(new Camera(this));
        player.addComponent(new Rigidbody(1.0f));
        player.addComponent(new CharacterController(camera));
        getScene().addGameObject(player);
        player.getTransform().setPosition(new Vector3f(0, 40, 0));
        camera.getTransform().setPosition(new Vector3f(0, 1.6f, 0));
        getScene().setActiveCamera(camera.GetComponent(Camera.class));
    }

}
