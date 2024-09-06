/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates. All rights reserved.
 * 
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

package ee.jakarta.tck.persistence.core.annotations.convert;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;

public class Client extends PMClientBase {

	public Client() {
	}

	

	public JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Address", pkgName + "B", pkgName + "CharConverter", pkgName + "CommaConverter",
				pkgName + "Country", pkgName + "Customer", pkgName + "Department",
				pkgName + "DisableAutoApplyConverter", pkgName + "DotConverter", pkgName + "DotConverter2",
				pkgName + "Employee", pkgName + "Employee2", pkgName + "Employee3", pkgName + "FullTimeEmployee",
				pkgName + "FullTimeEmployee2", pkgName + "IntegerConverter", pkgName + "NumberToStateConverter",
				pkgName + "SalaryConverter", pkgName + "SpaceConverter" };
		return createDeploymentJar("jpa_core_annotations_convert.jar", pkgNameWithoutSuffix, classes);
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
	 * @testName: convertTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3310; PERSISTENCE:JAVADOC:3311;
	 * PERSISTENCE:SPEC:1487; PERSISTENCE:SPEC:1489; PERSISTENCE:SPEC:1489.1;
	 * PERSISTENCE:SPEC:1956; PERSISTENCE:SPEC:1956.1; PERSISTENCE:SPEC:1991;
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void convertTest() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			Employee expected = new Employee(1, "Alan", "Smith", "3#5#0#0#0.0");
			logTrace( "Persisting Employee");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			logTrace(
					"find the previously persisted Employees and verify them and their departments");
			Employee emp = getEntityManager().find(Employee.class, expected.getId());
			if (emp != null) {
				logTrace( "Found employee: " + emp.getId());
				// Remove the '#' symbols from the expected salary and then do
				// comparison
				expected.setSalary(expected.getSalary().replace("#", ""));
				if (emp.equals(expected)) {
					logTrace( "Received expected employee:" + emp.toString());
					pass = true;
				} else {
					logErr( "Expected:" + expected.toString() + ", actual:" + emp.toString());
				}

			} else {
				logErr( "Find returned null Employee instead of:" + expected);
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Unexpected exception occurred: ", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("convertTest failed");
		}
	}

	/*
	 * @testName: convertExceptionDuringPersistTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1495; PERSISTENCE:JAVADOC:3312;
	 * PERSISTENCE:JAVADOC:3313; PERSISTENCE:SPEC:1483; PERSISTENCE:SPEC:1483.1;
	 * 
	 * @test_Strategy: Converter throws a RuntimeException wrapped by a
	 * PersistenceException during a persist
	 */
	@Test
	public void convertExceptionDuringPersistTest() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			logTrace( "isActive():" + getEntityTransaction().isActive());
			B b = new B("1", "name1", 1, new Address("500 Oracle Parkway", "Redwood Shores", -1));
			getEntityManager().persist(b);
			getEntityManager().flush();
			getEntityTransaction().commit();
			logErr( "Did not received expected PersistenceException");
		} catch (PersistenceException pe) {
			logTrace( "Received expected PersistenceException");
			if (getEntityTransaction().getRollbackOnly()) {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			} else {
				logErr( "Transaction was not marked for rollback");
			}

		} catch (Exception ex) {
			logErr( "Unexpected exception received:", ex);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception while rolling back TX:", re);
			}
		}
		if (!pass) {
			throw new Exception("convertExceptionDuringPersistTest failed");
		}
	}

	/*
	 * @testName: convertExceptionDuringLoadTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1495; PERSISTENCE:JAVADOC:3312;
	 * PERSISTENCE:JAVADOC:3313; PERSISTENCE:SPEC:1483; PERSISTENCE:SPEC:1483.1;
	 * 
	 * @test_Strategy: Converter throws a RuntimeException wrapped by a
	 * PersistenceException during a load
	 */
	@Test
	public void convertExceptionDuringLoadTest() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			logTrace( "isActive():" + getEntityTransaction().isActive());

			B b = new B("1", "name1", 1, new Address("500 Oracle Parkway", "Redwood Shores", -2));
			logTrace( "Persisting B");
			getEntityManager().persist(b);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			try {
				logTrace( "Finding B");

				B b1 = getEntityManager().find(B.class, "1");
				logErr( "Did not received expected PersistenceException");
			} catch (PersistenceException pe) {
				logTrace( "Received expected PersistenceException");
				if (getEntityTransaction().getRollbackOnly()) {
					logTrace( "Transaction was marked for rollback");
					pass = true;
				} else {
					logErr( "Transaction was not marked for rollback");
				}
			} catch (Exception ex) {
				ex.printStackTrace();

				logErr( "Unexpected exception received:", ex);
			} finally {
				try {
					if (getEntityTransaction().isActive()) {
						getEntityTransaction().rollback();
					}
				} catch (Exception re) {
					logErr( "Unexpected Exception while rolling back TX:", re);
				}
			}

		} catch (Exception ex) {
			logErr( "Unexpected exception received:", ex);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception while rolling back TX:", re);
			}
		}
		if (!pass) {
			throw new Exception("convertExceptionDuringLoadTest failed");
		}
	}

	/*
	 * @testName: convertsTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:3312; PERSISTENCE:JAVADOC:3313;
	 * PERSISTENCE:JAVADOC:3314; PERSISTENCE:JAVADOC:3315; PERSISTENCE:JAVADOC:3316;
	 * PERSISTENCE:SPEC:1482; PERSISTENCE:SPEC:1483; PERSISTENCE:SPEC:1483.1;
	 * PERSISTENCE:SPEC:1484; PERSISTENCE:SPEC:1489.3;
	 * 
	 * @test_Strategy: Converter throws a RuntimeException during a persist
	 */
	@Test
	public void convertsTest() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		try {
			getEntityTransaction().begin();
			String street = "500.Oracle.Parkway";
			String city = "Redwood Shores";
			// 1 - MA , 2 - CA
			B b = new B("1", "name1", 1000, new Address(street, city, 1));
			getEntityManager().persist(b);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			B b1 = getEntityManager().find(B.class, "1");
			logTrace( "B:" + b1.toString());
			if (b1.getBValue().equals(1110)) {
				logTrace( "Received expected value:" + b1.getBValue());
				pass1 = true;
			} else {
				logErr(
						"Converter was not properly applied, expected value:1002, actual" + b1.getBValue());
			}
			Address a = b1.getAddress();
			if (a.getStreet().equals(street.replace(".", "_"))) {
				logTrace( "Received expected street:" + a.getStreet());
				pass2 = true;
			} else {
				logErr(
						"Converter was not properly applied, expected street:" + street + ", actual:" + a.getStreet());
			}
			if (a.getState() == 1) {
				logTrace( "Received expected state:" + a.getState());
				pass3 = true;
			} else {
				logErr(
						"Converter was not properly applied, expected state: 1, actual: " + a.getState());
			}
			getEntityTransaction().commit();

		} catch (Exception ex) {
			logErr( "Unexpected exception received:", ex);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception while rolling back TX:", re);
			}
		}

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("convertsTest failed");
		}
	}

	/*
	 * @testName: mappedSuperClassTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1490; PERSISTENCE:SPEC:1481;
	 * PERSISTENCE:SPEC:1489.2; PERSISTENCE:SPEC:1957;
	 * 
	 * @test_Strategy: Test converter with property access and attributes from
	 * MappedSuperClass
	 */
	@Test
	public void mappedSuperClassTest() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			char[] c = { 'D', 'o', 'e' };
			FullTimeEmployee expected = new FullTimeEmployee(1, "John", c, "3#5#0#0#0.0");
			logTrace( "Persisting FullTimeEmployee:" + expected.toString());
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			logTrace(
					"find the previously persisted Employees and verify them and their departments");
			FullTimeEmployee emp = getEntityManager().find(FullTimeEmployee.class, expected.getId());
			if (emp != null) {
				logTrace( "Found FullTimeEmployee: " + emp.getId());
				expected.setSalary(expected.getSalary().replace("#", ""));
				c = new char[] { 'J', 'a', 'm', 'e', 's' };
				expected.setLastName(c);
				if (emp.equals(expected)) {
					logTrace( "Received expected FullTimeEmployee:" + emp.toString());
					pass = true;
				} else {
					logErr( "Expected:" + expected.toString() + ", actual:" + emp.toString());
				}
			} else {
				logErr( "Find returned null FullTimeEmployee instead of:" + expected);
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Unexpected exception occurred: ", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("mappedSuperClassTest failed");
		}
	}

	/*
	 * @testName: mappedSuperClass2Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1488; PERSISTENCE:SPEC:1491;
	 * PERSISTENCE:SPEC:1992;
	 * 
	 * @test_Strategy: Test overriding a converter
	 */
	@Test
	public void mappedSuperClass2Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			FullTimeEmployee2 expected = new FullTimeEmployee2(1, "J.o.h.n", "Hill", "3500.0");
			logTrace( "Persisting FullTimeEmployee2:" + expected.toString());
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			logTrace(
					"find the previously persisted Employees and verify them and their departments");
			FullTimeEmployee2 emp = getEntityManager().find(FullTimeEmployee2.class, expected.getId());
			if (emp != null) {
				logTrace( "Found FullTimeEmployee2: " + emp.getId());
				expected.setFirstName(expected.getFirstName().replace(".", "#"));
				if (emp.equals(expected)) {
					logTrace( "Received expected FullTimeEmployee:" + emp.toString());
					pass = true;
				} else {
					logErr( "Expected:" + expected.toString() + ", actual:" + emp.toString());
				}
			} else {
				logErr( "Find returned null FullTimeEmployee instead of:" + expected);
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Unexpected exception occurred: ", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("mappedSuperClass2Test failed");
		}
	}

	/*
	 * @testName: convert2Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1492; PERSISTENCE:SPEC:1494;
	 * 
	 * @test_Strategy: Test converters are applied when a JPQL or CriteriaQuery
	 * reads from the DB
	 */
	@Test
	public void convert2Test() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		try {
			getEntityTransaction().begin();
			Employee expected = new Employee(1, "Alan", "Smith", "3#5#0#0#0.0");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			pass1 = true;
		} catch (Exception ex) {
			logErr( "Unexpected exception received:", ex);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception while rolling back TX:", re);
			}
		}
		clearCache();
		try {
			getEntityTransaction().begin();
			logMsg( "Testing JPQL");
			List<Employee> o = getEntityManager().createQuery("Select e from Employee e WHERE e.id = 1", Employee.class)
					.getResultList();

			if (o.size() == 1) {
				Employee emp = o.get(0);
				logTrace( "Employee:" + emp.toString());
				if (emp.getSalary().equals("35000.0")) {
					logTrace( "Received expected value:" + emp.getSalary());
					pass2 = true;
				} else {
					logErr(
							"Converter was not properly applied, expected value:35000.0, actual" + emp.getSalary());
				}
			} else {
				logErr( "Expected 1 entity to be returned, actual:" + o.size());
			}
			getEntityTransaction().commit();

		} catch (Exception ex) {
			logErr( "Unexpected exception received:", ex);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception while rolling back TX:", re);
			}
		}
		clearCache();
		try {
			getEntityTransaction().begin();

			logMsg( "Testing Criteria");
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Employee> cquery = cbuilder.createQuery(Employee.class);
			if (cquery != null) {
				logTrace( "Obtained Non-null Criteria Query");
				Root<Employee> employee = cquery.from(Employee.class);
				cquery.select(employee).where(cbuilder.equal(employee.get("id"), 1));
				Employee emp = getEntityManager().createQuery(cquery).getSingleResult();
				if (emp != null) {
					logTrace( "Employee:" + emp.toString());
					if (emp.getSalary().equals("35000.0")) {
						logTrace( "Received expected value:" + emp.getSalary());
						pass3 = true;
					} else {
						logErr(
								"Converter was not properly applied, expected value:35000.0, actual" + emp.getSalary());
					}
				} else {
					logErr( "Null Employee result was returned");
				}
			} else {
				logErr( "createQuery returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception ex) {
			logErr( "Unexpected exception received:", ex);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception while rolling back TX:", re);
			}
		}

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("converts2Test failed");
		}
	}

	/*
	 * @testName: convert3Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1492; PERSISTENCE:SPEC:1493;
	 * PERSISTENCE:SPEC:1597;
	 * 
	 * @test_Strategy: Test converters are applied when a JPQL or CriteriaQuery
	 * write to the DB
	 */
	@Test
	public void convert3Test() throws Exception {
		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;
		try {
			getEntityTransaction().begin();
			Employee expected = new Employee(1, "Alan", "Smith", "0");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			pass1 = true;
		} catch (Exception ex) {
			logErr( "Unexpected exception received:", ex);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception while rolling back TX:", re);
			}
		}
		clearCache();
		try {
			getEntityTransaction().begin();
			logMsg( "Testing JPQL");
			String expected = "54321.0";
			getEntityManager().createQuery("Update Employee e set e.salary='5#4#3#2#1.0'  WHERE e.id = 1")
					.executeUpdate();
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			Employee emp = getEntityManager().find(Employee.class, 1);
			if (emp != null) {
				logTrace( "Found employee: " + emp.getId());
				if (emp.getSalary().equals(expected)) {
					logTrace( "Received expected value:" + emp.getSalary());
					pass2 = true;
				} else {
					logErr( "Converter was not properly applied, expected value:" + expected
							+ ", actual" + emp.getSalary());
				}
			} else {
				logErr( "Find returned null Employee");
			}
			getEntityTransaction().commit();

		} catch (Exception ex) {
			logErr( "Unexpected exception received:", ex);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception while rolling back TX:", re);
			}
		}
		clearCache();
		try {
			getEntityTransaction().begin();

			logMsg( "Testing Criteria");
			String expected = "3500.0";
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			CriteriaUpdate<Employee> cd = cbuilder.createCriteriaUpdate(Employee.class);
			Root<Employee> employee = cd.from(Employee.class);
			logTrace( "Obtained expected root");
			cd.set("salary", "3#5#0#0.0");
			cd.where(cbuilder.equal(employee.get("id"), 1));
			int actual = getEntityManager().createQuery(cd).executeUpdate();
			getEntityManager().flush();
			getEntityTransaction().commit();
			if (actual == 1) {
				clearCache();
				getEntityTransaction().begin();
				Employee emp = getEntityManager().find(Employee.class, 1);
				if (emp != null) {
					logTrace( "Found employee: " + emp.getId());
					if (emp.getSalary().equals("3500.0")) {
						logTrace( "Received expected value:" + emp.getSalary());
						pass3 = true;
					} else {
						logErr( "Converter was not properly applied, expected value:" + expected
								+ ", actual" + emp.getSalary());
					}
				} else {
					logErr( "Find returned null Employee instead of:" + expected);
				}
				getEntityTransaction().commit();
			} else {
				logErr( "Expected 1 update, actual:" + actual);
			}
		} catch (Exception ex) {
			logErr( "Unexpected exception received:", ex);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception while rolling back TX:", re);
			}
		}

		if (!pass1 || !pass2 || !pass3) {
			throw new Exception("converts3Test failed");
		}
	}

	/*
	 * @testName: embeddedTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1991; PERSISTENCE:SPEC:1994;
	 * 
	 * @test_Strategy: Test converter in an embedded entity
	 */
	@Test
	public void embeddedTest() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			String name = "john smith";
			String country = "United States";
			Customer expected = new Customer("1", name, new Country(country, "USA"));
			logTrace( "Persisting Customer:" + expected.toString());
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			logTrace( "find the previously persisted Customer and Country and verify them");
			Customer cust = getEntityManager().find(Customer.class, expected.getId());
			if (cust != null) {
				logTrace( "Found Customer: " + cust.toString());
				String tmp = country.replace(" ", "_");
				if (cust.getCountry().getCountry().equals(tmp)) {
					logTrace( "Received expected Country:" + cust.getCountry().getCountry());
					pass = true;
				} else {
					logErr( "Expected:" + tmp + ", actual:" + cust.getCountry().getCountry());
				}
			} else {
				logErr( "Find returned null Customer");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Unexpected exception occurred: ", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("embeddedTest failed");
		}
	}

	/*
	 * @testName: elementCollectionBasicType
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:1994;
	 * 
	 * @test_Strategy: Test converter of an element collection of basic type
	 */
	@Test
	public void elementCollectionBasicType() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			Customer expected = new Customer("1");
			List<String> phones = new ArrayList<String>();
			phones.add("781,442,2010");
			phones.add("781,442,2011");
			phones.add("781,442,2012");
			List<String> expectedphones = new ArrayList<String>();
			for (String p : phones) {
				expectedphones.add(p.replace(",", "-"));
			}

			expected.setPhones(phones);
			logTrace( "Persisting Customer:" + expected.toString());
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			logTrace( "find the previously persisted Customer and Country and verify them");
			Customer cust = getEntityManager().find(Customer.class, expected.getId());
			if (cust != null) {
				logTrace( "Found Customer: " + cust.toString());
				if (cust.getPhones().containsAll(expectedphones) && expectedphones.containsAll(cust.getPhones())
						&& cust.getPhones().size() == expectedphones.size()) {
					logTrace( "Received expected Phones:");
					for (String s : cust.getPhones()) {
						logTrace( "phone:" + s);
					}
					pass = true;
				} else {
					logErr( "Did not get expected results.");
					for (String s : expectedphones) {
						logTrace( "expected:" + s);
					}
					logErr( "actual:");
					for (String s : cust.getPhones()) {
						logTrace( "actual:" + s);
					}
				}
			} else {
				logErr( "Find returned null Customer");
			}

			getEntityTransaction().commit();
		} catch (Exception e) {
			logErr( "Unexpected exception occurred: ", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("elementCollectionBasicType failed");
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
			getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DEPARTMENT").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM B_EMBEDDABLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM CUST_TABLE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM PHONES").executeUpdate();
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
