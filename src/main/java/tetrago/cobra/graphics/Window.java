package tetrago.cobra.graphics;

import tetrago.cobra.core.IClosable;
import tetrago.cobra.core.Logger;
import tetrago.cobra.event.Events;
import tetrago.cobra.event.KeyEvent;
import tetrago.cobra.event.WindowEvent;
import tetrago.cobra.event.WindowResizeEvent;
import tetrago.cobra.graphics.Graphics;
import tetrago.cobra.io.Key;

import static org.lwjgl.glfw.GLFW.*;

public class Window implements IClosable
{
    public static class Instance
    {
        private static int referenceCounter_ = 0;

        /**
         * Pushes dependent window onto count.
         */
        public static void push()
        {
            ++referenceCounter_;

            if(referenceCounter_ == 1)
            {
                if(glfwInit())
                {
                    Logger.COBRA.fatal("Failed to initialize Glfw");
                }
            }
        }

        /**
         * Removes dependent window from count.
         */
        public static void pop()
        {
            --referenceCounter_;

            if(referenceCounter_ == 0)
            {
                glfwTerminate();
            }
        }
    }

    private final long handle_;
    private final Graphics graphics_;
    private int width_, height_;

    public Window(int width, int height, String title)
    {
        Instance.push();

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        handle_ = glfwCreateWindow(width_ = width, height_ = height, title, 0, 0);
        if(handle_ == 0)
        {
            Logger.COBRA.error("Failed to create window (%s).", title);
        }

        glfwMakeContextCurrent(handle_);
        graphics_ = new Graphics(this);

        glfwSetWindowCloseCallback(handle_, window -> Events.WINDOW.emit(new WindowEvent(this, WindowEvent.Type.CLOSE)));
        glfwSetWindowSizeCallback(handle_, (window, w, h) ->
        {
            width_ = w;
            height_ = h;

            Events.WINDOW.emit(new WindowResizeEvent(this, WindowEvent.Type.RESIZE, w, h));
        });

        glfwSetKeyCallback(handle_, (window, key, scancode, action, mods) ->
        {
            if(action != GLFW_REPEAT)
            {
                Events.WINDOW.emit(new KeyEvent(this, key, action == GLFW_PRESS));
            }
        });
    }

    @Override
    public void close()
    {
        graphics_.close();

        glfwDestroyWindow(handle_);
        Instance.pop();
    }

    public void update()
    {
        glfwPollEvents();
    }

    public void bind()
    {
        glfwMakeContextCurrent(handle_);
    }

    public void swap()
    {
        glfwSwapBuffers(handle_);
    }

    public int width() { return width_; }
    public int height() { return height_; }

    public Graphics graphics() { return graphics_; }
}
