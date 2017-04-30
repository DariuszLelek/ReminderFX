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
package hibernate.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.spi.Stoppable;

/**
 *
 * @author Dariusz
 */
public class HibernateUtil {
    // <editor-fold defaultstate="collapsed" desc=" Singleton ">
    private static HibernateUtil instance;

    public static HibernateUtil getInstance() {
        if (instance == null) {
            instance = new HibernateUtil();
        }
        return instance;
    }
    // </editor-fold>
    
    private final String HIBERNATE_CFG = "hibernate/config/hibernate.cfg.xml";
    private final Configuration configuration;
    private final SessionFactory sessionFactory;
    private final ServiceRegistry serviceRegistry;
    private Session session;

    private enum transType{
        UPDATE, SAVE, SAVEORUPDATE, DELETE
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Contructors ">
    public HibernateUtil() {
        configuration = new Configuration().configure(HIBERNATE_CFG);
        
        configuration.setProperty("hibernate.connection.url", "");
        configuration.setProperty("hibernate.connection.driver_class", "");
        configuration.setProperty("hibernate.connection.username", "");
        configuration.setProperty("hibernate.connection.password", "");
        configuration.setProperty("hibernate.default_catalog", "");
        
        StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
        serviceRegistryBuilder.applySettings(configuration.getProperties());
        serviceRegistry = serviceRegistryBuilder.build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Public methods ">
    public Session getSession() {
        if (session == null || !session.isOpen()) {
            try {
                session = sessionFactory.getCurrentSession();
            } catch (HibernateException he) {
                // logger
                he.printStackTrace();
                session = sessionFactory.openSession();
            }
        }
        return session;
    }
    
    public void stopConnectionProvider(){
        final SessionFactoryImplementor sessionFactoryImplementor = (SessionFactoryImplementor) sessionFactory;
        ConnectionProvider connectionProvider = sessionFactoryImplementor.getConnectionProvider();
        if (Stoppable.class.isInstance(connectionProvider)) {
            ((Stoppable) connectionProvider).stop();
        }
    }
    
    public <T> int saveEntity(T object) {
        return performTransaction(object, transType.SAVE);
    }
    
    public <T> void updateEntity(T object){
        performTransaction(object, transType.UPDATE);
    }
    
    public <T> void saveOrUpdateEntity(T object){
        performTransaction(object, transType.SAVEORUPDATE);
    }
    
    public <T> void deleteEntity(T object){
        performTransaction(object, transType.DELETE);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Private methods ">
    private <T> int performTransaction(T object, transType type){
        int id = -1;
        Session localSession = getSession();
        Transaction tx = null;
        try {
            tx = getSession().getTransaction();
            tx.begin();
            switch(type){
                case SAVE:          id = (Integer) localSession.save(object);break;
                case UPDATE:        localSession.update(object);break;
                case SAVEORUPDATE:  localSession.saveOrUpdate(object);break;
                case DELETE:        localSession.delete(object);break;
                default:            break;
            }
            tx.commit();
        } catch (Exception e) {
            if(tx != null){
                tx.rollback();
            }
            // logger
            e.printStackTrace();
        } finally {
            if (localSession != null && localSession.isOpen()) {
                localSession.close();
            }
        }
        return id;
    }
    // </editor-fold>
}
