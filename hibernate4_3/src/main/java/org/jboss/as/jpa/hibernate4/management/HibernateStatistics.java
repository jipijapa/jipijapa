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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Cache;
import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.jipijapa.management.spi.EntityManagerFactoryAccess;
import org.jipijapa.management.spi.Operation;
import org.jipijapa.management.spi.PathAddress;
import org.jipijapa.management.spi.Statistics;

/**
 * HibernateStatistics
 *
 * @author Scott Marlow
 */
public class HibernateStatistics extends HibernateAbstractStatistics {

    public static final String PROVIDER_LABEL = "hibernate-persistence-unit";
    public static final String OPERATION_CLEAR = "clear";
    public static final String OPERATION_EVICTALL = "evict-all";
    public static final String OPERATION_SUMMARY = "summary";
    public static final String OPERATION_STATISTICS_ENABLED = "enabled";
    public static final String OPERATION_ENTITY_DELETE_COUNT = "entity-delete-count";
    public static final String OPERATION_ENTITY_INSERT_COUNT = "entity-insert-count";
    public static final String OPERATION_ENTITY_LOAD_COUNT = "entity-load-count";
    public static final String OPERATION_ENTITY_FETCH_COUNT = "entity-fetch-count";
    public static final String OPERATION_ENTITY_UPDATE_COUNT = "entity-update-count";
    public static final String OPERATION_COLLECTION_FETCH_COUNT = "collection-fetch-count";
    public static final String OPERATION_COLLECTION_LOAD_COUNT = "collection-load-count";
    public static final String OPERATION_COLLECTION_RECREATED_COUNT = "collection-recreated-count";
    public static final String OPERATION_COLLECTION_REMOVE_COUNT = "collection-remove-count";
    public static final String OPERATION_COLLECTION_UPDATE_COUNT = "collection-update-count";
    public static final String OPERATION_QUERYCACHE_HIT_COUNT = "query-cache-hit-count";
    public static final String OPERATION_QUERYCACHE_MISS_COUNT ="query-cache-miss-count";
    public static final String OPERATION_QUERYQUERYCACHE_PUT_COUNT ="query-cache-put-count";
    public static final String OPERATION_QUERYEXECUTION_COUNT ="query-execution-count";
    public static final String OPERATION_QUERYEXECUTION_MAX_TIME ="query-execution-max-time";
    public static final String OPERATION_QUERYEXECUTION_MAX_TIME_STRING ="query-execution-max-time-query-string";
    public static final String OPERATION_SECONDLEVELCACHE_HIT_COUNT= "second-level-cache-hit-count";
    public static final String OPERATION_SECONDLEVELCACHE_MISS_COUNT="second-level-cache-miss-count";
    public static final String OPERATION_SECONDLEVELCACHE_PUT_COUNT="second-level-cache-put-count";

    public static final String OPERATION_FLUSH_COUNT = "flush-count";
    public static final String OPERATION_CONNECT_COUNT = "connect-count";
    public static final String OPERATION_SESSION_CLOSE_COUNT = "session-close-count";
    public static final String OPERATION_SESSION_OPEN_COUNT = "session-open-count";
    public static final String OPERATION_SUCCESSFUL_TRANSACTION_COUNT = "successful-transaction-count";
    public static final String OPERATION_COMPLETED_TRANSACTION_COUNT = "completed-transaction-count";
    public static final String OPERATION_PREPARED_STATEMENT_COUNT = "prepared-statement-count";
    public static final String OPERATION_CLOSE_STATEMENT_COUNT = "close-statement-count";
    public static final String OPERATION_OPTIMISTIC_FAILURE_COUNT = "optimistic-failure-count";
    public static final String ENTITYCACHE = "entity-cache";
    public static final String COLLECTION = "collection";
    public static final String ENTITY = "entity";
    public static final String QUERYCACHE = "query-cache";

