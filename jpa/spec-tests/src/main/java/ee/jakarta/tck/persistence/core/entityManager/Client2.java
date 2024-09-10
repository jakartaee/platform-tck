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

package ee.jakarta.tck.persistence.core.entityManager;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import ee.jakarta.tck.persistence.common.schema30.Util;

import com.sun.ts.lib.harness.CleanupMethod;
import com.sun.ts.lib.harness.SetupMethod;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.StoredProcedureQuery;

public class Client2 extends Util {



	List<Employee> empRef = new ArrayList<Employee>();

	Employee emp0 = null;

	Order[] orders = new Order[5];

	Properties props = null;

	Map map = new HashMap<String, Object>();

	String dataBaseName = null;

	final static String ORACLE = "oracle";

	final static String POSTGRESQL = "postgresql";

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
	
	public void setupOrderData(String[] args, Properties p) throws Exception {
		logTrace( "setupOrderData");
		try {
			super.setup(args,p);
			removeTestData();
			createOrderData();
			map.putAll(getEntityManager().getProperties());
			map.put("foo", "bar");
			displayMap(map);
			dataBaseName = System.getProperty("jdbc.db");
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	public void cleanup() throws Exception {
		try {
			logTrace( "cleanup complete, calling super.cleanup");
			super.cleanup();
		} finally {

		}
	}

	public List<List> getResultSetsFromStoredProcedure(StoredProcedureQuery spq) {
		logTrace( "in getResultSetsFromStoredProcedure");
		boolean results = true;
		List<List> listOfList = new ArrayList<List>();
		int rsnum = 1;
		int rowsAffected = 0;

		do {
			if (results) {
				logTrace( "Processing set:" + rsnum);
				List<Employee> empList = new ArrayList<Employee>();
				List list = spq.getResultList();
				if (list != null) {
					logTrace( "Getting result set: " + (rsnum) + ", size:" + list.size());
					for (Object o : list) {
						if (o instanceof Employee) {
							Employee e = (Employee) o;
							logTrace( "Saving:" + e);
							empList.add(e);
						} else {
							logErr(
									"Did not get instance of Employee, instead got:" + o.getClass().getName());
						}
					}
					if (empList.size() > 0) {
						listOfList.add(empList);
					}
				} else {
					logErr( "Result set[" + rsnum + "] returned was null");
				}
				rsnum++;
			} else {
				rowsAffected = spq.getUpdateCount();
				if (rowsAffected >= 0)
					logTrace( "rowsAffected:" + rowsAffected);
			}
			results = spq.hasMoreResults();
			logTrace( "Results:" + results);

		} while (results || rowsAffected != -1);
		return listOfList;
	}

	public boolean verifyListOfListEmployeeIds(List<Integer> expected, List<List> listOfList) {
		boolean result = false;
		int count = 0;
		for (List<Employee> lEmp : listOfList) {

			if (lEmp.size() > 0) {
				List<Integer> actual = new ArrayList<Integer>();
				for (Employee e : lEmp) {
					actual.add(e.getId());
				}

				if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
					logTrace( "Received expected result:");
					for (Integer a : actual) {
						logTrace( "id:" + a);
					}
					count++;
				} else {
					logErr( "Did not receive expected result:");
					for (Integer e : expected) {
						logErr( " Expected id:" + e);
					}
					for (Integer a : actual) {
						logErr( "Actual id:" + a);
					}
				}

			} else {
				logErr( "Result set that was returned had 0 length");
			}

		}
		if (count == listOfList.size()) {
			result = true;
		}
		return result;
	}

	public boolean verifyListOfListEmployees(List<Employee> expected, List<List> listOfList) {
		boolean result = false;
		int count = 0;
		for (List<Employee> lEmp : listOfList) {

			if (lEmp.size() > 0) {
				List<Employee> actual = new ArrayList<Employee>();
				for (Employee e : lEmp) {
					actual.add(e);
				}
				if (verifyListEmployees(expected, actual)) {
					count++;
				}
			} else {
				logErr( "Result set that was returned had 0 length");
			}
		}
		if (count == listOfList.size()) {
			result = true;
		}
		return result;
	}

	public boolean verifyListEmployees(List<Employee> expected, List<Employee> actual) {
		boolean result = false;
		if (expected.containsAll(actual) && actual.containsAll(expected) && expected.size() == actual.size()) {
			for (Employee e : expected) {
				logTrace( "Received expected result:" + e);
			}
			result = true;
		} else {
			logErr( "Did not receive expected result:");
			for (Employee e : expected) {
				logErr( "expected employee:" + e);
			}
			for (Employee e : actual) {
				logErr( "actual employee :" + e);
			}
		}
		return result;
	}

	/*
	 * @testName: persistExceptionsTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:31; PERSISTENCE:JAVADOC:506;
	 * PERSISTENCE:JAVADOC:507; PERSISTENCE:SPEC:618.1; PERSISTENCE:SPEC:618.2
	 *
	 * @test_Strategy: Call EntityManager.persist()
	 */
	@SetupMethod(name = "setupOrderData")
	@CleanupMethod(name = "cleanupData")
	
	public void persistExceptionsTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		logMsg( "Testing persisting an entity twice ");

		try {
			getEntityManager().detach(orders[0]);
			getEntityTransaction().begin();
			logTrace( "Try to persist an existing Order");
			getEntityManager().persist(orders[0]);
			getEntityManager().flush();
			getEntityTransaction().commit();

			logErr( "A PersistenceException was not thrown");
		} catch (EntityExistsException eee) {
			logTrace( "EntityExistsException Caught as Expected:", eee);
			pass1 = true;
		} catch (PersistenceException pe) {
			logTrace( "A PersistentException was caught:", pe);
			pass1 = true;
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in while rolling back TX:", re);
			}
		}

