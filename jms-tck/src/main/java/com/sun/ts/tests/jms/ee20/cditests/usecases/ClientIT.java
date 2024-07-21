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

package com.sun.ts.tests.jms.ee20.cditests.usecases;

import java.lang.System.Logger;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.porting.TSURL;

import jakarta.ejb.EJB;


public class ClientIT {
	// The webserver defaults (overidden by harness properties)
	private static final String PROTOCOL = "http";

	private static final String HOSTNAME = "localhost";

	private static final int PORTNUM = 8000;

	private TSURL ctsurl = new TSURL();

	private Properties props = null;

	private String hostname = HOSTNAME;

	private int portnum = PORTNUM;

	// URL properties used by the test
	private URL url = null;

	private URLConnection urlConn = null;

	@EJB(name = "ejb/CDIUseCasesCMBEAN1")
	static CMBean1IF cmbean1;

	@EJB(name = "ejb/CDIUseCasesCMBEAN2")
	static CMBean2IF cmbean2;

	@EJB(name = "ejb/CDIUseCasesBMBEAN1")
	static BMBean1IF bmbean1;

	@EJB(name = "ejb/CDIUseCasesBMBEAN2")
	static BMBean2IF bmbean2;

	private static final long serialVersionUID = 1L;

	long timeout;

	String user;

	String password;

	String mode;

	private static final Logger logger = (Logger) System.getLogger(ClientIT.class.getName());

	/* Test setup */

	/*
	 * @class.setup_props: jms_timeout; user; password; platform.mode;
	 * webServerHost; webServerPort;
	 */
	@BeforeEach
	public void setup() throws Exception {
		boolean pass = true;
		try {
			// get props
			timeout = Integer.parseInt(System.getProperty("jms_timeout"));
			user = System.getProperty("user");
			password = System.getProperty("password");
			mode = System.getProperty("platform.mode");
			hostname = System.getProperty("webServerHost");

			// check props for errors
			if (timeout < 1) {
				throw new Exception("'jms_timeout' (milliseconds) in must be > 0");
			}
			if (user == null) {
				throw new Exception("'user' is null ");
			}
			if (password == null) {
				throw new Exception("'password' is null ");
			}
			if (mode == null) {
				throw new Exception("'platform.mode' is null");
			}
			if (hostname == null) {
				throw new Exception("'webServerHost' is null");
			}
			try {
				portnum = Integer.parseInt(System.getProperty("webServerPort"));
			} catch (Exception e) {
				throw new Exception("'webServerPort' in must be a number");
			}
			logger.log(Logger.Level.INFO, "AppClient DEBUG: cmbean1=" + cmbean1 + " cmbean2=" + cmbean2);
			logger.log(Logger.Level.INFO, "AppClient DEBUG: bmbean1=" + bmbean1 + " bmbean2=" + bmbean2);
			if (cmbean1 == null || cmbean2 == null || bmbean1 == null || bmbean2 == null) {
				throw new Exception("setup failed: ejb injection failure");
			} else {
				// cmbean1.init(p);
				// cmbean2.init(p);
				// bmbean1.init(p);
				// bmbean2.init(p);
			}
		} catch (Exception e) {
			throw new Exception("setup failed:", e);
		}
		logger.log(Logger.Level.INFO, "setup ok");
	}

	@AfterEach
	public void cleanup() throws Exception {
		logger.log(Logger.Level.INFO, "cleanup ok");
	}

