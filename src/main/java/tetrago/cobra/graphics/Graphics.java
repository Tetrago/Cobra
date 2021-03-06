package tetrago.cobra.graphics;

import org.lwjgl.opengl.GL;
import tetrago.cobra.core.IClosable;
import tetrago.cobra.event.Events;
import tetrago.cobra.event.WindowEvent;
import tetrago.cobra.event.WindowResizeEvent;

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

        Events.WINDOW.listen(e ->
        {
            if(e.window() == window && e.type() == WindowEvent.Type.RESIZE)
            {
                use();

                WindowResizeEvent wre = (WindowResizeEvent)e;
                glViewport(0, 0, wre.width(), wre.height());
            }
        });
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

    public void setTextures(Texture[] textures, int count)
    {
        for(int i = 0; i < textures.length && i < count; ++i)
        {
            glActiveTexture(GL_TEXTURE0 + i);
            glBindTexture(GL_TEXTURE_2D, textures[i].handle());
        }
    }

    public void setTexture(int index, Texture texture)
    {
        glActiveTexture(GL_TEXTURE0 + index);
        glBindTexture(GL_TEXTURE_2D, texture.handle());
    }

    public void bind(Framebuffer framebuffer)
    {
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer.handle());
    }

    public void unbind()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void swap()
    {
        window_.swap();
    }
}
