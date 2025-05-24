/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */

package ee.jakarta.tck.persistence.ee.packaging.appclient.descriptor;


import java.util.Properties;

import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TSNamingContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public class Client extends EETest {



	private static final Coffee cRef[] = new Coffee[5];

	private EntityManagerFactory emf;

	private EntityManager em;

	private EntityTransaction et;

	private TSNamingContext nctx = null;

	private static final String emfRef = "java:comp/env/persistence/MyPersistenceUnit";

	public static void main(String[] args) {
  		Client theTests = new Client();
  		Status s = theTests.run(args, System.out, System.err);
  		s.exit();
	}

	/*
	 * @class.setup_props:
	 */
	public void setup(String[] args, Properties p) throws Exception {
		try {
			logTrace( "Obtain naming context");
			nctx = new TSNamingContext();
			if (nctx == null) {
				logErr( "NCTX is null");
				throw new Exception("Setup Failed!");
			}
			emf = (EntityManagerFactory) nctx.lookup(emfRef);
			if (emf != null) {
				em = emf.createEntityManager();
			} else {
				logErr( "EMF is null");
				throw new Exception("Setup Failed!");
			}
			if (em == null) {
				logErr( "EM is null");
				throw new Exception("Setup Failed!");
			}
			removeTestData();
		} catch (Exception e) {
			throw new Exception("Setup Failed!", e);
		}
	}

	/*
	 * @testName: test1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:906; PERSISTENCE:SPEC:937;
	 * PERSISTENCE:SPEC:843; JavaEE:SPEC:10056; JavaEE:SPEC:10057;
	 * JavaEE:SPEC:10058; PERSISTENCE:SPEC:949; PERSISTENCE:SPEC:950;
	 * 
	 * @test_Strategy: In JavaEE application client containers, only
	 * application-managed entity managers are required to be used. [JTA is not
	 * required to be supported in application client containers.]
	 *
	 * In JavaEE environment, the root of a persistence unit may be an application
	 * client jar file The persistence.xml resides in the META-INF directory of the
	 * client.jar
	 *
	 * RESOURCE_LOCAL Transaction Type Defined
	 *
	 * The EntityManagerFactory is obtained via JNDI lookup.
	 *
	 * The persistence-unit-ref elements are used in the client deployment
	 * descriptor.
	 *
	 * Entities are described via orm.xml descriptor which also resides in the
	 * META-INF directory of the client.jar.
	 *
	 * Deploy the client.jar to the application server with the above content.
	 * Create entities, persist them, then find.
	 *
	 */

	public void test1() throws Exception {

		logTrace( "Begin test1");
		boolean pass = true;

		try {

			logTrace( "getEntityTransaction");
			if (null != em) {
				et = em.getTransaction();
				if (et != null) {

					logTrace( "createTestData");
					et.begin();
					logTrace( "Create 5 Coffees");
					cRef[0] = new Coffee(1, "hazelnut", 1.0F);
					cRef[1] = new Coffee(2, "vanilla creme", 2.0F);
					cRef[2] = new Coffee(3, "decaf", 3.0F);
					cRef[3] = new Coffee(4, "breakfast blend", 4.0F);
					cRef[4] = new Coffee(5, "mocha", 5.0F);

					logTrace( "Start to persist coffees ");
					for (Coffee c : cRef) {
						if (c != null) {
							em.persist(c);
							logTrace( "persisted coffee " + c);
						}
					}
					et.commit();

					logTrace( "Clearing the persistence context");
					em.clear();

					et.begin();
					for (Coffee c : cRef) {
						if (c != null) {
							Coffee newcoffee = em.find(Coffee.class, c.getId());
							if (newcoffee != null) {
								em.remove(newcoffee);
								logTrace( "removed coffee " + newcoffee);
							} else {
								logErr( "find of coffee[" + c.getId() + "] returned null");
								pass = false;
							}
						}
					}
					et.commit();
				} else {
					logErr( "EntityTransaction is null");
					pass = false;
				}
			} else {
				logErr( "EntityManager is null");
				pass = false;
			}
		} catch (Exception e) {
			logErr( "Unexpected while creating test data:", e);
			pass = false;
		} finally {
			try {
				if (et.isActive()) {
					et.rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in rollback:", re);
			}
		}
		if (!pass)
			throw new Exception("test1 failed");
	}

	public void cleanup() throws Exception {
		logTrace( "cleanup");
		removeTestData();
		// clear the cache if the provider supports caching otherwise
		// the evictAll is ignored.
		logTrace( "Clearing cache");
		emf.getCache().evictAll();

	}

	private void removeTestData() {
		logTrace( "removeTestData");
		if (null == em) {
			em = emf.createEntityManager();
		}
		if (null == et) {
			et = em.getTransaction();
		}
		if (et.isActive()) {
			et.rollback();
		}
		try {
			et.begin();
			em.createNativeQuery("DELETE FROM COFFEE").executeUpdate();
			et.commit();
		} catch (Exception e) {
			logErr( "Exception encountered while removing entities:", e);
		} finally {
			try {
				if (et.isActive()) {
					et.rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in removeTestData:", re);
			}
		}

	}

}
