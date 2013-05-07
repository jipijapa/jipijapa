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

package org.jboss.as.jpa.hibernate4.management;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.jipijapa.management.spi.EntityManagerFactoryAccess;
import org.jipijapa.management.spi.Operation;
import org.jipijapa.management.spi.PathAddress;

/**
 * Hibernate entity statistics
 *
 * @author Scott Marlow
 */
public class HibernateEntityStatistics extends HibernateAbstractStatistics {

    public static final String OPERATION_ENTITY_DELETE_COUNT = "entity-delete-count";
    public static final String OPERATION_ENTITY_INSERT_COUNT = "entity-insert-count";
    public static final String OPERATION_ENTITY_LOAD_COUNT = "entity-load-count";
    public static final String OPERATION_ENTITY_FETCH_COUNT = "entity-fetch-count";
    public static final String OPERATION_ENTITY_UPDATE_COUNT = "entity-update-count";

    public HibernateEntityStatistics() {
        /**
         * specify the different operations
         */
        operations.put(OPERATION_ENTITY_DELETE_COUNT, entityDeleteCount);
        types.put(OPERATION_ENTITY_DELETE_COUNT, Long.class);

        operations.put(OPERATION_ENTITY_INSERT_COUNT, entityInsertCount);
        types.put(OPERATION_ENTITY_INSERT_COUNT, Long.class);

        operations.put(OPERATION_ENTITY_LOAD_COUNT, entityLoadCount);
        types.put(OPERATION_ENTITY_LOAD_COUNT, Long.class);

        operations.put(OPERATION_ENTITY_FETCH_COUNT, entityFetchCount);
        types.put(OPERATION_ENTITY_FETCH_COUNT, Long.class);

        operations.put(OPERATION_ENTITY_UPDATE_COUNT, entityUpdateCount);
        types.put(OPERATION_ENTITY_UPDATE_COUNT, Long.class);
    }

    private org.hibernate.stat.Statistics getBaseStatistics(EntityManagerFactory entityManagerFactory) {
        HibernateEntityManagerFactory entityManagerFactoryImpl = (HibernateEntityManagerFactory) entityManagerFactory;
        SessionFactory sessionFactory = entityManagerFactoryImpl.getSessionFactory();
        if (sessionFactory != null) {
            return sessionFactory.getStatistics();
        }
        return null;
    }

    private org.hibernate.stat.EntityStatistics getStatistics(EntityManagerFactory entityManagerFactory, String entityName) {
        HibernateEntityManagerFactory entityManagerFactoryImpl = (HibernateEntityManagerFactory) entityManagerFactory;
        SessionFactory sessionFactory = entityManagerFactoryImpl.getSessionFactory();
        if (sessionFactory != null) {
            return sessionFactory.getStatistics().getEntityStatistics(entityName);
        }
        return null;
    }

    private Operation entityDeleteCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args), getStatisticName(args)).getDeleteCount());
        }
    };

    private Operation entityFetchCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args), getStatisticName(args)).getFetchCount());
        }
    };

    private Operation entityInsertCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args), getStatisticName(args)).getInsertCount());
        }
    };

    private Operation entityLoadCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args), getStatisticName(args)).getLoadCount());
        }
    };

    private Operation entityUpdateCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args), getStatisticName(args)).getUpdateCount());
        }
    };

    @Override
    public Set<String> getNames() {

        return Collections.unmodifiableSet(operations.keySet());
    }

    @Override
    public Collection<String> getDynamicChildrenNames(EntityManagerFactoryAccess entityManagerFactoryLookup, PathAddress pathAddress) {

        return Collections.unmodifiableCollection(Arrays.asList(
                getBaseStatistics(entityManagerFactoryLookup.entityManagerFactory(pathAddress.getValue(HibernateStatistics.PROVIDER_LABEL))).getEntityNames()));
    }
}
