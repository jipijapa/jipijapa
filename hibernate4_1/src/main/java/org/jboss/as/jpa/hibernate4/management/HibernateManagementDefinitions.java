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

import java.util.ArrayList;
import java.util.Collection;

import org.jipijapa.spi.statistics.Description;
import org.jipijapa.spi.statistics.GroupDefinition;
import org.jipijapa.spi.statistics.Result;


/**
 * HibernateManagementDefinitions
 *
 * @author Scott Marlow
 */
public class HibernateManagementDefinitions implements GroupDefinition {

    private static final Collection<Description> valueDescriptions = new ArrayList<Description>();
    static {
        valueDescriptions.add(new Description() {
            @Override
            public String getName() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getDescription() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        })
    }


    @Override
    public Collection<Description> valueDescriptions() {
        return null;
    }

    @Override
    public Collection<Description> operationDescriptions() {
        return null;
    }

    @Override
    public Result getValue(Description description) {
        return null;
    }

    @Override
    public Result invokeOperation(Description description, Object... operationArgs) {
        return null;
    }

    @Override
    public Collection<Description> getSubgroupNames() {
        return null;
    }

    @Override
    public GroupDefinition getSubgroup(Description description) {
        return null;
    }
}
