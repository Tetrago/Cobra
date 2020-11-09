package tetrago.cobra.io;

import org.lwjgl.BufferUtils;
import tetrago.cobra.core.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

public class Resource
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

    public static ByteBuffer toBytes(InputStream stream)
    {
        try
        {
            ByteBuffer bytes = BufferUtils.createByteBuffer(stream.available());
            stream.read(bytes.array());
            return bytes;
        }
        catch(IOException e)
        {
            Logger.COBRA.error("Failed to read to bytes");
        }

        return null;
    }
}
