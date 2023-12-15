/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.ee.propagation.am;

import java.lang.System.Logger;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.ejb.EJB;

public class Client {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	@EJB(name = "ejb/Stateful3Bean", beanInterface = Stateful3IF.class)
	private static Stateful3IF statefulBean;

	@EJB(name = "ejb/Stateful3Bean2", beanInterface = Stateful3IF2.class)
	private static Stateful3IF2 statefulBean2;

	@EJB(name = "ejb/Stateless3Bean", beanInterface = Stateless3IF.class)
	private static Stateless3IF statelessBean;

	private Properties props;

	/*
	 * @class.setup_props:
	 */

	@BeforeEach
	public void setup() throws Exception {
	}

	/*
	 * @testName: test1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:684; PERSISTENCE:SPEC:842;
	 * PERSISTENCE:SPEC:850; PERSISTENCE:SPEC:852; PERSISTENCE:SPEC:853;
	 * PERSISTENCE:SPEC:859; PERSISTENCE:SPEC:879; PERSISTENCE:SPEC:880;
	 * PERSISTENCE:SPEC:885; PERSISTENCE:JAVADOC:58; PERSISTENCE:SPEC:1024
	 * 
	 * @test_Strategy: When an application-managed entity manager is used, the
	 * application interacts directly with the persistence provider's entity manager
	 * factory to manage the entity manager life cycle and to obtain and destroy
	 * persistence contexts. All such application-managed pcs are extended in scope
	 * and may span multiple transactions.
	 *
	 * Inject entity manager factory, but open and close each entity manager within
	 * the business method.
	 */
	@Test
	public void test1() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin test1");
		boolean pass = false;

		try {

			statelessBean.init(props);
			statelessBean.doCleanup();
			pass = statelessBean.test1();
			statelessBean.doCleanup();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception :", e);
		}

		if (!pass)
			throw new Exception("test1 failed");
	}

	/*
	 * @testName: test2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:880
	 * 
	 * @test_Strategy: When an application-managed entity manager is used, the
	 * application interacts directly with the persistence provider's entity manager
	 * factory to manage the entity manager life cycle and to obtain and destroy
	 * persistence contexts. All such application-managed pcs are extended in scope
	 * and may span multiple transactions.
	 *
	 * Inject entity manager factory, but open and close each entity manager within
	 * the business method.
	 *
	 */
	@Test
	public void test2() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin test2");
		boolean pass = false;

		try {

			statelessBean.init(props);
			statelessBean.doCleanup();
			pass = statelessBean.test2();
			statelessBean.doCleanup();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception :", e);
		}

		if (!pass)
			throw new Exception("test2 failed");
	}

	/*
	 * @testName: test3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:692; PERSISTENCE:JAVADOC:140;
	 * PERSISTENCE:JAVADOC:52
	 * 
	 * @test_Strategy: The persistence providers implementation of the merge
	 * operation must examine the version attribute when an entity is being merged
	 * and throw an OptimisticLockException if is discovered that the object being
	 * merged is a stale copy of the entity.
	 *
	 */
	@Test
	public void test3() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin test3");
		boolean pass = false;

		try {
			statefulBean.init(props);
			pass = statefulBean.test3();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception :", e);
		}

		if (!pass)
			throw new Exception("test3 failed");
	}

	/*
	 * @testName: test4
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:886; PERSISTENCE:SPEC:881
	 * 
	 * @test_Strategy: When a JTA application-managed entity manager is used, if the
	 * entity manager is created outside the scope of a current JTA transaction, it
	 * is the responsibility of the application to associate the entity manager with
	 * the transaction (if desired) by calling EntityManager.joinTransaction.
	 *
	 * The enitity manager factory is injected into the stateful session bean. The
	 * entity manager is obtained in the PostConstruct method of bean and closed
	 * with when the bean is removed by a business method annotated with the Remove
	 * annotation.
	 *
	 */
	@Test
	public void test4() throws Exception {

		logger.log(Logger.Level.TRACE, "Begin test4");
		boolean pass = false;

		try {
			statefulBean.init(props);
			pass = statefulBean.test4();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception :", e);
		}

		if (!pass)
			throw new Exception("test4 failed");
	}

	/*
	 * @testName: test5
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:164; PERSISTENCE:SPEC:2420;
	 * 
	 * @test_Strategy: Test the @PersistenceUnits and verify that a managed entity
	 * from one PU is not accessible in the other PU and visa versa.
	 */
	@Test
	public void test5() throws Exception {

		boolean pass = false;

		try {
			statefulBean2.init(props);
			pass = statefulBean2.test5();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception :", e);
		}

		if (!pass)
			throw new Exception("test5 failed");
	}

	@AfterEach
	public void cleanup() throws Exception {
		logger.log(Logger.Level.TRACE, "cleanup complete");
	}

}
