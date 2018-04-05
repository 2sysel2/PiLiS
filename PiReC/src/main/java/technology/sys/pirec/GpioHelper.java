package technology.sys.pirec;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import technology.sys.pilis.comon.GpioRequest;
import technology.sys.pilis.comon.GpioResponse;

/**
 *
 * @author Jaromir Sys
 */
public class GpioHelper
{

    private static final Logger LOG = LogManager.getLogger(GpioHelper.class);

    private final Map<String, GpioPinDigitalOutput> pinMap = new HashMap<>();
    private final GpioController gpioController;
    private LocalDateTime lastManualButtonPress = LocalDateTime.now();

    public GpioHelper()
    {
        gpioController = GpioFactory.getInstance();
        createManualOveride();
        addPin("GPIO 0");
    }

    private void createManualOveride()
    {
        GpioPinDigitalInput in = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);

        in.addListener((GpioPinListenerDigital) (GpioPinDigitalStateChangeEvent event)
                ->
                {
                    LocalDateTime now = LocalDateTime.now();
                    if (event.getState().isHigh() && ChronoUnit.SECONDS.between(lastManualButtonPress, now) >= 1)
                    {
                        lastManualButtonPress = now;
                        LOG.info("VALID INPUT @ [{}]", now);
                        pinMap.values().stream().forEach(pin -> togglePin(pin));
                    }

                    LOG.info(" --> GPIO PIN STATE CHANGE: [{}] = [{}]", event.getPin(), event.getState());
        });
    }

    private void togglePin(GpioPinDigitalOutput pin)
    {
        if (pin.getState().isHigh())
        {
            pin.low();
        }
        else
        {
            pin.high();
        }
    }

    private GpioPinDigitalOutput addPin(String pinName)
    {
        Pin rp = RaspiPin.getPinByName(pinName);
        GpioPinDigitalOutput pin = gpioController.provisionDigitalOutputPin(rp);
        pinMap.put(pinName, pin);
        return pin;
    }

    public GpioResponse handleRequest(GpioRequest request)
    {
        GpioPinDigitalOutput pin;
        if (pinMap.containsKey(request.getGpioPin()))
        {
            pin = pinMap.get(request.getGpioPin());
        }
        else
        {
            pin = addPin(request.getGpioPin());
        }

        switch (request.getAction())
        {
            case TOGGLE:
                togglePin(pin);
                break;
            case TURN_ON:
                pin.high();
                break;
            case TURN_OFF:
                pin.low();
                break;
        }

        GpioResponse response = new GpioResponse();
        response.setPinMap(getPinStateMap());
        return response;
    }

    private Map<String, Boolean> getPinStateMap()
    {
        Map<String, Boolean> result = new HashMap<>();

        pinMap
                .entrySet()
                .stream()
                .forEach((entry)
                        ->
                        {
                            result.put(entry.getKey(), entry.getValue().isHigh());
                });

        return result;
    }

}
