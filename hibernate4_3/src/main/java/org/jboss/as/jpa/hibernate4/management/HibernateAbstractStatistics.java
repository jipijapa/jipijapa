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

import org.jipijapa.core.AbstractStatistics;
import org.jipijapa.management.spi.EntityManagerFactoryAccess;
import org.jipijapa.management.spi.PathAddress;

/**
 * HibernateAbstractStatistics
 *
 * @author Scott Marlow
 */
public abstract class HibernateAbstractStatistics extends AbstractStatistics {

    private static final String RESOURCE_BUNDLE = HibernateAbstractStatistics.class.getPackage().getName() + ".LocalDescriptions";
    private static final String RESOURCE_BUNDLE_KEY_PREFIX = "hibernate";

    @Override
    public String getResourceBundleName() {
        return RESOURCE_BUNDLE;
    }

    @Override
    public String getResourceBundleKeyPrefix() {
        return RESOURCE_BUNDLE_KEY_PREFIX;
    }

    protected EntityManagerFactory getEntityManagerFactory(Object[] args) {
        PathAddress pathAddress = getPathAddress(args);
        for(Object arg :args) {
            if (arg instanceof EntityManagerFactoryAccess) {
                EntityManagerFactoryAccess entityManagerFactoryAccess = (EntityManagerFactoryAccess)arg;
                return entityManagerFactoryAccess.entityManagerFactory(pathAddress.getValue(HibernateStatistics.PROVIDER_LABEL));
            }
        }
        return null;
    }

}
