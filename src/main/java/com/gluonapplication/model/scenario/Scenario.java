package com.gluonapplication.model.scenario;

import com.gluonapplication.model.choice.Choice;

import java.util.ArrayList;

public class Scenario {

    private final String scenarioText;
    private final ArrayList<Choice> choices = new ArrayList<>();
    private final Integer ID;

    private String scenarioIDForImage;

    public Scenario(String scenarioTextDescription) {
        this.ID = null;
        scenarioText = scenarioTextDescription;
    }

    public Scenario(String scenarioTextDescription, Integer id) {
        this.ID = id;
        scenarioText = scenarioTextDescription;
    }

    public Scenario(String scenarioTextDescription, String scenarioIDForImage ) {
        this.ID = null;
        this.scenarioIDForImage = scenarioIDForImage;
        scenarioText = scenarioTextDescription;
    }

    public void addChoice(Choice choice) {
        this.choices.add(choice);
    }

    public String getScenarioIDForImage() {
        return scenarioIDForImage;
    }

    public String getScenarioText() {
        return scenarioText;
    }

    public ArrayList<Choice> getChoices() {
        return choices;
    }

    public Integer getID() {
        return ID;
    }
}
