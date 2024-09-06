/*
 * Copyright (c) 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.mapkey;



import java.util.Properties;

import com.sun.ts.lib.harness.Status;


public class Client2 extends Client {

	public Client2() {
	}

	public static void main(String[] args) {
		Client2 theTests = new Client2();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}


	private Employee2 empRef2;

	private Employee3 empRef3;

	private Employee4 empRef4;

	
	public void setupCreateTestData2(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);

			removeTestData();
			createTestData2();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: joinColumnInsertable
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:90
	 *
	 * @test_Strategy: The JoinColumn annotation with an attribute of insertable
	 * used to specify the mapping for the fk column to a second entity Execute a
	 * query returning Employees objects.
	 */
	public void joinColumnInsertable() throws Exception {
		boolean pass = true;

		try {
			getEntityTransaction().begin();
			clearCache();
			logTrace( "find employee2");
			Employee2 emp2 = getEntityManager().find(Employee2.class, 6);
			logTrace( "Name:" + emp2.getFirstName() + " " + emp2.getLastName());
			Department dept = emp2.getDepartment();

			if (dept == null) {
				logTrace( "Received expected null department for employee2");
			} else {
				pass = false;
				logErr( "Expected null department, actual:" + dept.getName());
			}
			clearCache();

			logTrace( "--------------");
			logTrace( "find employee3");
			Employee3 emp3 = getEntityManager().find(Employee3.class, 7);
			logTrace( "Name:" + emp3.getFirstName() + " " + emp3.getLastName());
			dept = emp3.getDepartment();

			if (dept != null && dept.getName().equals(deptRef[0].getName())) {
				logTrace( "Received expected department for employee3:" + dept.getName());
			} else {
				pass = false;
				if (dept != null) {
					logErr(
							"Expected department:" + deptRef[0].getName() + ", actual:" + dept.getName());
				} else {
					logErr( "Expected department:" + deptRef[0].getName() + ", actual:null");
				}
			}

			clearCache();
			logTrace( "--------------");
			logTrace( "find employee4");
			Employee4 emp4 = getEntityManager().find(Employee4.class, 8);
			logTrace( "Name:" + emp4.getFirstName() + " " + emp4.getLastName());
			dept = emp4.getDepartment();

			if (dept == null) {
				logTrace( "Received expected null department for employee4");
			} else {
				pass = false;
				logErr( "Expected department: null, actual:" + dept.getName());
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			pass = false;
			logErr( "Received unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("joinColumnInsertable Failed");
		}
	}

	/*
	 * @testName: joinColumnUpdatable
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:96
	 *
	 * @test_Strategy: The JoinColumn annotation with an attribute of updatable used
	 * to specify the mapping for the fk column to a second entity Execute a query
	 * returning Employees objects.
	 */
	public void joinColumnUpdatable() throws Exception {
		boolean pass = true;

		try {
			getEntityTransaction().begin();
			clearCache();
			logTrace( "find employee2");
			Employee2 emp2 = getEntityManager().find(Employee2.class, 6);
			logTrace( "Name:" + emp2.getFirstName() + " " + emp2.getLastName());
			logTrace( "set department to:" + deptRef[1].getId() + ", " + deptRef[1].getName());
			emp2.setDepartment(deptRef[1]);
			getEntityManager().merge(emp2);
			getEntityManager().flush();
			clearCache();
			logTrace( "find employee2 again");
			emp2 = getEntityManager().find(Employee2.class, 6);
			logTrace( "Name:" + emp2.getFirstName() + " " + emp2.getLastName());
			Department dept = emp2.getDepartment();
			if (dept == null) {
				logTrace( "Received expected null department");
			} else {
				pass = false;
				logErr( "Expected null department, actual:" + dept.getName());
			}

			clearCache();
			logTrace( "--------------");
			logTrace( "find employee3");
			Employee3 emp3 = getEntityManager().find(Employee3.class, 7);
			logTrace( "Name:" + emp3.getFirstName() + " " + emp3.getLastName());
			logTrace(
					"Department:" + emp3.getDepartment().getId() + ", " + emp3.getDepartment().getName());
			logTrace( "set department to:" + deptRef[1].getId() + ", " + deptRef[1].getName());
			emp3.setDepartment(deptRef[1]);
			getEntityManager().merge(emp3);
			getEntityManager().flush();
			clearCache();
			logTrace( "find employee3 again");
			emp3 = getEntityManager().find(Employee3.class, 7);
			logTrace( "Name:" + emp3.getFirstName() + " " + emp3.getLastName());
			dept = emp3.getDepartment();
			if (dept != null && dept.getName().equals(deptRef[0].getName())) {
				logTrace( "Received expected department:" + dept.getName());
			} else {
				pass = false;
				if (dept != null) {
					logErr(
							"Expected department:" + deptRef[0].getName() + ", actual:" + dept.getName());
				} else {
					logErr( "Expected department:" + deptRef[0].getName() + ", actual:null");
				}
			}

			clearCache();
			logTrace( "--------------");
			logTrace( "find employee4");
			Employee4 emp4 = getEntityManager().find(Employee4.class, 8);
			logTrace( "Name:" + emp4.getFirstName() + " " + emp4.getLastName());
			if (emp4.getFirstName() != null) {
				logErr( "Expected first name to be null, actual:" + emp4.getFirstName());
				pass = false;
			}
			if (emp4.getDepartment() != null) {
				logErr(
						"Expected Department to be null, actual:" + emp4.getDepartment().toString());
				pass = false;
			}
			logTrace( "set department to:" + deptRef[1].getId() + ", " + deptRef[1].getName());
			emp4.setDepartment(deptRef[1]);
			getEntityManager().merge(emp4);
			getEntityManager().flush();
			clearCache();
			logTrace( "find employee4 again");
			emp4 = getEntityManager().find(Employee4.class, 8);
			logTrace( "Name:" + emp4.getFirstName() + " " + emp4.getLastName());
			dept = emp4.getDepartment();
			if (dept != null && dept.getName().equals(deptRef[1].getName())) {
				logTrace( "Received expected department:" + dept.getName());
			} else {
				pass = false;
				if (dept != null) {
					logErr(
							"Expected " + deptRef[1].getName() + " department, actual:" + dept.getName());
				} else {
					logErr( "Expected " + deptRef[1].getName() + " department, actual:null");
				}
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			pass = false;
			logErr( "Received unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("joinColumnUpdatable Failed");
		}
	}

	/*
	 * @testName: columnInsertable
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:12
	 *
	 * @test_Strategy: The JoinColumn annotation with an attribute of insertable
	 * used to specify the mapping for the fk column to a second entity Execute a
	 * query returning Employees objects.
	 */
	public void columnInsertable() throws Exception {
		boolean pass = true;

		try {

			getEntityTransaction().begin();
			clearCache();
			logTrace( "find employee2");
			Employee2 emp2 = getEntityManager().find(Employee2.class, 6);
			String firstName = emp2.getFirstName();
			logTrace( "Name:" + firstName + " " + emp2.getLastName());

			if (firstName == null) {
				logTrace( "Received expected null firstName");
			} else {
				pass = false;
				logErr( "Expected firstName: null, actual:" + firstName);
			}

			clearCache();
			logTrace( "--------------");
			logTrace( "find employee3");
			Employee3 emp3 = getEntityManager().find(Employee3.class, 7);
			firstName = emp3.getFirstName();
			logTrace( "Name:" + firstName + " " + emp3.getLastName());

			if (firstName != null && firstName.equals("Paul")) {
				logTrace( "Received expected firstName:" + firstName);
			} else {
				pass = false;
				logErr( "Expected firstName: Paul, actual: null");
			}

			clearCache();
			logTrace( "--------------");
			logTrace( "find employee4");
			Employee4 emp4 = getEntityManager().find(Employee4.class, 8);
			firstName = emp4.getFirstName();
			logTrace( "Name:" + firstName + " " + emp4.getLastName());
			if (firstName == null) {
				logTrace( "Received expected null firstName");
			} else {
				pass = false;
				logErr( "Expected firstName: null, actual:" + firstName);
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			pass = false;
			logErr( "Received unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("columnInsertable Failed");
		}
	}

	/*
	 * @testName: columnUpdatable
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:20
	 *
	 * @test_Strategy: The JoinColumn annotation with an attribute of updatable used
	 * to specify the mapping for the fk column to a second entity Execute a query
	 * returning Employees objects.
	 */
	public void columnUpdatable() throws Exception {
		boolean pass = true;

		try {
			getEntityTransaction().begin();
			clearCache();
			logTrace( "find employee2");
			Employee2 emp2 = getEntityManager().find(Employee2.class, 6);
			logTrace( "Name:" + emp2.getFirstName() + " " + emp2.getLastName());
			logTrace( "set firstName and save");
			emp2.setFirstName("foo");
			getEntityManager().merge(emp2);
			getEntityManager().flush();
			clearCache();
			logTrace( "find employee2 again");
			emp2 = getEntityManager().find(Employee2.class, 6);
			logTrace( "Name:" + emp2.getFirstName() + " " + emp2.getLastName());
			String firstName = emp2.getFirstName();
			if (firstName == null) {
				logTrace( "Received expected null firstName");
			} else {
				pass = false;
				logErr( "Expected firstName: null, actual:" + firstName);
			}

			clearCache();
			logTrace( "--------------");
			logTrace( "find employee3");
			Employee3 emp3 = getEntityManager().find(Employee3.class, 7);
			logTrace( "Name:" + emp3.getFirstName() + " " + emp3.getLastName());
			logTrace( "set firstName and save");
			emp3.setFirstName("foo");
			getEntityManager().merge(emp3);
			getEntityManager().flush();
			clearCache();
			logTrace( "find employee3 again");
			emp3 = getEntityManager().find(Employee3.class, 7);
			logTrace( "Name:" + emp3.getFirstName() + " " + emp3.getLastName());
			firstName = emp3.getFirstName();
			if (firstName != null && firstName.equals("Paul")) {
				logTrace( "Received expected firstName:" + firstName);
			} else {
				pass = false;
				logErr( "Expected firstName: Paul, actual: null");
			}

			clearCache();
			logTrace( "--------------");
			logTrace( "find employee4");
			Employee4 emp4 = getEntityManager().find(Employee4.class, 8);
			logTrace( "Name:" + emp4.getFirstName() + " " + emp4.getLastName());
			logTrace( "set firstName and save");
			emp4.setFirstName("foo");
			getEntityManager().merge(emp4);
			getEntityManager().flush();
			clearCache();
			logTrace( "find employee4 again");
			emp4 = getEntityManager().find(Employee4.class, 8);
			logTrace( "Name:" + emp4.getFirstName() + " " + emp4.getLastName());
			firstName = emp4.getFirstName();
			if (firstName != null && firstName.equals("foo")) {
				logTrace( "Received expected firstName:" + firstName);
			} else {
				pass = false;
				logErr( "Expected firstName: foo, actual: null");
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			pass = false;
			logErr( "Received unexpected exception", e);
		}

		if (!pass) {
			throw new Exception("columnUpdatable Failed");
		}

	}

	public void createTestData2() throws Exception {
		try {

			logTrace( "createTestData2");
			createTestDataCommon();
			getEntityTransaction().begin();

			// insertable = false, updatable = false
			logTrace( "Create and persist employee2 ");
			empRef2 = new Employee2(6, "John", "Smith");
			empRef2.setDepartment(deptRef[0]);
			getEntityManager().persist(empRef2);

			// insertable = true, updatable = false
			logTrace( "Create and persist employee3 ");
			empRef3 = new Employee3(7, "Paul", "Jones");
			empRef3.setDepartment(deptRef[0]);
			getEntityManager().persist(empRef3);

			// insertable = false, updatable = true
			logTrace( "Create and persist employee4 ");
			empRef4 = new Employee4(8, "Thomas", "Brady");
			empRef4.setDepartment(deptRef[0]);
			getEntityManager().persist(empRef4);
			getEntityManager().flush();
			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Unexpected Exception creating test data:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in rollback:", re);
			}
		}
	}

}
