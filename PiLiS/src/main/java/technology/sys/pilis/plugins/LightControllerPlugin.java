package technology.sys.pilis.plugins;

import java.io.IOException;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import technology.sys.pilis.comon.Action;
import technology.sys.pilis.comon.GpioRequest;
import technology.sys.pilis.comon.GpioResponse;
import technology.sys.pilis.comon.helper.SocketHelper;
import technology.sys.pilis.comon.helper.XmlHelper;
import technology.sys.pilis.entities.Light;
import technology.sys.pilis.interfaces.ILightControllerPlugin;

/**
 *
 * @author Jaromir Sys
 */
@Service
@ComponentScan(value = "technology.sys.pilis.plugins")
public class LightControllerPlugin implements ILightControllerPlugin
{

    private static final Logger LOG = LogManager.getLogger(LightControllerPlugin.class);

    @Override
    public void turnOn(Light light)
    {
        GpioRequest request = new GpioRequest(Action.TURN_ON, light.getPinName());
        GpioResponse response = sendRequest(request, light.getControllerIp());
        if (response != null)
        {
            light.turnOn();
        }

    }

    @Override
    public void turnOff(Light light)
    {
        GpioRequest request = new GpioRequest(Action.TURN_OFF, light.getPinName());
        GpioResponse response = sendRequest(request, light.getControllerIp());
        if (response != null)
        {
            light.turnOff();
        }
    }

    @Override
    public void toggle(Light light)
    {
        GpioRequest request = new GpioRequest(Action.TOGGLE, light.getPinName());
        GpioResponse response = sendRequest(request, light.getControllerIp());
        if (response != null)
        {
            light.toggle();
        }
    }

    private GpioResponse sendRequest(GpioRequest request, String controllerIp)
    {
        try (Socket socket = new Socket(controllerIp, 50000))
        {
            String requestString = XmlHelper.marshallRequest(request);
            SocketHelper.sendString(socket, requestString);

            String responseString = SocketHelper.readAllFromSocket(socket);
            GpioResponse response = XmlHelper.unmarshallResponse(responseString);
            return response;
        }
        catch (IOException ex)
        {
            LOG.error(ex);
            return null;
        }
    }
}
