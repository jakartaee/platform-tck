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

/*
 * @version      "@(#)j2eeresourceClient1.java 1.8     06/02/11 SMI"
 */

package com.sun.ts.tests.j2eetools.mgmt.ee.j2eeresource;

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
import javax.ejb.*;

// Management imports
import javax.management.*;
import javax.management.j2ee.Management;
import javax.management.j2ee.ManagementHome;

// Our utility classes
import com.sun.ts.tests.j2eetools.mgmt.common.MOUtils;
import com.sun.ts.tests.j2eetools.mgmt.common.BaseMO;
import com.sun.ts.tests.j2eetools.mgmt.common.KeyNotFoundException;

/*
 * @class.setup_props: user;
 *                     password; 
 *                     secured.ejb.vehicle.client;
 */

public class j2eeresourceClient1 extends BaseMO {

  /**** MANAGED OBJECT LOOKUP SECTION ****/

  private static final String searchFor = "*:j2eeType=J2EEResource,*";

  public String getMOName() {
    return (searchFor);
  }

  /**** ATTRIBUTE SECTION ***/

  // J2EEResource adds no new attributes

  /**** MANAGED OBJECT NAME'S REQUIRED KEYS SECTION ***/

  private static final String J2EE_TYPE_VALUE = MOUtils.J2EE_RESOURCE;

  private static final String NAME_VALUE = "j2eeresourceClient1";

  protected Object getTypeKeyValue() {
    return J2EE_TYPE_VALUE;
  }

  protected Object getNameKeyValue() {
    return NAME_VALUE;
  }

  /**** MANAGED OBJECT NAME'S PARENT KEYS SECTION ***/

  protected Object getServerKeyValue() {
    return "serverKeyValue";
  }

  public Map initParentKeys() {
    Map keys = super.initParentKeys();
    keys.put(MOUtils.J2EE_SERVER, getServerKeyValue());
    return keys;
  }

  /**** TESTS ****/

  /*
   * Note: This class is part of the management data model. However there is no
   * such managed object in the management runtime. This class serves as a base
   * class for the following classes: JCAResource JTAResource JDBCResource
   * JMSResource JNDIResource RMIIIOPResource JavaMailResource URLResource
   */

  /**
   * Run test in standalone mode
   */
  public static void main(String[] args) {
    j2eeresourceClient1 theTests = new j2eeresourceClient1();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

} // end class j2eeresourceClient1
