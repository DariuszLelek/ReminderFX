/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reminder.dataControllers;

import hibernate.util.HibernateUtil;
import javafx.scene.layout.AnchorPane;
import utilities.Utilities;

/**
 *
 * @author Dariusz
 */
public abstract class ComponentUtil {
    protected final utilities.Utilities utils;
    protected final HibernateUtil hiberUtil;

    public ComponentUtil() {
        utils = Utilities.getInstance();
        hiberUtil = HibernateUtil.getInstance();
    }
    
    protected abstract void clearCache();
    
    protected abstract void addComponentToDisplay(Object component, AnchorPane pane);
}
