package net.kappabyte.kappaengine.graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.lwjgl.opengl.GL20;

import net.kappabyte.kappaengine.util.Log;

public abstract class Shader {

    InputStream file;
    String source = "";

    public Shader(InputStream file) {
        this.file = file;
    }

    public abstract int getCompiledShader(int program);

    protected final int compileShader(int shaderType) {
        int shader = GL20.glCreateShader(shaderType);
        if(shader == 0) {
            Log.error("Failed to create shader!");
        }

        //Load shader into memory, and set shader source
        if(this.source == "") {
            loadFromFile();
        }
        GL20.glShaderSource(shader, this.source);

        GL20.glCompileShader(shader);

        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == 0) {
            Log.info(source);
            Log.error("Failed to compile shader: " + file);
            //try get get the error
            String log = GL20.glGetShaderInfoLog(shader);
            Log.debug("A full walktrhough of the shader compilation error can be seen below:");
            Log.debug(log);
            return shader;
        }

        return shader;
    }

    protected final void loadFromFile() {
        InputStreamReader streamReader = new InputStreamReader(file);
        BufferedReader reader = new BufferedReader(streamReader);
        try {
            while(reader.ready()) {
                source += reader.readLine() + "\n";
            }
            reader.close();
		} catch (IOException e) {
            Log.error("Failed to read shaderfile!");
		}
    }
}
