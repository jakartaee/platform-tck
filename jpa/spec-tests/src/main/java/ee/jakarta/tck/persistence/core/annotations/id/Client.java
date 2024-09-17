/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.persistence.core.annotations.id;


import java.math.BigDecimal;
import java.math.BigInteger;
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
	 * @testName: FieldIntegerIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.2
	 * 
	 * @test_Strategy:
	 */

		public void FieldIntegerIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			Integer id = new Integer(1);

			FieldIntegerId expected = new FieldIntegerId(id, id);

			logTrace( "Persisting IntegerId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			FieldIntegerId actual = getEntityManager().find(FieldIntegerId.class, id);
			if (actual != null) {
				if (actual.getIntegerData().equals(id)) {
					logTrace( "Received expected result:" + actual.getIntegerData());
					pass = true;
				} else {
					logErr( "Expected Integer:" + id + ", actual: " + actual.getIntegerData());
				}

			} else {
				logErr( "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("FieldIntegerIdTest failed");
	}

	/*
	 * @testName: FieldIntIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.1
	 * 
	 * @test_Strategy:
	 */
		public void FieldIntIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			int id = 1;

			FieldIntId expected = new FieldIntId(id, id);

			logTrace( "Persisting IntId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			FieldIntId actual = getEntityManager().find(FieldIntId.class, id);
			if (actual != null) {
				if (actual.getIntData() == id) {
					logTrace( "Received expected result:" + actual.getIntData());
					pass = true;
				} else {
					logErr( "Expected int:" + id + ", actual: " + actual.getIntData());
				}

			} else {
				logErr( "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("FieldIntIdTest failed");
	}

	/*
	 * @testName: FieldBigIntegerIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.7
	 * 
	 * @test_Strategy:
	 */
		public void FieldBigIntegerIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final BigInteger id = new BigInteger("1");

			FieldBigIntegerId expected = new FieldBigIntegerId(id, id);

			logTrace( "Persisting BigIntegerId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			FieldBigIntegerId actual = getEntityManager().find(FieldBigIntegerId.class, id);
			if (actual != null) {
				if (actual.getBigInteger().equals(id)) {
					logTrace( "Received expected result:" + actual.getBigInteger());
					pass = true;
				} else {
					logErr( "Expected name:" + id + ", actual: " + actual.getBigInteger());
				}

			} else {
				logErr( "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("FieldBigIntegerIdTest failed");
	}

	/*
	 * @testName: FieldBigDecimalIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.6
	 * 
	 * @test_Strategy:
	 */
		public void FieldBigDecimalIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final BigDecimal id = new BigDecimal(new BigInteger("1"));

			FieldBigDecimalId expected = new FieldBigDecimalId(id, id);

			logTrace( "Persisting BigDecimalId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			FieldBigDecimalId actual = getEntityManager().find(FieldBigDecimalId.class, id);
			if (actual != null) {
				if (actual.getBigDecimal().equals(id)) {
					logTrace( "Received expected result:" + actual.getBigDecimal());
					pass = true;
				} else {
					logErr( "Expected value:" + id + ", actual: " + actual.getBigDecimal());
				}

			} else {
				logErr( "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("FieldBigDecimalIdTest failed");
	}

	/*
	 * @testName: FieldStringIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.3
	 * 
	 * @test_Strategy:
	 */
		public void FieldStringIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final String id = "1";

			FieldStringId expected = new FieldStringId(id, id);

			logTrace( "Persisting StringId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			FieldStringId actual = getEntityManager().find(FieldStringId.class, id);
			if (actual != null) {
				if (actual.getName().equals(id)) {
					logTrace( "Received expected result:" + actual.getName());
					pass = true;
				} else {
					logErr( "Expected name:" + id + ", actual: " + actual.getName());
				}

			} else {
				logErr( "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("FieldStringIdTest failed");
	}

	/*
	 * @testName: FieldSQLDateIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.5
	 * 
	 * @test_Strategy:
	 */
		public void FieldSQLDateIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final java.sql.Date id = getSQLDate(2006, 04, 15);

			FieldSQLDateId expected = new FieldSQLDateId(id, id);

			logTrace( "Persisting StringId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			FieldSQLDateId actual = getEntityManager().find(FieldSQLDateId.class, id);
			if (actual != null) {
				if (actual.getDate().equals(id)) {
					logTrace( "Received expected result:" + actual.getDate());
					pass = true;
				} else {
					logErr( "Expected name:" + id + ", actual: " + actual.getDate());
				}

			} else {
				logErr( "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("FieldSQLDateIdTest failed");
	}

	/*
	 * @testName: FieldUtilDateIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.4
	 * 
	 * @test_Strategy:
	 */
		public void FieldUtilDateIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final java.util.Date id = getPKDate(2006, 04, 15);

			FieldUtilDateId expected = new FieldUtilDateId(id, id);

			logTrace( "Persisting StringId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			FieldUtilDateId actual = getEntityManager().find(FieldUtilDateId.class, id);
			if (actual != null) {
				if (actual.getDate().equals(id)) {
					logTrace( "Received expected result:" + actual.getDate());
					pass = true;
				} else {
					logErr( "Expected name:" + id + ", actual: " + actual.getDate());
				}

			} else {
				logErr( "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("FieldUtilDateIdTest failed");
	}

	/*
	 * @testName: PropertyIntegerIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.2
	 * 
	 * @test_Strategy:
	 */
		public void PropertyIntegerIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			Integer id = new Integer(1);

			PropertyIntegerId expected = new PropertyIntegerId(id, id);

			logTrace( "Persisting IntegerId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			PropertyIntegerId actual = getEntityManager().find(PropertyIntegerId.class, id);
			if (actual != null) {
				if (actual.getIntegerData().equals(id)) {
					logTrace( "Received expected result:" + actual.getIntegerData());
					pass = true;
				} else {
					logErr( "Expected Integer:" + id + ", actual: " + actual.getIntegerData());
				}

			} else {
				logErr( "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("PropertyIntegerIdTest failed");
	}

	/*
	 * @testName: PropertyIntIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.1
	 * 
	 * @test_Strategy:
	 */
		public void PropertyIntIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			int id = 1;

			PropertyIntId expected = new PropertyIntId(id, id);

			logTrace( "Persisting IntId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			PropertyIntId actual = getEntityManager().find(PropertyIntId.class, id);
			if (actual != null) {
				if (actual.getIntData() == id) {
					logTrace( "Received expected result:" + actual.getIntData());
					pass = true;
				} else {
					logErr( "Expected int:" + id + ", actual: " + actual.getIntData());
				}

			} else {
				logErr( "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("PropertyIntIdTest failed");
	}

	/*
	 * @testName: PropertyBigIntegerIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.7
	 * 
	 * @test_Strategy:
	 */
		public void PropertyBigIntegerIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final BigInteger id = new BigInteger("1");

			PropertyBigIntegerId expected = new PropertyBigIntegerId(id, id);

			logTrace( "Persisting BigIntegerId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			PropertyBigIntegerId actual = getEntityManager().find(PropertyBigIntegerId.class, id);
			if (actual != null) {
				if (actual.getBigInteger().equals(id)) {
					logTrace( "Received expected result:" + actual.getBigInteger());
					pass = true;
				} else {
					logErr( "Expected name:" + id + ", actual: " + actual.getBigInteger());
				}

			} else {
				logErr( "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("PropertyBigIntegerIdTest failed");
	}

	/*
	 * @testName: PropertyBigDecimalIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.6
	 * 
	 * @test_Strategy:
	 */
		public void PropertyBigDecimalIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final BigDecimal id = new BigDecimal(new BigInteger("1"));

			PropertyBigDecimalId expected = new PropertyBigDecimalId(id, id);

			logTrace( "Persisting BigDecimalId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			PropertyBigDecimalId actual = getEntityManager().find(PropertyBigDecimalId.class, id);
			if (actual != null) {
				if (actual.getBigDecimal().equals(id)) {
					logTrace( "Received expected result:" + actual.getBigDecimal());
					pass = true;
				} else {
					logErr( "Expected value:" + id + ", actual: " + actual.getBigDecimal());
				}

			} else {
				logErr( "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("PropertyBigDecimalIdTest failed");
	}

	/*
	 * @testName: PropertyStringIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.3
	 * 
	 * @test_Strategy:
	 */
		public void PropertyStringIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final String id = "1";

			PropertyStringId expected = new PropertyStringId(id, id);

			logTrace( "Persisting StringId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			PropertyStringId actual = getEntityManager().find(PropertyStringId.class, id);
			if (actual != null) {
				if (actual.getName().equals(id)) {
					logTrace( "Received expected result:" + actual.getName());
					pass = true;
				} else {
					logErr( "Expected name:" + id + ", actual: " + actual.getName());
				}

			} else {
				logErr( "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("PropertyStringIdTest failed");
	}

	/*
	 * @testName: PropertySQLDateIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.5
	 * 
	 * @test_Strategy:
	 */
		public void PropertySQLDateIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final java.sql.Date id = getSQLDate(2006, 04, 15);

			PropertySQLDateId expected = new PropertySQLDateId(id, id);

			logTrace( "Persisting StringId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			PropertySQLDateId actual = getEntityManager().find(PropertySQLDateId.class, id);
			if (actual != null) {
				if (actual.getDate().equals(id)) {
					logTrace( "Received expected result:" + actual.getDate());
					pass = true;
				} else {
					logErr( "Expected name:" + id + ", actual: " + actual.getDate());
				}

			} else {
				logErr( "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("PropertySQLDateIdTest failed");
	}

	/*
	 * @testName: PropertyUtilDateIdTest
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:2025; PERSISTENCE:SPEC:2025.4
	 * 
	 * @test_Strategy:
	 */
		public void PropertyUtilDateIdTest() throws Exception {

		boolean pass = false;

		try {
			getEntityTransaction().begin();
			final java.util.Date id = getPKDate(2006, 04, 15);

			PropertyUtilDateId expected = new PropertyUtilDateId(id, id);

			logTrace( "Persisting StringId");
			getEntityManager().persist(expected);
			getEntityManager().flush();
			getEntityTransaction().commit();
			clearCache();
			getEntityTransaction().begin();
			PropertyUtilDateId actual = getEntityManager().find(PropertyUtilDateId.class, id);
			if (actual != null) {
				if (actual.getDate().equals(id)) {
					logTrace( "Received expected result:" + actual.getDate());
					pass = true;
				} else {
					logErr( "Expected name:" + id + ", actual: " + actual.getDate());
				}

			} else {
				logErr( "EntityManager.find returned null result");
			}
			getEntityTransaction().commit();

		} catch (Exception e) {
			logErr( "Unexpected exception occurred", e);
			pass = false;
		}

		if (!pass)
			throw new Exception("PropertyUtilDateIdTest failed");
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

	private void removeTestData() {
		logTrace( "removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM A_BASIC").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DATATYPES").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DATATYPES2").executeUpdate();
			getEntityManager().createNativeQuery("DELETE FROM DATATYPES3").executeUpdate();
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
