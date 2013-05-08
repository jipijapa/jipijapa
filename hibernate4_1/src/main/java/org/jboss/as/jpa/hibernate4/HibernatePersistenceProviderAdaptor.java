/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.as.jpa.hibernate4;

import java.util.Map;
import java.util.Properties;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.jboss.as.jpa.hibernate4.management.HibernateManagementAdaptor;
import org.jipijapa.plugin.spi.JtaManager;
import org.jipijapa.plugin.spi.ManagementAdaptor;
import org.jipijapa.plugin.spi.PersistenceProviderAdaptor;
import org.jipijapa.plugin.spi.PersistenceUnitMetadata;

/**
 * Implements the PersistenceProviderAdaptor for Hibernate
 *
 * @author Scott Marlow
 */
public class HibernatePersistenceProviderAdaptor implements PersistenceProviderAdaptor {

    private volatile JBossAppServerJtaPlatform appServerJtaPlatform;

    @Override
    public void injectJtaManager(JtaManager jtaManager) {
        appServerJtaPlatform = new JBossAppServerJtaPlatform(jtaManager);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addProviderProperties(Map properties, PersistenceUnitMetadata pu) {
        putPropertyIfAbsent(pu, properties, Configuration.USE_NEW_ID_GENERATOR_MAPPINGS, "true");
        putPropertyIfAbsent(pu, properties, org.hibernate.ejb.AvailableSettings.SCANNER, HibernateAnnotationScanner.class.getName());
        properties.put(AvailableSettings.APP_CLASSLOADER, pu.getClassLoader());
        putPropertyIfAbsent(pu, properties, AvailableSettings.JTA_PLATFORM, appServerJtaPlatform);
        properties.remove(AvailableSettings.TRANSACTION_MANAGER_STRATEGY);  // remove legacy way of specifying TX manager (conflicts with JTA_PLATFORM)
        putPropertyIfAbsent(pu,properties, org.hibernate.ejb.AvailableSettings.ENTITY_MANAGER_FACTORY_NAME, pu.getScopedPersistenceUnitName());
        putPropertyIfAbsent(pu, properties, AvailableSettings.SESSION_FACTORY_NAME, pu.getScopedPersistenceUnitName());
        if (!pu.getProperties().containsKey(AvailableSettings.SESSION_FACTORY_NAME)) {
            putPropertyIfAbsent(pu, properties, AvailableSettings.SESSION_FACTORY_NAME_IS_JNDI, Boolean.FALSE);
        }
    }

    @Override
    public void addProviderDependencies(PersistenceUnitMetadata pu) {
        Properties properties = pu.getProperties();
        if (Boolean.parseBoolean(properties.getProperty(AvailableSettings.USE_SECOND_LEVEL_CACHE))) {
            HibernateSecondLevelCache.addSecondLevelCacheDependencies(pu.getProperties(), pu.getScopedPersistenceUnitName());
        }
    }

    private void putPropertyIfAbsent(PersistenceUnitMetadata pu, Map properties, String property, Object value) {
        if (!pu.getProperties().containsKey(property)) {
            properties.put(property, value);
        }
    }

    @Override
    public void beforeCreateContainerEntityManagerFactory(PersistenceUnitMetadata pu) {
        // set backdoor annotation scanner access to pu
        HibernateAnnotationScanner.setThreadLocalPersistenceUnitMetadata(pu);
    }

    @Override
    public void afterCreateContainerEntityManagerFactory(PersistenceUnitMetadata pu) {
        // clear backdoor annotation scanner access to pu
        HibernateAnnotationScanner.clearThreadLocalPersistenceUnitMetadata();
    }

    @Override
    public ManagementAdaptor getManagementAdaptor() {
        return HibernateManagementAdaptor.getInstance();
    }

    /**
     * determine if management console can display the second level cache entries
     *
     * @param pu
     * @return false if a custom AvailableSettings.CACHE_REGION_PREFIX property is specified.
     *         true if the scoped persistence unit name is used to prefix cache entries.
     */
    @Override
    public boolean doesScopedPersistenceUnitNameIdentifyCacheRegionName(PersistenceUnitMetadata pu) {
        String cacheRegionPrefix = pu.getProperties().getProperty(AvailableSettings.CACHE_REGION_PREFIX);

        return cacheRegionPrefix == null || cacheRegionPrefix.equals(pu.getScopedPersistenceUnitName());
    }

    public void cleanup(PersistenceUnitMetadata pu) {
        HibernateAnnotationScanner.cleanup(pu);
    }
}

