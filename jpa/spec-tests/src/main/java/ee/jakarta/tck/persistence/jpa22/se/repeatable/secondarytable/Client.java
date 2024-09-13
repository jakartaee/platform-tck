/*
 * Copyright (c) 2017, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.jpa22.se.repeatable.secondarytable;



import java.util.Properties;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.Cache;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public class Client extends PMClientBase {

	

	private static final long serialVersionUID = 22L;

	public Client() {
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
	 * @testName: subClassInheritsCacheableTrue
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:189; PERSISTENCE:JAVADOC:190;
	 * PERSISTENCE:SPEC:1979; PERSISTENCE:SPEC:1980;
	 * 
	 * @test_Strategy: follow se/cache/inherit but without @SecondaryTables
	 */
	public void subClassInheritsCacheableTrue() throws Exception {
		Cache cache;
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		if (cachingSupported) {
			try {

				EntityManager em2 = getEntityManager();
				EntityTransaction et = getEntityTransaction();

				et.begin();

				Product product = new Product("1", 101);
				em2.persist(product);
				logTrace( "persisted Product " + product);

				SoftwareProduct sp = new SoftwareProduct();
				sp.setId("2");
				sp.setRevisionNumber(1D);
				sp.setQuantity(202);
				em2.persist(sp);
				logTrace( "persisted SoftwareProduct " + sp);

				HardwareProduct hp = new HardwareProduct();
				hp.setId("3");
				hp.setModelNumber(3);
				hp.setQuantity(303);
				em2.persist(hp);
				logTrace( "persisted HardwareProduct " + hp);

				em2.flush();
				et.commit();

				EntityManagerFactory emf = getEntityManagerFactory();
				cache = emf.getCache();

				if (cache != null) {
					boolean b1 = cache.contains(Product.class, "1");
					if (b1) {
						logTrace(
								"Cache returned: " + b1 + ", therefore cache does contain Product " + product);
						pass1 = true;
					} else {
						logErr(
								"Cache returned: " + b1 + ", therefore cache does not contain Product " + product);
					}
					boolean b2 = cache.contains(SoftwareProduct.class, "2");
					if (!b2) {
						logTrace(
								"Cache returned: " + b2 + ", therefore cache does not contain SoftwareProduct " + sp);
						pass2 = true;
					} else {
						logErr(
								"Cache returned: " + b2 + ", therefore cache does contain SoftwareProduct " + sp);
					}
					boolean b3 = cache.contains(HardwareProduct.class, "3");
					if (b3) {
						logTrace(
								"Cache returned: " + b3 + ", therefore cache does contain HardwareProduct " + hp);
						pass3 = true;
					} else {
						logErr(
								"Cache returned: " + b3 + ", therefore cache does not contain HardwareProduct " + hp);
					}
				} else {
					logErr( "Cache returned was null");
				}
			} catch (Exception e) {
				logErr( "Unexpected exception occurred", e);
			}
		} else {
			logMsg( "Cache not supported, bypassing test");
			pass1 = true;
			pass2 = true;
			pass3 = true;
		}
		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("subClassInheritsCacheableTrue failed");
		}

	}

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
			getEntityManager().createNativeQuery("DELETE FROM PRODUCT_DETAILS").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PRODUCT_TABLE").executeUpdate();
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
