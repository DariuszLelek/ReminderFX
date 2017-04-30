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

import hibernate.util.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Dariusz
 */
public class DataProvider {

    // <editor-fold defaultstate="collapsed" desc=" Singleton ">
    private static DataProvider instance;

    public synchronized static DataProvider getInstance() {
        if (instance == null) {
            instance = new DataProvider();
        }
        return instance;
    }
    // </editor-fold>

    private final HibernateUtil hibernateUtil = HibernateUtil.getInstance();

    public int getTableCount(Class tableName) {
        int result = 0;
        Transaction tx = null;
        Session session = hibernateUtil.getSession();
        tx = session.beginTransaction();
        result = ((Number) session.createCriteria(tableName).setProjection(Projections.rowCount()).uniqueResult()).intValue();
        tx.commit();
        return result;
    }

    public <T> T getEntityByUniqueKey(Class clazz, String criterion, String value) {
        return (T) hibernateUtil.getSession().createCriteria(clazz).add(Restrictions.eq(criterion, value)).uniqueResult();
    }

    public <T> List<T> getAllEntities(Class tableClass) {
        Transaction tx = null;
        List<T> objects = new ArrayList<>();

        try {
            Session session = hibernateUtil.getSession();
            tx = session.beginTransaction();
            List list = session.createQuery("FROM " + tableClass.getName()).list();

            if (list.size() > 0) {
                for (Object listObj : list) {
                    T object = (T) listObj;
                    objects.add(object);
                }
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) {
                tx.rollback();
            }
        }
        
        return objects;
    }
}
