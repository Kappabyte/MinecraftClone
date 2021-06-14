package net.kappabyte.kappaengine.graphics;

import java.nio.FloatBuffer;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import net.kappabyte.kappaengine.util.Log;

public class ShaderProgram {
    private final int programID;

    private HashMap<String, Integer> uniforms = new HashMap<>();

    private int[] shaders;

    public ShaderProgram(Shader... shaders) {
        this.programID = GL30.glCreateProgram();
        if(this.programID == 0) {
            Log.error("Failed to create shader program.");
            throw new IllegalStateException();
        }

        this.shaders = new int[shaders.length];

        for(int i = 0; i < shaders.length; i++) {
            this.shaders[i] = shaders[i].getCompiledShader(programID);
        }

        link();
    }

    public void link() {
        GL30.glLinkProgram(this.programID);

        if (GL30.glGetProgrami(this.programID, GL30.GL_LINK_STATUS) == 0) {
            Log.error("Error linking Shader code: " + GL30.glGetProgramInfoLog(programID, 1024));
            return;
        }

        for(int shader : shaders) {
            GL30.glDetachShader(programID, shader);
        }

        GL30.glValidateProgram(programID);
        if (GL30.glGetProgrami(programID, GL30.GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + GL30.glGetProgramInfoLog(programID, 1024));
        }
    }

    public void bind() {
        GL30.glUseProgram(programID);
    }

    public void unbind() {
        GL30.glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programID != 0) {
            GL30.glDeleteProgram(programID);
        }
    }

    public void createUniform(String uniformName) {
        if(uniforms.containsKey(uniformName)) return;

        int uniformLocation = GL30.glGetUniformLocation(programID, uniformName);

        if(uniformLocation < 0) {
            Log.warn("Failed to find uniform variable " + uniformName);
            return;
        }

        Log.debug("Registered uniform " + uniformName + " at location " + uniformLocation);
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniformName, Matrix4f value) {
        if(!uniforms.containsKey(uniformName)) {
            Log.warn("Attempted to set a uniform value for a uniform that has not been created (uniform: " + uniformName + ")!");
            Log.stack();
            return;
        }
        FloatBuffer fb = MemoryUtil.memAllocFloat(16);
        value.get(fb);
        GL30.glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
        MemoryUtil.memFree(fb);
    }

    public void setUniform(String uniformName, int value) {
        if(!uniforms.containsKey(uniformName)) {
            Log.warn("Attempted to set a uniform value for a uniform that has not been created (uniform: " + uniformName + ")!");
            return;
        }
        GL30.glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, Vector3f value) {
        setUniform(uniformName, value.x, value.y, value.z);
    }

    public void setUniform(String uniformName, float x, float y, float z) {
        if(!uniforms.containsKey(uniformName)) {
            Log.warn("Attempted to set a uniform value for a uniform that has not been created (uniform: " + uniformName + ")!");
            return;
        }
        GL30.glUniform3f(uniforms.get(uniformName), x, y, z);
    }

    public void setUniform(String uniformName, Vector4f value) {
        setUniform(uniformName, value.x, value.y, value.z, value.w);
    }

    public void setUniform(String uniformName, float x, float y, float z, float w) {
        if(!uniforms.containsKey(uniformName)) {
            Log.warn("Attempted to set a uniform value for a uniform that has not been created (uniform: " + uniformName + ")!");
            return;
        }
        GL30.glUniform4f(uniforms.get(uniformName), x, y, z, w);
    }

    public int getProgramID() {
        return programID;
    }
}
