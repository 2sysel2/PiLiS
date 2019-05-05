package technology.sys.pilis.services;

import technology.sys.pilis.comon.GpioResponse;
import technology.sys.pilis.entities.Light;

import java.util.Collection;

/**
 *
 * @author Jaromir Sys
 */
public interface ILightService {

    void turnOn(String lightName);

    void turnOff(String lightName);

    void toggle(String lightName);

    void applyStatesFromResponse(GpioResponse response);

    Collection<Light> getLights();
}
