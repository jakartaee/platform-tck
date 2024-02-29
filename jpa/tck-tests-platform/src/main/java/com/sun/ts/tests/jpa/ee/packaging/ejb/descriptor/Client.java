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

package com.sun.ts.tests.jpa.ee.packaging.ejb.descriptor;

import java.lang.System.Logger;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TSNamingContext;

public class Client {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public static final String StatefulRef = "java:comp/env/ejb/Stateful3Bean";

	public static final String StatelessRef = "java:comp/env/ejb/Stateless3Bean";

	private Stateful3IF bean;

	private Stateless3IF bean1;

	private Properties props;

	/*
	 * @class.setup_props:
	 */

	@BeforeEach
	public void setup() throws Exception {
		try {
			TSNamingContext nctx = new TSNamingContext();
			logger.log(Logger.Level.TRACE, "Look up bean: " + StatefulRef);
			bean = (Stateful3IF) nctx.lookup(StatefulRef);

			logger.log(Logger.Level.TRACE, "Look up bean: " + StatelessRef);
			bean1 = (Stateless3IF) nctx.lookup(StatelessRef);

			cleanup();
		} catch (Exception e) {
			throw new Exception("Setup Failed!", e);
		}
	}

	/*
	 * @testName: test1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:900; PERSISTENCE:SPEC:901;
	 * PERSISTENCE:SPEC:850; PERSISTENCE:SPEC:852; PERSISTENCE:SPEC:854;
	 * PERSISTENCE:SPEC:859; PERSISTENCE:SPEC:952; PERSISTENCE:SPEC:968;
	 * PERSISTENCE:SPEC:909; PERSISTENCE:SPEC:910; PERSISTENCE:SPEC:893;
	 * PERSISTENCE:SPEC:939; PERSISTENCE:SPEC:953; PERSISTENCE:SPEC:945;
	 * PERSISTENCE:SPEC:943; PERSISTENCE:SPEC:969; PERSISTENCE:SPEC:970;
	 * PERSISTENCE:JAVADOC:162; JavaEE:SPEC:10056; JavaEE:SPEC:10057;
	 * JavaEE:SPEC:10058; JavaEE:SPEC:10059; PERSISTENCE:SPEC:974;
	 * PERSISTENCE:SPEC:975; PERSISTENCE:SPEC:952; PERSISTENCE:SPEC:949.1;
	 * PERSISTENCE:SPEC:951; PERSISTENCE:SPEC:952; PERSISTENCE:SPEC:953;
	 * 
	 * @test_Strategy: With the above archive, deploy bean, create entities,
	 * persist, then find.
	 *
	 */
	@Test
	public void test1() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin test1");
		boolean pass = false;

		try {

			bean.init(props);
			pass = bean.test1();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception :", e);

		}

		if (!pass)
			throw new Exception("test1 failed");
	}

	/*
	 * @testName: test2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:852; PERSISTENCE:SPEC:612;
	 * PERSISTENCE:SPEC:880; PERSISTENCE:SPEC:884; PERSISTENCE:SPEC:885;
	 * PERSISTENCE:JAVADOC:60; PERSISTENCE:JAVADOC:57
	 * 
	 * @test_Strategy: The EntityManagerFactory API is used to obtain an
	 * application- managed entity manager and is the same whether this API is used
	 * in Java EE or Java SE environments.
	 *
	 * Use the EntityManagerFactory API to get an application- managed entity
	 * manager. The methods close, isOpen, joinTransaction, and getTransaction are
	 * used to managed application-managed entity managers and their life cycle.
	 *
	 * Close the entity manager and ensure isOpen returns false.
	 *
	 * The EntityManager close and isOpen methods are used to manage the lifecycle
	 * of an application-managed entity manager and its associate persistence
	 * context.
	 *
	 */
	@Test
	public void test2() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin test2");
		boolean pass = false;

		try {

			bean.init(props);
			pass = bean.test2();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception :", e);

		}

		if (!pass)
			throw new Exception("test2 failed");
	}

	/*
	 * @testName: test3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:882
	 * 
	 * @test_Strategy: The EntityManagerFactory API is used to obtain an
	 * application- managed entity manager and is the same whether this API is used
	 * in Java EE or Java SE environments.
	 *
	 * Use the EntityManagerFactory API to get an application- managed entity
	 * manager. The methods close, isOpen, joinTransaction, and getTransaction are
	 * used to managed application-managed entity managers and their life cycle.
	 *
	 * Close the entity manager, call close again and ensure an
	 * IllegalStateException is thrown.
	 *
	 */
	@Test
	public void test3() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin test3");
		boolean pass = false;

		try {

			bean.init(props);
			pass = bean.test3();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception :", e);

		}

		if (!pass)
			throw new Exception("test3 failed");
	}

	/*
	 * @testName: test4
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:48
	 * 
	 * @test_Strategy: The EntityManagerFactory API is used to obtain an
	 * application- managed entity manager and is the same whether this API is used
	 * in Java EE or Java SE environments.
	 *
	 * Use the EntityManagerFactory API to get an application- managed entity
	 * manager. The methods close, isOpen, joinTransaction, and getTransaction are
	 * used to managed application-managed entity managers and their life cycle.
	 *
	 * EntityManager.getTransaction() will throw an IllegalStateException if invoked
	 * on a JTA Entity Manager.
	 *
	 * This entitymanager is an Application-Managed Entity Manager, call
	 * getTransaction() and ensure IllegalStateException is thrown.
	 *
	 */
	@Test
	public void test4() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin test4");
		boolean pass = false;

		try {

			bean.init(props);
			pass = bean.test4();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception :", e);

		}

		if (!pass)
			throw new Exception("test4 failed");
	}

	/*
	 * @testName: test5
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:886; PERSISTENCE:JAVADOC:50
	 * 
	 * @test_Strategy: EntityManager.joinTransaction is used with a JTA
	 * Application-Managed Entity Manager.
	 *
	 */
	@Test
	public void test5() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin test5");
		boolean pass = false;

		try {

			bean1.init(props);
			pass = bean1.test5();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception :", e);

		}

		if (!pass)
			throw new Exception("test5 failed");
	}

	/*
	 * @testName: test6
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:954;
	 * 
	 * @test_Strategy: With the above archive, deploy bean, create entities,
	 * persist, then find. This test makes sure that the contents of an orm.xml file
	 * is automatically loaded into the persistence unit.
	 *
	 */
	@Test
	public void test6() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin test6");
		boolean pass = false;

		try {

			bean.init(props);
			pass = bean.test6();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception :", e);

		}

		if (!pass)
			throw new Exception("test6 failed");
	}

	@AfterEach
	public void cleanup() throws Exception {
		try {
			bean.removeTestData();
			bean1.removeTestData();
		} catch (Exception re) {
			logger.log(Logger.Level.TRACE, "Unexpected Exception in entity cleanup:", re);
		}
		logger.log(Logger.Level.TRACE, "cleanup complete");
	}

}
