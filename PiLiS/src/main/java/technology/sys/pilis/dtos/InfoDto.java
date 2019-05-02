package technology.sys.pilis.dtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import technology.sys.pilis.entities.Light;

/**
 *
 * @author Jaromir Sys
 */
public class InfoDto
{

    private String processId;
    private String directory;
    private LocalTime time;
    private LocalDate date;
    private List<LightDto> lights;

    public InfoDto()
    {
    }

    public String getProcessId()
    {
        return processId;
    }

    public void setProcessId(String processId)
    {
        this.processId = processId;
    }

    public String getDirectory()
    {
        return directory;
    }

    public void setDirectory(String directory)
    {
        this.directory = directory;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public void setTime(LocalTime time)
    {
        this.time = time;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public List<LightDto> getLights()
    {
        return lights;
    }

    public void setLights(List<LightDto> lights)
    {
        this.lights = lights;
    }

}
