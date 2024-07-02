package it.buzz.tahc.core.configuration.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class CheckSetting {

    private boolean enabled;
    private boolean alert;
    private boolean punish;

    private int maxVL;
    private String onEventMessage;
    private List<String> commandsOnVL;


}
