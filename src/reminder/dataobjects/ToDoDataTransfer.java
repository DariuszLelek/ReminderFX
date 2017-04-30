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
package reminder.dataobjects;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Dariusz
 */
public class ToDoDataTransfer {
    private String name;
    private int priority;
    private int index;
    private Date EnterDate;

    public ToDoDataTransfer(String name, int priority, Date EnterDate) {
        this.name = name;
        this.priority = priority;
        this.EnterDate = EnterDate;
    }

    public Date getEnterDate() {
        return EnterDate;
    }

    public void setEnterDate(Date EnterDate) {
        this.EnterDate = EnterDate;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ToDoDataTransfer && ((ToDoDataTransfer)obj).getName().equals(name);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + this.priority;
        hash = 97 * hash + this.index;
        return hash;
    }
}
