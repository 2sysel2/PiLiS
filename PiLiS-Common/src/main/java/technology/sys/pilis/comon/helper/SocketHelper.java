package technology.sys.pilis.comon.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Jaromir Sys
 */
public class SocketHelper
{

    public static String readAllFromSocket(Socket socket) throws IOException
    {
        StringBuilder result = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null)
        {
            result.append(line.trim());
        }
        return result.toString();
    }

    public static void sendString(Socket socket, String string) throws IOException
    {
        PrintWriter writter = new PrintWriter(socket.getOutputStream());
        writter.write(string);
        writter.flush();
        socket.shutdownOutput();
    }

    public static String getAddressFromSocket(Socket socket)
    {
        return socket.getLocalAddress().toString();
    }
}
