/*
 * Copyright 2017 Dariusz.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package reminder.dataControllers;

import hibernate.config.Todo;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import reminder.dataobjects.ToDoDataTransfer;

/**
 *
 * @author Dariusz
 */
public class ToDoDataController extends ComponentUtil implements ComponentsControll{
    
    // <editor-fold defaultstate="collapsed" desc=" Singleton ">
    private static ToDoDataController instance;

    public synchronized static ToDoDataController getInstance() {
        if (instance == null) {
            instance = new ToDoDataController();
        }
        return instance;
    }
    // </editor-fold>
    
    private final Map<String, AnchorPane> components;
    private final Map<String, ToDoDataTransfer> componentsData;
    private VBox containerToDo;
    private ToDoDataTransfer pendingToDo;
    private boolean displayPriorityOnly;

    public void setDisplayPriorityOnly(boolean displayPriorityOnly) {
        this.displayPriorityOnly = displayPriorityOnly;
        sortComponents();
    }

    private ToDoDataController() {
        components = new HashMap<>();
        componentsData = new TreeMap<>();
        displayPriorityOnly = false;
    }

    public ToDoDataTransfer getPendingToDo() {
        return pendingToDo;
    }

    public void setContainerToDo(VBox container) {
        this.containerToDo = container;
    }

    public void insertComponent(ToDoDataTransfer tddt) {
        if (tddt != null && !componentsData.values().contains(tddt)) {
            this.pendingToDo = tddt;
            try {
                URL resource = this.getClass().getResource("/reminder/components/ToDoComponentFXML.fxml");
                Object obj = FXMLLoader.load(resource);
                if (obj != null && obj instanceof AnchorPane) {
                    AnchorPane anchorPane = (AnchorPane) obj;
                    addComponentToDisplay(tddt, anchorPane);
                    components.put(tddt.getName(), anchorPane);
                    componentsData.put(tddt.getName(), tddt);
                    anchorPane.prefWidthProperty().bind(containerToDo.widthProperty());
                }
            } catch (IOException ex) {
                Logger.getLogger(ToDoDataController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.pendingToDo = null;
    }
    
    @Override
    public void loadComponents() {
        clearCache();
        List<Todo> todos = DataProvider.getInstance().getAllEntities(Todo.class);
        todos.sort((o1, o2) -> {
            return o1.getName().compareTo(o2.getName());
        });
        for (Todo todo : todos) {
            insertComponent(new ToDoDataTransfer(todo.getName(), todo.getPriority(), todo.getEnterDate()));
        }
    }
    
    @Override
    public void saveComponents() {
        List<Todo> oldTodos = DataProvider.getInstance().getAllEntities(Todo.class);
        deleteOldToDos(oldTodos);

        for (Map.Entry<String, ToDoDataTransfer> pair : componentsData.entrySet()) {
            Todo todo = new Todo();
            todo.setName(pair.getValue().getName());
            if (oldTodos.contains(todo)) {
                todo = oldTodos.get(oldTodos.indexOf(todo));
            } else {
                todo.setPriority(pair.getValue().getPriority());
                todo.setEnterDate(pair.getValue().getEnterDate());
            }
            hiberUtil.saveOrUpdateEntity(todo);
        }
    }

    @Override
    public void sortComponents(){
        containerToDo.getChildren().clear();
        for(ToDoDataTransfer tddt : componentsData.values()){
            addComponentToDisplay(tddt, components.get(tddt.getName()));
        }
    }
     
    @Override
    protected void addComponentToDisplay(Object component, AnchorPane anchorPane) {
        if (component instanceof ToDoDataTransfer) {
            if (displayPriorityOnly && ((ToDoDataTransfer)component).getPriority() > 0 || !displayPriorityOnly) {
                containerToDo.getChildren().add(anchorPane);
            }
        }
    }
    
    private void deleteOldToDos(List<Todo> oldTodos) {
        for (Todo todo : oldTodos) {
            if (!componentsData.keySet().contains(todo.getName())) {
                hiberUtil.deleteEntity(todo);
            }
        }
    }
    
    @Override
    public boolean componentExists(String name){
        boolean result = false;
        for(ToDoDataTransfer tddt : componentsData.values()){
            if(name.equals(tddt.getName())){
                result = true;
                break;
            }
        }
        return result;
    }
    
    @Override
    public void removeComponent(String name){
        containerToDo.getChildren().remove(components.get(name));
        components.remove(name);
        componentsData.remove(name);
    }

    @Override
    protected void clearCache() {
        components.clear();
        componentsData.clear();
        containerToDo.getChildren().clear();
    }
}
