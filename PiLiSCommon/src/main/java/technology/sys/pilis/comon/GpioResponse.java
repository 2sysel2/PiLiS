package technology.sys.pilis.comon;

import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jaromir Sys
 */
@XmlRootElement
public class GpioResponse
{

    private String controllerIp;
    private Map<String, Boolean> pinMap;

    public GpioResponse()
    {
    }

    public GpioResponse(String controllerIp, Map<String, Boolean> pinMap)
    {
        this.controllerIp = controllerIp;
        this.pinMap = pinMap;
    }

    public String getControllerIp()
    {
        return controllerIp;
    }

    @XmlElement
    public void setControllerIp(String controllerIp)
    {
        this.controllerIp = controllerIp;
    }

    public Map<String, Boolean> getPinMap()
    {
        return pinMap;
    }

    @XmlElement
    public void setPinMap(Map<String, Boolean> pinMap)
    {
        this.pinMap = pinMap;
    }

}
