/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.ee.propagation.cm.jta;

import java.lang.System.Logger;

import org.junit.jupiter.api.Test;

import com.sun.ts.tests.jpa.ee.util.AbstractUrlClient;

public class Client extends AbstractUrlClient {

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	public static final String SERVLET_NAME = "ServletTest";

	public static final String CONTEXT_ROOT = "/jpa_ee_propagation_cm_jta_web";

	/*
	 * @class.setup_props: webServerHost; webServerPort;
	 */

	/*
	 * @testName: test1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:678; PERSISTENCE:SPEC:679;
	 * PERSISTENCE:SPEC:844; PERSISTENCE:SPEC:845; PERSISTENCE:SPEC:858;
	 * PERSISTENCE:SPEC:862; PERSISTENCE:SPEC:863; PERSISTENCE:JAVADOC:151;
	 * PERSISTENCE:JAVADOC:154
	 * 
	 * @test_Strategy: Client -> TestServlet -> Stateful Session 3.0 Bean -> Entity
	 *
	 * Container-Managed Transaction-Scoped Persistence Context
	 *
	 * A new persistence context begins with the container-managed entity manager
	 * [specifically, when one of the methods of the EntityManager interface is
	 * invoked] in the scope of an active JTA transaction and there is no current
	 * persistence context already associated with the JTA transaction.
	 *
	 * Create an Account entity, find it in the web components method.
	 *
	 * From the stateful bean, find the Account entity and ensure the entities are
	 * identical.
	 */
	@Test
	public void test1() throws Exception {
		TEST_PROPS.setProperty(APITEST, "test1");
		invoke();
	}

	/*
	 * @testName: test1a
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:678; PERSISTENCE:SPEC:679;
	 * PERSISTENCE:SPEC:844; PERSISTENCE:SPEC:845; PERSISTENCE:SPEC:858;
	 * PERSISTENCE:SPEC:862; PERSISTENCE:SPEC:863; PERSISTENCE:JAVADOC:151;
	 * PERSISTENCE:JAVADOC:154; PERSISTENCE:JAVADOC:153; PERSISTENCE:SPEC:1811;
	 * PERSISTENCE:SPEC:1812; PERSISTENCE:SPEC:1812.1; PERSISTENCE:SPEC:1813;
	 * 
	 * @test_Strategy: Client -> TestServlet -> Stateful Session 3.0 Bean -> Entity
	 *
	 * Container-Managed Transaction-Scoped Persistence Context
	 *
	 * A new persistence context begins with the container-managed entity manager
	 * [specifically, when one of the methods of the EntityManager interface is
	 * invoked] in the scope of an active JTA transaction and there is no current
	 * persistence context already associated with the JTA transaction.
	 *
	 * Create an Account entity, find it in the web components method.
	 *
	 * From the stateful bean, find the Account entity and ensure the entities are
	 * identical.
	 *
	 * This test makes use of a second PersistenceContext via the
	 * 
	 * @PersistenceContexts() annotation.
	 */
	@Test
	public void test1a() throws Exception {
		TEST_PROPS.setProperty(APITEST, "test1a");
		invoke();
	}

	/*
	 * @testName: test2
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:873; PERSISTENCE:SPEC:875;
	 * PERSISTENCE:SPEC:861
	 * 
	 * @test_Strategy: Client -> TestServlet -> Stateful Session 3.0 Bean -> Entity
	 *
	 * Container-Managed Transaction-Scoped Persistence Context
	 *
	 * A new persistence context begins with the container-managed entity manager
	 * [specifically, when one of the methods of the EntityManager interface is
	 * invoked] in the scope of an active JTA transaction and there is no current
	 * persistence context already associated with the JTA transaction.
	 *
	 * Create account entities, modify the data, and ensure the updates are
	 * available after transaction commit.
	 */
	@Test
	public void test2() throws Exception {
		TEST_PROPS.setProperty(APITEST, "test2");
		invoke();
	}

	/*
	 * @testName: test3
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:687
	 * 
	 * @test_Strategy:client -> TestServlet -> bean
	 *
	 * For both transaction-scoped and extended persistence contexts transaction
	 * rollback causes all "pre-existing" managed instances and removed instances
	 * [these are instances that were not persistent in the database at the start of
	 * the transaction] instances to become detached. The instances' state will be
	 * the state of the instances at the point at which the transaction was rolled
	 * back.
	 *
	 * With a container-managed transaction-scoped persistence context, ensure the
	 * appropriate behavior as defined above.
	 */
	@Test
	public void test3() throws Exception {
		TEST_PROPS.setProperty(APITEST, "test3");
		invoke();
	}

