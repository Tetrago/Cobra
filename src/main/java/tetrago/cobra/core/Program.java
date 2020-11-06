package tetrago.cobra.core;

import tetrago.cobra.event.Events;
import tetrago.cobra.event.WindowEvent;
import tetrago.cobra.graphics.RenderStack;
import tetrago.cobra.graphics.Renderer2D;
import tetrago.cobra.graphics.Window;
import tetrago.cobra.io.Keyboard;

public abstract class Program implements IClosable
{
    private static Program instance_;

    private final Logger logger_;
    private final Window window_;
    private boolean running_;

    public Program(String name)
    {
        instance_ = this;

        logger_ = new Logger(name);
        window_ = new Window(1024, 576, name);

        Keyboard.init();
        Renderer2D.init();

        Events.WINDOW.listen(e ->
        {
            if(running_ && e.window() == window_ && e.type() == WindowEvent.Type.CLOSE)
            {
                stop();
            }
        });

        setup();
    }

    @Override
    public void close()
    {
        Renderer2D.free();
        window_.close();
    }

    public void run()
    {
        RenderStack.push(window_.graphics());

        running_ = true;
        while(running_)
        {
            Time.tick();
            window_.update();

            update();

            RenderStack.current().swap();
        }

        RenderStack.pop();
    }

    public void stop()
    {
        running_ = false;
    }

    public abstract void setup();
    public abstract void update();

    public static Program get()
    {
        return instance_;
    }

    public Logger logger() { return logger_; }
    public Window window() { return window_; }
}
