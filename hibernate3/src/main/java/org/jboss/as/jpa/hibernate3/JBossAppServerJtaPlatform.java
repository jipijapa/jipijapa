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

package org.jboss.as.jpa.hibernate3;

import java.util.Properties;

import javax.transaction.TransactionManager;

import org.hibernate.HibernateException;
import org.hibernate.transaction.JNDITransactionManagerLookup;
import org.jipijapa.plugin.spi.JtaManager;


/**
 * Hibernate 3.6.x integration
 *
 * @author Scott Marlow
 */
public class JBossAppServerJtaPlatform extends JNDITransactionManagerLookup {

    private static volatile JtaManager jtaManager;

    public static void initJBossAppServerJtaPlatform(final JtaManager manager) {
        jtaManager = manager;
    }

    @Override
    public TransactionManager getTransactionManager(Properties props) throws HibernateException {
        return jtaManager.locateTransactionManager();
    }

    @Override
    protected String getName() {
        return "java:jboss/TransactionManager";
    }

    @Override
    public String getUserTransactionName() {
        return "java:comp/UserTransaction";
    }
}
