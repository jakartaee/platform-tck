/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jms.ee20.cditests.mdb;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jms.common.*;
import com.sun.javatest.Status;

import javax.ejb.EJB;
import javax.jms.*;
import javax.naming.InitialContext;
import java.net.*;
import java.util.Properties;
import java.util.Iterator;

public class Client extends EETest {
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

  private String SERVLET = "/cditestsmdb_web/ServletTest";

  @EJB(name = "ejb/CDITestsMDBClntBean")
  static EjbClientIF ejbclient;

  private static final long serialVersionUID = 1L;

  long timeout;

  String user;

  String password;

  String mode;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: jms_timeout; user; password; platform.mode;
   * webServerHost; webServerPort;
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    boolean pass = true;
    try {
      // get props
      timeout = Integer.parseInt(p.getProperty("jms_timeout"));
      user = p.getProperty("user");
      password = p.getProperty("password");
      mode = p.getProperty("platform.mode");
      hostname = p.getProperty("webServerHost");

      // check props for errors
      if (timeout < 1) {
        throw new Exception(
            "'jms_timeout' (milliseconds) in ts.jte must be > 0");
      }
      if (user == null) {
        throw new Exception("'user' in ts.jte must not be null ");
      }
      if (password == null) {
        throw new Exception("'password' in ts.jte must not be null ");
      }
      if (mode == null) {
        throw new Exception("'platform.mode' in ts.jte must not be null");
      }
      if (hostname == null) {
        throw new Exception("'webServerHost' in ts.jte must not be null");
      }
      try {
        portnum = Integer.parseInt(p.getProperty("webServerPort"));
      } catch (Exception e) {
        throw new Exception("'webServerPort' in ts.jte must be a number");
      }
      TestUtil.logMsg("AppClient DEBUG: ejbclient=" + ejbclient);
      if (ejbclient == null) {
        throw new Fault("setup failed: ejbclient injection failure");
      } else {
        ejbclient.init(p);
      }
    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
    ejbclient.init(p);
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

  /*
   * @testName: testCDIInjectionOfMDBWithQueueReplyFromEjb
   * 
   * @assertion_ids: JMS:JAVADOC:1120; JMS:JAVADOC:1121; JMS:JAVADOC:1127;
   * JMS:JAVADOC:1128; JMS:SPEC:280;
   * 
   * @test_Strategy: Test CDI injection in a MDB. Send a message to the MDB and
   * MDB sends a reply back to the Reply Queue using the CDI injected
   * JMSContext.
   */
  public void testCDIInjectionOfMDBWithQueueReplyFromEjb() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("------------------------------------------");
      TestUtil.logMsg("testCDIInjectionOfMDBWithQueueReplyFromEjb");
      TestUtil.logMsg("------------------------------------------");
      boolean passEjb = ejbclient
          .echo("testCDIInjectionOfMDBWithQueueReplyFromEjb");
      if (!passEjb) {
        pass = false;
        TestUtil.logErr("CDI injection test failed from Ejb");
      } else {
        TestUtil.logMsg("CDI injection test passed from Ejb");
      }
      passEjb = ejbclient.echo("testCDIInjectionOfMDBWithQueueReplyFromEjb");
      if (!passEjb) {
        pass = false;
        TestUtil.logErr("CDI injection test failed from Ejb");
      } else {
        TestUtil.logMsg("CDI injection test passed from Ejb");
      }
    } catch (Exception e) {
      TestUtil.logErr("CDI injection test failed from Ejb");
      pass = false;
    }

    if (!pass) {
      throw new Fault("testCDIInjectionOfMDBWithQueueReplyFromEjb failed");
    }
  }

  /*
   * @testName: testCDIInjectionOfMDBWithTopicReplyFromEjb
   * 
   * @assertion_ids: JMS:JAVADOC:1120; JMS:JAVADOC:1121; JMS:JAVADOC:1127;
   * JMS:JAVADOC:1128; JMS:SPEC:280;
   * 
   * @test_Strategy: Test CDI injection in a MDB. Send a message to the MDB and
   * MDB sends a reply back to the Reply Topic using the CDI injected
   * JMSContext.
   */
  public void testCDIInjectionOfMDBWithTopicReplyFromEjb() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("------------------------------------------");
      TestUtil.logMsg("testCDIInjectionOfMDBWithTopicReplyFromEjb");
      TestUtil.logMsg("------------------------------------------");
      boolean passEjb = ejbclient
          .echo("testCDIInjectionOfMDBWithTopicReplyFromEjb");
      if (!passEjb) {
        pass = false;
        TestUtil.logErr("CDI injection test failed from Ejb");
      } else {
        TestUtil.logMsg("CDI injection test passed from Ejb");
      }
      passEjb = ejbclient.echo("testCDIInjectionOfMDBWithTopicReplyFromEjb");
      if (!passEjb) {
        pass = false;
        TestUtil.logErr("CDI injection test failed from Ejb");
      } else {
        TestUtil.logMsg("CDI injection test passed from Ejb");
      }
    } catch (Exception e) {
      TestUtil.logErr("CDI injection test failed from Ejb");
      pass = false;
    }

    if (!pass) {
      throw new Fault("testCDIInjectionOfMDBWithTopicReplyFromEjb failed");
    }
  }
}
