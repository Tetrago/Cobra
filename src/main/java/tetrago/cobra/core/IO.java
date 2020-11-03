package tetrago.cobra.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IO
{
    public static String toString(InputStream stream)
    {
        StringBuilder builder = new StringBuilder();

        try(InputStreamReader input = new InputStreamReader(stream);
            BufferedReader reader = new BufferedReader(input))
        {
            String line;
            while((line = reader.readLine()) != null)
            {
                builder.append(line).append('\n');
            }
        }
        catch(IOException e)
        {
            Logger.COBRA.error("Failed when loading stream into string: %s", e.getMessage());
        }

        return builder.toString();
    }
}
