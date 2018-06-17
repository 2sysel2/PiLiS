package technology.sys.pilis.entities;

/**
 *
 * @author Jaromir Sys
 */
public class Light {

	private final String name;
	private State state;
	private final String pinName;
	private final String controllerIp;

	public Light(String name, String pinName, String controllerIp) {
		this.name = name;
		this.state = State.UNKNOWN;
		this.pinName = pinName;
		this.controllerIp = controllerIp;
	}

	public String getName() {
		return name;
	}

	public State getState() {
		return state;
	}

	public void turnOn() {
		this.state = State.ON;
	}

	public void turnOff() {
		this.state = State.OFF;
	}

	public void toggle() {
		if (this.state == State.ON) {
			this.state = State.OFF;
		} else {
			this.state = State.ON;
		}
	}

	public String getPinName() {
		return pinName;
	}

	public String getControllerIp() {
		return controllerIp;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append(String.format(" - state[%s] - pin[%s] - ip[%s]", state,
				pinName, controllerIp));
		return sb.toString();
	}

	public void stateUnknow() {
		this.state = State.UNKNOWN;
	}

}
