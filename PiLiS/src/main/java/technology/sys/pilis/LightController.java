package technology.sys.pilis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import technology.sys.pilis.dtos.ButtonDto;
import technology.sys.pilis.dtos.InfoDto;
import technology.sys.pilis.entities.Light;
import technology.sys.pilis.interfaces.ILightControllerPlugin;

/**
 *
 * @author Jaromir Sys
 */
@Controller
@Service
@EnableAutoConfiguration
@ComponentScan(value = "technology.sys.pilis.plugins")
public class LightController
{

    private static final Logger LOG = LogManager
            .getLogger(LightController.class);

    @Autowired
    private ILightControllerPlugin lightControllerPlugin;

    private final List<Light> lights = new ArrayList<Light>();

    @RequestMapping(value =
    {
        "/pilis/turnOn/{lightName}", "/pilis/turnOn"
    })
    RedirectView turnOn(@PathVariable(required = false) String lightName)
    {
        if (!StringUtils.isEmpty(lightName))
        {
            lights.stream().filter(light -> light.getName().equals(lightName))
                    .forEach(light -> lightControllerPlugin.turnOn(light));
        }
        else
        {
            lights.stream()
                    .forEach(light -> lightControllerPlugin.turnOn(light));
        }
        return getRedirectView("/pilis/info");
    }

    @RequestMapping(value =
    {
        "/pilis/turnOff", "/pilis/turnOff/{lightName}"
    })
    RedirectView turnOff(RedirectAttributes attributes,
            @PathVariable(required = false) String lightName)
    {
        if (!StringUtils.isEmpty(lightName))
        {
            lights.stream().filter(light -> light.getName().equals(lightName))
                    .forEach(light -> lightControllerPlugin.turnOff(light));
        }
        else
        {
            lights.stream()
                    .forEach((light) -> lightControllerPlugin.turnOff(light));
        }
        return getRedirectView("/pilis/info");
    }

    @RequestMapping(value =
    {
        "/pilis/toggle/{lightName}", "/pilis/toggle"
    })
    RedirectView toggle(@PathVariable(required = false) String lightName)
    {
        if (!StringUtils.isEmpty(lightName))
        {
            lights.stream().filter(light -> light.getName().equals(lightName))
                    .forEach(light -> lightControllerPlugin.toggle(light));
        }
        else
        {
            lights.stream()
                    .forEach((light) -> lightControllerPlugin.toggle(light));
        }
        return getRedirectView("/pilis/info");
    }

    @PostConstruct
    void init()
    {
        LOG.info("Initializing PiLiS");
        initLights();
        lights.stream().forEach(light -> LOG.info("Light[{}]", light));
    }

    @RequestMapping("/")
    RedirectView home()
    {
        return getRedirectView("pilis/info");
    }

    @RequestMapping(value =
    {
        "/pilis/info", "/pilis/"
    })
    String info(Model model)
    {
        InfoDto info = new InfoDto();

        try (BufferedReader br = new BufferedReader(new FileReader(Main.PID)))
        {
            info.setProcessId(br.readLine());
        }
        catch (Exception e)
        {
            info.setProcessId("reading current PID from file failed");
        }

        info.setDirectory(System.getProperty("user.dir"));
        info.setDate(LocalDate.now());
        info.setTime(LocalTime.now());
        info.setLights(lights);

        List<ButtonDto> buttons = new ArrayList<>();
        buttons.add(new ButtonDto("toggle", "Toggle"));
        buttons.add(new ButtonDto("turnOn", "Turn On"));
        buttons.add(new ButtonDto("turnOff", "Turn Off"));
        buttons.add(new ButtonDto("info", "Info"));
        model.addAttribute("buttons", buttons);
        model.addAttribute("info", info);

        return "index";
    }

    private RedirectView getRedirectView(String redirectAdress)
    {
        RedirectView rv = new RedirectView();
        rv.setUrl(redirectAdress);
        return rv;
    }

    private void initLights()
    {
        File config = new File("lights.json");
        try
        {
            Gson gson = new GsonBuilder().create();
            if (config.exists())
            {
                String json = FileUtils.readFileToString(config, StandardCharsets.UTF_8);
                lights.addAll(gson.fromJson(json, new TypeToken<List<Light>>()
                {
                }.getType()));
            }
            else
            {
                lights.add(new Light("Light", "GPIO 0", "localhost"));
                String json = gson.toJson(lights);
                FileUtils.write(config, json, StandardCharsets.UTF_8);
            }
        }
        catch (IOException ex)
        {

        }
    }
}