		logMsg( "Testing non-entity ");
		try {
			getEntityTransaction().begin();
			getEntityManager().persist(this);
			logErr( "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			logTrace( "IllegalArgumentException caught as expected");
			pass2 = true;
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in while rolling back TX:", re);
			}
		}

		if (!pass1 || !pass2) {
			throw new Exception("persistExceptionsTest failed");
		}
	}

	/*
	 * @testName: refreshRemovedObjectEntityNotFoundExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:511
	 *
	 * @test_Strategy: Call EntityManager.refresh() method
	 */
	@SetupMethod(name = "setupOrderData")
	@CleanupMethod(name = "cleanupData")
	
	public void refreshRemovedObjectEntityNotFoundExceptionTest() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			logTrace( "Finding Order");
			Order o = getEntityManager().find(Order.class, 1);
			logTrace( "Removing all data");
			getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER").executeUpdate();
			logTrace( "Refreshing previous order");
			getEntityManager().refresh(o);
			getEntityTransaction().commit();
			logErr( "EntityNotFoundException not thrown");
		} catch (EntityNotFoundException e) {
			logTrace( "EntityNotFoundException Caught as Expected.");
			pass = true;
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
			throw new Exception("refreshRemovedObjectEntityNotFoundExceptionTest failed");
		}
	}

	/*
	 * @testName: refreshRemovedObjectMapEntityNotFoundExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:514;
	 *
	 * @test_Strategy: Call EntityManager.refresh() method
	 */
	@SetupMethod(name = "setupOrderData")
	@CleanupMethod(name = "cleanupData")
	
	public void refreshRemovedObjectMapEntityNotFoundExceptionTest() throws Exception {
		boolean pass = false;
		Map<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("some.cts.specific.property", "nothing.in.particular");
		try {
			getEntityTransaction().begin();
			Order o = getEntityManager().find(Order.class, 2);
			logTrace( "Removing all data");
			getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER").executeUpdate();
			logTrace( "Refreshing previous order");
			getEntityManager().refresh(o, myMap);
			getEntityTransaction().commit();
			logErr( "EntityNotFoundException not thrown");
		} catch (EntityNotFoundException e) {
			logTrace( "EntityNotFoundException Caught as Expected.");
			pass = true;
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
			throw new Exception("refreshRemovedObjectMapEntityNotFoundExceptionTest failed");
		}
	}

	/*
	 * @testName: refreshRemovedObjectLockModeTypeEntityNotFoundExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:517
	 *
	 * @test_Strategy: Call EntityManager.refresh() method
	 */
	@SetupMethod(name = "setupOrderData")
	@CleanupMethod(name = "cleanupData")
	
	public void refreshRemovedObjectLockModeTypeEntityNotFoundExceptionTest() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			Order o = getEntityManager().find(Order.class, 3);
			logTrace( "Removing all data");
			getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER").executeUpdate();
			getEntityManager().refresh(o, LockModeType.PESSIMISTIC_READ);
			logTrace( "Refreshing previous order");
			getEntityManager().refresh(o, LockModeType.PESSIMISTIC_READ);
			getEntityTransaction().commit();
			logErr( "EntityNotFoundException not thrown");
		} catch (EntityNotFoundException e) {
			logTrace( "EntityNotFoundException Caught as Expected.");
			pass = true;
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
			throw new Exception("refreshRemovedObjectLockModeTypeEntityNotFoundExceptionTest failed");
		}
	}

	/*
	 * @testName: refreshRemovedObjectLockModeTypeMapEntityNotFoundExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:523
	 *
	 * @test_Strategy: Call EntityManager.refresh() method
	 */
	@SetupMethod(name = "setupOrderData")
	@CleanupMethod(name = "cleanupData")
	
	public void refreshRemovedObjectLockModeTypeMapEntityNotFoundExceptionTest() throws Exception {
		boolean pass = false;
		Map<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("some.cts.specific.property", "nothing.in.particular");
		try {
			getEntityTransaction().begin();
			Order o = getEntityManager().find(Order.class, 4);
			logTrace( "Removing all data");
			getEntityManager().createNativeQuery("DELETE FROM PURCHASE_ORDER").executeUpdate();
			getEntityManager().refresh(o, LockModeType.PESSIMISTIC_READ, myMap);
			logTrace( "Refreshing previous order");
			getEntityManager().refresh(o, LockModeType.PESSIMISTIC_READ, myMap);
			getEntityTransaction().commit();
			logErr( "EntityNotFoundException not thrown");
		} catch (EntityNotFoundException e) {
			logTrace( "EntityNotFoundException Caught as Expected.");
			pass = true;
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
			throw new Exception("refreshRemovedObjectLockModeTypeMapEntityNotFoundExceptionTest failed");
		}
	}

	public void createOrderData() {

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

	private void createEmployeeData() {

		try {
			getEntityTransaction().begin();
			logMsg( "Creating Employees");

			final Date d1 = getUtilDate("2000-02-14");
			final Date d2 = getUtilDate("2001-06-27");
			final Date d3 = getUtilDate("2002-07-07");
			final Date d4 = getUtilDate("2003-03-03");
			final Date d5 = getUtilDate();

			emp0 = new Employee(1, "Alan", "Frechette", d1, (float) 35000.0);
			empRef.add(emp0);
			empRef.add(new Employee(2, "Arthur", "Frechette", d2, (float) 35000.0));
			empRef.add(new Employee(3, "Shelly", "McGowan", d3, (float) 50000.0));
			empRef.add(new Employee(4, "Robert", "Bissett", d4, (float) 55000.0));
			empRef.add(new Employee(5, "Stephen", "DMilla", d5, (float) 25000.0));
			for (Employee e : empRef) {
				if (e != null) {
					getEntityManager().persist(e);
					logTrace( "persisted employee:" + e);
				}
			}
			getEntityManager().flush();
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

	public void removeTestData() {
		logTrace( "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE").executeUpdate();
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
