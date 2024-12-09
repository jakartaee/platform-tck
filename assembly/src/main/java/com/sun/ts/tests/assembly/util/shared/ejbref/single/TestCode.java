/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)TestCode.java	1.11 03/05/16
 */

package com.sun.ts.tests.assembly.util.shared.ejbref.single;

import java.util.Properties;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.assembly.util.refbean.StatefulExternal;
import com.sun.ts.tests.assembly.util.refbean.StatefulInternal;
import com.sun.ts.tests.assembly.util.refbean.StatelessExternal;
import com.sun.ts.tests.assembly.util.refbean.StatelessInternal;

public class TestCode {

  protected static final String prefix = "java:comp/env/ejb/";

  protected static final String statelessInternalName = prefix
      + "StatelessBean_SameJAR";

  protected static final String statelessExternalName = prefix
      + "StatelessBean_ExternalJAR";

  protected static final String statefulInternalName = prefix
      + "StatefulBean_SameJAR";

  protected static final String statefulExternalName = prefix
      + "StatefulBean_ExternalJAR";

  private static StatefulExternal ssfExternalBeanRef1 = null;

  private static StatefulInternal ssfInternalBeanRef1 = null;

  public static boolean testStatelessInternal(TSNamingContext nctx,
      Properties props) {

    StatelessInternal bean = null;
    boolean pass;

    try {
      bean = (StatelessInternal) nctx.lookup(statelessInternalName,
          StatelessInternal.class);
      bean.initLogging(props);

      pass = bean.isTestStatelessInternal();
    } catch (Exception e) {
    	e.printStackTrace();
      TestUtil.logErr("TestBean: Caught exception: " + e, e);
      pass = false;
    }

    return pass;
  }

  public static boolean testStatelessExternal(TSNamingContext nctx,
      Properties props) {

    StatelessExternal bean = null;
    boolean pass;

    try {
      bean = (StatelessExternal) nctx.lookup(statelessExternalName,
          StatelessExternal.class);
      bean.initLogging(props);
      pass = bean.isTestStatelessExternal();

      pass = true;
    } catch (Exception e) {
    	e.printStackTrace();
      TestUtil.logErr(
          "TestBean: Exception in " + "testStatelessExternal(): " + e, e);
      pass = false;
    }
    return pass;
  }

  public static boolean testStatefulInternal(TSNamingContext nctx,
      Properties props) {

    boolean pass;

    try {
      ssfInternalBeanRef1 = (StatefulInternal) nctx.lookup(statefulInternalName,
          StatefulInternal.class);
      pass = ssfInternalBeanRef1.isTestStatefulInternal();

    } catch (Exception e) {
    	e.printStackTrace();
      TestUtil.logErr("Caught exception: " + e, e);
      pass = false;
    }

    return pass;
  }

  public static boolean testStatefulExternal(TSNamingContext nctx,
      Properties props) {

    boolean pass;

    try {
      ssfExternalBeanRef1 = (StatefulExternal) nctx.lookup(statefulExternalName,
          StatefulExternal.class);
      pass = ssfExternalBeanRef1.isTestStatefulExternal();
    } catch (Exception e) {
    	e.printStackTrace();
      TestUtil.logErr("TestBean: Got exception: " + e, e);
      pass = false;
    }

    return pass;
  }

}
