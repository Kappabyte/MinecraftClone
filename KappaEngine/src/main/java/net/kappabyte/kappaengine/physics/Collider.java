package net.kappabyte.kappaengine.physics;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import net.kappabyte.kappaengine.graphics.Mesh;
import net.kappabyte.kappaengine.graphics.RenderData;
import net.kappabyte.kappaengine.graphics.materials.RainbowMaterial;
import net.kappabyte.kappaengine.scenes.components.Renderable;
import net.kappabyte.kappaengine.util.Log;

public abstract class Collider extends Renderable {

    public boolean debugEnabled = false;

    private static ArrayList<Collider> colliders = new ArrayList<Collider>();

    protected Mesh debugMesh;

    public Collider() {
        super(new RainbowMaterial(), false);
    }

    public abstract boolean isColliding();
    public abstract List<Collider> getCollisions();
    public abstract List<Collider> getCollisions(List<Collider> colliders);
    public abstract List<Collider> getCollisions(Vector3f offset, List<Collider> colliders);

    public abstract Mesh generateDebugMesh();

    public final static List<Collider> getAllColliders() {
        return colliders;
    }

    @Override
    public boolean shouldRender() {
        return debugEnabled;
    }

    @Override
    public void onStart() {
        colliders.add(this);
        if(debugEnabled) {
            super.onStart();
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    public void onDestroy() {
        colliders.remove(this);
        if(debugEnabled) {
            super.onDestroy();
        }
    }

    @Override
    protected RenderData supplyRenderData() {
        return new RenderData(getTransform(), debugMesh, material, getGameObject());
    }

    @Override
    public void Render() {
        GL30.glPolygonMode(GL30.GL_FRONT_AND_BACK, GL30.GL_LINE);
        GL30.glDisable(GL30.GL_CULL_FACE);
        //Get the render data and bind the shader
        RenderData data = supplyRenderData();
        data.getShaderProgram().bind();

        //Update the data to be rendered

        //Update matrices
        data.getShaderProgram().setUniform("modelViewMatrix", data.getTransform().getModelViewMatrixNoRotation(data.getCamera().getViewMatrix()));
        data.getShaderProgram().setUniform("projectionMatrix", data.getCamera().getProjectionMatrix());
        //Bind our vao (dont need to bind vbo, as it is stored in the vao)
        GL30.glBindVertexArray(vao);
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);

        data.getMaterial().render(data);
        data.getMaterial().enableVertexAttribArrays();

        //Draw the stuff
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, indicesVBO);
        GL30.glFrontFace(GL30.GL_CW);
        GL30.glEnable(GL30.GL_DEPTH_TEST);
        GL30.glDrawElements(GL30.GL_TRIANGLES, data.getMesh().getIndicies().length, GL30.GL_UNSIGNED_INT, 0);

        //Unbind vao
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        data.getMaterial().disableVertexAttribArrays();
        GL30.glBindVertexArray(0);

        //Unbind the shader, which will be used next render call
        data.getShaderProgram().unbind();
        GL30.glEnable(GL30.GL_CULL_FACE);
        GL30.glPolygonMode(GL30.GL_FRONT_AND_BACK, GL30.GL_FILL);
    }
}
