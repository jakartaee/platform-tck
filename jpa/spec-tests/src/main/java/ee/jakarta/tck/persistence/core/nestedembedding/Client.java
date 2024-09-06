/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.nestedembedding;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.sun.ts.lib.harness.Status;
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
		String[] classes = { pkgName + "A", pkgName + "Address", pkgName + "B", pkgName + "ZipCode" };
		return createDeploymentJar("jpa_core_override_nestedembedding.jar", pkgNameWithoutSuffix, classes);

	}

	public static void main(String[] args) {
		Client theTests = new Client();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
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
	 * @testName: NE1XMTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:618; PERSISTENCE:SPEC:623;
	 * PERSISTENCE:JAVADOC:129; PERSISTENCE:JAVADOC:132; PERSISTENCE:SPEC:566
	 * 
	 * @test_Strategy: The new entity bean instance becomes both managed and
	 * persistent by invoking the persist method on it. The semantics of the persist
	 * operation as applied to entity X is as follows: The perist operation is
	 * cascaded to entities referenced by X, if the relationship from X to these
	 * other entities is annotated with cascade=ALL annotation member.
	 *
	 * Invoke persist on a OneToMany relationship from X annotated with cascade=ALL
	 * and ensure the persist operation is cascaded.
	 *
	 */
	@Test
	public void NE1XMTest1() throws Exception {
		boolean pass = false;
		A aRef;
		Collection newCol;

		getEntityTransaction().begin();
		try {
			logTrace( "New instances");

			ZipCode z1 = new ZipCode("01801", "1234");
			ZipCode z2 = new ZipCode("01803", "1234");

			Address addr1 = new Address("1 Network Drive", "Burlington", "MA");
			Address addr2 = new Address("Some Address", "Boston", "MA");

			addr1.setZipCode(z1);
			addr2.setZipCode(z2);

			B b1 = new B("1", "b1", 1);
			b1.setAddress(addr1);
			B b2 = new B("2", "b2", 1);
			b2.setAddress(addr2);
			B b3 = new B("3", "b3", 1);
			b3.setAddress(addr1);
			B b4 = new B("4", "b4", 1);
			b4.setAddress(addr2);

			Vector v1 = new Vector();
			v1.add(b1);
			v1.add(b2);
			v1.add(b3);
			v1.add(b4);
			aRef = new A("1", "bean1", 1, v1);
			getEntityManager().persist(aRef);
			getEntityManager().flush();
			clearCache();
			newCol = aRef.getBCol();

			dumpCollectionDataB(newCol);

			if (newCol.contains(b1) && newCol.contains(b2) && newCol.contains(b3) && newCol.contains(b4)) {

				B newB = getBFromCollection(newCol, b1);
				if (newB != null) {

					if (newB.getAddress().getStreet().equals("1 Network Drive")
							&& newB.getAddress().getCity().equals("Burlington")
							&& newB.getAddress().getState().equals("MA")
							&& newB.getAddress().getZipCode().getZip().equals("01801")
							&& newB.getAddress().getZipCode().getPlusFour().equals("1234")) {
						pass = true;
						logTrace( "verified nested embedded class contents ");
					} else {
						logErr( "Expected address:" + addr1.toString());
						logErr( "actual address:" + newB.getAddress().toString());
					}
				} else {
					logErr( "b not found in Collection");
				}
			} else {
				logErr( "Collection did not contain all entries:");
				if (newCol.contains(b1)) {
					logTrace( "found b1");
				} else {
					logErr( "b1 NOT FOUND");
				}
				if (newCol.contains(b2)) {
					logTrace( "found b2");
				} else {
					logErr( "b2 NOT FOUND");
				}
				if (newCol.contains(b3)) {
					logTrace( "found b3");
				} else {
					logErr( "b3 NOT FOUND");
				}
				if (newCol.contains(b4)) {
					logTrace( "found b4");
				} else {
					logErr( "b4 NOT FOUND");
				}

			}
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
			throw new Exception("cascadeAll1XMTest1 failed");
		}
	}

	/*
	 * @testName: NE1XMTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:326
	 * 
	 * @test_Strategy: Use Nested embeddable class in Query
	 *
	 */
	@Test
	public void NE1XMTest2() throws Exception {
		boolean pass = false;
		A aRef;
		Collection<B> newCol;

		getEntityTransaction().begin();

		try {
			logTrace( "New instances");

			ZipCode z1 = new ZipCode("01801", "1234");
			ZipCode z2 = new ZipCode("01803", "1234");

			Address addr1 = new Address("1 Network Drive", "Burlington", "MA");
			Address addr2 = new Address("Some Address", "Boston", "MA");

			addr1.setZipCode(z1);
			addr2.setZipCode(z2);

			B b1 = new B("1", "b1", 1);
			b1.setAddress(addr1);
			B b2 = new B("2", "b2", 1);
			b2.setAddress(addr2);

			Vector v1 = new Vector();
			v1.add(b1);
			v1.add(b2);

			aRef = new A("1", "bean1", 1, v1);
			getEntityManager().persist(aRef);
			getEntityManager().flush();
			clearCache();
			A newA = findA("1");

			if (newA != null) {

				newCol = aRef.getBCol();

				// Get the B collection in ResultList using Query
				B newB = (B) getEntityManager().createQuery("Select b from B b where b.address.zipcode.zip='01801'")
						.getSingleResult();

				// Verify Embedded contents
				if (newB != null) {
					logTrace( "newB:" + newB.toString());
					if (newCol.contains(newB)) {
						logTrace( "b contains the searched embeddable Address");
						pass = true;
					} else {
						logErr( "Expected:" + newCol.toString() + ", actual:" + newB.toString());
					}

				} else {
					logErr( "newB is null");
				}
			} else {
				logErr( "newA is null");
			}
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
			throw new Exception("NE1XMTest2 failed");
		}
	}

	/*
	 * @testName: NE1XMTest3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:326
	 * 
	 * @test_Strategy: Use embedded class in Query
	 *
	 */
	@Test
	public void NE1XMTest3() throws Exception {
		boolean pass = false;
		A aRef;
		Collection newCol;

		getEntityTransaction().begin();

		try {
			logTrace( "New instances");

			ZipCode z1 = new ZipCode("01801", "1234");
			ZipCode z2 = new ZipCode("01803", "1234");

			Address addr1 = new Address("1 Network Drive", "Burlington", "MA");
			Address addr2 = new Address("Some Address", "Boston", "MA");

			addr1.setZipCode(z1);
			addr2.setZipCode(z2);

			B b1 = new B("1", "b1", 1);
			b1.setAddress(addr1);
			B b2 = new B("2", "b2", 1);
			b2.setAddress(addr2);

			Vector v1 = new Vector();
			v1.add(b1);
			v1.add(b2);

			aRef = new A("1", "bean1", 1, v1);
			getEntityManager().persist(aRef);
			getEntityManager().flush();
			clearCache();
			A newA = findA("1");

			if (newA != null) {

				newCol = aRef.getBCol();

				// Get the B using embeddable class in Query
				B newB = (B) getEntityManager()
						.createQuery("Select b from B b where b.address.street='1 Network Drive'").getSingleResult();

				// Verify Embedded contents
				if (newB != null) {
					if (newCol.contains(newB)) {
						logTrace( "b contains the searched embeddable Address");
						pass = true;
					} else {
						logErr( "Expected:" + newCol.toString() + ", actual:" + newB.toString());
					}

				} else {
					logErr( "newB is null");
				}
			} else {
				logErr( "newA is null");
			}
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
			throw new Exception("NE1XMTest3 failed");
		}
	}

	/*
	 *
	 * Business Methods to set up data for Test Cases
	 *
	 */
	private void createA(final A a) {
		logTrace( "Entered createA method");
		getEntityTransaction().begin();
		getEntityManager().persist(a);
		getEntityTransaction().commit();
	}

	private A findA(final String id) {
		// logTrace("Entered findA method");
		return getEntityManager().find(A.class, id);
	}

	private void createB(final B b) {
		logTrace( "Entered createB method");
		getEntityTransaction().begin();
		getEntityManager().persist(b);
		getEntityTransaction().commit();
	}

	private B findB(final String id) {
		// logTrace("Entered findB method");
		return getEntityManager().find(B.class, id);
	}

	private List findByName(final String name) {
		logTrace( "Entered findByName method");
		return getEntityManager().createQuery("select a from A a where a.name = :name").setParameter("name", name)
				.getResultList();
	}

	private boolean getInstanceStatus(final Object o) {
		logTrace( "Entered getInstanceStatus method");
		return getEntityManager().contains(o);
	}

	private void dumpCollectionDataA(final Collection c) {
		logTrace( "collection Data");
		logTrace( "---------------");
		logTrace( "- size=" + c.size());
		Iterator i = c.iterator();
		int elem = 1;
		while (i.hasNext()) {
			A v = (A) i.next();
			logTrace( "- Element #" + elem++);
			logTrace(
					"  id=" + v.getAId() + ", name=" + v.getAName() + ", value=" + v.getAValue());
		}
	}

	private void dumpCollectionDataB(final Collection c) {
		logTrace( "collection Data");
		logTrace( "---------------");
		logTrace( "- size=" + c.size());
		Iterator i = c.iterator();
		int elem = 1;
		while (i.hasNext()) {
			B v = (B) i.next();
			logTrace( "- Element #" + elem++);
			logTrace(
					"  id=" + v.getBId() + ", name=" + v.getBName() + ", value=" + v.getBValue());
		}
	}

	public B getBFromCollection(final Collection c, final B b) {
		logTrace( "getBFromCollection");
		B resultB = null;
		if (c.size() != 0) {
			Iterator iterator = c.iterator();
			while (iterator.hasNext()) {
				B newB = (B) iterator.next();
				if (newB.getBId().equals(b.getBId()) && newB.getBName().equals(b.getBName())
						&& newB.getBValue() == b.getBValue()) {
					logTrace( "Found B in Collection");
					resultB = newB;
					return resultB;
				} else {
					logTrace( "b not found in Collection");
				}
			}
		}
		return resultB;
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logTrace( "Cleanup data");
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
			getEntityManager().createNativeQuery("DELETE FROM ANE_1XM_BI_BTOB").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM BNE_1XM_BI_BTOB").executeUpdate();
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
