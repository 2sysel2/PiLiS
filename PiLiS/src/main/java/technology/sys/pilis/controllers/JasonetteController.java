package technology.sys.pilis.controllers;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Jaromir Sys
 */
@Controller
@Service
@EnableAutoConfiguration
public class JasonetteController {

    private static final Logger LOG = LogManager.getLogger(JasonetteController.class);

    @RequestMapping("/pilis/app")
    public @ResponseBody
    String jasonette() {
        ClassLoader classLoader = getClass().getClassLoader();
        String result = StringUtils.EMPTY;

        try {
            InputStream inputStream = classLoader.getResourceAsStream("app.json");
            List<String> lines = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
            StringBuilder builder = new StringBuilder();
            lines.forEach(builder::append);
            result = builder.toString();
        } catch (IOException e) {
            LOG.error(e);
        }

        return result;
    }
}
