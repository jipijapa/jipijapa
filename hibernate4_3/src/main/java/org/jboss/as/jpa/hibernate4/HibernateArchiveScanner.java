/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.jpa.hibernate4;

import org.jboss.as.jpa.spi.PersistenceUnitMetadata;

import org.hibernate.jpa.boot.scan.spi.AbstractScannerImpl;
import org.hibernate.jpa.boot.scan.spi.Scanner;

/**
 * Annotation scanner for Hibernate
 *
 * @author Steve Ebersole
 * @author Scott Marlow
 * @author Ales Justin
 */
public class HibernateArchiveScanner extends AbstractScannerImpl implements Scanner {
//    private static final ThreadLocal<PersistenceUnitMetadata> PERSISTENCE_UNIT_METADATA_TLS = new ThreadLocal<PersistenceUnitMetadata>();

	public HibernateArchiveScanner() {
		super( VirtualFileSystemArchiveDescriptorFactory.INSTANCE );
	}

//	public static void setThreadLocalPersistenceUnitMetadata(final PersistenceUnitMetadata pu) {
//        PERSISTENCE_UNIT_METADATA_TLS.set(pu);
//    }
//
//    public static void clearThreadLocalPersistenceUnitMetadata() {
//        PERSISTENCE_UNIT_METADATA_TLS.remove();
//    }
}
