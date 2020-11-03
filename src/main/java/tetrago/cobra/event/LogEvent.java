package tetrago.cobra.event;

import tetrago.cobra.core.LogLevel;

public class LogEvent
{
    public LogLevel level;
    public String name;
    public String message;

    public LogEvent(LogLevel level, String name, String message)
    {
        this.level = level;
        this.name = name;
        this.message = message;
    }
}
