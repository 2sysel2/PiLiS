package technology.sys.pilis.interfaces;

import technology.sys.pilis.entities.Light;

/**
 *
 * @author Jaromir Sys
 */
public interface ILightControllerPlugin {

    public void turnOn(Light light);

    public void turnOff(Light light);

    public void toggle(Light light);

    //public void status(Light light);
}
