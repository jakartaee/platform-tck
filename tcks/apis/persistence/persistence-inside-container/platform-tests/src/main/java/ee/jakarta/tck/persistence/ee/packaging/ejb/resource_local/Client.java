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
 * $Id$
 */

package ee.jakarta.tck.persistence.ee.packaging.ejb.resource_local;

import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.lib.harness.Status;

import java.util.Properties;

public class Client extends EETest {

	private Stateless3IF bean = null;
	private Properties props;

	/* appclient entry point */
	public static void main(String[] args) {
		Client client = new Client();
		Status s = client.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @class.setup_props:
	 */
	public void setup(String[] args, Properties props) throws Exception {
		try {
			bean = Stateless3IFProxy.newInstance(props);
			logTrace( "Looked up Bean: " + bean);

		} catch (Exception e) {
			throw new Exception("Setup Failed!", e);
		}
	}

	/*
	 * Packaging:
	 *
	 * client.jar - Application Client
	 *
	 * EJB archive (ejb.jar) containing: ejb-jar - EJB 3.0 Container-Managed
	 * Stateless Session Bean
	 *
	 * EJB-JAR is the root of the persistence unit persistence.xml resides in
	 * EJB-JAR META-INF directory The required persistence unit name set to empty
	 * string "" which is same as default
	 *
	 * persistence unit configuration information: EntityManagerFactory looked up
	 * via JNDI lookup defined in ejb.xml using persistence-unit-ref descriptor
	 * optional unit name, optional description Application-Managed Resource_Local
	 * Entity Manager passing in Map of properties Type - RESOURCE_LOCAL - Unit_name
	 * should be optional as it only has one PU defined.
	 *
	 */

	/*
	 * @testName: test1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:859; PERSISTENCE:SPEC:860;
	 * PERSISTENCE:JAVADOC:66; PERSISTENCE:JAVADOC:59
	 * 
	 * @test_Strategy: EntityTransactionInterface
	 *
	 * begin() starts a resource_transaction
	 */
	public void test1() throws Exception {

		logTrace( "Begin test1");
		boolean pass = false;

		try {
			bean.init(props);
			pass = bean.test1();

		} catch (Exception e) {
			logErr( "Unexpected Exception :", e);
		}

		if (!pass)
			throw new Exception("test1 failed");
	}

	/*
	 * @testName: test2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:66
	 * 
	 * @test_Strategy: EntityTransactionInterface
	 *
	 * begin() throws an IllegalStateException if isActive() is true
	 */
	public void test2() throws Exception {

		logTrace( "Begin test2");
		boolean pass = false;

		try {
			bean.init(props);
			pass = bean.test2();

		} catch (Exception e) {
			logErr( "Unexpected Exception :", e);
		}

		if (!pass)
			throw new Exception("test2 failed");
	}

	/*
	 * @testName: test3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:67
	 * 
	 * @test_Strategy: EntityTransactionInterface
	 *
	 * commit() commits the current transaction
	 */
	public void test3() throws Exception {

		logTrace( "Begin test3");
		boolean pass = false;

		try {
			bean.init(props);
			pass = bean.test3();

		} catch (Exception e) {
			logErr( "Unexpected Exception :", e);
		}

		if (!pass)
			throw new Exception("test3 failed");
	}

	/*
	 * @testName: test4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:67
	 * 
	 * @test_Strategy: EntityTransactionInterface
	 *
	 * commit() throws an IllegalStateException if isActive() is false
	 */
	public void test4() throws Exception {

		logTrace( "Begin test4");
		boolean pass = false;

		try {
			bean.init(props);
			pass = bean.test4();

		} catch (Exception e) {
			logErr( "Unexpected Exception :", e);
		}

		if (!pass)
			throw new Exception("test4 failed");
	}

	/*
	 * @testName: test5
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:67;
	 * PERSISTENCE:JAVADOC:190; PERSISTENCE:JAVADOC:185
	 * 
	 * @test_Strategy: EntityTransactionInterface
	 *
	 * commit() throws a RollbackException if commit fails
	 */
	public void test5() throws Exception {

		logTrace( "Begin test5");
		boolean pass = false;

		try {
			bean.init(props);
			pass = bean.test5();

		} catch (Exception e) {
			logErr( "Unexpected Exception :", e);
		}

		if (!pass)
			throw new Exception("test5 failed");
	}

	/*
	 * @testName: test6
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:70
	 * 
	 * @test_Strategy: EntityTransactionInterface
	 *
	 * rollback() rolls back the current transaction
	 */
	public void test6() throws Exception {

		logTrace( "Begin test6");
		boolean pass = false;

		try {
			bean.init(props);
			pass = bean.test6();

		} catch (Exception e) {
			logErr( "Unexpected Exception :", e);
		}

		if (!pass)
			throw new Exception("test6 failed");
	}

	/*
	 * @testName: test7
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:71
	 * 
	 * @test_Strategy: EntityTransactionInterface
	 *
	 * setRollbackOnly() marks the current transaction so the only outcome is for
	 * the transaction to be rolled back
	 */
	public void test7() throws Exception {

		logTrace( "Begin test7");
		boolean pass = false;

		try {
			bean.init(props);
			pass = bean.test7();

		} catch (Exception e) {
			logErr( "Unexpected Exception :", e);
		}

		if (!pass)
			throw new Exception("test7 failed");
	}

	/*
	 * @testName: test8
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:71
	 * 
	 * @test_Strategy: EntityTransactionInterface
	 *
	 * setRollbackOnly throws IllegalStateException if isActive() is false
	 */
	public void test8() throws Exception {

		logTrace( "Begin test8");
		boolean pass = false;

		try {
			bean.init(props);
			pass = bean.test8();

		} catch (Exception e) {
			logErr( "Unexpected Exception :", e);
		}

		if (!pass)
			throw new Exception("test8 failed");
	}

	/*
	 * @testName: test9
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:68
	 * 
	 * @test_Strategy: EntityTransactionInterface
	 *
	 * getRollbackOnly determines if the current transaction has been marked for
	 * rollback test getRollbackOnly when isActive() is true and TX has been marked
	 * for rollback, so getRollbackOnly will return true
	 */
	public void test9() throws Exception {

		logTrace( "Begin test9");
		boolean pass = false;

		try {
			bean.init(props);
			pass = bean.test9();

		} catch (Exception e) {
			logErr( "Unexpected Exception :", e);
		}

		if (!pass)
			throw new Exception("test9 failed");
	}

	/*
	 * @testName: test10
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:68
	 * 
	 * @test_Strategy: EntityTransactionInterface
	 *
	 * getRollbackOnly throws IllegalStateException if isActive() is false
	 */
	public void test10() throws Exception {

		logTrace( "Begin test10");
		boolean pass = false;

		try {
			bean.init(props);
			pass = bean.test10();

		} catch (Exception e) {
			logErr( "Unexpected Exception :", e);
		}

		if (!pass)
			throw new Exception("test10 failed");
	}

	/*
	 * @testName: test11
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:68
	 * 
	 * @test_Strategy: EntityTransactionInterface test getRollbackOnly when
	 * isActive() is true and TX has not been marked for rollback, so
	 * getRollbackOnly will return false
	 *
	 */
	public void test11() throws Exception {

		logTrace( "Begin test11");
		boolean pass = false;

		try {
			bean.init(props);
			pass = bean.test11();

		} catch (Exception e) {
			logErr( "Unexpected Exception :", e);
		}

		if (!pass)
			throw new Exception("test11 failed");
	}

	/*
	 * @testName: test12
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:69;
	 * PERSISTENCE:JAVADOC:157
	 * 
	 * @test_Strategy: EntityTransactionInterface
	 *
	 * isActive() indicates whether a transaction is in progress Try when TX is
	 * active
	 */
	public void test12() throws Exception {

		logTrace( "Begin test12");
		boolean pass = false;

		try {
			bean.init(props);
			pass = bean.test12();

		} catch (Exception e) {
			logErr( "Unexpected Exception :", e);
		}

		if (!pass)
			throw new Exception("test12 failed");
	}

	/*
	 * @testName: test13
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:69
	 * 
	 * @test_Strategy: EntityTransactionInterface
	 *
	 * isActive() indicates whether a transaction is in progress Try when TX is not
	 * active
	 */
	public void test13() throws Exception {

		logTrace( "Begin test13");
		boolean pass = false;

		try {
			bean.init(props);
			pass = bean.test13();

		} catch (Exception e) {
			logErr( "Unexpected Exception :", e);
		}

		if (!pass)
			throw new Exception("test13 failed");
	}

	/*
	 * @testName: test14
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:860; PERSISTENCE:JAVADOC:72;
	 * PERSISTENCE:SPEC:741
	 * 
	 * @test_Strategy: EntityTransactionInterface
	 *
	 * When a resource-local entity manager is used, and the persistence provider
	 * runtime throws an exception defined to cause a transaction rollback, it must
	 * mark the transaction for rollback.
	 */
	public void test14() throws Exception {

		logTrace( "Begin test14");
		boolean pass = false;

		try {
			bean.init(props);
			pass = bean.test14();

		} catch (Exception e) {
			logErr( "Unexpected Exception :", e);
		}

		if (!pass)
			throw new Exception("test14 failed");
	}

	/*
	 * @testName: test15
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:882; PERSISTENCE:JAVADOC:48;
	 * PERSISTENCE:JAVADOC:49
	 * 
	 * @test_Strategy: After the close method has been invoked, all methods on the
	 * EntityManager instance will throw the IllegalStateException except for
	 * getTransaction.
	 *
	 * 
	 */
	public void test15() throws Exception {

		logTrace( "Begin test15");
		boolean pass = false;

		try {
			bean.init(props);
			pass = bean.test15();

		} catch (Exception e) {
			logErr( "Unexpected Exception :", e);
		}

		if (!pass)
			throw new Exception("test15 failed");
	}

	public void cleanup() throws Exception {
		try {
			bean.removeTestData();
		} catch (Exception re) {
			logErr( "Unexpected Exception in entity cleanup:", re);
		}
		logTrace( "cleanup complete");
	}

}
