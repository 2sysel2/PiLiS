package technology.sys.pirec;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ControllerConfig {

	private String overridePinName;
	private List<String> relayPinNames;

	public ControllerConfig() {
	}

	public String getOverridePinName() {
		return overridePinName;
	}
	@XmlElement
	public void setOverridePinName(String overridePinName) {
		this.overridePinName = overridePinName;
	}

	public List<String> getRelayPinNames() {
		return relayPinNames;
	}
	@XmlElement
	public void setRelayPinNames(List<String> relayPinNames) {
		this.relayPinNames = relayPinNames;
	}

}
