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
 * @version      "@(#)webmoduleClient1.java 1.7     06/02/11 SMI"
 */

package com.sun.ts.tests.j2eetools.mgmt.ee.webmodule;

// Java imports
import java.io.*;
import java.util.*;

// Harness imports
import com.sun.javatest.Status;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

// RMI imports
import java.rmi.RemoteException;

// EJB imports
import jakarta.ejb.*;

// Management imports
import javax.management.*;
import javax.management.j2ee.Management;
import javax.management.j2ee.ManagementHome;

// Our utility classes
import com.sun.ts.tests.j2eetools.mgmt.common.MOUtils;
import com.sun.ts.tests.j2eetools.mgmt.common.BaseMO;
import com.sun.ts.tests.j2eetools.mgmt.common.KeyNotFoundException;
import com.sun.ts.tests.j2eetools.mgmt.ee.j2eemodule.j2eemoduleClient1;

/*
 * @class.setup_props: user;
 *                     password; 
 *                     secured.ejb.vehicle.client;
 */

public class webmoduleClient1 extends j2eemoduleClient1 {

  /**** MANAGED OBJECT LOOKUP SECTION ****/

  private static final String searchFor = "*:j2eeType=WebModule,*";

  public String getMOName() {
    return (searchFor);
  }

  /**** ATTRIBUTE SECTION ***/

  private static final String SERVLETS_ATR = "servlets";

  protected Map initAttributes() {
    Map result = super.initAttributes();
    result.put(SERVLETS_ATR, new String[] { "servlet1", "servlet2" });
    return result;
  }

  /**** MANAGED OBJECT NAME'S REQUIRED KEYS SECTION ***/

  private static final String J2EE_TYPE_VALUE = MOUtils.WEB_MOD;

  private static final String NAME_VALUE = "webmoduleClient1";

  protected Object getTypeKeyValue() {
    return J2EE_TYPE_VALUE;
  }

  protected Object getNameKeyValue() {
    return NAME_VALUE;
  }

  /**** MANAGED OBJECT NAME'S PARENT KEYS SECTION ***/

  // Defined in super class - j2eemoduleClient1

  /**** TESTS ****/

  /*
   * Note: The implementations of the test methods are located in the BaseMO
   * class.
   */

  /*
   * @testName: testAttributes
   * 
   * @assertion_ids: J2EEMGMT:JAVADOC:1; J2EEMGMT:JAVADOC:4;
   * J2EEMGMT:JAVADOC:15; J2EEMgmt:SPEC:1; J2EEMgmt:SPEC:2; J2EEMgmt:SPEC:42;
   * J2EEMgmt:SPEC:43; J2EEMgmt:SPEC:44; J2EEMgmt:SPEC:47; J2EEMgmt:SPEC:48;
   * J2EEMgmt:SPEC:58; J2EEMgmt:SPEC:59;
   * 
   * @test_Strategy:
   */

  /*
   * @testName: testMandatoryKeys
   * 
   * @assertion_ids: J2EEMGMT:JAVADOC:1; J2EEMGMT:JAVADOC:4; J2EEMgmt:SPEC:3;
   * 
   * @test_Strategy:
   */

  /*
   * @testName: testParentKeys
   * 
   * @assertion_ids: J2EEMGMT:JAVADOC:1; J2EEMGMT:JAVADOC:4; J2EEMgmt:SPEC:3;
   * 
   * @test_Strategy:
   */

  /**
   * Run test in standalone mode
   */
  public static void main(String[] args) {
    webmoduleClient1 theTests = new webmoduleClient1();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

} // end class webmoduleClient1
