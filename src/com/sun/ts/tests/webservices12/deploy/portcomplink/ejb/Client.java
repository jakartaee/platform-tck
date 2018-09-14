/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.webservices12.deploy.portcomplink.ejb;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.javatest.Status;

import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.util.Properties;

import com.sun.ts.tests.webservices12.deploy.portcomplink.ejb.inter.*;

public class Client extends EETest {
  InterModuleService svc = null;

  InterModuleSei port = null;

  private void getStub() throws Exception {
    TestUtil.logMsg(
        "Lookup webservice java:comp/env/service/WSportcomplinkejb/inter");
    InitialContext ctx = new InitialContext();
    InterModuleService svc = (InterModuleService) ctx
        .lookup("java:comp/env/service/WSportcomplinkejb/inter");
    TestUtil.logMsg("service=" + svc);
    TestUtil.logMsg("Get port from service");
    port = (InterModuleSei) svc.getPort(InterModuleSei.class);
    TestUtil.logMsg("port=" + port);
    JAXWS_Util.dumpTargetEndpointAddress(port);
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxws-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort;
   */
  public void setup(String[] args, Properties p) throws Fault {
    try {
      getStub();
    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

  /*
   * @testName: PortCompLinkEjbTest
   *
   * @assertion_ids: WS4EE:SPEC:175; WS4EE:SPEC:71; WS4EE:SPEC:72;
   * WS4EE:SPEC:73; WS4EE:SPEC:74; WS4EE:SPEC:76; WS4EE:SPEC:77; WS4EE:SPEC:78;
   * WS4EE:SPEC:79; WS4EE:SPEC:111;
   *
   * 
   * @test_Strategy: Call InterModuleSei implementation, which will in turn call
   * IntraModuleSei implementation.
   */
  public void PortCompLinkEjbTest() throws Fault {
    TestUtil.logMsg("PortCompLinkEjbTest");
    try {
      InterRequest req = new InterRequest();
      req.setArgument("WSPortCompLinkEjb");
      InterResponse ret = port.sayInter(req);
      if (ret == null) {
        TestUtil.logMsg(
            "test PortCompLinkEjb failed: return value from server is: null");
        throw new Fault("PortCompLinkTest failed");
      } else if (!ret.getArgument().equals("inter intra WSPortCompLinkEjb")) {
        TestUtil.logMsg(
            "test PortCompLinkEjb failed: return value from server is: " + ret);
        throw new Fault("PortCompLinkTest failed");
      }
    } catch (Throwable t) {
      TestUtil
          .logMsg("test PortCompLinkEjb failed: got exception " + t.toString());
      throw new Fault("PortCompLinkTest failed");
    }
    return;
  }
}