	/*
	 * @testName: getTransactionIllegalStateException
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:488
	 * 
	 * @test_Strategy:client -> TestServlet
	 *
	 * For JTA entity manager call getTransaction() and verify that
	 * IllegalStateException is thrown
	 */
	@Test
	public void getTransactionIllegalStateException() throws Exception {
		TEST_PROPS.setProperty(APITEST, "getTransactionIllegalStateException");
		invoke();
	}

	/*
	 * @testName: closeObjectTransactionRequiredExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:457
	 * 
	 * @test_Strategy:client -> TestServlet
	 *
	 * For JTA entity manager call close() and verify that IllegalStateException is
	 * thrown
	 */
	@Test
	public void closeObjectTransactionRequiredExceptionTest() throws Exception {
		TEST_PROPS.setProperty(APITEST, "closeObjectTransactionRequiredExceptionTest");
		invoke();
	}

	/*
	 * @testName: mergeObjectTransactionRequiredExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:505
	 * 
	 * @test_Strategy:client -> TestServlet
	 *
	 * For JTA entity manager call merge() and verify that
	 * TransactionRequiredException is thrown
	 */
	@Test
	public void mergeObjectTransactionRequiredExceptionTest() throws Exception {
		TEST_PROPS.setProperty(APITEST, "mergeObjectTransactionRequiredExceptionTest");
		invoke();
	}

	/*
	 * @testName: persistObjectTransactionRequiredExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:508
	 * 
	 * @test_Strategy:client -> TestServlet
	 *
	 * For JTA entity manager call persist() and verify that
	 * TransactionRequiredException is thrown
	 */
	@Test
	public void persistObjectTransactionRequiredExceptionTest() throws Exception {
		TEST_PROPS.setProperty(APITEST, "persistObjectTransactionRequiredExceptionTest");
		invoke();
	}

	/*
	 * @testName: refreshObjectTransactionRequiredExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:510
	 * 
	 * @test_Strategy:client -> TestServlet
	 *
	 * For JTA entity manager call refresh() and verify that
	 * TransactionRequiredException is thrown
	 */
	@Test
	public void refreshObjectTransactionRequiredExceptionTest() throws Exception {
		TEST_PROPS.setProperty(APITEST, "refreshObjectTransactionRequiredExceptionTest");
		invoke();
	}

	/*
	 * @testName: refreshObjectMapTransactionRequiredExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:513
	 * 
	 * @test_Strategy:client -> TestServlet
	 *
	 * For JTA entity manager call refresh() and verify that
	 * TransactionRequiredException is thrown
	 */
	@Test
	public void refreshObjectMapTransactionRequiredExceptionTest() throws Exception {
		TEST_PROPS.setProperty(APITEST, "refreshObjectMapTransactionRequiredExceptionTest");
		invoke();
	}

	/*
	 * @testName: refreshObjectLockModeTypeTransactionRequiredExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:516
	 * 
	 * @test_Strategy:client -> TestServlet
	 *
	 * For JTA entity manager call refresh() and verify that
	 * TransactionRequiredException is thrown
	 */
	@Test
	public void refreshObjectLockModeTypeTransactionRequiredExceptionTest() throws Exception {
		TEST_PROPS.setProperty(APITEST, "refreshObjectLockModeTypeTransactionRequiredExceptionTest");
		invoke();
	}

	/*
	 * @testName: refreshObjectLockModeTypeMapTransactionRequiredExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:522
	 * 
	 * @test_Strategy:client -> TestServlet
	 *
	 * For JTA entity manager call refresh() and verify that
	 * TransactionRequiredException is thrown
	 */
	@Test
	public void refreshObjectLockModeTypeMapTransactionRequiredExceptionTest() throws Exception {
		TEST_PROPS.setProperty(APITEST, "refreshObjectLockModeTypeMapTransactionRequiredExceptionTest");
		invoke();
	}

	/*
	 * @testName: removeObjectTransactionRequiredExceptionTest
	 * 
	 * @assertion_ids: PERSISTENCE:JAVADOC:528
	 * 
	 * @test_Strategy:client -> TestServlet
	 *
	 * For JTA entity manager call remove() and verify that
	 * TransactionRequiredException is thrown
	 */
	@Test
	public void removeObjectTransactionRequiredExceptionTest() throws Exception {
		TEST_PROPS.setProperty(APITEST, "removeObjectTransactionRequiredExceptionTest");
		invoke();
	}

}
