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

package org.jipijapa.eclipselink;

import org.jboss.logging.Logger;
import org.jipijapa.plugin.spi.JtaManager;
import org.jipijapa.plugin.spi.ManagementAdaptor;
import org.jipijapa.plugin.spi.PersistenceProviderAdaptor;
import org.jipijapa.plugin.spi.PersistenceUnitMetadata;
import org.jipijapa.plugin.spi.Platform;

import java.util.Map;

public class EclipseLinkPersistenceProviderAdaptor implements
        PersistenceProviderAdaptor {

    private final Logger logger = Logger.getLogger(EclipseLinkPersistenceProviderAdaptor.class);

    public static final String
        ECLIPSELINK_TARGET_SERVER = "eclipselink.target-server",
        ECLIPSELINK_ARCHIVE_FACTORY = "eclipselink.archive.factory",
        ECLIPSELINK_LOGGING_LOGGER = "eclipselink.logging.logger";

    private final boolean hasJTABug;

    public EclipseLinkPersistenceProviderAdaptor() {
        logger.trace("EclipseLink integration activated");
        int major, minor, rev;
        try {
            String[] eclipseLinkVersion = org.eclipse.persistence.Version.getVersion().split("\\.");
            major = Integer.parseInt(eclipseLinkVersion[0]);
            minor = Integer.parseInt(eclipseLinkVersion[1]);
            rev = Integer.parseInt(eclipseLinkVersion[2]);
        } catch (NumberFormatException ex) {
            logger.warn("Could not parse EclipseLink version string " + ex + ", using default configuration");
            major = -1;
            minor = -1;
            rev = -1;
        }
        if (major < 2) {
            logger.warn("EclipseLink 1.x is not tested. You are running version " + major + '.' + minor + '.' + rev + ". ");
            hasJTABug = true;
        } else {
            // Version-specific handling here
            if ( (major == 2 && minor == 3 && rev <= 2) || (major == 2 && minor < 3) ) {
                logger.info("Enabling workaronud for EclipseLink bug 365704 for EclipseLink 2.3.2 and older");
                hasJTABug = true;
            } else {
                hasJTABug = false;
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addProviderProperties(Map properties, PersistenceUnitMetadata pu) {
        if (!properties.containsKey(ECLIPSELINK_TARGET_SERVER) && !pu.getProperties().containsKey(ECLIPSELINK_TARGET_SERVER)) {
            properties.put(ECLIPSELINK_TARGET_SERVER, JBossAS7ServerPlatform.class.getName());
            properties.put(ECLIPSELINK_ARCHIVE_FACTORY, JBossArchiveFactoryImpl.class.getName());
            properties.put(ECLIPSELINK_LOGGING_LOGGER, JBossLogger.class.getName());
        }
        logger.trace("Property " + ECLIPSELINK_TARGET_SERVER + " set to " + properties.get(ECLIPSELINK_TARGET_SERVER));
        logger.trace("Property " + ECLIPSELINK_ARCHIVE_FACTORY + " set to " + properties.get(ECLIPSELINK_ARCHIVE_FACTORY));
        logger.trace("Property " + ECLIPSELINK_LOGGING_LOGGER + " set to " + properties.get(ECLIPSELINK_LOGGING_LOGGER));
    }

    @Override
    public void injectJtaManager(JtaManager jtaManager) {
        // No action required, EclipseLink looks this up from JNDI
    }

    @Override
    public void injectPlatform(Platform platform) {

    }

    @Override
    public void addProviderDependencies(PersistenceUnitMetadata pu) {
        // No action required
    }

    @Override
    public void beforeCreateContainerEntityManagerFactory(
            PersistenceUnitMetadata pu) {
        // no action required
    }

    @Override
    public void afterCreateContainerEntityManagerFactory(
            PersistenceUnitMetadata pu) {
        // TODO: Force creation of metamodel here?
    }

    @Override
    public ManagementAdaptor getManagementAdaptor() {
        // no action required
        return null;
    }

    @Override
    public boolean doesScopedPersistenceUnitNameIdentifyCacheRegionName(PersistenceUnitMetadata pu) {
        return false;
    }

    @Override
    public void cleanup(PersistenceUnitMetadata pu) {
        // no action required
    }

}