    private final Map<String, Statistics> childrenStatistics = new HashMap<String,Statistics>();
    public HibernateStatistics() {

        /**
         * specify the different operations
         */
        operations.put(PROVIDER_LABEL, label);
        types.put(PROVIDER_LABEL,String.class);

        operations.put(OPERATION_CLEAR, clear);
        types.put(OPERATION_CLEAR, Operation.class);

        operations.put(OPERATION_EVICTALL, evictAll);
        types.put(OPERATION_EVICTALL, Operation.class);

        operations.put(OPERATION_SUMMARY, summary);
        types.put(OPERATION_SUMMARY, Operation.class);

        operations.put(OPERATION_STATISTICS_ENABLED, statisticsEnabled);
        types.put(OPERATION_STATISTICS_ENABLED, Boolean.class);
        writeableNames.add(OPERATION_STATISTICS_ENABLED);   // make 'enabled' writeable

        operations.put(OPERATION_ENTITY_DELETE_COUNT, entityDeleteCount);
        types.put(OPERATION_ENTITY_DELETE_COUNT, Long.class);

        operations.put(OPERATION_COLLECTION_FETCH_COUNT, collectionFetchCount);
        types.put(OPERATION_COLLECTION_FETCH_COUNT, Long.class);

        operations.put(OPERATION_COLLECTION_LOAD_COUNT, collectionLoadCount);
        types.put(OPERATION_COLLECTION_LOAD_COUNT, Long.class);

        operations.put(OPERATION_COLLECTION_RECREATED_COUNT, collectionRecreatedCount);
        types.put(OPERATION_COLLECTION_RECREATED_COUNT, Long.class);

        operations.put(OPERATION_COLLECTION_REMOVE_COUNT, collectionRemoveCount);
        types.put(OPERATION_COLLECTION_REMOVE_COUNT, Long.class);

        operations.put(OPERATION_COLLECTION_UPDATE_COUNT, collectionUpdateCount);
        types.put(OPERATION_COLLECTION_UPDATE_COUNT, Long.class);

        operations.put(OPERATION_QUERYCACHE_HIT_COUNT, queryCacheHitCount);
        types.put(OPERATION_QUERYCACHE_HIT_COUNT, Long.class);

        operations.put(OPERATION_QUERYCACHE_MISS_COUNT, queryCacheMissCount);
        types.put(OPERATION_QUERYCACHE_MISS_COUNT, Long.class);

        operations.put(OPERATION_QUERYQUERYCACHE_PUT_COUNT, queryCachePutCount);
        types.put(OPERATION_QUERYQUERYCACHE_PUT_COUNT, Long.class);

        operations.put(OPERATION_QUERYEXECUTION_COUNT, queryExecutionCount);
        types.put(OPERATION_QUERYEXECUTION_COUNT, Long.class);

        operations.put(OPERATION_QUERYEXECUTION_MAX_TIME, queryExecutionMaxTime);
        types.put(OPERATION_QUERYEXECUTION_MAX_TIME, Long.class);

        operations.put(OPERATION_QUERYEXECUTION_MAX_TIME_STRING, queryExecutionMaxTimeString);
        types.put(OPERATION_QUERYEXECUTION_MAX_TIME_STRING, String.class);

        operations.put(OPERATION_ENTITY_INSERT_COUNT, entityInsertCount);
        types.put(OPERATION_ENTITY_INSERT_COUNT, Long.class);

        operations.put(OPERATION_ENTITY_LOAD_COUNT, entityLoadCount);
        types.put(OPERATION_ENTITY_LOAD_COUNT, Long.class);

        operations.put(OPERATION_ENTITY_FETCH_COUNT, entityFetchCount);
        types.put(OPERATION_ENTITY_FETCH_COUNT, Long.class);

        operations.put(OPERATION_ENTITY_UPDATE_COUNT, entityUpdateCount);
        types.put(OPERATION_ENTITY_UPDATE_COUNT, Long.class);

        operations.put(OPERATION_FLUSH_COUNT, flushCount);
        types.put(OPERATION_FLUSH_COUNT, Long.class);

        operations.put(OPERATION_CONNECT_COUNT, connectCount);
        types.put(OPERATION_CONNECT_COUNT, Long.class);

        operations.put(OPERATION_SESSION_CLOSE_COUNT, sessionCloseCount);
        types.put(OPERATION_SESSION_CLOSE_COUNT, Long.class);

        operations.put(OPERATION_SESSION_OPEN_COUNT, sessionOpenCount);
        types.put(OPERATION_SESSION_OPEN_COUNT, Long.class);

        operations.put(OPERATION_SUCCESSFUL_TRANSACTION_COUNT, transactionCount);
        types.put(OPERATION_SUCCESSFUL_TRANSACTION_COUNT, Long.class);

        operations.put(OPERATION_COMPLETED_TRANSACTION_COUNT, transactionCompletedCount);
        types.put(OPERATION_COMPLETED_TRANSACTION_COUNT, Long.class);

        operations.put(OPERATION_PREPARED_STATEMENT_COUNT, preparedStatementCount);
        types.put(OPERATION_PREPARED_STATEMENT_COUNT, Long.class);

        operations.put(OPERATION_CLOSE_STATEMENT_COUNT, closedStatementCount);
        types.put(OPERATION_CLOSE_STATEMENT_COUNT, Long.class);

        operations.put(OPERATION_OPTIMISTIC_FAILURE_COUNT, optimisticFailureCount);
        types.put(OPERATION_OPTIMISTIC_FAILURE_COUNT, Long.class);

        operations.put(OPERATION_SECONDLEVELCACHE_HIT_COUNT, secondLevelCacheHitCount);
        types.put(OPERATION_SECONDLEVELCACHE_HIT_COUNT, Long.class);

        operations.put(OPERATION_SECONDLEVELCACHE_MISS_COUNT, secondLevelCacheMissCount);
        types.put(OPERATION_SECONDLEVELCACHE_MISS_COUNT, Long.class);

        operations.put(OPERATION_SECONDLEVELCACHE_PUT_COUNT, secondLevelCachePutCount);
        types.put(OPERATION_SECONDLEVELCACHE_PUT_COUNT, Long.class);

        /**
         * Specify the children statistics
         */
        childrenNames.add(ENTITY);
        childrenStatistics.put(ENTITY, new HibernateEntityStatistics());

        childrenNames.add(ENTITYCACHE);
        childrenStatistics.put(ENTITYCACHE, new HibernateEntityCacheStatistics());

        childrenNames.add(COLLECTION);
        childrenStatistics.put(COLLECTION, new HibernateCollectionStatistics());

        childrenNames.add(QUERYCACHE);
        childrenStatistics.put(QUERYCACHE , new HibernateQueryCacheStatistics());

    }

