/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reminder.dataControllers;

/**
 *
 * @author Dariusz
 */
public interface ComponentsControll {

    public void sortComponents();

    public void loadComponents();

    public void saveComponents();
    
    public boolean componentExists(String name);
    
    public void removeComponent(String name);

}
