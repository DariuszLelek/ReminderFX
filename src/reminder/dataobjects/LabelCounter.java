/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reminder.dataobjects;

import java.util.Objects;
import javafx.scene.control.Label;

/**
 *
 * @author Dariusz
 */
public class LabelCounter {
    private String name;
    private Label label;
    private boolean isDaily;

    public LabelCounter(String name, Label label, boolean isDaily) {
        this.name = name;
        this.label = label;
        this.isDaily = isDaily;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public boolean isIsDaily() {
        return isDaily;
    }

    public void setIsDaily(boolean isDaily) {
        this.isDaily = isDaily;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if(obj instanceof LabelCounter){
            LabelCounter lc = (LabelCounter) obj;
            result = lc.getName().equals(this.name);
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.name);
        return hash;
    }
}
