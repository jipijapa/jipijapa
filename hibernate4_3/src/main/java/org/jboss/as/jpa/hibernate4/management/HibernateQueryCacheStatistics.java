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
 * Hibernate query cache statistics
 *
 * @author Scott Marlow
 */
public class HibernateQueryCacheStatistics extends HibernateAbstractStatistics {

    public static final String ATTRIBUTE_QUERY_NAME = "query-name";
    public static final String OPERATION_QUERY_EXECUTION_COUNT = "query-execution-count";
    public static final String OPERATION_QUERY_EXECUTION_ROW_COUNT = "query-execution-row-count";
    public static final String OPERATION_QUERY_EXECUTION_AVG_TIME = "query-execution-average-time";
    public static final String OPERATION_QUERY_EXECUTION_MAX_TIME = "query-execution-max-time";
    public static final String OPERATION_QUERY_EXECUTION_MIN_TIME = "query-execution-min-time";
    public static final String OPERATION_QUERY_CACHE_HIT_COUNT = "query-cache-hit-count";
    public static final String OPERATION_QUERY_CACHE_MISS_COUNT = "query-cache-miss-count";
    public static final String OPERATION_QUERY_CACHE_PUT_COUNT = "query-cache-put-count";

    public HibernateQueryCacheStatistics() {
        /**
         * specify the different operations
         */
        operations.put(ATTRIBUTE_QUERY_NAME, showQueryName);
        types.put(ATTRIBUTE_QUERY_NAME,String.class);

        operations.put(OPERATION_QUERY_EXECUTION_COUNT, queryExecutionCount);
        types.put(OPERATION_QUERY_EXECUTION_COUNT, Long.class);

        operations.put(OPERATION_QUERY_EXECUTION_ROW_COUNT, queryExecutionRowCount);
        types.put(OPERATION_QUERY_EXECUTION_ROW_COUNT, Long.class);

        operations.put(OPERATION_QUERY_EXECUTION_AVG_TIME, queryExecutionAverageTime);
        types.put(OPERATION_QUERY_EXECUTION_AVG_TIME, Long.class);

        operations.put(OPERATION_QUERY_EXECUTION_MAX_TIME, queryExecutionMaximumTime);
        types.put(OPERATION_QUERY_EXECUTION_MAX_TIME, Long.class);

        operations.put(OPERATION_QUERY_EXECUTION_MIN_TIME, queryExecutionMinimumTime);
        types.put(OPERATION_QUERY_EXECUTION_MIN_TIME, Long.class);

        operations.put(OPERATION_QUERY_CACHE_HIT_COUNT, queryCacheHitCount);
        types.put(OPERATION_QUERY_CACHE_HIT_COUNT, Long.class);

        operations.put(OPERATION_QUERY_CACHE_MISS_COUNT, queryCacheMissCount);
        types.put(OPERATION_QUERY_CACHE_MISS_COUNT, Long.class);

        operations.put(OPERATION_QUERY_CACHE_PUT_COUNT, queryCachePutCount);
        types.put(OPERATION_QUERY_CACHE_PUT_COUNT, Long.class);
   }

    org.hibernate.stat.QueryStatistics getStatistics(EntityManagerFactory entityManagerFactory, String queryName) {
        HibernateEntityManagerFactory entityManagerFactoryImpl = (HibernateEntityManagerFactory) entityManagerFactory;
        SessionFactory sessionFactory = entityManagerFactoryImpl.getSessionFactory();
        if (sessionFactory != null) {
            return sessionFactory.getStatistics().getQueryStatistics(queryName);
        }
        return null;
    }

    private Operation queryExecutionCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args), getStatisticName(args)).getExecutionCount());
        }
    };

    private Operation queryExecutionMaximumTime = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args), getStatisticName(args)).getExecutionMaxTime());
        }
    };

    private Operation queryExecutionRowCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args), getStatisticName(args)).getExecutionRowCount());
        }
    };

    private Operation queryExecutionAverageTime = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args), getStatisticName(args)).getExecutionAvgTime());
        }
    };

    private Operation queryExecutionMinimumTime = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args), getStatisticName(args)).getExecutionMinTime());
        }
    };

    private Operation queryCacheHitCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args), getStatisticName(args)).getCacheHitCount());
        }
    };

    private Operation queryCacheMissCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args), getStatisticName(args)).getCacheMissCount());
        }
    };

    private Operation queryCachePutCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args), getStatisticName(args)).getCachePutCount());
        }
    };

    private Operation showQueryName = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return getStatisticName(args);
        }
    };

}
