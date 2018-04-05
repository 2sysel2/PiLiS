package technology.sys.pirec;

import com.sanityinc.jargs.CmdLineParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Jaromir Sys
 */
public class PortCalculator
{

    private static final Logger LOG = LogManager.getLogger(PortCalculator.class);

    private static final int DEFAULT_PORT = 50000;

    public static int getPortFromArgs(String[] args)
    {
        CmdLineParser parser = new CmdLineParser();
        CmdLineParser.Option port = parser.addIntegerOption('p', "port");
        try
        {
            parser.parse(args);
        }
        catch (CmdLineParser.OptionException ex)
        {
            LOG.error("Parsing server port failed using the default[{}]", DEFAULT_PORT);
            return DEFAULT_PORT;
        }
        Object argPort = parser.getOptionValue(port);
        if (argPort != null)
        {
            return Integer.parseInt(argPort.toString());
        }
        else
        {
            LOG.warn("Server started without '-p' or 'port' using the default[{}]", DEFAULT_PORT);
            return DEFAULT_PORT;
        }
    }
}
