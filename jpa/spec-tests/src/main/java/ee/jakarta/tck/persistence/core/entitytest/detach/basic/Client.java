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

/*
 *  $Id$
 */

package ee.jakarta.tck.persistence.core.entitytest.detach.basic;



import java.util.Properties;

import com.sun.ts.lib.harness.Status;





import ee.jakarta.tck.persistence.common.PMClientBase;

public class Client extends PMClientBase {

	

	public Client() {
	}
	public static void main(String[] args) {
		Client theTests = new Client();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	public void setup(String[] args, Properties p) throws Exception {
		logTrace( "setup");
		try {
			super.setup(args,p);
			
			removeTestData();
		} catch (Exception e) {
			throw new Exception("Setup failed:", e);

		}
	}

	/*
	 * BEGIN Test Cases
	 */

	/*
	 * @testName: detachBasicTest1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:635
	 * 
	 * @test_Strategy: If X is a detached entity, invoking the remove method on it
	 * will cause an IllegalArgumentException to be thrown or the transaction commit
	 * will fail. Invoke remove on a detached entity.
	 *
	 */
		public void detachBasicTest1() throws Exception {
		logTrace( "Begin detachBasicTest1");
		boolean pass = false;
		final A aRef = new A("1", "a1", 1);

		try {

			logTrace( "Persist Instance");
			createA(aRef);

			clearCache();

			getEntityTransaction().begin();
			logTrace( "tx started, see if entity is detached");
			if (getEntityManager().contains(aRef)) {
				logErr(
						"contains method returned true; expected false" + " (detached), test fails.");
				pass = false;
			} else {

				try {
					logTrace( "try remove");
					getEntityManager().remove(aRef);
				} catch (IllegalArgumentException iae) {
					logTrace( "IllegalArgumentException caught as expected", iae);
					pass = true;
				}

			}

			logTrace( "tx commit");
			getEntityTransaction().commit();

		} catch (Exception e) {
			logTrace( "or, Transaction commit will fail. " + " Test the commit failed by testing"
					+ " the transaction is marked for rollback");
			if (!pass) {
				if (e instanceof jakarta.transaction.TransactionRolledbackException
						|| e instanceof jakarta.persistence.PersistenceException) {
					pass = true;
				} else {
					logErr(
							"Not TransactionRolledbackException nor PersistenceException, totally unexpected:", e);
				}
			}

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
			throw new Exception("detachBasicTest1 failed");
	}

	/*
	 * @testName: detachBasicTest2
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:323; PERSISTENCE:SPEC:649;
	 * PERSISTENCE:SPEC:650;
	 * 
	 * @test_Strategy: Do a find of an entity, detached it, then modify it. Do
	 * another find and verify the changes were not persisted.
	 *
	 */
		public void detachBasicTest2() throws Exception {
		logTrace( "Begin detachBasicTest2");
		boolean pass = false;
		final A expected = new A("1", "a1", 1);

		try {

			logTrace( "Persist Instance");
			createA(new A("1", "a1", 1));

			getEntityTransaction().begin();
			logTrace( "Executing find");
			A newA = getEntityManager().find(A.class, "1");
			logTrace( "newA:" + newA.toString());

			logTrace( "changing name");
			newA.setAName("foobar");
			logTrace( "newA:" + newA.toString());
			logTrace( "executing detach");
			getEntityManager().detach(newA);
			logTrace( "newA:" + newA.toString());

			logTrace( "tx commit");
			getEntityTransaction().commit();
			A newAA = getEntityManager().find(A.class, "1");
			logTrace( "newAA:" + newAA.toString());

			if (expected.equals(newAA)) {
				pass = true;
			} else {
				logErr(
						"Changes made to entity were persisted even though it was detached without a flush");
				logErr( "expected A:" + expected.toString() + ", actual A:" + newAA.toString());

			}

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
			throw new Exception("detachBasicTest2 failed");
	}

	/*
	 * Business Methods for Test Cases
	 */

	private void createA(final A a) {
		logTrace( "Entered createA method");
		getEntityTransaction().begin();
		getEntityManager().persist(a);
		getEntityTransaction().commit();
	}

	
	public void cleanup() throws Exception {
		try {
			logTrace( "Cleanup data");
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
			getEntityManager().createNativeQuery("DELETE FROM AEJB_1XM_BI_BTOB").executeUpdate();
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
