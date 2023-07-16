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

package com.sun.ts.tests.jms.ee20.ra.activationconfig.queue.selectordups.descriptor;

import java.lang.System.Logger;

import org.junit.jupiter.api.Test;


public class ClientIT extends com.sun.ts.tests.jms.ee20.ra.activationconfig.common.QueueClientBaseIT {

	private static final Logger logger = (Logger) System.getLogger(ClientIT.class.getName());

	/*
	 * @class.setup_props: jms_timeout; user; password;
	 */

	/*
	 * @testName: test1
	 * 
	 * @assertion_ids: JMS:SPEC:276; JMS:SPEC:276.1; JMS:SPEC:276.2; JMS:SPEC:276.3;
	 * JMS:SPEC:276.4; JMS:SPEC:276.5;
	 * 
	 * @test_Strategy: test activation-config related elements in deployment
	 * descriptors, and their annotation counterparts.
	 *
	 * Sends message and waits for response. The message should reach the target
	 * MDB, and a response should be received by this client.
	 */
	@Test
	public void test1() throws Exception {
		logger.log(Logger.Level.INFO, "Testing the following activationConfig properties");
		logger.log(Logger.Level.INFO, "  connectionFactoryLookup=jms/QueueConnectionFactory");
		logger.log(Logger.Level.INFO, "  destinationLookup=MDB_QUEUE");
		logger.log(Logger.Level.INFO, "  destinationType=jakarta.jms.Queue");
		logger.log(Logger.Level.INFO, "  acknowledgeMode=Dups-ok-acknowledge");
		logger.log(Logger.Level.INFO, "  messageSelector=\"(COM_SUN_JMS_TESTNAME='test1' AND TestCaseNum < 1)\"");
		logger.log(Logger.Level.INFO, "Send message that MATCHES the message selector");
		logger.log(Logger.Level.INFO, "Set message property: COM_SUN_JMS_TESTNAME=test1");
		logger.log(Logger.Level.INFO, "Set message property: TestCaseNum=0");
		logger.log(Logger.Level.INFO, "Must receive message back since it MATCHES message selector");
		super.test1();
	}

	/*
	 * @testName: negativeTest1
	 * 
	 * @assertion_ids: JMS:SPEC:276; JMS:SPEC:276.1; JMS:SPEC:276.2; JMS:SPEC:276.3;
	 * JMS:SPEC:276.4; JMS:SPEC:276.5;
	 * 
	 * @test_Strategy: test activation-config related elements in deployment
	 * descriptors, and their annotation counterparts.
	 *
	 * Sends message and waits for response. The message should not reach the target
	 * MDB, and no response should be received by this client.
	 */
	@Test
	public void negativeTest1() throws Exception {
		logger.log(Logger.Level.INFO, "Testing the following activationConfig properties");
		logger.log(Logger.Level.INFO, "  connectionFactoryLookup=jms/QueueConnectionFactory");
		logger.log(Logger.Level.INFO, "  destinationLookup=MDB_QUEUE");
		logger.log(Logger.Level.INFO, "  destinationType=jakarta.jms.Queue");
		logger.log(Logger.Level.INFO, "  acknowledgeMode=Dups-ok-acknowledge");
		logger.log(Logger.Level.INFO, "  messageSelector=\"(COM_SUN_JMS_TESTNAME='test1' AND TestCaseNum < 1)\"");
		logger.log(Logger.Level.INFO, "Send message that DOES NOT MATCH the message selector");
		logger.log(Logger.Level.INFO, "Set message property: COM_SUN_JMS_TESTNAME=test1");
		logger.log(Logger.Level.INFO, "Set message property: TestCaseNum=1");
		logger.log(Logger.Level.INFO, "Must not receive message back since it DOES NOT MATCH message selector");
		super.negativeTest1();
	}

	/*
	 * @testName: negativeTest2
	 * 
	 * @assertion_ids: JMS:SPEC:276; JMS:SPEC:276.1; JMS:SPEC:276.2; JMS:SPEC:276.3;
	 * JMS:SPEC:276.4; JMS:SPEC:276.5;
	 * 
	 * @test_Strategy: test activation-config related elements in deployment
	 * descriptors, and their annotation counterparts.
	 *
	 * Sends message and waits for response. The message should not reach the target
	 * MDB, and no response should be received by this client.
	 */
	@Test
	public void negativeTest2() throws Exception {
		logger.log(Logger.Level.INFO, "Testing the following activationConfig properties");
		logger.log(Logger.Level.INFO, "  connectionFactoryLookup=jms/QueueConnectionFactory");
		logger.log(Logger.Level.INFO, "  destinationLookup=MDB_QUEUE");
		logger.log(Logger.Level.INFO, "  destinationType=jakarta.jms.Queue");
		logger.log(Logger.Level.INFO, "  acknowledgeMode=Dups-ok-acknowledge");
		logger.log(Logger.Level.INFO, "  messageSelector=\"(COM_SUN_JMS_TESTNAME='test1' AND TestCaseNum < 1)\"");
		logger.log(Logger.Level.INFO, "Send message that DOES NOT MATCH the message selector");
		logger.log(Logger.Level.INFO, "Set message property: COM_SUN_JMS_TESTNAME=negativeTest2");
		logger.log(Logger.Level.INFO, "Set message property: TestCaseNum=0");
		logger.log(Logger.Level.INFO, "Must not receive message back since it DOES NOT MATCH message selector");
		super.negativeTest2();
	}
}
