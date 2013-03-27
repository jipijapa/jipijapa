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

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.jipijapa.spi.statistics.Operation;

/**
 * Hibernate entity cache (SecondLevelCacheStatistics) statistics
 *
 * @author Scott Marlow
 */
public class HibernateEntityCacheStatistics extends HibernateAbstractStatistics {

    public static final String ATTRIBUTE_ENTITY_CACHE_REGION_NAME = "entity-cache-region-name";
    public static final String OPERATION_SECOND_LEVEL_CACHE_HIT_COUNT = "second-level-cache-hit-count";
    public static final String OPERATION_SECOND_LEVEL_CACHE_MISS_COUNT = "second-level-cache-miss-count";
    public static final String OPERATION_SECOND_LEVEL_CACHE_PUT_COUNT = "second-level-cache-put-count";
    public static final String OPERATION_SECOND_LEVEL_CACHE_COUNT_IN_MEMORY = "second-level-cache-count-in-memory";
    public static final String OPERATION_SECOND_LEVEL_CACHE_SIZE_IN_MEMORY = "second-level-cache-size-in-memory";

    public HibernateEntityCacheStatistics() {
        /**
         * specify the different operations
         */
        operations.put(ATTRIBUTE_ENTITY_CACHE_REGION_NAME, getEntityCacheRegionName);
        types.put(ATTRIBUTE_ENTITY_CACHE_REGION_NAME,String.class);

        operations.put(OPERATION_SECOND_LEVEL_CACHE_HIT_COUNT, entityCacheHitCount);
        types.put(OPERATION_SECOND_LEVEL_CACHE_HIT_COUNT, Long.class);

        operations.put(OPERATION_SECOND_LEVEL_CACHE_MISS_COUNT, entityCacheMissCount);
        types.put(OPERATION_SECOND_LEVEL_CACHE_MISS_COUNT, Long.class);

        operations.put(OPERATION_SECOND_LEVEL_CACHE_PUT_COUNT, entityCachePutCount);
        types.put(OPERATION_SECOND_LEVEL_CACHE_PUT_COUNT, Long.class);

        operations.put(OPERATION_SECOND_LEVEL_CACHE_COUNT_IN_MEMORY, entityCacheCountInMemory);
        types.put(OPERATION_SECOND_LEVEL_CACHE_COUNT_IN_MEMORY, Long.class);

        operations.put(OPERATION_SECOND_LEVEL_CACHE_SIZE_IN_MEMORY, entityCacheSizeInMemory);
        types.put(OPERATION_SECOND_LEVEL_CACHE_SIZE_IN_MEMORY, Long.class);

    }

    org.hibernate.stat.SecondLevelCacheStatistics getStatistics(EntityManagerFactory entityManagerFactory, String entityCacheRegionName) {
        HibernateEntityManagerFactory entityManagerFactoryImpl = (HibernateEntityManagerFactory) entityManagerFactory;
        SessionFactory sessionFactory = entityManagerFactoryImpl.getSessionFactory();
        if (sessionFactory != null) {
            return sessionFactory.getStatistics().getSecondLevelCacheStatistics(entityCacheRegionName);
        }
        return null;
    }
    private Operation getEntityCacheRegionName = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return getStatisticName(args);
        }
    };


    private Operation entityCacheHitCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args), getStatisticName(args)).getHitCount());
        }
    };

    private Operation entityCacheMissCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args), getStatisticName(args)).getMissCount());
        }
    };

    private Operation entityCachePutCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args), getStatisticName(args)).getPutCount());
        }
    };

    private Operation entityCacheSizeInMemory = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args), getStatisticName(args)).getSizeInMemory());
        }
    };

    private Operation entityCacheCountInMemory = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args), getStatisticName(args)).getElementCountInMemory());
        }
    };

}
