import tetrago.cobra.core.Cobra;
import tetrago.cobra.core.LogLevel;
import tetrago.cobra.event.Events;

import java.io.PrintStream;

public class Main
{
    public static void main(String[] args)
    {
        System.out.format("PID: %d%n", ProcessHandle.current().pid());

        Events.LOG.listen(e ->
        {
            PrintStream stream = e.level == LogLevel.ERROR ? System.err : System.out;

            if(e.level == LogLevel.FATAL)
            {
                throw new IllegalStateException(String.format("[%s] %s", e.name, e.message));
            }
            else
            {
                stream.format("[%s] (%s) %s%n", e.name, e.level.toString(), e.message);
            }
        });

        Cobra.run(TestProgram.class);
    }
}
