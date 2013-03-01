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

package org.jipijapa.spi.statistics;

import java.util.Collection;

/**
 * defines group of statistics that allows for nested statistics as well.  Each defined level
 * can include values, operations and optionally sub-level(s).
 *
 * @author Scott Marlow
 */
public interface GroupDefinition {

    /**
     * get description of available values
     */
    Collection<Description> valueDescriptions();

    /**
     * get description of available operations
     * @return
     */
    Collection<Description> operationDescriptions();

    Result getValue(Description description);

    Result invokeOperation(Description description, Object... operationArgs);

    /**
     * get definitions of subgroups.
     *
     * top-level example for Hibernate  { "Entity", "Collection", "NaturalId", "Query", "SecondLevelCache"}
     * getSubgroup("SecondLevelCache") will return a GroupDefinition that has a sub-group definition for
     * each cache region name (names of entity classes)
     *
     * @return names
     */
    Collection<Description> getSubgroupNames();

    GroupDefinition getSubgroup(Description description);


}
