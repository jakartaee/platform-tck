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

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TransactionRequiredException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;

public class Client1 extends PMClientBase {



	Employee[] empRef = new Employee[5];

	Order[] orders = new Order[5];

	Properties props = null;

	Map map = new HashMap<String, Object>();

	Employee emp = new Employee(1, "foo", "bar", getUtilDate("2000-02-14"), (float) 35000.0);

	String dataBaseName = null;

	final static String ORACLE = "oracle";

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client1.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "DoesNotExist", pkgName + "Employee", pkgName + "Order" };
		return createDeploymentJar("jpa_core_entityManager1.jar", pkgNameWithoutSuffix, classes);

	}

	public Client1() {
	}

	/*
	 * setup() is called before each test
	 *
	 * @class.setup_props: jdbc.db;
	 */
	
	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			createDeployment();
			map.putAll(getEntityManager().getProperties());
			map.put("foo", "bar");
			displayMap(map);
			dataBaseName = System.getProperty("jdbc.db");
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logTrace( "cleanup complete, calling super.cleanup");
			super.cleanup();
		} finally {

        }
	}

	/*
	 * @testName: findExceptionsTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:465; PERSISTENCE:JAVADOC:466;
	 * PERSISTENCE:JAVADOC:467; PERSISTENCE:JAVADOC:468; PERSISTENCE:JAVADOC:474;
	 * PERSISTENCE:JAVADOC:474; PERSISTENCE:SPEC:592;
	 * 
	 * @test_Strategy: Call EntityManager.find() method with various invalid
	 * argument combinations and verify various exceptions are thrown
	 */
	@Test
	public void findExceptionsTest() throws Exception {
		int pass = 0;
		logMsg( "Testing findClassObjectIllegalArgumentException");

		logMsg( "Invalid Object test");
		try {
			getEntityTransaction().begin();
			getEntityManager().find(Client1.class, 1);
			getEntityTransaction().commit();
			logErr( "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
			logTrace( "IllegalArgumentException Caught as Expected.");
			pass += 1;
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
		logMsg( "Invalid PK test");

		try {
			getEntityTransaction().begin();
			getEntityManager().find(Order.class, "PK");
			getEntityTransaction().commit();
			logErr( "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
			logTrace( "IllegalArgumentException Caught as Expected.");
			pass += 1;
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
		logMsg( "Null PK test");

		try {
			getEntityTransaction().begin();
			getEntityManager().find(Order.class, null);
			getEntityTransaction().commit();
			logErr( "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
			logTrace( "IllegalArgumentException Caught as Expected.");
			pass += 1;
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

		logMsg( "Testing findClassObjectMapIllegalArgumentException");

		Map<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("some.cts.specific.property", "nothing.in.particular");

		logTrace( "Invalid Object test");
		try {
			getEntityTransaction().begin();
			getEntityManager().find(Client1.class, 1, myMap);
			getEntityTransaction().commit();
			logErr( "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
			logTrace( "IllegalArgumentException Caught as Expected.");
			pass += 1;
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
		logMsg( "Invalid PK test");

		try {
			getEntityTransaction().begin();
			getEntityManager().find(Order.class, "PK", myMap);
			getEntityTransaction().commit();
			logErr( "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
			logTrace( "IllegalArgumentException Caught as Expected.");
			pass += 1;
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
		logMsg( "Null PK test");

		try {
			getEntityTransaction().begin();
			getEntityManager().find(Order.class, null, myMap);
			getEntityTransaction().commit();
			logErr( "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
			logTrace( "IllegalArgumentException Caught as Expected.");
			pass += 1;
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

		logMsg( "Testing findClassObjectLockModeTypeIllegalArgumentException");

		logMsg( "Invalid Object test");
		try {
			getEntityTransaction().begin();
			getEntityManager().find(Client1.class, 1, LockModeType.NONE);
			getEntityTransaction().commit();
			logErr( "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
			logTrace( "IllegalArgumentException Caught as Expected.");
			pass += 1;
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
		logMsg( "Invalid PK test");

		try {
			getEntityTransaction().begin();
			getEntityManager().find(Order.class, "PK", LockModeType.NONE);
			getEntityTransaction().commit();
			logErr( "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
			logTrace( "IllegalArgumentException Caught as Expected.");
			pass += 1;
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
		logMsg( "Null PK test");

		try {
			getEntityTransaction().begin();
			getEntityManager().find(Order.class, null, LockModeType.NONE);
			getEntityTransaction().commit();
			logErr( "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
			logTrace( "IllegalArgumentException Caught as Expected.");
			pass += 1;
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

		logMsg( "Testing findClassObjectLockModeTypeTransactionRequiredException");
		// Make sure there is no transaction active
		try {
			if (getEntityTransaction().isActive()) {
				logTrace( "A transaction is active, execute rollback");
				getEntityTransaction().rollback();
			} else {
				logTrace( "No transaction is active");
			}
		} catch (Exception fe) {
			logErr( "Unexpected exception rolling back TX:", fe);
		}
		try {
			getEntityManager().find(Order.class, 1, LockModeType.PESSIMISTIC_READ);
			logErr( "TransactionRequiredException not thrown");
		} catch (TransactionRequiredException tre) {
			logTrace( "TransactionRequiredException Caught as Expected.");
			pass += 1;
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}

		logMsg( "Testing findClassObjectLockModeTypeMapIllegalArgumentException");

		logMsg( "Invalid Object test");
		try {
			getEntityTransaction().begin();
			getEntityManager().find(Client1.class, 1, LockModeType.NONE, myMap);
			getEntityTransaction().commit();
			logErr( "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
			logTrace( "IllegalArgumentException Caught as Expected.");
			pass += 1;
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
		logMsg( "Invalid PK test");

		try {
			getEntityTransaction().begin();
			getEntityManager().find(Order.class, "PK", LockModeType.NONE, myMap);
			getEntityTransaction().commit();
			logErr( "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
			logTrace( "IllegalArgumentException Caught as Expected.");
			pass += 1;
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
		logMsg( "Null PK test");

		try {
			getEntityTransaction().begin();
			getEntityManager().find(Order.class, null, LockModeType.NONE, myMap);
			getEntityTransaction().commit();
			logErr( "IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
			logTrace( "IllegalArgumentException Caught as Expected.");
			pass += 1;
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

		logMsg( "Testing findClassObjectLockModeTypeMapTransactionRequiredException");
		// Make sure there is no transaction active
		try {
			if (getEntityTransaction().isActive()) {
				logTrace( "A transaction is active, execute rollback");
				getEntityTransaction().rollback();
			}
		} catch (Exception fe) {
			logErr( "Unexpected exception rolling back TX:", fe);
		}
		try {
			getEntityManager().find(Order.class, 0, LockModeType.PESSIMISTIC_READ, myMap);
			logErr( "TransactionRequiredException not thrown");

		} catch (TransactionRequiredException tre) {
			logTrace( "TransactionRequiredException Caught as Expected.");
			pass += 1;
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);

		}

		if (pass != 14) {
			throw new Exception("findExceptionsTest failed");
		}
	}

	/*
	 * @testName: flushExceptionsTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:479
	 * 
	 * @test_Strategy: Call EntityManager.flush() method with various invalid
	 * argument combinations and verify various exceptions are thrown
	 */
	@Test
	public void flushExceptionsTest() throws Exception {
		boolean pass = false;
		try {

			getEntityManager().flush();
			logErr( "TransactionRequiredException was not thrown");
		} catch (TransactionRequiredException e) {
			logTrace( "TransactionRequiredException Caught as Expected.");
			pass = true;
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}

		if (!pass) {
			throw new Exception("flushExceptionsTest failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback1Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
	 * 
	 * @test_Strategy: Call EntityManager.contains(Class) that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback1Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().contains(PMClientBase.class);
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback1Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback2Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611;
	 * 
	 * @test_Strategy: Call EntityManager.createNamedQuery(String) that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback2Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().createNamedQuery("doesnotexist");
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback2Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback3Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611;
	 * 
	 * @test_Strategy: Call EntityManager.createNamedQuery(String,Class) that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback3Test() throws Exception {
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			getEntityManager().createNamedQuery("doesnotexist", PMClientBase.class);
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback3Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback4Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611;
	 * 
	 * @test_Strategy: Call EntityManager.createNamedStoredProcedureQuery(String)
	 * that causes RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback4Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().createNamedStoredProcedureQuery("doesnotexist");
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback4Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback5Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611;
	 * 
	 * @test_Strategy: Call EntityManager.createQuery(CriteriaDelete) that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback5Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
			CriteriaDelete<DoesNotExist> cd = cbuilder.createCriteriaDelete(DoesNotExist.class);
			cd.from(DoesNotExist.class);
			try {
				Query q = getEntityManager().createQuery(cd);
				logMsg( "RuntimeException wasn't thrown, try executing it");
				q.executeUpdate();
				logErr( "RuntimeException not thrown");
			} catch (RuntimeException e) {
				logTrace( "RuntimeException Caught as Expected.", e);
				if (!getEntityTransaction().getRollbackOnly()) {
					logErr( "Transaction was not marked for rollback");
				} else {
					logTrace( "Transaction was marked for rollback");
					pass = true;
				}
			} catch (Exception e) {
				logErr( "Unexpected exception occurred", e);
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred while creating CriteriaDelete", e);
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback5Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback6Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611;
	 * 
	 * @test_Strategy: Call EntityManager.createQuery(CriteriaQuery) that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback6Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<PMClientBase> cquery = cbuilder.createQuery(PMClientBase.class);
			Query q = getEntityManager().createQuery(cquery);
			logMsg( "RuntimeException wasn't thrown, try executing it");
			q.executeUpdate();
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback6Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback7Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611;
	 * 
	 * @test_Strategy: Call EntityManager.createQuery(CriteriaUpdate) that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback7Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			CriteriaBuilder cbuilder = getEntityManager().getCriteriaBuilder();

			CriteriaUpdate<DoesNotExist> cu = cbuilder.createCriteriaUpdate(DoesNotExist.class);
			Root<DoesNotExist> root = cu.from(DoesNotExist.class);
			cu.where(cbuilder.equal(root.get("id"), 1));
			cu.set(root.get("firstName"), "foobar");
			try {
				Query q = getEntityManager().createQuery(cu);
				logMsg( "RuntimeException wasn't thrown, try executing it");
				q.executeUpdate();
				logErr( "RuntimeException not thrown");
			} catch (RuntimeException e) {
				logTrace( "RuntimeException Caught as Expected.");
				if (!getEntityTransaction().getRollbackOnly()) {
					logErr( "Transaction was not marked for rollback");
				} else {
					logTrace( "Transaction was marked for rollback");
					pass = true;
				}
			} catch (Exception e) {
				logErr( "Unexpected exception occurred", e);
			}
		} catch (Exception e) {
			logErr( "Unexpected exception occurred while creating CriteriaUpdate", e);
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback7Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback8Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611;
	 * 
	 * @test_Strategy: Call EntityManager.createQuery(String) that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback8Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().createQuery("invalid");
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback8Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback9Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
	 * 
	 * @test_Strategy: Call EntityManager.createQuery(String,Class) that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback9Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().createQuery("invalid", PMClientBase.class);
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback9Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback10Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611;
	 * 
	 * @test_Strategy: Call EntityManager.createStoredProcedureQuery(String) that
	 * causes RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback10Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("doesnotexist");
			logMsg( "RuntimeException not thrown, try executing it");
			spq.execute();
			logErr( "RuntimeException was not thrown, after trying to executing it");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback10Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback11Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
	 * 
	 * @test_Strategy: Call EntityManager.createStoredProcedureQuery(String,Class)
	 * that causes RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback11Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("doesnotexist",
					PMClientBase.class);
			logMsg( "RuntimeException not thrown, try executing it");
			spq.execute();
			logErr( "RuntimeException was not thrown, after trying to executing it");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback11Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback12Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611;
	 * 
	 * @test_Strategy: Call EntityManager.createStoredProcedureQuery(String,String)
	 * that causes RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback12Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			StoredProcedureQuery spq = getEntityManager().createStoredProcedureQuery("doesnotexist", "doesnotexist");
			logMsg( "RuntimeException not thrown, try executing it");
			spq.execute();
			logErr( "RuntimeException was not thrown, after trying to executing it");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback12Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback13Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
	 * 
	 * @test_Strategy: Call EntityManager.detach(Object) that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback13Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().detach(PMClientBase.class);
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback13Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback14Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611;
	 * 
	 * @test_Strategy: Call EntityManager.find(Class,Object) that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback14Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().find(PMClientBase.class, "doesnotexist");
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback14Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback15Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611;
	 * 
	 * @test_Strategy: Call EntityManager.find(Class,Object,LockModeType) that
	 * causes RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback15Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().find(PMClientBase.class, "doesnotexist", LockModeType.NONE);
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback15Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback16Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611;
	 * 
	 * @test_Strategy: Call EntityManager.find(Class,Object,LockModeType,Map) that
	 * causes RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback16Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().find(PMClientBase.class, "doesnotexist", LockModeType.NONE, map);
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback16Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback17Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611;
	 * 
	 * @test_Strategy: Call EntityManager.find(Class,Object,Map) that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback17Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().find(PMClientBase.class, "doesnotexist", map);
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback17Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback21Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611;
	 * 
	 * @test_Strategy: Call EntityManager. getLockMode(Object) that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback21Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().getLockMode(PMClientBase.class);
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback21Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback23Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611;
	 * 
	 * @test_Strategy: Call EntityManager.getReference(Class,Object) that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback23Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().getReference(PMClientBase.class, "doesnotexist");
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback23Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback24Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
	 * 
	 * @test_Strategy: Call EntityManager.lock(Object,LockModeType) that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback24Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().lock(PMClientBase.class, LockModeType.NONE);
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback24Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback25Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611;
	 * 
	 * @test_Strategy: Call EntityManager.lock(Object,LockModeType,Map() that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback25Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().lock(PMClientBase.class, LockModeType.NONE, map);
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback25Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback26Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
	 * 
	 * @test_Strategy: Call EntityManager.merge(Class) that causes RuntimeException
	 * and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback26Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().merge(PMClientBase.class);
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback26Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback27Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
	 * 
	 * @test_Strategy: Call EntityManager.persist(Object) that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback27Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().persist(PMClientBase.class);
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback27Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback28Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
	 * 
	 * @test_Strategy: Call EntityManager.refresh(Object) that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback28Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().refresh(PMClientBase.class);
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback28Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback29Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
	 * 
	 * @test_Strategy: Call EntityManager.refresh(Object,LockModeType) that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback29Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().refresh(PMClientBase.class, LockModeType.NONE);
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback29Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback30Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
	 * 
	 * @test_Strategy: Call EntityManager.refresh(Object,LockModeType,Map) that
	 * causes RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback30Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().refresh(PMClientBase.class, LockModeType.NONE, map);
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback30Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback31Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
	 * 
	 * @test_Strategy: Call EntityManager.refresh(Object,Map) that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback31Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().refresh(PMClientBase.class, map);
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback31Test failed");
		}
	}

	/*
	 * @testName: entityManagerMethodsRuntimeExceptionsCauseRollback32Test
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:611; PERSISTENCE:SPEC:592;
	 * 
	 * @test_Strategy: Call EntityManager.remove(Object) that causes
	 * RuntimeException and verify Transaction is set for rollback
	 */
	@Test
	public void entityManagerMethodsRuntimeExceptionsCauseRollback32Test() throws Exception {
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().remove(PMClientBase.class);
			logErr( "RuntimeException not thrown");
		} catch (RuntimeException e) {
			logTrace( "RuntimeException Caught as Expected.");
			if (!getEntityTransaction().getRollbackOnly()) {
				logErr( "Transaction was not marked for rollback");
			} else {
				logTrace( "Transaction was marked for rollback");
				pass = true;
			}
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
			throw new Exception("entityManagerMethodsRuntimeExceptionsCauseRollback32Test failed");
		}

	}
}
