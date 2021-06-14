package net.kappabyte.kappaengine.ui;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import net.kappabyte.kappaengine.graphics.RenderData;
import net.kappabyte.kappaengine.graphics.materials.Material;
import net.kappabyte.kappaengine.scenes.components.Component;
import net.kappabyte.kappaengine.util.Log;
import net.kappabyte.kappaengine.util.Profiling;

import static org.lwjgl.system.MemoryUtil.memFree;

public abstract class UIRenderable extends Component {

    int vao, verticesVBO, indicesVBO, normalsVBO;

    Material material;

    public UIRenderable(Material material) {
        this.material  = material;
    }

    public boolean shouldRender() {
        return true;
    }

    protected abstract RenderData supplyRenderData();

    public void Init() {
        //Get the render data and bind the shader
        RenderData data = supplyRenderData();
        data.getShaderProgram().bind();

        //Create the vao - stores a bunch of vbos
        vao = GL30.glGenVertexArrays();
        Log.debug("VAO generated and bound!");

        GL30.glEnable(GL30.GL_CULL_FACE);

        //Init uniform variables
        //VBOs is a memory buffer of the GPU which stores vertex information. We create the buffer, bind it so we can use it, set the data and free the memory used by our data as we no longer need it.
        verticesVBO = GL30.glGenBuffers();
        indicesVBO = GL30.glGenBuffers();
        normalsVBO = GL30.glGenBuffers();

        updateVBOs(data);
    }

    protected void updateVBOs(RenderData data) {
        Profiling.startTimer("ke_internal:update_vbo");

        //Modify data to be readble by openGL
        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(data.getMesh().getVertices().length);
        verticesBuffer.put(data.getMesh().getVertices()).flip();
        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(data.getMesh().getIndicies().length);
        indicesBuffer.put(data.getMesh().getIndicies()).flip();
        FloatBuffer normalsBuffer = MemoryUtil.memAllocFloat(data.getMesh().getNormals().length);
        normalsBuffer.put(data.getMesh().getNormals()).flip();

        //Bind all the stuff
        GL30.glBindVertexArray(vao);

        //Set Data for Vertices
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, verticesVBO);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, verticesBuffer, GL30.GL_STATIC_DRAW);
        memFree(verticesBuffer);

        //Define the structure of our VBO, and store it in the VAO
        GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 0, 0);
        GL30.glVertexAttribPointer(1, 3, GL30.GL_FLOAT, false, 0, 0);

        //Set Data for Indices
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, indicesVBO);
        GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL30.GL_STATIC_DRAW);
        memFree(indicesBuffer);

        //Set Data for Normals
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, normalsVBO);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, normalsBuffer, GL30.GL_STATIC_DRAW);
        memFree(normalsBuffer);
        Profiling.stopTimer("ke_internal:update_vbo");

        //Material data
        Profiling.startTimer("ke_internal:material_render");
        data.getMaterial().setRenderData(data);
        Profiling.stopTimer("ke_internal:material_render");

        //Unbind everything, as we dont need it anymore
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    public void Render() {
        Profiling.startTimer("ke_internal:full_render");
        //Get the render data and bind the shader
        RenderData data = supplyRenderData();
        data.getShaderProgram().bind();

        //Update the data to be rendered
        updateVBOs(data);

        Profiling.startTimer("ke_internal:render");
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
        Profiling.stopTimer("ke_internal:render");
        Profiling.stopTimer("ke_internal:full_render");
    }

    public final void Cleanup() {
        //Get the render data and bind the shader
        RenderData data = supplyRenderData();
        data.getShaderProgram().bind();

        if (data.getShaderProgram() != null) {
            data.getShaderProgram().cleanup();
        }

        GL30.glDisableVertexAttribArray(0);

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
        GL30.glDeleteBuffers(verticesVBO);
        GL30.glDeleteBuffers(indicesVBO);

        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vao);
    }

    public void onStart() {
        Init();
    }

    public void onUpdate() {
        // Render();
    }

    public void onDestroy() {
        Cleanup();
    }
}
