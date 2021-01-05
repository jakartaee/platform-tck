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
 * @(#)Client.java	1.13 03/05/16
 */

package com.sun.ts.tests.assembly.standalone.war;

import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

public class Client extends EETest {

  /** Name we use to lookup the URL */
  public static final String urlLookup = "java:comp/env/url/myURL";

  /** Name of the property set by the JSP */
  public static final String jspPropName = "standalone_war";

  private Properties props = null;

  private TSNamingContext nctx = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * webServerHost; webServerPort;
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    try {
      TestUtil.logTrace("[Client] Getting Naming Context...");
      nctx = new TSNamingContext();
    } catch (Exception e) {
      throw new Fault("Setup failed:" + e, e);
    }
  }

  /**
   * @testName: testStandaloneWar
   *
   * @assertion_ids: JavaEE:SPEC:261
   *
   * @test_Strategy: Package a war file containing a JSP
   *                 (assembly_standalone_war_component_jsp.jar).
   *
   *                 Package a .ear file containing an application client
   *                 accessing the JSP packaged in the stand-alone WAR module
   *                 (URL resource factory).
   *
   *                 Deploy the WAR module and the .ear file.
   *
   *                 Run the client and check that we can access this JSP at
   *                 runtime.
   *
   */
  public void testStandaloneWar() throws Fault {
    boolean pass = false;
    String value;
    URL myUrl;
    URLConnection urlConnection;
    Properties jspProps;

    try {
      TestUtil.logTrace("[Client] looking up " + urlLookup);
      myUrl = (java.net.URL) nctx.lookup(urlLookup);
      TestUtil.logTrace("[Client] get a new URL connection...");
      urlConnection = myUrl.openConnection();

      jspProps = TestUtil.getResponseProperties(urlConnection);
      value = jspProps.getProperty(jspPropName);
      pass = (null != value) && value.equals("true");

      if (!pass) {
        throw new Fault("Standalone war test failed: " + jspPropName + " = "
            + ((null == value) ? "null" : value) + ", expected 'true'!");
      }
    } catch (Exception e) {
      logErr("[Client] Stand-alone test failed: " + e);
      throw new Fault("Stand-alone test failed: ", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }

}
