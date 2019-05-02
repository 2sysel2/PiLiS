package technology.sys.pilis.comon;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jaromir Sys
 */
@XmlRootElement
public class GpioRequest
{

    private Action action;
    private String gpioPin;
    private String controllerIp;

    public GpioRequest()
    {
    }

    public GpioRequest(Action action, String gpioPin,String controllerIp)
    {
        this.action = action;
        this.gpioPin = gpioPin;
        this.controllerIp = controllerIp;
    }

    public Action getAction()
    {
        return action;
    }

    @XmlElement
    public void setAction(Action action)
    {
        this.action = action;
    }

    public String getGpioPin()
    {
        return gpioPin;
    }

    @XmlElement
    public void setGpioPin(String gpioPin)
    {
        this.gpioPin = gpioPin;
    }

    public String getControllerIp() {
        return controllerIp;
    }
    @XmlElement
    public void setControllerIp(String controllerIp) {
        this.controllerIp = controllerIp;
    }
}
