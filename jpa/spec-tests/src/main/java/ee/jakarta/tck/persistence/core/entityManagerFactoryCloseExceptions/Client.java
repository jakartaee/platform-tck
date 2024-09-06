/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package ee.jakarta.tck.persistence.core.entityManagerFactoryCloseExceptions;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.CleanupMethod;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityManagerFactory;

public class Client extends PMClientBase {



	Properties props = null;

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = {};
		return createDeploymentJar("jpa_core_entityManagerFactoryCloseExceptions.jar", pkgNameWithoutSuffix, classes);

	}


	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			createDeployment();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			super.cleanup();
		} finally {

        }
	}

	public void nullCleanup() throws Exception {
	}

	/*
	 * @testName: exceptionsTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:536; PERSISTENCE:JAVADOC:538;
	 * PERSISTENCE:JAVADOC:537; PERSISTENCE:JAVADOC:531; PERSISTENCE:JAVADOC:532;
	 * PERSISTENCE:JAVADOC:533; PERSISTENCE:JAVADOC:534; PERSISTENCE:JAVADOC:535
	 * 
	 * @test_Strategy: Close the EntityManagerFactory, then call various methods
	 */
	@CleanupMethod(name = "nullCleanup")
	@Test
	public void exceptionsTest() throws Exception {
		int passCount = 0;
		Map<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("some.cts.specific.property", "nothing.in.particular");

		EntityManagerFactory emf;
		logMsg( "Getting EntityManagerFactory");
		if (isStandAloneMode()) {
			emf = getEntityManager().getEntityManagerFactory();
		} else {
			emf = getEntityManagerFactory();
		}
		if (emf != null) {
			if (emf.isOpen()) {
				logMsg( "EMF is open, now closing it");
				emf.close();
			} else {
				logMsg( "EMF is already closed");
			}

			logMsg( "Testing getMetamodel() after close");
			try {
				emf.getMetamodel();
				logErr( "IllegalStateException not thrown");
			} catch (IllegalStateException ise) {
				logTrace( "Received expected IllegalStateException");
				passCount++;
			} catch (Exception e) {
				logErr( "Unexpected exception occurred", e);
			}

			logMsg( "Testing emf.getProperties()");
			try {
				emf.getProperties();
				logErr( "IllegalStateException not thrown");
			} catch (IllegalStateException ise) {
				logTrace( "Received expected IllegalStateException");
				passCount++;
			} catch (Exception e) {
				logErr( "Unexpected exception occurred", e);
			}

			logMsg( "Testing getPersistenceUnitUtil() after close");
			try {
				emf.getPersistenceUnitUtil();
				logErr( "Did no throw IllegalStateException");
			} catch (IllegalStateException ise) {
				logTrace( "Received expected IllegalStateException");
				passCount++;
			} catch (Exception e) {
				logErr( "Unexpected exception occurred", e);
			}

			logMsg( "Testing close after close ");
			try {
				emf.close();
				logErr( "IllegalStateException not thrown");
			} catch (IllegalStateException e) {
				logTrace( "IllegalStateException Caught as Expected.");
				passCount++;
			} catch (Exception e) {
				logErr( "Unexpected exception occurred", e);
			}

			logMsg( "Testing createEntityManager() after close");
			try {
				emf.createEntityManager();
				logErr( "IllegalStateException not thrown");
			} catch (IllegalStateException e) {
				logTrace( "IllegalStateException Caught as Expected.");
				passCount++;
			} catch (Exception e) {
				logErr( "Unexpected exception occurred", e);
			}

			logMsg( "Testing createEntityManager(Map) after close");
			try {
				emf.createEntityManager(myMap);
				logErr( "IllegalStateException not thrown");
			} catch (IllegalStateException e) {
				logTrace( "IllegalStateException Caught as Expected.");
				passCount++;
			} catch (Exception e) {
				logErr( "Unexpected exception occurred", e);
			}

			logMsg( "Testing getCache after close ");
			try {
				emf.getCache();
				logErr( "IllegalStateException not thrown");
			} catch (IllegalStateException e) {
				logTrace( "IllegalStateException Caught as Expected.");
				passCount++;
			} catch (Exception e) {
				logErr( "Unexpected exception occurred", e);
			}

			try {
				emf.getCriteriaBuilder();
				logErr( "IllegalStateException was not thrown");
			} catch (IllegalStateException ise) {
				passCount++;
				logTrace( "Received expected IllegalStateException");
			} catch (Exception e) {
				logErr( "Unexpected exception occurred", e);
			}
		} else {
			logErr( "Could not obtain an EntityManagerFactory");
		}
		if (passCount != 8) {
			throw new Exception("exceptionsTest failed");
		}
	}

}
