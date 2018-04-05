package technology.sys.pilis;

import java.io.File;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

/**
 *
 * @author Jaromir Sys
 */
public class Main
{

    public static File PID = new File("pid.txt");

    public static void main(String[] args)
    {
        SpringApplication springApplication = new SpringApplication(LightController.class);
        springApplication.addListeners(new ApplicationPidFileWriter(PID));
        springApplication.run(args);
    }

}
