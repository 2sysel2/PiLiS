package technology.sys.pilis.plugins;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.commons.lang3.BooleanUtils;
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
import technology.sys.pilis.netty.MessageClient;

/**
 * @author Jaromir Sys
 */
@Service
@ComponentScan(value = "technology.sys.pilis.plugins")
public class LightControllerPlugin implements ILightControllerPlugin {

    private static final Logger LOG = LogManager.getLogger(LightControllerPlugin.class);

    private final List<Light> lights = new ArrayList<>();

    @Override
    public void turnOn(Light light) {
        GpioRequest request = new GpioRequest(Action.TURN_ON, light.getPinName(),light.getControllerIp());
        sendRequest(request, light.getControllerIp());
    }

    @Override
    public void turnOff(Light light) {
        GpioRequest request = new GpioRequest(Action.TURN_OFF, light.getPinName(),light.getControllerIp());
        sendRequest(request, light.getControllerIp());
    }

    @Override
    public void toggle(Light light) {
        GpioRequest request = new GpioRequest(Action.TOGGLE, light.getPinName(),light.getControllerIp());
        sendRequest(request, light.getControllerIp());
    }

    private void sendRequest(GpioRequest request, String controllerIp) {
        String marshaledRequest = XmlHelper.marshallRequest(request);
        int port = 50000;

        MessageClient messageClient = new MessageClient(controllerIp, port);
        try {
            messageClient.sendMessage(marshaledRequest);
        } catch (InterruptedException e) {
            LOG.error(e);
        }
    }

    @Override
    public void applyStatesFromResponse(GpioResponse response) {

        List<Light> lightsForController = lights.stream()//
                .filter(light -> light.getControllerIp().equals(response.getControllerIp()))//
                .collect(Collectors.toList());

        for (Light light : lightsForController) {
            Boolean stateOfLight = response.getPinMap().get(light.getPinName());
            if (stateOfLight == null) {
                light.stateUnknow();
            } else if (BooleanUtils.isTrue(stateOfLight)) {
                light.turnOn();
            } else {
                light.turnOff();
            }
        }
    }


    @Override
    public void addLight(Light light) {
        lights.add(light);
    }

    @Override
    public Collection<Light> getLights() {
        return lights;
    }


}
