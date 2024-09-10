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

package ee.jakarta.tck.persistence.core.StoredProcedureQuery;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.StoredProcedureQuery;

public class Client extends PMClientBase {

	

	List<Employee> empRef = new ArrayList<Employee>();

	List<Employee2> empRef2 = new ArrayList<Employee2>();

	Employee emp0 = null;

	Employee2 emp2 = null;

	final Date utilDate = getUtilDate("2000-02-14");

	final Calendar calDate = getCalDate(2000, 02, 14);

	String dataBaseName = null;

	final static String ORACLE = "oracle";

	final static String POSTGRESQL = "postgresql";

	public Client() {
	}

	public static void main(String[] args) {
		Client theTests = new Client();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	public void setup(String[] args, Properties p) throws Exception {
		logTrace("setup");

		super.setup(args, p);
		removeTestData();
		createEmployeeTestData();
		dataBaseName = p.getProperty("jdbc.db");
	}

	public void cleanup() throws Exception {
		logTrace( "Cleanup data");
		removeTestData();
		logTrace( "cleanup complete, calling super.cleanup");
		super.cleanup();
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
									"Did not get instance of Employee, instead got:" + o.getClass().getSimpleName());
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

	public boolean verifyEmployeeIds(List<Integer> expected, List<List> listOfList) {
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

	private void createEmployeeTestData() {

		try {
			getEntityTransaction().begin();

			logMsg("Creating Employees");

			final Date d2 = getUtilDate("2001-06-27");
			final Date d3 = getUtilDate("2002-07-07");
			final Date d4 = getUtilDate("2003-03-03");
			final Date d5 = getUtilDate();

			emp0 = new Employee(1, "Alan", "Frechette", utilDate, (float) 35000.0);
			empRef.add(emp0);
			empRef.add(new Employee(2, "Arthur", "Frechette", d2, (float) 35000.0));
			empRef.add(new Employee(3, "Shelly", "McGowan", d3, (float) 50000.0));
			empRef.add(new Employee(4, "Robert", "Bissett", d4, (float) 55000.0));
			empRef.add(new Employee(5, "Stephen", "DMilla", d5, (float) 25000.0));
			for (Employee e : empRef) {
				if (e != null) {
					getEntityManager().persist(e);
					logTrace("persisted employee:" + e);
				}
			}

			getEntityManager().flush();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr("Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception fe) {
				logErr("Unexpected exception rolling back TX:", fe);
			}
		}
	}

	protected void removeTestData() {
		logTrace( "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM INSURANCE").executeUpdate();
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
