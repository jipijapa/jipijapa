package org.jipijapa.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.persistence.EntityManagerFactory;

import org.jipijapa.management.spi.DynamicName;
import org.jipijapa.management.spi.EntityManagerFactoryAccess;
import org.jipijapa.management.spi.Operation;
import org.jipijapa.management.spi.StatisticName;
import org.jipijapa.management.spi.Statistics;

/**
 * AbstractStatistics
 *
 * @author Scott Marlow
 */
public abstract class AbstractStatistics implements Statistics {

    protected Map<String,Operation> operations = new HashMap<String, Operation>();
    protected Set<String> childrenNames = new HashSet<String>();
    protected Set<String> writeableNames = new HashSet<String>();
    protected Map<String, Class> types = new HashMap<String, Class>();
    protected Map<Locale, ResourceBundle> rbs = new HashMap<Locale, ResourceBundle>();

    @Override
    public Set<String> getNames() {
        return Collections.unmodifiableSet(operations.keySet());
    }

    @Override
    public Class getType(String name) {
        return types.get(name);
    }

    @Override
    public boolean isOperation(String name) {
        return Operation.class.equals(getType(name));
    }

    @Override
    public boolean isAttribute(String name) {
        return ! isOperation(name);
    }

    @Override
    public boolean isWriteable(String name) {
        return writeableNames.contains(name);
    }

    @Override
    public Object getValue(String name, EntityManagerFactoryAccess entityManagerFactoryAccess, StatisticName statisticName, DynamicName dynamicName) {
        return operations.get(name).invoke(entityManagerFactoryAccess, statisticName, dynamicName);
    }

    @Override
    public void setValue(String name, Object newValue, EntityManagerFactoryAccess entityManagerFactoryAccess, StatisticName statisticName) {
        operations.get(name).invoke(newValue, entityManagerFactoryAccess, statisticName);
    }

    protected EntityManagerFactory getEntityManagerFactory(Object[] args) {
        for(Object arg :args) {
            if (arg instanceof EntityManagerFactoryAccess) {
                EntityManagerFactoryAccess entityManagerFactoryAccess = (EntityManagerFactoryAccess)arg;
                return entityManagerFactoryAccess.entityManagerFactory();
            }
        }
        return null;
    }

    protected EntityManagerFactoryAccess getEntityManagerFactoryAccess(Object[] args) {
        for(Object arg :args) {
            if (arg instanceof EntityManagerFactoryAccess) {
                EntityManagerFactoryAccess entityManagerFactoryAccess = (EntityManagerFactoryAccess)arg;
                return entityManagerFactoryAccess;
            }
        }
        return null;
    }

    protected String getDynamicName(Object[] args) {
        for(Object arg :args) {
            if (arg instanceof DynamicName) {
                DynamicName name = (DynamicName)arg;
                return name.getName();
            }
        }
        return null;
    }

    protected String getStatisticName(Object[] args) {
        for(Object arg :args) {
            if (arg instanceof StatisticName) {
                StatisticName name = (StatisticName)arg;
                return name.getName();
            }
        }
        return null;
    }

    @Override
    public Set<String> getChildrenNames() {
        return Collections.unmodifiableSet(childrenNames);
    }

    @Override
    public Statistics getChild(String childName) {
        return null;
    }

}
