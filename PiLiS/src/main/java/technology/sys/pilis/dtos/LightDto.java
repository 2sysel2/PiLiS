package technology.sys.pilis.dtos;

import technology.sys.pilis.entities.Light;

import java.util.ArrayList;
import java.util.List;

public class LightDto {

    private Light light;
    private List<ButtonDto> buttons;

    public LightDto(Light light) {
        this.light = light;
        this.buttons = new ArrayList<>();


        this.buttons.add(new ButtonDto(String.format("toggle/%s",light.getName()), "Toggle"));
        this.buttons.add(new ButtonDto(String.format("turnOn/%s",light.getName()), "Turn On"));
        this.buttons.add(new ButtonDto(String.format("turnOff/%s",light.getName()), "Turn Off"));

    }

    public Light getLight() {
        return light;
    }

    public List<ButtonDto> getButtons() {
        return buttons;
    }
}
