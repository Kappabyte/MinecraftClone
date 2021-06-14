package net.kappabyte.kappaengine.graphics.materials;

import java.nio.FloatBuffer;
import java.util.HashMap;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import net.kappabyte.kappaengine.graphics.RenderData;
import net.kappabyte.kappaengine.graphics.ShaderProgram;
import net.kappabyte.kappaengine.util.Log;

public abstract class Material {

    protected HashMap<String, Integer> materialVBOs = new HashMap<>();

    public abstract ShaderProgram getShaderProgram();

    /**
     * This function should fill vbos with data
     * @param data Data about the object being rendered.
     */
    public abstract void setRenderData(RenderData data);

    /**
     * This function is called on Render
     */
    public abstract void render(RenderData data);

    public final void enableVertexAttribArrays() {
        for(int i = 0; i < materialVBOs.size(); i++) {
            GL20.glEnableVertexAttribArray(i + 2);
        }
    }

    public final void disableVertexAttribArrays() {
        for(int i = 0; i < materialVBOs.size(); i++) {
            GL20.glDisableVertexAttribArray(i + 2);
        }
    }

    protected final int createVBO(String name) {
        Log.debug("VBO generated!");
        materialVBOs.put(name, GL30.glGenBuffers());
        return materialVBOs.size() - 1;
    }

    protected final void fillVBODataFloat(float[] data, String name) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data).flip();

        Log.debug(data.toString());

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, materialVBOs.get(name));
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
        MemoryUtil.memFree(buffer);
        int location = GL30.glGetAttribLocation(getShaderProgram().getProgramID(), name);
        GL30.glVertexAttribPointer(location, 3, GL30.GL_FLOAT, false, 0, 0);
    }

    protected final void fillVBODataFloat(float[] data, String name, int dimentions) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data).flip();

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, materialVBOs.get(name));
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
        MemoryUtil.memFree(buffer);
        int location = GL30.glGetAttribLocation(getShaderProgram().getProgramID(), name);
        GL30.glVertexAttribPointer(location, dimentions, GL30.GL_FLOAT, false, 0, 0);
    }
}
