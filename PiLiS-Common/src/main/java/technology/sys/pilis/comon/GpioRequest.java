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

    public GpioRequest()
    {
    }

    public GpioRequest(Action action, String gpioPin)
    {
        this.action = action;
        this.gpioPin = gpioPin;
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

}
