package technology.sys.pilis;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;
import technology.sys.pilis.controllers.LightController;

/**
 * @author Jaromir Sys
 */
@SpringBootApplication
public class PiLiSApplication {

    public static File PID = new File("pid.txt");

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(PiLiSApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter(PID));
        springApplication.run(args);
    }

}
