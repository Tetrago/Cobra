package tetrago.cobra.graphics;

import org.lwjgl.opengl.GL;
import tetrago.cobra.core.IClosable;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Graphics implements IClosable
{
    private final Window window_;

    public Graphics(Window window)
    {
        window_ = window;

        window.bind();
        GL.createCapabilities();
    }

    @Override
    public void close()
    {
        GL.destroy();
    }

    public void clear()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void use()
    {
        window_.bind();
        glViewport(0, 0, window_.width(), window_.height());
    }

    public void drawIndexed(int count)
    {
        glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_INT, 0);
    }

    public void setVertexArray(VertexArray vao)
    {
        glBindVertexArray(vao.handle());
    }

    public void setShader(Shader shader)
    {
        glUseProgram(shader.handle());
    }

    public void swap()
    {
        window_.swap();
    }
}
