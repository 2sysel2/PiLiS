package technology.sys.pirec;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import technology.sys.pilis.comon.GpioRequest;
import technology.sys.pilis.comon.GpioResponse;
import technology.sys.pilis.comon.helper.SocketHelper;
import technology.sys.pilis.comon.helper.XmlHelper;
import technology.sys.pirec.dtos.CmdLineParametersDto;

/**
 *
 * @author Jaromir Sys
 */
public class Main {

	private static final Logger LOG = LogManager.getLogger(Main.class);
	private static GpioHelper GPIO_HELPER;

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		CmdLineParametersDto paramers = CmdLineOptionHelper
				.getCmdLineConfig(args);

		int port = paramers.getPort();
		LOG.info("PiReC is starting on port[{}]", port);

		GPIO_HELPER = new GpioHelper(paramers.getConfigFilePath());

		try (ServerSocket serverSocket = new ServerSocket(port)) {
			while (true) {
				Socket socket = serverSocket.accept();
				handleRequest(socket);
			}
		} catch (IOException ex) {
			LOG.error(ex);
		}
	}

	private static void handleRequest(Socket socket) throws IOException {
		LOG.info("Starting request processing");
		String requestString = SocketHelper.readAllFromSocket(socket);

		LOG.info("Recieved request[{}]", requestString);
		GpioRequest request = XmlHelper.unmarshallRequest(requestString);
		GpioResponse response = GPIO_HELPER.handleRequest(request);

		LOG.info("Request handled starting reponse");
		response.setControllerIp(SocketHelper.getAddressFromSocket(socket));

		String responseString = XmlHelper.marshallResponse(response);
		LOG.info("Response string :[{}]", responseString);

		SocketHelper.sendString(socket, responseString);
		LOG.info("response written to socket");
	}
}
