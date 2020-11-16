package tetrago.cobra.core;

import tetrago.cobra.event.Events;
import tetrago.cobra.event.LogEvent;

public class Logger
{
    public static final Logger COBRA = new Logger("Cobra");

    private final String name_;

    /**
     * Constructs a logger with the name {@code name}.
     *
     * @param   name    Name of logger.
     */
    public Logger(String name)
    {
        name_ = name;
    }

    public void log(LogLevel level, String format, Object... args)
    {
        Events.LOG.emit(new LogEvent(level, name_, String.format(format, args)));
    }

    public void trace(String format, Object... args)
    {
        log(LogLevel.TRACE, String.format(format, args));
    }

    public void info(String format, Object... args)
    {
        log(LogLevel.INFO, String.format(format, args));
    }

    public void warn(String format, Object... args)
    {
        log(LogLevel.WARNING, String.format(format, args));
    }

    public void error(String format, Object... args)
    {
        log(LogLevel.ERROR, format, args);
    }

    public void fatal(String format, Object... args)
    {
        log(LogLevel.FATAL, String.format(format, args));
    }
}
