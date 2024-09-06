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

package ee.jakarta.tck.persistence.core.exceptions;



import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ee.jakarta.tck.persistence.common.PMClientBase;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.PessimisticLockException;
import jakarta.persistence.Query;
import jakarta.persistence.QueryTimeoutException;
import jakarta.persistence.RollbackException;
import jakarta.persistence.TransactionRequiredException;

public class Client extends PMClientBase {



	public Client() {
	}
	public static void main(String[] args) {
		Client theTests = new Client();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	public JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = Client.class.getPackageName();
		String pkgName = pkgNameWithoutSuffix + ".";
		String[] classes = { pkgName + "Coffee" };
		return createDeploymentJar("jpa_core_exceptions.jar", pkgNameWithoutSuffix, classes);

	}


	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			createDeployment();

			logTrace( "Cleanup data");
			removeTestData();
		} catch (Exception e) {
			logErr( "Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: TransactionRequiredExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:606; PERSISTENCE:JAVADOC:220;
	 * JavaEE:SPEC:10006;
	 * 
	 * @test_Strategy: persist throws a TransactionRequiredException if invoked on a
	 * container-managed entity manager of type PersistenceContextType.TRANSACTION
	 * and there is no transaction
	 *
	 * When an EntityManager with an extended persistence context is used (as in
	 * Java SE environments) the persist operation may be called regardless whether
	 * a transaction is active.
	 */
	@Test
	public void TransactionRequiredExceptionTest() throws Exception {
		boolean pass = false;
		final Coffee newCoffee = new Coffee(1, "hazelnut", 1.0F);

		try {
			logTrace( "Invoked persist without an active transaction");
			getEntityManager().persist(newCoffee);

		} catch (jakarta.persistence.TransactionRequiredException tre) {
			pass = true;
			logTrace( "In JavaEE, Exception Caught as Expected: " + tre);
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		}

		if ((!pass) && (isStandAloneMode())) {
			logTrace( "In JavaSE, Exception Not Thrown as Expected");
			pass = true;
		}

		if (!pass)
			throw new Exception("TransactionRequiredExceptionTest failed");
	}

	/*
	 * @testName: TransactionRequiredException2Test
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:219; PERSISTENCE:JAVADOC:157
	 * 
	 * @test_Strategy: create and throw TransactionRequiredException() and validate
	 * contents of Exception
	 */
	@Test
	public void TransactionRequiredException2Test() throws Exception {

		logTrace( "Test TransactionRequiredExceptionNullMsg");
		boolean pass = true;

		try {
			throw new TransactionRequiredException();

		} catch (TransactionRequiredException eee) {
			logTrace( "TransactionRequiredException Caught as Expected");
			if (eee.getMessage() != null) {
				logErr(
						"TransactionRequiredException should have had null message, actual message="
								+ eee.getMessage());
				pass = false;
			}
		} catch (Exception e) {
			logErr( "Unexpected Exception Caught", e);
			pass = false;
		}

		logMsg( "Test TransactionRequiredExceptionStringMsg");

		String expected = "This is the String message";
		try {
			throw new TransactionRequiredException(expected);

		} catch (TransactionRequiredException eee) {
			logTrace( "TransactionRequiredException Caught as Expected");
			String msg = eee.getMessage();
			if (msg != null) {
				if (!msg.equals(expected)) {
					logErr( "Expected=" + msg + ", Actual=" + msg);
					pass = false;
				}
			} else {
				logErr( "TransactionRequiredException returned null message");
				pass = false;
			}
		} catch (Exception e) {
			logErr( "Unexpected Exception Caught", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("TransactionRequiredException2Test failed");
	}

	/*
	 * @testName: exceptionTest2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:606; PERSISTENCE:SPEC:738;
	 * PERSISTENCE:SPEC:740; JavaEE:SPEC:10006
	 * 
	 * @test_Strategy: flush() throws a
	 * jakarta.persistence.TransactionRequiredException if there is no transaction
	 */
	@Test
	public void exceptionTest2() throws Exception {

		logTrace( "Begin exceptionTest2");
		final Coffee newCoffee = new Coffee(2, "french roast", 9.0F);
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			getEntityManager().persist(newCoffee);
			getEntityTransaction().commit();

			logTrace( "try flush");
			getEntityManager().flush();

		} catch (jakarta.persistence.TransactionRequiredException tre) {
			pass = true;
			logTrace( "Exception Caught as Expected: " + tre);
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				logErr( "Unexpected Exception :", re);
			}

		}

		if (!pass)
			throw new Exception("exceptionTest2 failed");

	}

	/*
	 * @testName: exceptionTest3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:606; JavaEE:SPEC:10006
	 * 
	 * @test_Strategy: refresh throws a TransactionRequiredException if invoked on a
	 * container-managed entity manager of type PersistenceContextType.TRANSACTION
	 * and there is no transaction
	 *
	 * When an EntityManager with an extended persistence context is used (as in
	 * Java SE environments) the refresh operation may be called regardless whether
	 * a transaction is active.
	 */
	@Test
	public void exceptionTest3() throws Exception {

		logTrace( "Begin exceptionTest3");
		boolean pass = false;
		final Coffee newCoffee = new Coffee(3, "french roast", 9.0F);

		try {

			getEntityTransaction().begin();
			logTrace( "Persist Coffee ");
			getEntityManager().persist(newCoffee);
			getEntityTransaction().commit();

			logTrace( "Call refresh without an active transaction");
			getEntityManager().refresh(newCoffee);

		} catch (TransactionRequiredException tre) {
			pass = true;
			logTrace( "Exception Caught as Expected: " + tre);
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}

			} catch (Exception fe) {
				logErr( "Unexpected Exception Caught rolling back TX:", fe);
			}
		}

		if ((!pass) && (isStandAloneMode())) {
			logTrace( "In JavaSE, Exception Not Thrown as Expected");
			pass = true;
		}

		if (!pass)
			throw new Exception("exceptionTest3 failed");
	}

	/*
	 * @testName: exceptionTest4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:606; JavaEE:SPEC:10006
	 * 
	 * @test_Strategy: remove throws a TransactionRequiredException if there invoked
	 * on a container-managed entity manager of type
	 * PersistenceContextType.TRANSACTION and there is no transaction
	 *
	 * When an EntityManager with an extended persistence context is used (as in
	 * Java SE environments) the remove operation may be called regardless whether a
	 * transaction is active.
	 * 
	 */
	@Test
	public void exceptionTest4() throws Exception {

		logTrace( "Begin exceptionTest4");
		final Coffee newCoffee = new Coffee(5, "breakfast blend", 3.0F);
		boolean pass = false;

		try {
			getEntityTransaction().begin();
			getEntityManager().persist(newCoffee);
			getEntityTransaction().commit();

			logTrace( "Call remove without an active transaction");
			getEntityManager().remove(newCoffee);

		} catch (jakarta.persistence.TransactionRequiredException tre) {
			pass = true;
			logTrace( "Exception Caught as Expected: " + tre);
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}

			} catch (Exception fe) {
				logErr( "Unexpected Exception Caught rolling back TX:", fe);
			}

		}

		if ((!pass) && (isStandAloneMode())) {
			logTrace( "In JavaSE, Exception Not Thrown as Expected");
			pass = true;
		}

		if (!pass)
			throw new Exception("exceptionTest4 failed");
	}

	/*
	 * @testName: exceptionTest5
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:606; PERSISTENCE:JAVADOC:36;
	 * JavaEE:SPEC:10006
	 * 
	 * @test_Strategy: close throws an IllegalStateException will be thrown if the
	 * EntityManager is container-managed.
	 */
	@Test
	public void exceptionTest5() throws Exception {

		logTrace( "Begin exceptionTest5");
		boolean pass = false;
		try {
			getEntityTransaction().begin();
			getEntityManager().close();
			getEntityTransaction().commit();

		} catch (IllegalStateException ise) {
			pass = true;
			logTrace( "Exception Caught as Expected: " + ise);
		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}

			} catch (Exception fe) {
				logErr( "Unexpected Exception Caught rolling back TX:", fe);
			}

		}

		if ((!pass) && (isStandAloneMode())) {
			logTrace( "In JavaSE, Exception Not Thrown as Expected");
			pass = true;
		}

		if (!pass)
			throw new Exception("exceptionTest5 failed");
	}

	/*
	 * @testName: exceptionTest6
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:606; JavaEE:SPEC:10006
	 * 
	 * @test_Strategy: refresh throws an IllegalArgumentException if the entity is
	 * not managed
	 */
	@Test
	public void exceptionTest6() throws Exception {

		logTrace( "Begin exceptionTest6");
		boolean pass = false;
		final Coffee newCoffee = new Coffee(7, "cinnamon", 7.0F);

		try {
			getEntityTransaction().begin();
			getEntityManager().persist(newCoffee);
			clearCache();

			if (!getEntityManager().contains(newCoffee)) {
				getEntityManager().refresh(newCoffee);
			} else {
				logTrace( "Entity is managed, cannot proceed with test");
			}
			getEntityTransaction().commit();

		} catch (IllegalArgumentException iae) {
			pass = true;
			logTrace( "Exception Caught as Expected: " + iae);
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

		if (!pass)
			throw new Exception("exceptionTest6 failed");
	}

	/*
	 * @testName: RollbackExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:184; PERSISTENCE:JAVADOC:185;
	 * PERSISTENCE:JAVADOC:186; PERSISTENCE:JAVADOC:187
	 * 
	 * @test_Strategy: create and throw RollbackException() and validate contents of
	 * Exception
	 */
	@Test
	public void RollbackExceptionTest() throws Exception {

		logMsg( "Test RollbackExceptionNullMsg");
		boolean pass = true;

		try {
			throw new RollbackException();
		} catch (RollbackException re) {
			logTrace( "RollbackException Caught as Expected");
			if (re.getMessage() != null) {
				pass = false;
				logErr(
						"RollbackException should have had null message, actual message=" + re.getMessage());
			}
		} catch (Exception e) {
			logErr( "Unexpected Exception Caught", e);
			pass = false;
		}

		logMsg( "Test RollbackExceptionStringMsg");

		String expected = "This is the String message";
		try {
			throw new RollbackException(expected);
		} catch (RollbackException re) {
			logTrace( "RollbackException Caught as Expected");
			String msg = re.getMessage();
			if (msg != null) {
				if (!msg.equals(expected)) {
					pass = false;
					logErr( "Expected=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr( "RollbackException returned null message");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test RollbackExceptionStringThrowable");

		expected = "This is the String message";
		String expected2 = "This is the Throwable message";

		try {
			throw new RollbackException(expected, new IllegalAccessException(expected2));

		} catch (RollbackException re) {
			logTrace( "RollbackException Caught as Expected");
			String msg = re.getMessage();
			if (msg != null) {
				if (msg.equals(expected)) {
					Throwable t = re.getCause();
					if (t instanceof IllegalAccessException) {
						msg = t.getMessage();
						if (!msg.equals(expected2)) {
							pass = false;
							logErr( "Expected Throwable msg=" + msg + ", Actual=" + msg);
						}
					} else {
						pass = false;
						logErr(
								"getCause did not return an instance of IllegalAccessException, instead got " + t);
					}

				} else {
					pass = false;
					logErr( "Expected=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr( "RollbackException returned null message");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test RollbackExceptionThrowable");
		expected = "This is the Throwable message";

		try {
			throw new RollbackException(new IllegalAccessException(expected));

		} catch (RollbackException re) {
			logTrace( "RollbackException Caught as Expected");
			Throwable t = re.getCause();
			if (t instanceof IllegalAccessException) {
				String msg = t.getMessage();
				if (!msg.equals(expected)) {
					pass = false;
					logErr( "Expected Throwable msg=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr(
						"getCause did not return an instance of IllegalAccessException, instead got " + t);
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		if (!pass)
			throw new Exception("RollbackExceptionTest failed");
	}

	/*
	 * @testName: EntityExistsExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:30; PERSISTENCE:JAVADOC:31;
	 * PERSISTENCE:JAVADOC:32; PERSISTENCE:JAVADOC:33
	 * 
	 * @test_Strategy: create and throw EntityExistsException() and validate
	 * contents of Exception
	 */
	@Test
	public void EntityExistsExceptionTest() throws Exception {

		logMsg( "Test EntityExistsExceptionNullMsg");
		boolean pass = true;

		try {
			throw new EntityExistsException();

		} catch (EntityExistsException eee) {
			logTrace( "EntityExistsException Caught as Expected");
			if (eee.getMessage() != null) {
				pass = false;
				logErr(
						"EntityExistsException should have had null message, actual message=" + eee.getMessage());
			}
		} catch (Exception e) {
			logErr( "Unexpected Exception Caught", e);
			pass = false;
		}

		logMsg( "Test EntityExistskExceptionStringMsg");

		String expected = "This is the String message";
		try {
			throw new EntityExistsException(expected);

		} catch (EntityExistsException eee) {
			logTrace( "EntityExistsException Caught as Expected");
			String msg = eee.getMessage();
			if (msg != null) {
				if (!msg.equals(expected)) {
					pass = false;
					logErr( "Expected=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr( "EntityExistsException returned null message");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test EntityExistsExceptionStringThrowable");

		expected = "This is the String message";
		String expected2 = "This is the Throwable message";

		try {
			throw new EntityExistsException(expected, new IllegalAccessException(expected2));

		} catch (EntityExistsException eee) {
			logTrace( "EntityExistsException Caught as Expected");
			String msg = eee.getMessage();
			if (msg != null) {
				if (msg.equals(expected)) {
					Throwable t = eee.getCause();
					if (t instanceof IllegalAccessException) {
						msg = t.getMessage();
						if (!msg.equals(expected2)) {
							pass = false;
							logErr( "Expected Throwable msg=" + msg + ", Actual=" + msg);
						}
					} else {
						pass = false;
						logErr(
								"getCause did not return an instance of IllegalAccessException, instead got " + t);
					}

				} else {
					pass = false;
					logErr( "Expected=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr( "EntityExistsException returned null message");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test EntityExistsExceptionThrowable");

		expected = "This is the Throwable message";

		try {
			throw new EntityExistsException(new IllegalAccessException(expected));

		} catch (EntityExistsException eee) {
			logTrace( "EntityExistsException Caught as Expected");
			Throwable t = eee.getCause();
			if (t instanceof IllegalAccessException) {
				String msg = t.getMessage();
				if (!msg.equals(expected)) {
					pass = false;
					logErr( "Expected Throwable msg=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr(
						"getCause did not return an instance of IllegalAccessException, instead got " + t);
			}

		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		if (!pass)
			throw new Exception("EntityExistsExceptionTest failed");
	}

	/*
	 * @testName: EntityNotFoundExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:61; PERSISTENCE:JAVADOC:62
	 * 
	 * @test_Strategy: create and throw EntityNotFoundException() and validate
	 * contents of Exception
	 */
	@Test
	public void EntityNotFoundExceptionTest() throws Exception {

		logMsg( "Test EntityNotFoundExceptionNullMsg");
		boolean pass = true;

		try {
			throw new EntityNotFoundException();

		} catch (EntityNotFoundException enf) {
			logTrace( "EntityNotFoundException Caught as Expected");
			if (enf.getMessage() != null) {
				pass = false;
				logErr(
						"EntityNotFoundException should have had null message, actual message=" + enf.getMessage());
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test EntityNotFoundExceptionStringMsg");

		String expected = "This is the String message";
		try {
			throw new EntityNotFoundException(expected);

		} catch (EntityNotFoundException enf) {
			logTrace( "EntityNotFoundException Caught as Expected");
			String msg = enf.getMessage();
			if (msg != null) {
				if (!msg.equals(expected)) {
					pass = false;
					logErr( "Expected=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr( "EntityNotFoundException returned null message");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		if (!pass)
			throw new Exception("EntityNotFoundExceptionTest failed");
	}

	/*
	 * @testName: OptimisticLockExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:139; PERSISTENCE:JAVADOC:140;
	 * PERSISTENCE:JAVADOC:141; PERSISTENCE:JAVADOC:142; PERSISTENCE:JAVADOC:143;
	 * PERSISTENCE:JAVADOC:138; PERSISTENCE:JAVADOC:144
	 * 
	 * @test_Strategy: create and throw OptimisticLockException() and validate
	 * contents of Exception
	 */
	@Test
	public void OptimisticLockExceptionTest() throws Exception {

		logMsg( "Test OptimisticLockExceptionNullMsg");
		boolean pass = true;

		try {
			throw new OptimisticLockException();

		} catch (OptimisticLockException ole) {
			logTrace( "OptimisticLockException Caught as Expected");
			if (ole.getMessage() != null) {
				pass = false;
				logErr(
						"OptimisticLockException should have had null message, actual message=" + ole.getMessage());
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test OptimisticLockExceptionStringMsg");

		String expected = "This is the String message";
		try {
			throw new OptimisticLockException(expected);

		} catch (OptimisticLockException ole) {
			logTrace( "OptimisticLockException Caught as Expected");
			String msg = ole.getMessage();
			if (msg != null) {
				if (!msg.equals(expected)) {
					pass = false;
					logErr( "Expected=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr( "OptimisticLockException returned null message");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test OptimisticLockExceptionStringThrowable");

		expected = "This is the String message";
		String expected2 = "This is the Throwable message";

		try {
			throw new OptimisticLockException(expected, new IllegalAccessException(expected2));

		} catch (OptimisticLockException ole) {
			logTrace( "OptimisticLockException Caught as Expected");
			String msg = ole.getMessage();
			if (msg != null) {
				if (msg.equals(expected)) {
					Throwable t = ole.getCause();
					if (t instanceof IllegalAccessException) {
						msg = t.getMessage();
						if (!msg.equals(expected2)) {
							pass = false;
							logErr( "Expected Throwable msg=" + msg + ", Actual=" + msg);
						}
					} else {
						pass = false;
						logErr(
								"getCause did not return an instance of IllegalAccessException, instead got " + t);
					}

				} else {
					pass = false;
					logErr( "Expected=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr( "OptimisticLockException returned null message");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test OptimisticLockExceptionThrowable");

		expected = "This is the Throwable message";

		try {
			throw new OptimisticLockException(new IllegalAccessException(expected));

		} catch (OptimisticLockException ole) {
			logTrace( "OptimisticLockException Caught as Expected");
			Throwable t = ole.getCause();
			if (t instanceof IllegalAccessException) {
				String msg = t.getMessage();
				if (!msg.equals(expected)) {
					pass = false;
					logErr( "Expected Throwable msg=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr(
						"getCause did not return an instance of IllegalAccessException, instead got " + t);
			}

		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test OptimisticLockExceptionObjectMsg");

		Coffee cExpected = new Coffee(1, "hazelnut", 1.0F);

		try {
			throw new OptimisticLockException(cExpected);

		} catch (OptimisticLockException ol) {
			logTrace( "OptimisticLockException Caught as Expected");
			Coffee c = (Coffee) ol.getEntity();
			if (c != null) {
				if (!c.equals(cExpected)) {
					pass = false;
					logErr( "Expected=" + cExpected + ", Actual=" + c);
				}
			} else {
				pass = false;
				logErr( "OptimisticLockException returned null message");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test OptimisticLockExceptionStringThrowableObject");

		expected = "This is the String message";
		expected2 = "This is the Throwable message";
		cExpected = new Coffee(1, "hazelnut", 1.0F);

		try {
			throw new OptimisticLockException(expected, new IllegalAccessException(expected2), cExpected);

		} catch (OptimisticLockException ole) {
			logTrace( "OptimisticLockException Caught as Expected");
			String msg = ole.getMessage();
			if (msg != null) {
				if (msg.equals(expected)) {
					Throwable t = ole.getCause();
					if (t instanceof IllegalAccessException) {
						msg = t.getMessage();
						if (msg.equals(expected2)) {
							Coffee c = (Coffee) ole.getEntity();
							if (!c.equals(cExpected)) {
								pass = false;
								logErr( "Expected Entity=" + cExpected + ", Actual=" + c);
							}
						} else {
							pass = false;
							logErr( "Expected Throwable msg=" + msg + ", Actual=" + msg);
						}
					} else {
						pass = false;
						logErr(
								"getCause did not return an instance of IllegalAccessException, instead got " + t);
					}

				} else {
					pass = false;
					logErr( "Expected=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr( "OptimisticLockException returned null message");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		if (!pass)
			throw new Exception("OptimisticLockExceptionTest failed");
	}

	/*
	 * @testName: PersistenceExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:156; PERSISTENCE:JAVADOC:157;
	 * PERSISTENCE:JAVADOC:158; PERSISTENCE:JAVADOC:159
	 * 
	 * @test_Strategy: create and throw PersistenceException() and validate contents
	 * of Exception
	 */
	@Test
	public void PersistenceExceptionTest() throws Exception {

		logMsg( "Test PersistenceExceptionNullMsg");
		boolean pass = true;

		try {
			throw new PersistenceException();

		} catch (PersistenceException eee) {
			logTrace( "PersistenceException Caught as Expected");
			if (eee.getMessage() != null) {
				pass = false;
				logErr(
						"PersistenceException should have had null message, actual message=" + eee.getMessage());
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test EntityExistskExceptionStringMsg");

		String expected = "This is the String message";
		try {
			throw new PersistenceException(expected);

		} catch (PersistenceException eee) {
			logTrace( "PersistenceException Caught as Expected");
			String msg = eee.getMessage();
			if (msg != null) {
				if (!msg.equals(expected)) {
					pass = false;
					logErr( "Expected=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr( "PersistenceException returned null message");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test PersistenceExceptionStringThrowable");

		expected = "This is the String message";
		String expected2 = "This is the Throwable message";

		try {
			throw new PersistenceException(expected, new IllegalAccessException(expected2));

		} catch (PersistenceException eee) {
			logTrace( "PersistenceException Caught as Expected");
			String msg = eee.getMessage();
			if (msg != null) {
				if (msg.equals(expected)) {
					Throwable t = eee.getCause();
					if (t instanceof IllegalAccessException) {
						msg = t.getMessage();
						if (!msg.equals(expected2)) {
							pass = false;
							logErr( "Expected Throwable msg=" + msg + ", Actual=" + msg);
						}
					} else {
						pass = false;
						logErr(
								"getCause did not return an instance of IllegalAccessException, instead got " + t);
					}

				} else {
					pass = false;
					logErr( "Expected=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr( "PersistenceException returned null message");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test PersistenceExceptionThrowable");

		expected = "This is the Throwable message";

		try {
			throw new PersistenceException(new IllegalAccessException(expected));

		} catch (PersistenceException eee) {
			logTrace( "PersistenceException Caught as Expected");
			Throwable t = eee.getCause();
			if (t instanceof IllegalAccessException) {
				String msg = t.getMessage();
				if (!msg.equals(expected)) {
					pass = false;
					logErr( "Expected Throwable msg=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr(
						"getCause did not return an instance of IllegalAccessException, instead got " + t);
			}

		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		if (!pass)
			throw new Exception("PersistenceExceptionTest failed");
	}

	/*
	 * @testName: LockTimeoutExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:344; PERSISTENCE:JAVADOC:345;
	 * PERSISTENCE:JAVADOC:346; PERSISTENCE:JAVADOC:347; PERSISTENCE:JAVADOC:348;
	 * PERSISTENCE:JAVADOC:343; PERSISTENCE:JAVADOC:349
	 * 
	 * @test_Strategy: create and throw LockTimeoutException() and validate contents
	 * of Exception
	 */
	@Test
	public void LockTimeoutExceptionTest() throws Exception {

		logMsg( "Test LockTimeoutExceptionNullMsg");
		boolean pass = true;

		try {
			throw new LockTimeoutException();

		} catch (LockTimeoutException ole) {
			logTrace( "LockTimeoutException Caught as Expected");
			if (ole.getMessage() != null) {
				pass = false;
				logErr(
						"LockTimeoutException should have had null message, actual message=" + ole.getMessage());
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test LockTimeoutExceptionStringMsg");

		String expected = "This is the String message";
		try {
			throw new LockTimeoutException(expected);

		} catch (LockTimeoutException ole) {
			logTrace( "LockTimeoutException Caught as Expected");
			String msg = ole.getMessage();
			if (msg != null) {
				if (!msg.equals(expected)) {
					pass = false;
					logErr( "Expected=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr( "LockTimeoutException returned null message");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test LockTimeoutExceptionStringThrowable");

		expected = "This is the String message";
		String expected2 = "This is the Throwable message";

		try {
			throw new LockTimeoutException(expected, new IllegalAccessException(expected2));

		} catch (LockTimeoutException ole) {
			logTrace( "LockTimeoutException Caught as Expected");
			String msg = ole.getMessage();
			if (msg != null) {
				if (msg.equals(expected)) {
					Throwable t = ole.getCause();
					if (t instanceof IllegalAccessException) {
						msg = t.getMessage();
						if (!msg.equals(expected2)) {
							pass = false;
							logErr( "Expected Throwable msg=" + msg + ", Actual=" + msg);
						}
					} else {
						pass = false;
						logErr(
								"getCause did not return an instance of IllegalAccessException, instead got " + t);
					}

				} else {
					pass = false;
					logErr( "Expected=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr( "LockTimeoutException returned null message");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test LockTimeoutExceptionThrowable");

		expected = "This is the Throwable message";

		try {
			throw new LockTimeoutException(new IllegalAccessException(expected));

		} catch (LockTimeoutException ole) {
			logTrace( "LockTimeoutException Caught as Expected");
			Throwable t = ole.getCause();
			if (t instanceof IllegalAccessException) {
				String msg = t.getMessage();
				if (!msg.equals(expected)) {
					pass = false;
					logErr( "Expected Throwable msg=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr(
						"getCause did not return an instance of IllegalAccessException, instead got " + t);
			}

		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test LockTimeoutExceptionObjectMsg");

		Coffee cExpected = new Coffee(1, "hazelnut", 1.0F);

		try {
			throw new LockTimeoutException(cExpected);

		} catch (LockTimeoutException ol) {
			logTrace( "LockTimeoutException Caught as Expected");
			Coffee c = (Coffee) ol.getObject();
			if (c != null) {
				if (!c.equals(cExpected)) {
					pass = false;
					logErr( "Expected=" + cExpected + ", Actual=" + c);
				}
			} else {
				pass = false;
				logErr( "LockTimeoutException returned null message");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test LockTimeoutExceptionStringThrowableObject");

		expected = "This is the String message";
		expected2 = "This is the Throwable message";
		cExpected = new Coffee(1, "hazelnut", 1.0F);

		try {
			throw new LockTimeoutException(expected, new IllegalAccessException(expected2), cExpected);

		} catch (LockTimeoutException ole) {
			logTrace( "LockTimeoutException Caught as Expected");
			String msg = ole.getMessage();
			if (msg != null) {
				if (msg.equals(expected)) {
					Throwable t = ole.getCause();
					if (t instanceof IllegalAccessException) {
						msg = t.getMessage();
						if (msg.equals(expected2)) {
							Coffee c = (Coffee) ole.getObject();
							if (!c.equals(cExpected)) {
								pass = false;
								logErr( "Expected Entity=" + cExpected + ", Actual=" + c);
							}
						} else {
							pass = false;
							logErr( "Expected Throwable msg=" + msg + ", Actual=" + msg);
						}
					} else {
						pass = false;
						logErr(
								"getCause did not return an instance of IllegalAccessException, instead got " + t);
					}

				} else {
					pass = false;
					logErr( "Expected=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr( "LockTimeoutException returned null message");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		if (!pass)
			throw new Exception("LockTimeoutExceptionTest failed");
	}

	/*
	 * @testName: PessimisticLockExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:391; PERSISTENCE:JAVADOC:392;
	 * PERSISTENCE:JAVADOC:393; PERSISTENCE:JAVADOC:394; PERSISTENCE:JAVADOC:395;
	 * PERSISTENCE:JAVADOC:390; PERSISTENCE:JAVADOC:396
	 * 
	 * @test_Strategy: create and throw PessimisticLockException() and validate
	 * contents of Exception
	 */
	@Test
	public void PessimisticLockExceptionTest() throws Exception {

		logMsg( "Test PessimisticLockExceptionNullMsg");
		boolean pass = true;

		try {
			throw new PessimisticLockException();

		} catch (PessimisticLockException ole) {
			logTrace( "PessimisticLockException Caught as Expected");
			if (ole.getMessage() != null) {
				pass = false;
				logErr(
						"PessimisticLockException should have had null message, actual message=" + ole.getMessage());
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test PessimisticLockExceptionStringMsg");

		String expected = "This is the String message";
		try {
			throw new PessimisticLockException(expected);

		} catch (PessimisticLockException ole) {
			logTrace( "PessimisticLockException Caught as Expected");
			String msg = ole.getMessage();
			if (msg != null) {
				if (!msg.equals(expected)) {
					pass = false;
					logErr( "Expected=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr( "PessimisticLockException returned null message");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test PessimisticLockExceptionStringThrowable");

		expected = "This is the String message";
		String expected2 = "This is the Throwable message";

		try {
			throw new PessimisticLockException(expected, new IllegalAccessException(expected2));

		} catch (PessimisticLockException ole) {
			logTrace( "PessimisticLockException Caught as Expected");
			String msg = ole.getMessage();
			if (msg != null) {
				if (msg.equals(expected)) {
					Throwable t = ole.getCause();
					if (t instanceof IllegalAccessException) {
						msg = t.getMessage();
						if (!msg.equals(expected2)) {
							pass = false;
							logErr( "Expected Throwable msg=" + msg + ", Actual=" + msg);
						}
					} else {
						pass = false;
						logErr(
								"getCause did not return an instance of IllegalAccessException, instead got " + t);
					}

				} else {
					pass = false;
					logErr( "Expected=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr( "PessimisticLockException returned null message");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test PessimisticLockExceptionThrowable");

		expected = "This is the Throwable message";

		try {
			throw new PessimisticLockException(new IllegalAccessException(expected));

		} catch (PessimisticLockException ole) {
			logTrace( "PessimisticLockException Caught as Expected");
			Throwable t = ole.getCause();
			if (t instanceof IllegalAccessException) {
				String msg = t.getMessage();
				if (!msg.equals(expected)) {
					pass = false;
					logErr( "Expected Throwable msg=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr(
						"getCause did not return an instance of IllegalAccessException, instead got " + t);
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test PessimisticLockExceptionObjectMsg");

		Coffee cExpected = new Coffee(1, "hazelnut", 1.0F);

		try {
			throw new PessimisticLockException(cExpected);

		} catch (PessimisticLockException ol) {
			logTrace( "PessimisticLockException Caught as Expected");
			Coffee c = (Coffee) ol.getEntity();
			if (c != null) {
				if (!c.equals(cExpected)) {
					pass = false;
					logErr( "Expected=" + cExpected + ", Actual=" + c);
				}
			} else {
				pass = false;
				logErr( "PessimisticLockException returned null message");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		logMsg( "Test PessimisticLockExceptionStringThrowableObject");

		expected = "This is the String message";
		expected2 = "This is the Throwable message";
		cExpected = new Coffee(1, "hazelnut", 1.0F);

		try {
			throw new PessimisticLockException(expected, new IllegalAccessException(expected2), cExpected);

		} catch (PessimisticLockException ole) {
			logTrace( "PessimisticLockException Caught as Expected");
			String msg = ole.getMessage();
			if (msg != null) {
				if (msg.equals(expected)) {
					Throwable t = ole.getCause();
					if (t instanceof IllegalAccessException) {
						msg = t.getMessage();
						if (msg.equals(expected2)) {
							Coffee c = (Coffee) ole.getEntity();
							if (!c.equals(cExpected)) {
								pass = false;
								logErr( "Expected Entity=" + cExpected + ", Actual=" + c);
							}
						} else {
							pass = false;
							logErr( "Expected Throwable msg=" + msg + ", Actual=" + msg);
						}
					} else {
						pass = false;
						logErr(
								"getCause did not return an instance of IllegalAccessException, instead got " + t);
					}

				} else {
					pass = false;
					logErr( "Expected=" + msg + ", Actual=" + msg);
				}
			} else {
				pass = false;
				logErr( "PessimisticLockException returned null message");
			}
		} catch (Exception e) {
			pass = false;
			logErr( "Unexpected Exception Caught", e);
		}

		if (!pass)
			throw new Exception("PessimisticLockExceptionTest failed");
	}

	/*
	 * @testName: QueryTimeoutExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:419; PERSISTENCE:JAVADOC:420;
	 * PERSISTENCE:JAVADOC:421; PERSISTENCE:JAVADOC:422; PERSISTENCE:JAVADOC:423;
	 * PERSISTENCE:JAVADOC:418; PERSISTENCE:JAVADOC:424
	 * 
	 * @test_Strategy: create and throw QueryTimeoutException() and validate
	 * contents of Exception
	 */
	@Test
	public void QueryTimeoutExceptionTest() throws Exception {

		logMsg( "Begin QueryTimeoutExceptionNullMsgTest");
		boolean pass = true;

		try {
			throw new QueryTimeoutException();

		} catch (QueryTimeoutException ole) {
			logTrace( "QueryTimeoutException Caught as Expected");
			if (ole.getMessage() != null) {
				logErr(
						"QueryTimeoutException should have had null message, actual message=" + ole.getMessage());
				pass = false;
			}
		}

		logMsg( "Test QueryTimeoutExceptionStringMsg");

		String expected = "This is the String message";
		try {
			throw new QueryTimeoutException(expected);

		} catch (QueryTimeoutException ole) {
			logTrace( "QueryTimeoutException Caught as Expected");
			String msg = ole.getMessage();
			if (msg != null) {
				if (!msg.equals(expected)) {
					logErr( "Expected=" + msg + ", Actual=" + msg);
					pass = false;
				}
			} else {
				logErr( "QueryTimeoutException returned null message");
				pass = false;
			}
		}

		logMsg( "Test QueryTimeoutExceptionStringThrowable");

		expected = "This is the String message";
		String expected2 = "This is the Throwable message";

		try {
			throw new QueryTimeoutException(expected, new IllegalAccessException(expected2));

		} catch (QueryTimeoutException ole) {
			logTrace( "QueryTimeoutException Caught as Expected");
			String msg = ole.getMessage();
			if (msg != null) {
				if (msg.equals(expected)) {
					Throwable t = ole.getCause();
					if (t instanceof IllegalAccessException) {
						msg = t.getMessage();
						if (!msg.equals(expected2)) {
							logErr( "Expected Throwable msg=" + msg + ", Actual=" + msg);
							pass = false;
						}
					} else {
						logErr(
								"getCause did not return an instance of IllegalAccessException, instead got " + t);
						pass = false;
					}

				} else {
					logErr( "Expected=" + msg + ", Actual=" + msg);
					pass = false;
				}
			} else {
				logErr( "QueryTimeoutException returned null message");
				pass = false;
			}
		}

		logMsg( "Test QueryTimeoutExceptionThrowable");
		expected = "This is the Throwable message";

		try {
			throw new QueryTimeoutException(new IllegalAccessException(expected));

		} catch (QueryTimeoutException qte) {
			logTrace( "QueryTimeoutException Caught as Expected");
			Throwable t = qte.getCause();
			if (t instanceof IllegalAccessException) {
				String msg = t.getMessage();
				if (msg != null) {
					if (!msg.equals(expected)) {
						logErr( "Expected Throwable msg=" + msg + ", Actual=" + msg);
						pass = false;
					}
				} else {
					logErr( "t.getMessage() returned null");
					pass = false;
				}
			} else {
				logErr(
						"getCause did not return an instance of IllegalAccessException, instead got " + t);
				pass = false;
			}
		}

		logMsg( "Begin QueryTimeoutExceptionObjectMsgTest");

		Query qExpected = getEntityManager().createQuery("select c from Coffee c");
		try {
			throw new QueryTimeoutException(qExpected);

		} catch (QueryTimeoutException qte) {
			logTrace( "QueryTimeoutException Caught as Expected");
			Query q = qte.getQuery();
			if (q != null) {
				if (q.equals(qExpected)) {
					pass = true;
				} else {
					logErr( "Expected=" + expected + ", Actual=" + q);
					pass = false;
				}
			} else {
				logErr( "QueryTimeoutException returned null message");
				pass = false;
			}

		}

		logMsg( "Test QueryTimeoutExceptionStringThrowableObject");

		expected = "This is the String message";
		expected2 = "This is the Throwable message";
		qExpected = getEntityManager().createQuery("select c from Coffee c");

		try {
			throw new QueryTimeoutException(expected, new IllegalAccessException(expected2), qExpected);

		} catch (QueryTimeoutException qte) {
			logTrace( "QueryTimeoutException Caught as Expected");
			String msg = qte.getMessage();
			if (msg != null) {
				if (msg.equals(expected)) {
					Throwable t = qte.getCause();
					if (t instanceof IllegalAccessException) {
						msg = t.getMessage();
						if (msg != null) {
							if (msg.equals(expected2)) {
								Query q = qte.getQuery();
								if (q != null) {
									if (!q.equals(qExpected)) {
										logErr(
												"Expected Entity=" + qExpected + ", Actual=" + q);
										pass = false;
									}
								} else {
									logErr( "getQuery returned null");
									pass = false;
								}
							} else {
								logErr( "Expected Throwable msg=" + msg + ", Actual=" + msg);
								pass = false;
							}
						} else {
							logErr( "t.getMessage returned null");
							pass = false;
						}
					} else {
						logErr(
								"getCause did not return an instance of IllegalAccessException, instead got " + t);
						pass = false;
					}

				} else {
					logErr( "Expected=" + msg + ", Actual=" + msg);
					pass = false;
				}
			} else {
				logErr( "QueryTimeoutException returned null message");
				pass = false;
			}
		} catch (Exception e) {
			logErr( "Unexpected Exception Caught", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("QueryTimeoutExceptionTest failed");
	}

	/*
	 * @testName: NonUniqueResultExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:125; PERSISTENCE:JAVADOC:126
	 * 
	 * @test_Strategy: create and throw NonUniqueResultException() and validate
	 * contents of Exception
	 */
	@Test
	public void NonUniqueResultExceptionTest() throws Exception {

		boolean pass = true;
		logMsg( "Begin NonUniqueResultExceptionNullMsgTest");

		try {
			throw new NonUniqueResultException();
		} catch (NonUniqueResultException nure) {
			logTrace( "NonUniqueResultException Caught as Expected");
			if (nure.getMessage() != null) {
				logErr(
						"NonUniqueResultException should have had null message, actual message=" + nure.getMessage());
				pass = false;
			}
		} catch (Exception e) {
			logErr( "Unexpected Exception Caught", e);
			pass = false;
		}

		logTrace( "Testing NonUniqueResultExceptionStringMsg");

		String expected = "This is the String message";
		try {
			throw new NonUniqueResultException(expected);

		} catch (NonUniqueResultException ole) {
			logTrace( "NonUniqueResultException Caught as Expected");
			String msg = ole.getMessage();
			if (msg != null) {
				if (!msg.equals(expected)) {
					logErr( "Expected=" + msg + ", Actual=" + msg);
					pass = false;
				}
			} else {
				logErr( "NonUniqueResultException returned null message");
				pass = false;
			}

		} catch (Exception e) {
			logErr( "Unexpected Exception Caught", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("NonUniqueResultExceptionTest failed");
	}

	/*
	 * @testName: NoResultExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:127; PERSISTENCE:JAVADOC:128
	 * 
	 * @test_Strategy: create and throw NonUniqueResultException() and validate
	 * contents of Exception
	 */
	@Test
	public void NoResultExceptionTest() throws Exception {

		boolean pass = true;
		logMsg( "Testing NoResultExceptionNullMsg");
		try {
			throw new NoResultException();
		} catch (NoResultException nre) {
			logTrace( "NoResultException Caught as Expected");
			if (nre.getMessage() != null) {
				logErr(
						"NoResultException should have had null message, actual message=" + nre.getMessage());
				pass = false;
			}
		} catch (Exception e) {
			logErr( "Unexpected Exception Caught", e);
			pass = false;
		}

		logMsg( "Testing NoResultExceptionStringMsgTest");

		String expected = "This is the String message";
		try {
			throw new NoResultException(expected);

		} catch (NoResultException nre) {
			logTrace( "NoResultException Caught as Expected");
			String msg = nre.getMessage();
			if (msg != null) {
				if (!msg.equals(expected)) {
					logErr( "Expected=" + msg + ", Actual=" + msg);
					pass = false;
				}
			} else {
				logErr( "NoResultException returned null message");
				pass = false;
			}
		}

		if (!pass)
			throw new Exception("NoResultExceptionTest failed");
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			logTrace( "Cleanup data");
			if (getEntityManager().isOpen()) {
				removeTestData();
			}
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
			getEntityManager().createNativeQuery("DELETE FROM COFFEE").executeUpdate();
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
