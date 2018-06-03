package technology.sys.pirec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sanityinc.jargs.CmdLineParser;
import com.sanityinc.jargs.CmdLineParser.Option;

import technology.sys.pirec.dtos.CmdLineParametersDto;

/**
 *
 * @author Jaromir Sys
 */
public class CmdLineOptionHelper {

	private static final Logger LOG = LogManager
			.getLogger(CmdLineOptionHelper.class);

	public static CmdLineParametersDto getCmdLineConfig(String[] args) {
		CmdLineParametersDto result = new CmdLineParametersDto();
		CmdLineParser parser = new CmdLineParser();

		Option<Integer> portOption = parser.addIntegerOption('p', "port");
		Option<String> configFilePathOption = parser.addStringOption('c',
				"config");

		try {
			parser.parse(args);
		} catch (CmdLineParser.OptionException ex) {
			LOG.error("Parsing cmd line options failed. Using the defaults.");
			return result;
		}

		Integer port = parser.getOptionValue(portOption);
		String configFilePath = parser.getOptionValue(configFilePathOption);

		if (port != null) {
			result.setPort(port);
		} else {
			LOG.warn("Server started without '-p' or 'port'");
		}
		if (configFilePath != null) {
			result.setConfigFilePath(configFilePath);
		} else {
			LOG.warn("Server started without '-c' or 'config'");
		}

		return result;
	}
}
