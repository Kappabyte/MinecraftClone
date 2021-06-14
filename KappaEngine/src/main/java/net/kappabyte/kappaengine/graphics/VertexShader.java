package net.kappabyte.kappaengine.graphics;

import java.io.InputStream;

import org.lwjgl.opengl.GL20;

public class VertexShader extends Shader {

	public VertexShader(InputStream file) {
		super(file);
	}

	@Override
	public int getCompiledShader(int program) {
        int shaderID = compileShader(GL20.GL_VERTEX_SHADER);

        GL20.glAttachShader(program, shaderID);

        return shaderID;
	}

}
