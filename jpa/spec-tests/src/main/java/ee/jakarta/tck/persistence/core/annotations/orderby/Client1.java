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

package ee.jakarta.tck.persistence.core.annotations.orderby;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client1 extends PMClientBase {



	List<Address> addrRef;

	Address addr1 = null;

	Address addr2 = null;

	Address addr3 = null;

	List<Address2> addrRef2;

	Address2 addr11 = null;

	Address2 addr12 = null;

	Address2 addr13 = null;

	public Client1() {
	}

	public static void main(String[] args) {
		Client1 theTests = new Client1();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "A", pkgName + "A2", pkgName + "Address", pkgName + "Address2",
				pkgName + "Customer", pkgName + "Customer2", pkgName + "Department", pkgName + "Employee",
				pkgName + "Insurance", pkgName + "ZipCode", pkgName + "ZipCode2" };
		return createDeploymentJar("jpa_core_annotations_orderby1.jar", pkgNameWithoutSuffix, classes);
	}

	
	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			createDeployment();

			removeTestData();
			createTestData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: orderByTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1103; PERSISTENCE:SPEC:1104;
	 * PERSISTENCE:SPEC:1106; PERSISTENCE:JAVADOC:145;
	 * 
	 * @test_Strategy: The OrderBy annotation specifies the ordering of the elements
	 * of a collection valued association at the point when the association is
	 * retrieved.
	 * 
	 * The property name must correspond to that of a persistenct property of the
	 * associated class.
	 *
	 * The property used in the ordering must correspond to columns for which
	 * comparison operations are supported.
	 *
	 * If DESC is specified, the elements will be ordered in descending order.
	 * 
	 * Retrieve the Collection using getter property accessor.
	 */
	@Test
	public void orderByTest1() throws Exception {

		logTrace( "Begin orderByTest1");
		boolean pass1 = true;
		boolean pass2 = false;
		List resultsList = new ArrayList();
		final String[] expectedResult = new String[] { "Zoe", "Song", "Jie", "Ay" };

		try {
			getEntityTransaction().begin();

			Employee empChange = getEntityManager().find(Employee.class, 65);

			empChange.setFirstName("Ay");
			getEntityManager().merge(empChange);
			getEntityManager().flush();

			final Insurance newIns = getEntityManager().find(Insurance.class, 60);
			getEntityManager().refresh(newIns);

			final List insResult = newIns.getEmployees();

			if (insResult.size() != 4) {
				logTrace(
						"orderByTest1:  Did not get expected results.  Expected: 4, " + "got: " + insResult.size());
				pass1 = false;
			} else if (pass1) {
				Iterator i1 = insResult.iterator();
				logTrace( "Check Employee Collection for expected first names");
				while (i1.hasNext()) {
					Employee e1 = (Employee) i1.next();
					resultsList.add((String) e1.getFirstName());
					logTrace(
							"orderByTest1: got Employee FirstName:" + (String) e1.getFirstName());
				}

				logTrace( "Compare first names received with expected first names ");
				String[] result = (String[]) (resultsList.toArray(new String[resultsList.size()]));
				pass2 = Arrays.equals(expectedResult, result);

			}

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass2 = false;
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass1 || !pass2)
			throw new Exception("orderByTest1 failed");
	}

	/*
	 * @testName: orderByTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1103; PERSISTENCE:SPEC:1104;
	 * PERSISTENCE:SPEC:1106; PERSISTENCE:SPEC:1109; PERSISTENCE:SPEC:1110;
	 * PERSISTENCE:JAVADOC:145; PERSISTENCE:SPEC:653
	 * 
	 * @test_Strategy: The OrderBy annotation specifies the ordering of the elements
	 * of a collection valued association at the point when the association is
	 * retrieved.
	 *
	 * The property name must correspond to that of a persistenct property of the
	 * associated class.
	 *
	 * The property used in the ordering must correspond to columns for which
	 * comparison operations are supported.
	 *
	 * If ASC is specified, the elements will be ordered in ascending order.
	 * 
	 * Retrieve the Collection using getter property accessor.
	 */
	@Test
	public void orderByTest2() throws Exception {

		logTrace( "Begin orderByTest2");
		boolean pass1 = true;
		boolean pass2 = false;
		List resultsList = new ArrayList();
		final String[] expectedResult = new String[] { "Jie", "Song", "Yay", "Zoe" };

		try {
			getEntityTransaction().begin();

			Employee emp2Change = getEntityManager().find(Employee.class, 65);

			emp2Change.setFirstName("Yay");
			getEntityManager().merge(emp2Change);
			getEntityManager().flush();

			final Department newDept = getEntityManager().find(Department.class, 50);
			getEntityManager().refresh(newDept);

			final List deptResult = newDept.getEmployees();

			if (deptResult.size() != 4) {
				logTrace(
						"orderByTest2:  Did not get expected results.  Expected: 4, " + "got: " + deptResult.size());
				pass1 = false;
			} else if (pass1) {
				Iterator i2 = deptResult.iterator();
				logTrace( "Check Employee Collection for expected first names");
				while (i2.hasNext()) {
					Employee e2 = (Employee) i2.next();
					resultsList.add((String) e2.getFirstName());
					logTrace(
							"orderByTest2: got Employee FirstName:" + (String) e2.getFirstName());
				}

				logTrace( "Compare first names received with expected first names ");
				String[] result = (String[]) (resultsList.toArray(new String[resultsList.size()]));
				pass2 = Arrays.equals(expectedResult, result);

			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass1 || !pass2)
			throw new Exception("orderByTest2 failed");
	}

	/*
	 * @testName: orderByTest3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1103; PERSISTENCE:SPEC:1104;
	 * PERSISTENCE:SPEC:1106; PERSISTENCE:JAVADOC:145
	 * 
	 * @test_Strategy: The OrderBy annotation specifies the ordering of the elements
	 * of a collection valued association at the point when the association is
	 * retrieved.
	 *
	 * If DESC is specified, the elements will be ordered in descending order.
	 * 
	 * Add to the Collection then retrieve the updated Collection and ensure the
	 * list is ordered.
	 * 
	 */
	@Test
	public void orderByTest3() throws Exception {
		logTrace( "Begin orderByTest3");
		boolean pass1 = true;
		boolean pass2 = false;
		List insResult;
		List resultsList = new ArrayList();
		final String[] expectedResult = new String[] { "Zoe", "Song", "Penelope", "May", "Jie" };

		try {
			getEntityTransaction().begin();

			Employee emp3Change = getEntityManager().find(Employee.class, 85);
			Insurance ins = getEntityManager().find(Insurance.class, 60);

			emp3Change.setInsurance(ins);
			getEntityManager().merge(emp3Change);
			ins.getEmployees().add(emp3Change);
			getEntityManager().merge(ins);
			getEntityManager().flush();

			getEntityManager().refresh(ins);

			insResult = ins.getEmployees();

			if (insResult.size() != 5) {
				logErr( "orderByTest3: Expected List Size of 5 " + "got: " + insResult.size());
				pass1 = false;
			} else if (pass1) {
				Iterator i3 = insResult.iterator();
				logTrace( "Check Employee Collection for expected first names");
				while (i3.hasNext()) {
					Employee e3 = (Employee) i3.next();
					resultsList.add((String) e3.getFirstName());
					logTrace(
							"orderByTest3: got Employee FirstName:" + (String) e3.getFirstName());
				}

				logTrace( "orderByTest3: Expected size received, check ordering . . .");
				String[] result = (String[]) (resultsList.toArray(new String[resultsList.size()]));
				pass2 = Arrays.equals(expectedResult, result);

			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass1 || !pass2)
			throw new Exception("orderByTest3 failed");
	}

	/*
	 * @testName: orderByTest4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1103; PERSISTENCE:SPEC:1104;
	 * PERSISTENCE:SPEC:1106; PERSISTENCE:JAVADOC:145
	 * 
	 * @test_Strategy: The OrderBy annotation specifies the ordering of the elements
	 * of a collection valued association at the point when the association is
	 * retrieved.
	 * 
	 * If ASC is specified, the elements will be ordered in ascending order.
	 * 
	 * Retrieve the Collection, add to the Collection and retrieve it again making
	 * sure the list is ordered .
	 * 
	 */
	@Test
	public void orderByTest4() throws Exception {
		logTrace( "Begin orderByTest4");
		boolean pass1 = true;
		boolean pass2 = false;
		List resultsList = new ArrayList();
		final String[] expectedResult = new String[] { "Jie", "May", "Penelope", "Song", "Zoe" };

		try {
			getEntityTransaction().begin();

			Employee emp4Change = getEntityManager().find(Employee.class, 85);
			Department dept = getEntityManager().find(Department.class, 50);

			emp4Change.setDepartment(dept);
			getEntityManager().merge(emp4Change);
			dept.getEmployees().add(emp4Change);
			getEntityManager().merge(dept);
			getEntityManager().flush();

			getEntityManager().refresh(dept);
			final List deptResult = dept.getEmployees();

			if (deptResult.size() != 5) {
				logErr(
						"orderByTest4: Expected Collection Size of 5 " + "got: " + deptResult.size());
				pass1 = false;
			} else if (pass1) {
				Iterator i4 = deptResult.iterator();
				logTrace( "Check Employee Collection for expected first names");
				while (i4.hasNext()) {
					Employee e4 = (Employee) i4.next();
					resultsList.add((String) e4.getFirstName());
					logTrace(
							"orderByTest4: got Employee FirstName:" + (String) e4.getFirstName());
				}

				logTrace( "orderByTest4: Expected size received, check ordering . . .");
				String[] result = (String[]) (resultsList.toArray(new String[resultsList.size()]));
				pass2 = Arrays.equals(expectedResult, result);
			}

			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in rollback:", re);
			}
		}

		if (!pass1 || !pass2)
			throw new Exception("orderByTest4 failed");
	}

	private void createTestData() throws Exception {
		try {
			logTrace( "createTestData");
			getEntityTransaction().begin();
			final float salary = 10000.00F;

			Department d1 = new Department(50, "SJSAS Appserver");
			getEntityManager().persist(d1);

			Insurance s1 = new Insurance(60, "United");
			getEntityManager().persist(s1);

			final Employee e1 = new Employee(70, "Jie", "Leng", salary, d1, s1);
			final Employee e2 = new Employee(80, "Zoe", "Leng", salary, d1, s1);
			final Employee e3 = new Employee(90, "Song", "Leng", salary, d1, s1);
			final Employee e4 = new Employee(65, "May", "Leng", salary, d1, s1);
			final Employee e5 = new Employee(85, "Penelope", "Leng", salary);
			getEntityManager().persist(e1);
			getEntityManager().persist(e2);
			getEntityManager().persist(e3);
			getEntityManager().persist(e4);
			getEntityManager().persist(e5);

			List<Employee> link = new ArrayList<Employee>();
			link.add(e1);
			link.add(e2);
			link.add(e3);
			link.add(e4);

			d1.setEmployees(link);
			getEntityManager().merge(d1);

			s1.setEmployees(link);
			getEntityManager().merge(s1);

			logTrace( "persisted Entity Data");
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
			getEntityManager().createNativeQuery("Delete from EMPLOYEE").executeUpdate();
			getEntityManager().createNativeQuery("Delete from DEPARTMENT").executeUpdate();
			getEntityManager().createNativeQuery("Delete from INSURANCE").executeUpdate();
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
