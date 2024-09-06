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

package ee.jakarta.tck.persistence.core.cache.basicTests;



import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.Cache;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public class Client extends PMClientBase {

	

	public Client() {
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Order" };
		String[] xmlFiles = {};
		return createDeploymentJar("jpa_core_cache_basicTests.jar", pkgNameWithoutSuffix, classes, "persistence_se.xml",
				xmlFiles);
	}

	
	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {

			super.setup(args,p);
			removeTestData();

		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: getcacheTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:500; PERSISTENCE:SPEC:501;
	 * PERSISTENCE:SPEC:503; PERSISTENCE:SPEC:504; PERSISTENCE:SPEC:505;
	 * PERSISTENCE:SPEC:506; PERSISTENCE:SPEC:507; PERSISTENCE:SPEC:508;
	 * PERSISTENCE:SPEC:932; PERSISTENCE:SPEC:936; PERSISTENCE:SPEC:939;
	 * PERSISTENCE:SPEC:943; PERSISTENCE:SPEC:946; PERSISTENCE:SPEC:930;
	 * PERSISTENCE:SPEC:1018; PERSISTENCE:SPEC:1019; PERSISTENCE:SPEC:1020;
	 * PERSISTENCE:SPEC:1021; PERSISTENCE:SPEC:1023; PERSISTENCE:SPEC:1025;
	 * PERSISTENCE:SPEC:848; PERSISTENCE:SPEC:856; PERSISTENCE:SPEC:908;
	 * PERSISTENCE:SPEC:909; PERSISTENCE:SPEC:915; PERSISTENCE:SPEC:925;
	 * PERSISTENCE:SPEC:918; PERSISTENCE:SPEC:928; PERSISTENCE:SPEC:929;
	 * PERSISTENCE:JAVADOC:149; PERSISTENCE:JAVADOC:152; PERSISTENCE:JAVADOC:163;
	 * PERSISTENCE:SPEC:846; PERSISTENCE:JAVADOC:338
	 * 
	 * @test_Strategy: With basic entity requirements, persist/remove an entity.
	 */
	@Test
	public void getcacheTest() throws Exception {
		Cache cache;
		boolean pass = false;
		final int count = 5;
		if (cachingSupported) {
			try {

				EntityManager em2 = getEntityManager();
				EntityTransaction et = getEntityTransaction();

				Order[] orders = new Order[count];
				et.begin();

				for (int i = 1; i < count; i++) {
					orders[i] = new Order(i, 100 * i);
					em2.persist(orders[i]);
					logTrace( "persisted order " + orders[i]);
				}
				em2.flush();

				EntityManagerFactory emf = getEntityManagerFactory();
				cache = emf.getCache();

				if (cache != null) {
					pass = true;
				} else {
					logErr( "Cache returned was null");
				}

				for (int i = 1; i < count; i++) {
					em2.remove(orders[i]);
					logTrace( "Removed order " + orders[i]);
				}

				et.commit();

			} catch (Exception e) {
				logErr( "Unexpected exception occurred", e);
			}
		} else {
			logMsg( "Cache not supported, bypassing test");
			pass = true;
		}
		if (!pass) {
			throw new Exception("getcacheTest failed");
		}

	}

	/*
	 * @testName: evictTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:500; PERSISTENCE:SPEC:501;
	 * PERSISTENCE:SPEC:503; PERSISTENCE:SPEC:504; PERSISTENCE:SPEC:505;
	 * PERSISTENCE:SPEC:506; PERSISTENCE:SPEC:507; PERSISTENCE:SPEC:508;
	 * PERSISTENCE:SPEC:932; PERSISTENCE:SPEC:936; PERSISTENCE:SPEC:939;
	 * PERSISTENCE:SPEC:943; PERSISTENCE:SPEC:946; PERSISTENCE:SPEC:930;
	 * PERSISTENCE:SPEC:1018; PERSISTENCE:SPEC:1019; PERSISTENCE:SPEC:1020;
	 * PERSISTENCE:SPEC:1021; PERSISTENCE:SPEC:1023; PERSISTENCE:SPEC:1025;
	 * PERSISTENCE:SPEC:848; PERSISTENCE:SPEC:856; PERSISTENCE:SPEC:908;
	 * PERSISTENCE:SPEC:909; PERSISTENCE:SPEC:915; PERSISTENCE:SPEC:925;
	 * PERSISTENCE:SPEC:918; PERSISTENCE:SPEC:928; PERSISTENCE:SPEC:929;
	 * PERSISTENCE:JAVADOC:149; PERSISTENCE:JAVADOC:152; PERSISTENCE:JAVADOC:163;
	 * PERSISTENCE:SPEC:846; PERSISTENCE:JAVADOC:304; PERSISTENCE:JAVADOC:305
	 * 
	 * @test_Strategy: Persist data, evict class and specific PK
	 */
	@Test
	public void evictTest1() throws Exception {
		Cache cache;
		final int count = 5;
		boolean pass1 = false;
		boolean pass2 = false;
		if (cachingSupported) {

			try {

				getEntityTransaction().begin();
				logTrace( "Transaction status after begin:" + getEntityTransaction().isActive());
				Order[] orders = new Order[count];

				for (int i = 1; i < count; i++) {
					orders[i] = new Order(i, 100 * i);
					getEntityManager().persist(orders[i]);
					logTrace( "persisted order " + orders[i]);
				}
				getEntityManager().flush();
				getEntityTransaction().commit();
				logTrace( "Transaction status after commit:" + getEntityTransaction().isActive());

				cache = getEntityManagerFactory().getCache();

				if (cache != null) {
					pass1 = true;
					logTrace( "cache was successfully obtained");

					boolean cacheContains = cache.contains(Order.class, 1);

					if (cacheContains) {
						logTrace( "Order 1 found, evicting it from cache");
						cache.evict(Order.class, 1);

						// Recheck whether the removed entity is still in cache
						cacheContains = cache.contains(Order.class, 1);

						// if not found then evict was successful
						if (!cacheContains) {
							pass2 = true;
							logTrace( "Order 1 was successfully evicted");
						}
					} else {
						logErr( "cache did not contain Order 1");
					}
				} else {
					logErr( "Cache returned was null, eventhough Cache is supported.");
				}
			} catch (Exception e) {
				logErr( "Unexpected exception occurred", e);
			}
		} else {
			logMsg( "Cache not supported, bypassing test");
			pass1 = true;
			pass2 = true;
		}
		if (!pass1 || !pass2) {
			throw new Exception("evictTest1 failed");
		}

	}

	/*
	 * @testName: evictTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:500; PERSISTENCE:SPEC:501;
	 * PERSISTENCE:SPEC:503; PERSISTENCE:SPEC:504; PERSISTENCE:SPEC:505;
	 * PERSISTENCE:SPEC:506; PERSISTENCE:SPEC:507; PERSISTENCE:SPEC:508;
	 * PERSISTENCE:SPEC:932; PERSISTENCE:SPEC:936; PERSISTENCE:SPEC:939;
	 * PERSISTENCE:SPEC:943; PERSISTENCE:SPEC:946; PERSISTENCE:SPEC:930;
	 * PERSISTENCE:SPEC:1018; PERSISTENCE:SPEC:1019; PERSISTENCE:SPEC:1020;
	 * PERSISTENCE:SPEC:1021; PERSISTENCE:SPEC:1023; PERSISTENCE:SPEC:1025;
	 * PERSISTENCE:SPEC:848; PERSISTENCE:SPEC:856; PERSISTENCE:SPEC:908;
	 * PERSISTENCE:SPEC:909; PERSISTENCE:SPEC:915; PERSISTENCE:SPEC:925;
	 * PERSISTENCE:SPEC:918; PERSISTENCE:SPEC:928; PERSISTENCE:SPEC:929;
	 * PERSISTENCE:JAVADOC:149; PERSISTENCE:JAVADOC:152; PERSISTENCE:JAVADOC:163;
	 * PERSISTENCE:SPEC:846; PERSISTENCE:JAVADOC:304; PERSISTENCE:JAVADOC:306
	 *
	 * @test_Strategy: Persist data, evict class
	 */
	@Test
	public void evictTest2() throws Exception {
		Cache cache;
		final int count = 5;
		boolean pass1, pass2 = false;
		pass1 = false;
		if (cachingSupported) {

			try {

				Order[] orders = new Order[count];
				getEntityTransaction().begin();

				for (int i = 1; i < count; i++) {
					orders[i] = new Order(i, 100 * i);
					getEntityManager().persist(orders[i]);
					logTrace( "persisted order " + orders[i]);
				}
				getEntityManager().flush();
				getEntityTransaction().commit();

				cache = getEntityManagerFactory().getCache();

				if (cache != null) {
					pass1 = true;
					logTrace( "cache was successfully obtained");

					boolean cacheContains = cache.contains(Order.class, 1);

					if (cacheContains) {
						logTrace( "evicting Order 1 from cache");
						cache.evict(Order.class);

						// Recheck whether the removed entity is still in cache
						cacheContains = cache.contains(Order.class, 1);

						// if not found then evict was successful
						if (!cacheContains) {
							pass2 = true;
							logTrace( "Order 1 was successfully evicted");
						}
					} else {
						logErr( "Cache did not contain Order 1");
					}
				} else {
					logErr( "Cache returned was null, eventhough Cache is supported.");
				}
			} catch (Exception e) {
				logErr( "Unexpected exception occurred", e);
			}
		} else {
			logMsg( "Cache not supported, bypassing test");
			pass1 = true;
			pass2 = true;
		}
		if (!pass1 || !pass2) {
			throw new Exception("evictTest2 failed");
		}
	}

	/*
	 * @testName: evictallTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:500; PERSISTENCE:SPEC:501;
	 * PERSISTENCE:SPEC:503; PERSISTENCE:SPEC:504; PERSISTENCE:SPEC:505;
	 * PERSISTENCE:SPEC:506; PERSISTENCE:SPEC:507; PERSISTENCE:SPEC:508;
	 * PERSISTENCE:SPEC:932; PERSISTENCE:SPEC:936; PERSISTENCE:SPEC:939;
	 * PERSISTENCE:SPEC:943; PERSISTENCE:SPEC:946; PERSISTENCE:SPEC:930;
	 * PERSISTENCE:SPEC:1018; PERSISTENCE:SPEC:1019; PERSISTENCE:SPEC:1020;
	 * PERSISTENCE:SPEC:1021; PERSISTENCE:SPEC:1023; PERSISTENCE:SPEC:1025;
	 * PERSISTENCE:SPEC:848; PERSISTENCE:SPEC:856; PERSISTENCE:SPEC:908;
	 * PERSISTENCE:SPEC:909; PERSISTENCE:SPEC:915; PERSISTENCE:SPEC:925;
	 * PERSISTENCE:SPEC:918; PERSISTENCE:SPEC:928; PERSISTENCE:SPEC:929;
	 * PERSISTENCE:JAVADOC:149; PERSISTENCE:JAVADOC:152; PERSISTENCE:JAVADOC:163;
	 * PERSISTENCE:SPEC:846; PERSISTENCE:JAVADOC:304; PERSISTENCE:JAVADOC:307
	 *
	 * @test_Strategy: Persist data, evict all
	 */
	@Test
	public void evictallTest() throws Exception {
		Cache cache;
		final int count = 5;
		boolean pass1 = false;
		boolean pass2 = false;
		int pass2Count = 0;
		Order[] orders = new Order[count];
		int[] ids = new int[count];
		if (cachingSupported) {

			try {
				getEntityTransaction().begin();

				for (int i = 1; i < count; i++) {
					orders[i] = new Order(i, 100 * i);
					ids[i] = orders[i].getId();
					getEntityManager().persist(orders[i]);
					logTrace( "persisted order " + orders[i]);
				}
				getEntityManager().flush();
				getEntityTransaction().commit();

				cache = getEntityManagerFactory().getCache();

				if (cache != null) {
					pass1 = true;
					logTrace( "cache was successfully obtained, evicting all Orders from cache");
					cache.evictAll();
					for (int i : ids) {
						// Recheck whether the evicted entities are still in cache
						logTrace( "Testing order:" + i);
						boolean cacheContains = cache.contains(Order.class, i);

						if (!cacheContains) {
							pass2Count++;
							logTrace( "Order:" + i + " was successfully evicted");
						}
					}
					if (pass2Count == orders.length) {
						pass2 = true;
					} else {
						logErr( "Not all orders were evicted.");
					}
				} else {
					logErr( "Cache returned was null, eventhough Cache is supported.");
				}
			} catch (Exception e) {
				logErr( "Unexpected exception occurred", e);
			}
		} else {
			logMsg( "Cache not supported, bypassing test");
			pass1 = true;
			pass2 = true;
		}
		if (!pass1 || !pass2) {
			throw new Exception("evictallTest failed");
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
			getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER").executeUpdate();
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