	/*
	 * @testName: beanUseCaseA
	 * 
	 * @assertion_ids: JMS:SPEC:277; JMS:SPEC:279; JMS:SPEC:280;
	 * 
	 * @test_Strategy: Use case A: Two methods on the same bean within separate
	 * transactions. A remote client obtains a reference to Bean1 and calls the
	 * methods method1a and method1b in turn.
	 *
	 * Bean1 is a stateless session bean configured to use container managed
	 * transactions with the two business methods method1a and method1b. Each method
	 * is configured to require a transaction. The bean has an injected JMSContext.
	 * Each method uses the context to send a message.
	 */
	@Test
	public void beanUseCaseA() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "------------");
			logger.log(Logger.Level.INFO, "BeanUseCaseA");
			logger.log(Logger.Level.INFO, "------------");
			cmbean1.method1a();
			cmbean1.method1b();
			boolean status = cmbean1.cleanupQueue(2);
			if (!status) {
				pass = false;
				logger.log(Logger.Level.ERROR, "beanUseCaseA cleanup of Queue failed");
			} else {
				logger.log(Logger.Level.INFO, "beanUseCaseA passed");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			logger.log(Logger.Level.ERROR, "beanUseCaseA failed");
			pass = false;
		}

		if (!pass) {
			throw new Exception("beanUseCaseA failed");
		}
	}

	/*
	 * @testName: beanUseCaseB
	 * 
	 * @assertion_ids: JMS:SPEC:277; JMS:SPEC:279; JMS:SPEC:280;
	 * 
	 * @test_Strategy: Use case B: Two methods on the same bean within the same
	 * transaction. A remote client obtains a reference to Bean1 and calls method2.
	 *
	 * Consider two stateless session beans, Bean1 and Bean2.
	 *
	 * Bean1 is configured to use container managed transactions and has a business
	 * method method2, which is configured to require a transaction. This invokes
	 * method2a and method2b on Bean2 in turn.
	 *
	 * Bean2 is also configured to use container managed transactions and has the
	 * two business methods method2a and method2b which are configured to require a
	 * transaction. The bean has an injected JMSContext. Each method uses the
	 * context to send a message.
	 */
	@Test
	public void beanUseCaseB() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "------------");
			logger.log(Logger.Level.INFO, "BeanUseCaseB");
			logger.log(Logger.Level.INFO, "------------");
			cmbean1.method2();
			boolean status = cmbean1.cleanupQueue(2);
			if (!status) {
				pass = false;
				logger.log(Logger.Level.ERROR, "beanUseCaseB cleanup of Queue failed");
			} else {
				logger.log(Logger.Level.INFO, "beanUseCaseB passed");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			logger.log(Logger.Level.ERROR, "beanUseCaseB failed");
			pass = false;
		}

		if (!pass) {
			throw new Exception("beanUseCaseB failed");
		}
	}

	/*
	 * @testName: beanUseCaseC
	 * 
	 * @assertion_ids: JMS:SPEC:277; JMS:SPEC:279; JMS:SPEC:280;
	 * 
	 * @test_Strategy: Use case C: One bean which calls another within the same
	 * transaction transaction. A remote client obtains a reference to Bean1 and
	 * calls method3.
	 *
	 * Consider two stateless session beans, Bean1 and Bean2.
	 *
	 * Bean1 is configured to use container managed transactions and has the
	 * business method method3, which is configured to require a transaction. The
	 * bean has an injected JMSContext. method3 uses this context to send a message
	 * and then invokes method3 on Bean2.
	 *
	 * Bean2 is also configured to use container-managed transactions and has the
	 * business method method3, which is also configured to require a transaction.
	 * The bean also has an injected JMSContext with identical annotations to Bean1.
	 * method3 simply uses this context to send a second message.
	 */
	@Test
	public void beanUseCaseC() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "------------");
			logger.log(Logger.Level.INFO, "BeanUseCaseC");
			logger.log(Logger.Level.INFO, "------------");
			cmbean1.method3();
			boolean status = cmbean1.cleanupQueue(2);
			if (!status) {
				pass = false;
				logger.log(Logger.Level.ERROR, "beanUseCaseC cleanup of Queue failed");
			} else {
				logger.log(Logger.Level.INFO, "beanUseCaseC passed");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			logger.log(Logger.Level.ERROR, "beanUseCaseC failed");
			pass = false;
		}

		if (!pass) {
			throw new Exception("beanUseCaseC failed");
		}
	}

	/*
	 * @testName: beanUseCaseD
	 * 
	 * @assertion_ids: JMS:SPEC:277; JMS:SPEC:279; JMS:SPEC:280;
	 * 
	 * @test_Strategy: Use case D: One bean which sends two messages within the same
	 * transaction. A remote client obtains a reference to Bean1 and calls method4.
	 *
	 * Bean1 is configured to use container managed transactions and has a business
	 * method method4 which is configured to require a transaction. The bean has an
	 * injected JMSContext. method4 uses this context to send two messages.
	 */
	@Test
	public void beanUseCaseD() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "------------");
			logger.log(Logger.Level.INFO, "BeanUseCaseD");
			logger.log(Logger.Level.INFO, "------------");
			cmbean1.method4();
			boolean status = cmbean1.cleanupQueue(2);
			if (!status) {
				pass = false;
				logger.log(Logger.Level.ERROR, "beanUseCaseD cleanup of Queue failed");
			} else {
				logger.log(Logger.Level.INFO, "beanUseCaseD passed");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			logger.log(Logger.Level.ERROR, "beanUseCaseD failed");
			pass = false;
		}

		if (!pass) {
			throw new Exception("beanUseCaseD failed");
		}
	}

	/*
	 * @testName: beanUseCaseE
	 * 
	 * @assertion_ids: JMS:SPEC:277; JMS:SPEC:279; JMS:SPEC:280;
	 * 
	 * @test_Strategy: Use case E: One bean which sends two messages when there is
	 * no transaction.
	 *
	 * Consider a stateless session bean Bean1. This is configured to use
	 * bean-managed transactions and has a business method method1. The bean has an
	 * injected JMSContext. method1 does not start a transaction and uses the
	 * context to send two messages.
	 *
	 * A remote client obtains a reference to Bean1 and calls method1.
	 */
	@Test
	public void beanUseCaseE() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "------------");
			logger.log(Logger.Level.INFO, "BeanUseCaseE");
			logger.log(Logger.Level.INFO, "------------");
			bmbean1.method1();
			boolean status = bmbean1.cleanupQueue(2);
			if (!status) {
				pass = false;
				logger.log(Logger.Level.ERROR, "beanUseCaseE cleanup of Queue failed");
			} else {
				logger.log(Logger.Level.INFO, "beanUseCaseE passed");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			logger.log(Logger.Level.ERROR, "beanUseCaseE failed");
			pass = false;
		}

		if (!pass) {
			throw new Exception("beanUseCaseE failed");
		}
	}

	/*
	 * @testName: beanUseCaseF
	 * 
	 * @assertion_ids: JMS:SPEC:277; JMS:SPEC:279; JMS:SPEC:280;
	 * 
	 * @test_Strategy: Use case F: One bean which calls another when there is no
	 * transaction.
	 *
	 * Consider two stateless session beans, Bean1 and Bean2.
	 *
	 * Bean1 is configured to use bean-managed transactions and has a business
	 * method method2. The bean has an injected JMSContext. method2 does not start a
	 * transaction, uses this context to send a message, and then invoke method2 on
	 * Bean2.
	 *
	 * Bean2 is also configured to use bean-managed transactions and has a business
	 * method method2. The bean has an injected JMSContext. method2 does not start a
	 * transaction and simply uses this context to send a second message.
	 *
	 * A remote client obtains a reference to Bean1 and calls method2.
	 */
	@Test
	public void beanUseCaseF() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "------------");
			logger.log(Logger.Level.INFO, "BeanUseCaseF");
			logger.log(Logger.Level.INFO, "------------");
			bmbean1.method2();
			boolean status = bmbean1.cleanupQueue(2);
			if (!status) {
				pass = false;
				logger.log(Logger.Level.ERROR, "beanUseCaseF cleanup of Queue failed");
			} else {
				logger.log(Logger.Level.INFO, "beanUseCaseF passed");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			logger.log(Logger.Level.ERROR, "beanUseCaseF failed");
			pass = false;
		}

		if (!pass) {
			throw new Exception("beanUseCaseF failed");
		}
	}

	/*
	 * @testName: beanUseCaseG
	 * 
	 * @assertion_ids: JMS:SPEC:277; JMS:SPEC:279; JMS:SPEC:280;
	 * 
	 * @test_Strategy: Use case G: One bean method which uses two transactions.
	 *
	 * Consider a stateless session bean Bean1. This is configured to use
	 * bean-managed transactions and has a business method, method3. The bean has an
	 * injected JMSContext. method3 starts a transaction and uses the context to
	 * send two messages. It then commits the transaction and starts a second
	 * transaction. It then uses the context to send two further messages and
	 * finally commits the second transaction.
	 *
	 * A remote client obtains a reference to Bean1 and calls method3.
	 */
	@Test
	public void beanUseCaseG() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "------------");
			logger.log(Logger.Level.INFO, "BeanUseCaseG");
			logger.log(Logger.Level.INFO, "------------");
			bmbean1.method3();
			boolean status = bmbean1.cleanupQueue(4);
			if (!status) {
				pass = false;
				logger.log(Logger.Level.ERROR, "beanUseCaseG cleanup of Queue failed");
			} else {
				logger.log(Logger.Level.INFO, "beanUseCaseG passed");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			logger.log(Logger.Level.ERROR, "beanUseCaseG failed");
			pass = false;
		}

		if (!pass) {
			throw new Exception("beanUseCaseG failed");
		}
	}

	/*
	 * @testName: beanUseCaseH
	 * 
	 * @assertion_ids: JMS:SPEC:277; JMS:SPEC:279; JMS:SPEC:280;
	 * 
	 * @test_Strategy: Use case H. A bean which uses a context both outside and
	 * within a transaction.
	 *
	 * Consider a stateless session bean Bean1. This is configured to use
	 * bean-managed transactions and has a business method method4. The bean has an
	 * injected JMSContext. method4 does not start a transaction and uses the
	 * context variable to send two messages. It then starts a transaction and uses
	 * the context variable to send a third message. It then commits the transaction
	 * and uses the context variable to send a fourth and fifth more messages.
	 *
	 * A remote client obtains a reference to Bean1 and calls method4.
	 */
	@Test
	public void beanUseCaseH() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "------------");
			logger.log(Logger.Level.INFO, "BeanUseCaseH");
			logger.log(Logger.Level.INFO, "------------");
			bmbean1.method4();
			boolean status = bmbean1.cleanupQueue(5);
			if (!status) {
				pass = false;
				logger.log(Logger.Level.ERROR, "beanUseCaseH cleanup of Queue failed");
			} else {
				logger.log(Logger.Level.INFO, "beanUseCaseH passed");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			logger.log(Logger.Level.ERROR, "beanUseCaseH failed");
			pass = false;
		}

		if (!pass) {
			throw new Exception("beanUseCaseH failed");
		}
	}

	/*
	 * @testName: beanUseCaseJ
	 * 
	 * @assertion_ids: JMS:SPEC:277; JMS:SPEC:279; JMS:SPEC:280;
	 * 
	 * @test_Strategy: Use case J. Two separate container-managed transactions on
	 * the same thread, one suspended before the second is started
	 *
	 * Consider two stateless session beans, Bean1 and Bean2.
	 * 
	 * Bean1 is configured to use container-managed transactions and has a business
	 * method method5, which is configured to require a transaction. The bean has an
	 * injected JMSContext. method5 uses this context to send a message and then
	 * invokes method5 on bean2. It then sends a further message.
	 *
	 * Bean2 is also configured to use container-managed transactions and has a
	 * business method method5, which is configured to require a new transaction.
	 * The bean has an injected JMSContext. method5 simply uses this context to send
	 * a message.
	 *
	 * A remote client obtains a reference to Bean1 and calls method5.
	 */
	@Test
	public void beanUseCaseJ() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "------------");
			logger.log(Logger.Level.INFO, "BeanUseCaseJ");
			logger.log(Logger.Level.INFO, "------------");
			cmbean1.method5();
			boolean status = cmbean1.cleanupQueue(3);
			if (!status) {
				pass = false;
				logger.log(Logger.Level.ERROR, "beanUseCaseJ cleanup of Queue failed");
			} else {
				logger.log(Logger.Level.INFO, "beanUseCaseJ passed");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			logger.log(Logger.Level.ERROR, "beanUseCaseJ failed");
			pass = false;
		}

		if (!pass) {
			throw new Exception("beanUseCaseJ failed");
		}
	}

	/*
	 * @testName: beanUseCaseK
	 * 
	 * @assertion_ids: JMS:SPEC:277; JMS:SPEC:279; JMS:SPEC:280;
	 * 
	 * @test_Strategy: Use case K. One bean which calls another within the same
	 * transaction, but using different connection factories
	 *
	 * (Note that this use case is identical to use case C except that the two beans
	 * specify different connection factories).
	 *
	 * Consider two stateless session beans, Bean1 and Bean2
	 *
	 * Bean1 is configured to use container-managed transactions and has a business
	 * method method6, which is configured to require a transaction. The bean has an
	 * injected JMSContext. method6 uses this context to send a message and then
	 * invokes method6 on Bean2.
	 *
	 * Bean2 is also configured to use container-managed transactions and has a
	 * business method method6, which is also configured to require a transaction.
	 * The bean also has an injected JMSContext to Bean1, but specifies a different
	 * connection factory. method6 simply uses this context to send a second
	 * message.
	 *
	 * A remote client obtains a reference to Bean1 and calls method6
	 */
	@Test
	public void beanUseCaseK() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "------------");
			logger.log(Logger.Level.INFO, "BeanUseCaseK");
			logger.log(Logger.Level.INFO, "------------");
			cmbean1.method6();
			boolean status = cmbean1.cleanupQueue(2);
			if (!status) {
				pass = false;
				logger.log(Logger.Level.ERROR, "beanUseCaseK cleanup of Queue failed");
			} else {
				logger.log(Logger.Level.INFO, "beanUseCaseK passed");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			logger.log(Logger.Level.ERROR, "beanUseCaseK failed");
			pass = false;
		}

		if (!pass) {
			throw new Exception("beanUseCaseK failed");
		}
	}
}
