package technology.sys.pirec;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pi4j.io.gpio.RaspiPin;

public class ControllerConfigHelper {

	private static final Logger LOG = LogManager
			.getLogger(ControllerConfig.class);

	public static ControllerConfig getConfig(String configFilePath) {
		ControllerConfig config = getConfigFromFile(configFilePath);

		if (config == null) {
			config = getDefaultConfig();
		}

		return config;
	}

	private static ControllerConfig getConfigFromFile(String configFilePath) {
		List<String> lines = new ArrayList<>();
		try {
			lines.addAll(Files.readAllLines(Paths.get(configFilePath)));
		} catch (IOException e) {
			LOG.warn("Reading config from file failed");
			LOG.warn(e);
			return null;
		}
		StringBuilder configString = new StringBuilder();
		lines.stream().forEach(line -> configString.append(line));

		return unmarshall(configString.toString());

	}

	private static ControllerConfig getDefaultConfig() {
		ControllerConfig config = new ControllerConfig();
		config.setOverridePinName(RaspiPin.GPIO_02.getName());
		config.setRelayPinNames(new ArrayList<>());
		config.getRelayPinNames().add(RaspiPin.GPIO_00.getName());
		LOG.info("DEFAULT CONFIF : [{}]", marshall(config));
		return config;
	}

	public static String marshall(ControllerConfig config) {
		String result = null;
		try {
			JAXBContext jaxbContext = JAXBContext
					.newInstance(ControllerConfig.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
			StringWriter stringWriter = new StringWriter();
			marshaller.marshal(config, stringWriter);
			result = stringWriter.toString();
		} catch (JAXBException ex) {
			LOG.error("Marshalling failed for config [{}]", config);
			LOG.error(ex);
		}

		return result;
	}

	public static ControllerConfig unmarshall(String config) {
		ControllerConfig result = null;

		try {
			JAXBContext jaxbContext = JAXBContext
					.newInstance(ControllerConfig.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader stringReader = new StringReader(config);
			result = (ControllerConfig) unmarshaller.unmarshal(stringReader);
		} catch (JAXBException ex) {
			LOG.error("Unmarshalling failed for config [{}]", config);
			LOG.error(ex);
		}

		return result;
	}
}
