package technology.sys.pilis.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import technology.sys.pilis.PiLiSApplication;
import technology.sys.pilis.dtos.ButtonDto;
import technology.sys.pilis.dtos.InfoDto;
import technology.sys.pilis.dtos.LightDto;
import technology.sys.pilis.services.ILightService;

/**
 * @author Jaromir Sys
 */
@Controller
@Service
@EnableAutoConfiguration
//@ComponentScan//(value = "technology.sys.pilis.services")
public class LightController {

    private static final String REDIRECT_INFO = "redirect:/pilis/info";

    private static final Logger LOG = LogManager.getLogger(LightController.class);

    private ILightService lightControllerPlugin;

    @Autowired
    public LightController(ILightService lightControllerPlugin) {
        this.lightControllerPlugin = lightControllerPlugin;
    }

    @RequestMapping(value = {"/pilis/turnOn/{lightName}", "/pilis/turnOn"})
    public String turnOn(@PathVariable(required = false) String lightName) {
        lightControllerPlugin.turnOn(lightName);
        return REDIRECT_INFO;
    }

    @RequestMapping(value = {"/pilis/turnOff", "/pilis/turnOff/{lightName}"})
    public String turnOff(@PathVariable(required = false) String lightName) {
        lightControllerPlugin.turnOff(lightName);
        return REDIRECT_INFO;
    }

    @RequestMapping(value = {"/pilis/toggle", "/pilis/toggle/{lightName}"})
    public String toggle(@PathVariable(required = false) String lightName) {
        lightControllerPlugin.toggle(lightName);
        return REDIRECT_INFO;
    }

    @RequestMapping("/")
    public String home() {
        return REDIRECT_INFO;
    }

    @RequestMapping(value = {"/pilis/info", "/pilis/"})
    public String info(Model model) {
        InfoDto info = new InfoDto();

        try (BufferedReader br = new BufferedReader(new FileReader(PiLiSApplication.PID))) {
            info.setProcessId(br.readLine());
        } catch (Exception e) {
            info.setProcessId("reading current PID from file failed");
        }

        info.setDirectory(System.getProperty("user.dir"));
        info.setDate(LocalDate.now());
        info.setTime(LocalTime.now());

        info.setLights(lightControllerPlugin.getLights().stream().map(LightDto::new).collect(Collectors.toList()));

        List<ButtonDto> buttons = new ArrayList<>();
        buttons.add(new ButtonDto("toggle", "Toggle"));
        buttons.add(new ButtonDto("turnOn", "Turn On"));
        buttons.add(new ButtonDto("turnOff", "Turn Off"));
        buttons.add(new ButtonDto("info", "Info"));
        model.addAttribute("buttons", buttons);
        model.addAttribute("info", info);

        return "index";
    }
}
