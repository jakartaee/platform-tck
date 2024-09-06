/*
 * Copyright (c) 2011, 2024 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.persistenceUnitUtil;


import java.math.BigInteger;
import java.util.Properties;

import ee.jakarta.tck.persistence.core.versioning.Member;
import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.PersistenceUnitUtil;

public class Client extends PMClientBase {



	private final Employee empRef[] = new Employee[2];

	public Client() {
	}

	
	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			removeTestData();
			createTestData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: getPersistenceUtilUtilTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:384;
	 *
	 * @test_Strategy:
	 *
	 */
	
	public void getPersistenceUtilUtilTest() throws Exception {
		boolean pass = false;
		PersistenceUnitUtil puu = getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil();
		if (puu != null) {
			pass = true;
		} else {
			logErr( "getPersistenceUtil() returned null");
		}

		if (!pass) {
			throw new Exception("getPersistenceUtilUtilTest failed");
		}
	}

	/*
	 * @testName: getIdentifierTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:385;
	 *
	 * @test_Strategy: Call PersistenceUnitUtil.getIdentifierTest on an entity and
	 * verify the correct id is returned
	 */
	
	public void getIdentifierTest() throws Exception {
		boolean pass = true;
		Employee emp = new Employee(1, "foo", "bar", getSQLDate("2000-02-14"), (float) 35000.0);

		logMsg( "Test entity not yet persisted");
		try {
			PersistenceUnitUtil puu = getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil();
			Integer id = (Integer) puu.getIdentifier(emp);
			if (id == null || id != 1) {
				logErr( "expected a null or id: 1, actual id:" + id);
				pass = false;
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}
		logMsg( "Test entity persisted");

		try {
			getEntityTransaction().begin();
			getEntityManager().persist(emp);

			PersistenceUnitUtil puu = getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil();
			Integer id = (Integer) puu.getIdentifier(emp);
			if (id != 1) {
				logErr( "expected a null or id: 1, actual id:" + id);
				pass = false;
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
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
			throw new Exception("getIdentifierTest failed");
		}
	}

	/*
	 * @testName: getIdentifierIllegalArgumentExceptionTest
	 *
	 * @assertion_ids: PERSISTENCE:JAVADOC:548;
	 *
	 * @test_Strategy: Call PersistenceUnitUtil.getIdentifierTest of a non-entity
	 * and verify IllegalArgumentException is thrown
	 */
	
	public void getIdentifierIllegalArgumentExceptionTest() throws Exception {
		boolean pass = false;
		try {
			PersistenceUnitUtil puu = getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil();
			puu.getIdentifier(this);
			logErr( "IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException iae) {
			pass = true;
			logTrace( "Received expected IllegalArgumentException");
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}
		if (!pass) {
			throw new Exception("getIdentifierIllegalArgumentExceptionTest failed");
		}
	}

	
	public void getVersionTest() throws Exception {
		boolean pass = false;
		final int ID = 1;
		Member member = new Member(ID, "Member 1", true, BigInteger.valueOf(1000L));

		//Try cleanup first
		try {
			getEntityTransaction().begin();
			Member member1 = getEntityManager().find(Member.class, ID);
			if (member1 != null) {
				getEntityManager().remove(member1);
				getEntityTransaction().commit();
			}
		} catch (Exception e) {
			logErr( "Exception encountered while removing entity:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in removeTestData:", re);
			}
		}
		//Prepare test data and test created version after commit
		try {
			getEntityTransaction().begin();
			getEntityManager().persist(member);

			PersistenceUnitUtil puu = getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil();
			getEntityTransaction().commit();
			Integer version = (Integer) puu.getVersion(member);
			if (version != null && version.equals(member.getVersion())) {
				pass = true;
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
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
			throw new Exception("getVersionTest failed");
		}
	}

	
	public void loadIsLoadTest() throws Exception {
		boolean pass = false;

		try {
			Employee employee = getEntityManager().find(Employee.class, empRef[0].getId());
			PersistenceUnitUtil puu = getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil();
			puu.load(employee, "salary");
			if (puu.isLoaded(employee, "salary")) {
				pass = true;
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("loadIsLoadTest failed");
		}
	}

	
	public void isInstanceTest() throws Exception {
		boolean pass = false;

		try {
			Employee foundEmployee = getEntityManager().find(Employee.class, empRef[0].getId());
			PersistenceUnitUtil puu = getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil();
			if (puu.isInstance(foundEmployee, Employee.class)) {
				pass = true;
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("isInstanceTest failed");
		}
	}

	
	public void getClassTest() throws Exception {
		boolean pass = false;

		try {
			Employee foundEmployee = getEntityManager().find(Employee.class, empRef[0].getId());
			PersistenceUnitUtil puu = getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil();
			Class<?> clazz = puu.getClass(foundEmployee);
			if (clazz == Employee.class) {
				pass = true;
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}
		if (!pass) {
			throw new Exception("getClassTest failed");
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

	private void createTestData() throws Exception {
		logTrace( "createTestData");

		try {
			getEntityTransaction().begin();
			// logTrace("Create 20 employees");
			empRef[0] = new Employee(100, "Alan", "Frechette", getSQLDate("2000-02-14"), (float) 35000.0);

			// logTrace("Start to persist employees ");
			for (Employee e : empRef) {
				if (e != null) {
					getEntityManager().persist(e);
					logTrace( "persisted employee " + e);
				}
			}

			getEntityTransaction().commit();
			logTrace( "Created TestData");

		} catch (Exception re) {
			logErr( "Unexpected Exception in createTestData:", re);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception in createTestData while rolling back TX:", re);
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
			getEntityManager().createNativeQuery("DELETE FROM EMPLOYEE").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM MEMBER").executeUpdate();
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
