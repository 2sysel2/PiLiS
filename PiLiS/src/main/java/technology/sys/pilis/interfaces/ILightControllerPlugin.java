package technology.sys.pilis.interfaces;

import technology.sys.pilis.comon.GpioResponse;
import technology.sys.pilis.entities.Light;

import java.util.Collection;

/**
 *
 * @author Jaromir Sys
 */
public interface ILightControllerPlugin {

    void turnOn(Light light);

    void turnOff(Light light);

    void toggle(Light light);

    void applyStatesFromResponse(GpioResponse response);

    void addLight(Light light);

    Collection<Light> getLights();
}
