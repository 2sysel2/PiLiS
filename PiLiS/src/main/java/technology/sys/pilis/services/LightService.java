package technology.sys.pilis.services;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Service;
import technology.sys.pilis.comon.Action;
import technology.sys.pilis.comon.GpioRequest;
import technology.sys.pilis.comon.GpioResponse;
import technology.sys.pilis.comon.helper.XmlHelper;
import technology.sys.pilis.entities.Light;
import technology.sys.pilis.netty.MessageClient;

import javax.annotation.PostConstruct;

/**
 * @author Jaromir Sys
 */
@Service

public class LightService implements ILightService {

    private static final Logger LOG = LogManager.getLogger(LightService.class);

    private final List<Light> lights = new ArrayList<>();

    @PostConstruct
    void init() {
        LOG.info("Initializing PiLiS");
        File config = new File("lights.json");
        try {
            Gson gson = new GsonBuilder().create();
            List<Light> lightList = new ArrayList<>();

            if (config.exists()) {
                String json = FileUtils.readFileToString(config, StandardCharsets.UTF_8);

                lightList.addAll(gson.fromJson(json, new TypeToken<List<Light>>() {
                }.getType()));
            } else {
                lightList.add(new Light("Light", "GPIO 0", "localhost"));
                String json = gson.toJson(lightList);
                FileUtils.write(config, json, StandardCharsets.UTF_8);
            }

            lights.addAll(lightList);

        } catch (IOException ex) {
            LOG.error(ex);
        }
        lights.forEach(light -> LOG.info("Light[{}]", light));
    }


    @Override
    public void turnOn(String lightName) {
        for (Light light : getLights(lightName)) {
            turnOn(light);
        }
    }

    @Override
    public void turnOff(String lightName) {
        for (Light light : getLights(lightName)) {
            turnOff(light);
        }
    }

    @Override
    public void toggle(String lightName) {
        for (Light light : getLights(lightName)) {
            toggle(light);
        }
    }

    private void turnOn(Light light) {
        sendRequest(light,Action.TURN_ON);
    }

    private void turnOff(Light light) {
        sendRequest(light,Action.TURN_OFF);
    }

    private void toggle(Light light) {
        sendRequest(light,Action.TOGGLE);
    }

    private List<Light> getLights(String lightName) {

        if (!StringUtils.isEmpty(lightName)) {
            return lights.stream()//
                    .filter(light -> light.getName().equals(lightName))//
                    .collect(Collectors.toList());
        } else {
            return lights;
        }


    }

    private void sendRequest(Light light, Action action) {
        GpioRequest request = new GpioRequest(action, light.getPinName(), light.getControllerIp());
        String marshaledRequest = XmlHelper.marshallRequest(request);

        int port = 50000;

        MessageClient messageClient = new MessageClient(light.getControllerIp(), port);
        try {
            messageClient.sendMessage(marshaledRequest);
        } catch (Exception e) {
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
    public Collection<Light> getLights() {
        return lights;
    }


}
