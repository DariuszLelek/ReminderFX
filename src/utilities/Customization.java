/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Dariusz
 */
public class Customization {
    public List<AnchorPane> toDoComponents;

    public Customization() {
        toDoComponents = new ArrayList<>();
    }
    
    public enum CustomizationObject{
        TODOCOMPONENT;
    }
}
