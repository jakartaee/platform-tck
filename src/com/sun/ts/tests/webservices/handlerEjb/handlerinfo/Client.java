/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.webservices.handlerEjb.handlerinfo;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxrpc.common.*;
import com.sun.javatest.Status;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.rmi.Remote;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.util.Properties;

public class Client extends EETest {

  private Hello port;

  private void getStub() throws Exception {
    TestUtil.logMsg("JNDI lookup for handlerinfohandler Service");
    InitialContext ctx = new InitialContext();
    Service svc = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/handlerinfohandler");
    TestUtil.logMsg("Service found");
    TestUtil.logMsg("Get port from Service");
    port = (Hello) svc.getPort(Hello.class);
    TestUtil.logMsg("Port obtained");
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxrpc-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort;
   */

  public void setup(String[] args, Properties p) throws Fault {
    try {
      getStub();
    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  private void printSeperationLine() {
    TestUtil.logMsg("---------------------------");
  }

  /*
   * @testName: checkOnce
   *
   * @assertion_ids: WS4EE:SPEC:102; WS4EE:SPEC:165; WS4EE:SPEC:100
   *
   * @test_Strategy:
   */
  public void checkOnce() throws Fault {
    TestUtil.logMsg("checkOnce");
    try {
      String txt = port.hello("Hi there");
      if (txt.equals("Hi there to you too!"))
        TestUtil.logMsg("checkOnce passed");
      else
        throw new RuntimeException("Msg returned from hello() incorrect");
    } catch (Throwable t) {
      TestUtil.logMsg("test checkOnce failed: got exception " + t.toString());
      throw new Fault("checkOnce failed");
    }
    return;
  }

  /*
   * @testName: checkTwice
   *
   * @assertion_ids: WS4EE:SPEC:102; WS4EE:SPEC:165; WS4EE:SPEC:100
   *
   * @test_Strategy:
   */
  public void checkTwice() throws Fault {
    TestUtil.logMsg("checkTwice");
    try {
      String txt = port.bye("Bye-bye");
      if (txt.equals("Bye-bye and take care"))
        TestUtil.logMsg("checkTwice passed");
      else
        throw new RuntimeException("Msg returned from bye() incorrect");
    } catch (Throwable t) {
      TestUtil.logMsg("test checkTwice failed: got exception " + t.toString());
      throw new Fault("checkTwice failed");
    }
    return;
  }
}
