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

package ee.jakarta.tck.persistence.core.entityManager2;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TransactionRequiredException;

public class Client2 extends PMClientBase {



	Employee[] empRef = new Employee[5];

	Order[] orders = new Order[5];

	Properties props = null;

	Map map = new HashMap<String, Object>();

	Employee emp = new Employee(1, "foo", "bar", getUtilDate("2000-02-14"), (float) 35000.0);

	String dataBaseName = null;

	final static String ORACLE = "oracle";

	public Client2() {
	}
	public static void main(String[] args) {
		Client2 theTests = new Client2();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}


	/*
	 * setupOrderData() is called before each test
	 *
	 * @class.setup_props: jdbc.db;
	 */

	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);

			removeTestData();
			createOrderData();
			map.putAll(getEntityManager().getProperties());
			map.put("foo", "bar");
			displayMap(map);
			dataBaseName = p.getProperty("jdbc.db");
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}


	public void cleanup() throws Exception {
		try {
			logTrace( "Cleanup data");
			removeTestData();
			super.cleanup();
		} finally {

        }
	}

	/*
	 * @testName: lockTransactionRequiredExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:491; PERSISTENCE:JAVADOC:492;
	 * PERSISTENCE:JAVADOC:498; PERSISTENCE:JAVADOC:499
	 * IllegalArgumentException may also be thrown by lock method as per
	 * jakarta.ee/specifications/persistence/3.2/apidocs/jakarta.persistence/jakarta/persistence/entitymanager#lock(java.lang.Object,jakarta.persistence.LockModeType)
	 * 
	 * @test_Strategy: Call EntityManager.lock() method
	 */
	
	public void lockTransactionRequiredExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		Map<String, Object> myMap = new HashMap<String, Object>();

		myMap.put("some.cts.specific.property", "nothing.in.particular");

		logMsg( "Testing TransactionRequiredException for lock(Object, LockModeType)");
		try {
			Order o = getEntityManager().find(Order.class, 1);
			getEntityManager().lock(o, LockModeType.WRITE);
			logErr( "TransactionRequiredException not thrown");
		} catch (TransactionRequiredException e) {
			logTrace( "TransactionRequiredException Caught as Expected.");
			pass1 = true;
		} catch (IllegalArgumentException e) {
			logTrace( "IllegalArgumentException caught which can also be thrown from lock call.");
			pass1 = true;
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}

		logMsg( "Testing TransactionRequiredException for lock(Object, LockModeType, Map)");
		try {
			Order o = getEntityManager().find(Order.class, 1);
			getEntityManager().lock(o, LockModeType.WRITE, myMap);
			logErr( "TransactionRequiredException not thrown");
		} catch (TransactionRequiredException e) {
			logTrace( "TransactionRequiredException Caught as Expected.");
			pass2 = true;
		} catch (IllegalArgumentException e) {
			logTrace( "IllegalArgumentException caught which can also be thrown from lock call.");
			pass2 = true;
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass1 || !pass2) {
			throw new Exception("lockTransactionRequiredExceptionTest failed");
		}
	}

	/*
	 * @testName: refreshTransactionRequiredExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1313;
	 *
	 * @test_Strategy: Call EntityManager.refresh() method without a transaction
	 */
	
	public void refreshTransactionRequiredExceptionTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		logMsg( "Testing refresh(Object, LockModeType)");

		try {
			getEntityTransaction().begin();
			Order o = getEntityManager().find(Order.class, 4);
			getEntityTransaction().commit();
			o.setdescription("FOOBAR");
			getEntityManager().refresh(o, LockModeType.PESSIMISTIC_READ);
			logErr( "TransactionRequiredException not thrown");
		} catch (TransactionRequiredException tre) {
			logTrace( "TransactionRequiredException Caught as Expected.");
			pass1 = true;
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
		logMsg( "Testing refresh(Object, LockModeType, Map)");
		Map<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("some.cts.specific.property", "nothing.in.particular");
		try {
			getEntityTransaction().begin();
			Order o = getEntityManager().find(Order.class, 4);
			getEntityTransaction().commit();
			o.setdescription("FOOBAR");
			getEntityManager().refresh(o, LockModeType.PESSIMISTIC_READ, myMap);
			logErr( "TransactionRequiredException not thrown");
		} catch (TransactionRequiredException tre) {
			logTrace( "TransactionRequiredException Caught as Expected.");
			pass2 = true;
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

		if (!pass1 || !pass2) {
			throw new Exception("refreshTransactionRequiredExceptionTest failed");
		}
	}

	/*
	 * @testName: lockTransactionRequiredException2Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1313;
	 * IllegalArgumentException may also be thrown by lock method as per
	 * jakarta.ee/specifications/persistence/3.2/apidocs/jakarta.persistence/jakarta/persistence/entitymanager#lock(java.lang.Object,jakarta.persistence.LockModeType)
	 *
	 * @test_Strategy: Call EntityManager.lock() method without a transaction
	 */
	
	public void lockTransactionRequiredException2Test() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		logMsg( "Testing lock(Object, LockModeType)");
		try {
			getEntityTransaction().begin();
			Order o = getEntityManager().find(Order.class, 4);
			getEntityTransaction().commit();
			removeTestData();
			getEntityManager().lock(o, LockModeType.PESSIMISTIC_READ);
			logErr( "TransactionRequiredException not thrown");
		} catch (TransactionRequiredException tre) {
			logTrace( "TransactionRequiredException Caught as Expected.");
			pass1 = true;
		} catch (IllegalArgumentException e) {
			logTrace( "IllegalArgumentException caught which can also be thrown from lock call.");
			pass1 = true;
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
		logMsg( "Testing lock(Object, LockModeType, Map)");
		Map<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("some.cts.specific.property", "nothing.in.particular");
		try {
			getEntityTransaction().begin();
			Order o = getEntityManager().find(Order.class, 4);
			getEntityTransaction().commit();
			removeTestData();
			getEntityManager().lock(o, LockModeType.PESSIMISTIC_READ, myMap);
			logErr( "TransactionRequiredException not thrown");
		} catch (TransactionRequiredException tre) {
			logTrace( "TransactionRequiredException Caught as Expected.");
			pass2 = true;
		} catch (IllegalArgumentException e) {
			logTrace( "IllegalArgumentException caught which can also be thrown from lock call.");
			pass2 = true;
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

		if (!pass1 || !pass2) {
			throw new Exception("lockTransactionRequiredException2Test failed");
		}
	}

	private void createOrderData() {

		try {
			getEntityTransaction().begin();
			logMsg( "Creating Orders");
			orders[0] = new Order(1, 111, "desc1");
			orders[1] = new Order(2, 222, "desc2");
			orders[2] = new Order(3, 333, "desc3");
			orders[3] = new Order(4, 444, "desc4");
			orders[4] = new Order(5, 555, "desc5");
			for (Order o : orders) {
				logTrace( "Persisting order:" + o.toString());
				getEntityManager().persist(o);
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
