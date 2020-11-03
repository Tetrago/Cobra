package tetrago.cobra.core;

import java.lang.reflect.InvocationTargetException;

public class Cobra
{
    public static <T extends Program> void run(Class<T> klass)
    {
        try
        {
            Program program = klass.getDeclaredConstructor().newInstance();
            program.run();
            program.close();
        }
        catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
        {
            e.printStackTrace();
        }
    }
}
