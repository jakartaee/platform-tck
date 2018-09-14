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
 * @(#)TestCode.java	1.11 03/05/16
 */

package com.sun.ts.tests.assembly.util.shared.ejbref.single;

import java.util.Properties;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.assembly.util.refbean.*;

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

  protected static final String BMPInternalName = prefix + "BMPBean_SameJAR";

  protected static final String BMPExternalName = prefix
      + "BMPBean_ExternalJAR";

  protected static final String CMP11InternalName = prefix
      + "CMP11Bean_SameJAR";

  protected static final String CMP11ExternalName = prefix
      + "CMP11Bean_ExternalJAR";

  protected static final String CMP20InternalName = prefix
      + "CMP20Bean_SameJAR";

  protected static final String CMP20ExternalName = prefix
      + "CMP20Bean_ExternalJAR";

  /**
   * Primary key to use for Entity beans - Careful about conflicts, we can be
   * called from another bean of the same type).
   */
  protected static final int safePK = 10;

  private static StatefulExternal ssfExternalBeanRef1 = null;

  private static StatefulInternal ssfInternalBeanRef1 = null;

  public static boolean testStatelessInternal(TSNamingContext nctx,
      Properties props) {

    StatelessInternalHome home = null;
    StatelessInternal bean = null;
    boolean pass;

    try {
      home = (StatelessInternalHome) nctx.lookup(statelessInternalName,
          StatelessInternalHome.class);
      bean = home.create();
      bean.initLogging(props);

      pass = bean.isTestStatelessInternal();
    } catch (Exception e) {
      TestUtil.logErr("TestBean: Caught exception: " + e, e);
      pass = false;
    }

    return pass;
  }

  public static boolean testStatelessExternal(TSNamingContext nctx,
      Properties props) {

    StatelessExternalHome home = null;
    StatelessExternal bean = null;
    boolean pass;

    try {
      home = (StatelessExternalHome) nctx.lookup(statelessExternalName,
          StatelessExternalHome.class);
      bean = home.create();
      bean.initLogging(props);
      pass = bean.isTestStatelessExternal();

      pass = true;
    } catch (Exception e) {
      TestUtil.logErr(
          "TestBean: Exception in " + "testStatelessExternal(): " + e, e);
      pass = false;
    }
    return pass;
  }

  public static boolean testStatefulInternal(TSNamingContext nctx,
      Properties props) {

    StatefulInternalHome home = null;
    boolean pass;

    try {
      home = (StatefulInternalHome) nctx.lookup(statefulInternalName,
          StatefulInternalHome.class);
      ssfInternalBeanRef1 = home.create(props);
      pass = ssfInternalBeanRef1.isTestStatefulInternal();

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e, e);
      pass = false;
    }

    return pass;
  }

  public static boolean testStatefulExternal(TSNamingContext nctx,
      Properties props) {

    StatefulExternalHome home = null;
    boolean pass;

    try {
      home = (StatefulExternalHome) nctx.lookup(statefulExternalName,
          StatefulExternalHome.class);
      ssfExternalBeanRef1 = home.create(props);
      pass = ssfExternalBeanRef1.isTestStatefulExternal();
    } catch (Exception e) {
      TestUtil.logErr("TestBean: Got exception: " + e, e);
      pass = false;
    }

    return pass;
  }

  public static boolean testBMPInternal(TSNamingContext nctx,
      Properties props) {

    BMPInternalHome home = null;
    BMPInternal bean = null;
    boolean pass;

    try {
      home = (BMPInternalHome) nctx.lookup(BMPInternalName,
          BMPInternalHome.class);
      bean = home.create(props, safePK, "coffee-1", 1);

      pass = bean.isTestBMPInternal();
    } catch (Exception e) {
      TestUtil.logErr("TestBean: Got exception: " + e, e);
      pass = false;
    } finally {
      try {
        bean.remove();
      } catch (Exception e) {
        TestUtil.logTrace(
            "TestBean: Ignoring exception on " + "bean remove: " + e, e);
      }
    }

    return pass;
  }

  public static boolean testBMPExternal(TSNamingContext nctx,
      Properties props) {

    BMPExternalHome home = null;
    BMPExternal bean = null;
    boolean pass;

    try {
      home = (BMPExternalHome) nctx.lookup(BMPExternalName,
          BMPExternalHome.class);
      bean = home.create(props, safePK, "coffee-1", 1);

      pass = bean.isTestBMPExternal();
    } catch (Exception e) {
      TestUtil.logErr("TestBean: Got exception: " + e, e);
      pass = false;
    } finally {
      try {
        bean.remove();
      } catch (Exception e) {
        TestUtil.logTrace(
            "TestBean: Ignoring exception on " + "bean remove: " + e, e);
      }

    }

    return pass;
  }

  public static boolean testCMP11Internal(TSNamingContext nctx,
      Properties props) {

    CMP11InternalHome home = null;
    CMP11Internal bean = null;
    boolean pass = false;

    try {
      TestUtil.logTrace("TestBean: Looking up " + CMP11InternalName);
      home = (CMP11InternalHome) nctx.lookup(CMP11InternalName,
          CMP11InternalHome.class);
      TestUtil.logTrace("TestBean: Creating bean " + CMP11InternalName);
      bean = home.create(props, safePK, "coffee-1", 1);

      pass = bean.isTestCMP11Internal();
    } catch (Exception e) {
      TestUtil.logErr("TestBean: Got exception: " + e, e);
      pass = false;
    } finally {
      try {
        bean.remove();
      } catch (Exception e) {
        TestUtil
            .logTrace("TestBean: Ignoring exception on " + "bean remove: " + e);
      }
    }

    return pass;
  }

  public static boolean testCMP11External(TSNamingContext nctx,
      Properties props) {

    CMP11ExternalHome home = null;
    CMP11External bean = null;
    boolean pass = false;

    try {
      TestUtil.logTrace("TestBean: Looking up " + CMP11ExternalName);
      home = (CMP11ExternalHome) nctx.lookup(CMP11ExternalName,
          CMP11ExternalHome.class);
      TestUtil.logTrace("TestBean: Creating bean " + CMP11ExternalName);
      bean = home.create(props, safePK, "coffee-1", 1);

      pass = bean.isTestCMP11External();
    } catch (Exception e) {
      TestUtil.logErr("TestBean: Got exception: " + e, e);
      pass = false;
    } finally {
      try {
        bean.remove();
      } catch (Exception e) {
        TestUtil
            .logTrace("TestBean: Ignoring exception on " + "bean remove: " + e);
      }
    }

    return pass;
  }

  public static boolean testCMP20Internal(TSNamingContext nctx,
      Properties props) {

    CMP20InternalHome home = null;
    CMP20Internal bean = null;
    boolean pass = false;

    try {
      TestUtil.logTrace("TestBean: Looking up " + CMP20InternalName);
      home = (CMP20InternalHome) nctx.lookup(CMP20InternalName,
          CMP20InternalHome.class);
      TestUtil.logTrace("TestBean: Creating bean " + CMP20InternalName);
      bean = home.create(props, safePK, "coffee-1", 1);

      pass = bean.isTestCMP20Internal();
    } catch (Exception e) {
      TestUtil.logErr("TestBean: Got exception: " + e, e);
      pass = false;
    } finally {
      try {
        bean.remove();
      } catch (Exception e) {
        TestUtil
            .logTrace("TestBean: Ignoring exception on " + "bean remove: " + e);
      }
    }

    return pass;
  }

  public static boolean testCMP20External(TSNamingContext nctx,
      Properties props) {

    CMP20ExternalHome home = null;
    CMP20External bean = null;
    boolean pass = false;

    try {
      TestUtil.logTrace("TestBean: Looking up " + CMP20ExternalName);
      home = (CMP20ExternalHome) nctx.lookup(CMP20ExternalName,
          CMP20ExternalHome.class);
      TestUtil.logTrace("TestBean: Creating bean " + CMP20ExternalName);
      bean = home.create(props, safePK, "coffee-1", 1);

      pass = bean.isTestCMP20External();
    } catch (Exception e) {
      TestUtil.logErr("TestBean: Got exception: " + e, e);
      pass = false;
    } finally {
      try {
        bean.remove();
      } catch (Exception e) {
        TestUtil
            .logTrace("TestBean: Ignoring exception on " + "bean remove: " + e);
      }
    }

    return pass;
  }

  public static void cleanUpStatefulBean() {
    TestUtil.logTrace("cleanUpStatefulBean");
    try {
      if (ssfInternalBeanRef1 != null) {
        TestUtil.logTrace("cleanUp Session StatefulInternal Bean");
        ssfInternalBeanRef1.remove();
        ssfInternalBeanRef1 = null;
      }
      if (ssfExternalBeanRef1 != null) {
        TestUtil.logTrace("cleanUp Session StatefulExternal Bean");
        ssfExternalBeanRef1.remove();
        ssfExternalBeanRef1 = null;
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "Exception caught trying to remove Stateful Session beans", e);
    }
  }

}
