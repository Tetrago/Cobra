package tetrago.cobra.graphics;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import tetrago.cobra.core.IClosable;
import tetrago.cobra.core.Logger;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class Shader implements IClosable
{
    private final int handle_;
    private final Map<String, Integer> locations_ = new HashMap<>();

    private static int findShaderType(String str)
    {
        str = str.toLowerCase();

        if(str.equals("vert"))
        {
            return GL_VERTEX_SHADER;
        }
        else if(str.equals("frag"))
        {
            return GL_FRAGMENT_SHADER;
        }

        Logger.COBRA.error("Unknown shader type: %s", str);
        return GL_VERTEX_SHADER;
    }

    public Shader(String source)
    {
        handle_ = glCreateProgram();

        String[] shaders = source.split("\\$");
        int[] ids = new int[shaders.length];

        for(int i = 1; i < shaders.length; ++i)
        {
            int index = shaders[i].indexOf('\n');
            int id = glCreateShader(findShaderType(shaders[i].substring(0, index)));

            glShaderSource(id, shaders[i].substring(index + 1));
            glCompileShader(id);

            if(glGetShaderi(id, GL_COMPILE_STATUS) == 0)
            {
                Logger.COBRA.error("Failed to compile shader: %s", glGetShaderInfoLog(handle_));
            }

            glAttachShader(handle_, id);

            ids[i - 1] = id;
        }

        glLinkProgram(handle_);

        if(glGetProgrami(handle_, GL_LINK_STATUS) == 0)
        {
            Logger.COBRA.error("Failed to link program: %s", glGetProgramInfoLog(handle_));
        }

        for(int id : ids)
        {
            glDeleteShader(id);
        }
    }

    @Override
    public void close()
    {
        glDeleteProgram(handle_);
    }

    public void upload(String name, Vector4f v)
    {
        glUniform4f(getLocation(name), v.x, v.y, v.z, v.w);
    }

    public void upload(String name, Matrix4f v)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        glUniformMatrix4fv(getLocation(name), false, v.get(buffer));
    }

    private int getLocation(String name)
    {
        if(locations_.containsKey(name))
        {
            return locations_.get(name);
        }

        int location = glGetUniformLocation(handle_, name);
        locations_.put(name, location);
        return location;
    }

    public int handle() { return handle_; }
}
