package technology.sys.pirec.dtos;

public class CmdLineParametersDto {

	private int port;
	private String configFilePath;

	public CmdLineParametersDto() {
		port = 50000;
		configFilePath = "";
	}

	public CmdLineParametersDto(int port, String configFilePath) {
		this.port = port;
		this.configFilePath = configFilePath;
	}

	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getConfigFilePath() {
		return configFilePath;
	}
	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}
}
