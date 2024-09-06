/*
 * Copyright (c) 2009, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.collectiontable;


import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	public Client() {
	}



	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "A", pkgName + "Address", };
		return createDeploymentJar("jpa_core_annotations_collectiontable.jar", pkgNameWithoutSuffix, classes);

	}

	
	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			createDeployment();
			removeTestData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);

		}
	}

	/*
	 * @testName: collectionTable1
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:314; PERSISTENCE:JAVADOC:315;
	 * PERSISTENCE:SPEC:1246;
	 * 
	 * @test_Strategy:
	 *
	 */
	@Test
	public void collectionTable1() throws Exception {
		logTrace( "Begin collectionTable1");
		boolean pass = false;
		A aRef = null;
		Collection bCol = null;
		Collection newCol = null;

		try {
			logTrace( "New instances");

			final Address addr1 = new Address("1 Network Drive", "Burlington", "MA", "01801");
			final Address addr2 = new Address("Some Address", "Boston", "MA", "01803");

			Set<Address> s1 = new HashSet();
			s1.add(addr1);
			s1.add(addr2);

			aRef = new A("1", "bean1", 1);
			aRef.setAddress(s1);
			getEntityTransaction().begin();
			getEntityManager().persist(aRef);
			getEntityTransaction().commit();

			getEntityTransaction().begin();
			A newA = findA("1");
			final Set<Address> newAddressSet = newA.getAddress();

			dumpAddresses(newAddressSet);

			boolean pass1 = false;
			boolean pass2 = false;

			for (Address addr : newAddressSet) {
				if (addr.getStreet().equals("1 Network Drive") && addr.getCity().equals("Burlington")
						&& addr.getState().equals("MA") && addr.getZip().equals("01801")) {
					pass1 = true;
					logTrace( "pass1 = " + pass1);
				}
				if (addr.getStreet().equals("Some Address") && addr.getCity().equals("Boston")
						&& addr.getState().equals("MA") && addr.getZip().equals("01803")) {
					pass2 = true;
					logTrace( "pass2 = " + pass2);
				}
			}

			if (pass1 && pass2)
				pass = true;

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logErr( "Unexpected exception rolling back TX:", fe);
			}

		}

		if (!pass) {
			throw new Exception("collectionTable1 failed");
		}
	}

	/*
	 *
	 * Business Methods to set up data for Test Cases
	 *
	 */

	private A findA(final String id) {
		return getEntityManager().find(A.class, id);
	}

	private void dumpAddresses(final Set<Address> addr) {
		logTrace( "address Data");
		logTrace( "---------------");
		logTrace( "size=" + addr.size());
		int elem = 1;
		for (Address v : addr) {
			logTrace( "- Element #" + elem++);
			if (v != null) {
				logTrace( "  street=" + v.getStreet() + ", city=" + v.getCity() + ", state="
						+ v.getState() + ", zip=" + v.getZip());
			} else {
				logTrace( "  address=numm");
			}
		}
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logTrace( "cleanup");
			removeTestData();
			logTrace( "cleanup complete, calling super.cleanup");
			super.cleanup();
		} finally {

        }

	}

	private void removeTestData() {
		logTrace( "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("Delete from COLTAB_ADDRESS").executeUpdate();
			getEntityManager().createNativeQuery("Delete from COLTAB").executeUpdate();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Exception encountered while removing entities:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in removeTestData:", re);
			}
		}
	}

}