    @Override
    public Statistics getChild(String childName) {
        return childrenStatistics.get(childName);
    }

    static final org.hibernate.stat.Statistics getStatistics(final EntityManagerFactory entityManagerFactory) {
        HibernateEntityManagerFactory entityManagerFactoryImpl = (HibernateEntityManagerFactory) entityManagerFactory;
        SessionFactory sessionFactory = entityManagerFactoryImpl.getSessionFactory();
        if (sessionFactory != null) {
            return sessionFactory.getStatistics();
        }
        return null;
    }

    @Override
    public Collection<String> getDynamicChildrenNames(EntityManagerFactoryAccess entityManagerFactoryLookup, PathAddress pathAddress) {

        return Collections.EMPTY_LIST;
    }

    private Operation clear = new Operation() {
        @Override
        public Object invoke(Object... args) {
            getStatistics(getEntityManagerFactory(args)).clear();
            return null;
        }
    };

    private Operation label = new Operation() {
        @Override
        public Object invoke(Object... args) {
            PathAddress pathAddress = getPathAddress(args);
            if (pathAddress != null) {
                return pathAddress.getValue(PROVIDER_LABEL);
            }
            return "";
        }
    };

    private Operation evictAll = new Operation() {
        @Override
        public Object invoke(Object... args) {
            Cache secondLevelCache = getEntityManagerFactory(args).getCache();
            if (secondLevelCache != null) {
                secondLevelCache.evictAll();
            }
            return null;
        }
    };

    private Operation summary = new Operation() {
        @Override
        public Object invoke(Object... args) {
            getStatistics(getEntityManagerFactory(args)).logSummary();
            return null;
        }
    };

    private Operation statisticsEnabled = new Operation() {
        @Override
        public Object invoke(Object... args) {
            if (args.length > 0 && args[0] instanceof Boolean) {
                Boolean newValue = (Boolean) args[0];
                getStatistics(getEntityManagerFactory(args)).setStatisticsEnabled(newValue.booleanValue());
            }
            return Boolean.valueOf(getStatistics(getEntityManagerFactory(args)).isStatisticsEnabled());
        }
    };

    private Operation entityDeleteCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getEntityDeleteCount());
        }
    };

    private Operation collectionLoadCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getCollectionLoadCount());
        }
    };

    private Operation collectionFetchCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getCollectionFetchCount());
        }
    };

    private Operation collectionRecreatedCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getCollectionRecreateCount());
        }
    };

    private Operation collectionRemoveCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getCollectionRemoveCount());
        }
    };

    private Operation collectionUpdateCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getCollectionUpdateCount());
        }
    };

    private Operation queryCacheHitCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getQueryCacheHitCount());
        }
    };
    private Operation queryCacheMissCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getQueryCacheMissCount());
        }
    };
    private Operation queryCachePutCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getQueryCachePutCount());
        }
    };
    private Operation queryExecutionCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getQueryExecutionCount());
        }
    };
    private Operation queryExecutionMaxTime = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getQueryExecutionMaxTime());
        }
    };
    private Operation queryExecutionMaxTimeString = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return String.valueOf(getStatistics(getEntityManagerFactory(args)).getQueryExecutionMaxTimeQueryString());
        }
    };

    private Operation entityFetchCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getEntityFetchCount());
        }
    };

    private Operation entityInsertCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getEntityInsertCount());
        }
    };

    private Operation entityLoadCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getEntityLoadCount());
        }
    };

    private Operation entityUpdateCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getEntityUpdateCount());
        }
    };

    private Operation flushCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getFlushCount());
        }
    };

    private Operation connectCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getConnectCount());
        }
    };

    private Operation sessionCloseCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getSessionCloseCount());
        }
    };

    private Operation sessionOpenCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getSessionOpenCount());
        }
    };

    private Operation transactionCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getTransactionCount());
        }
    };

    private Operation transactionCompletedCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getSuccessfulTransactionCount());
        }
    };

    private Operation preparedStatementCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getPrepareStatementCount());
        }
    };

    private Operation closedStatementCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getCloseStatementCount());
        }
    };

    private Operation optimisticFailureCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getOptimisticFailureCount());
        }
    };

    private Operation secondLevelCacheHitCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getSecondLevelCacheHitCount());
        }
    };

    private Operation secondLevelCacheMissCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getSecondLevelCacheMissCount());
        }
    };

    private Operation secondLevelCachePutCount = new Operation() {
        @Override
        public Object invoke(Object... args) {
            return Long.valueOf(getStatistics(getEntityManagerFactory(args)).getSecondLevelCachePutCount());
        }
    };

}
