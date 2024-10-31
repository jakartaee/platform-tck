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
 * $Id$
 */

package com.sun.ts.tests.common.vehicle.stateless3;

import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.vehicle.VehicleRunnable;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Stateless3VehicleRunner implements VehicleRunnable {

  public static final String STATELESS3_REF_NAME = "java:comp/env/ejb/Stateless3VehicleBean";

  public Status run(String[] args, Properties props) {
    Status sTestStatus = null;
    Class beanType = Stateless3VehicleIF.class;
    String refName = beanType.getName();
    try {
      TSNamingContext jc = new TSNamingContext();
      Stateless3VehicleIF bean = null;
      try {
        bean = (Stateless3VehicleIF) jc.lookup(STATELESS3_REF_NAME);
      } catch (NamingException e) {

        TestUtil.logErr("NamingException looking up vehicle: " + refName, e);
        TestUtil.logErr("TSNamingContext.CS: " + TSNamingContext.class.getProtectionDomain().getCodeSource());
        TSNamingContext.dumpJndi("", new InitialContext());
        throw e;
      }
      TestUtil.logTrace("stateless3 runner looked up vehicle: " + bean);
      props.put("persistence.unit.name", "CTS-EM");
      sTestStatus = (bean.runTest(args, props)).toStatus();
    } catch (Exception e) {
      TestUtil.logErr("Test failed.", e);
      sTestStatus = Status.failed("Test run in stateless3 vehicle failed.");
    }
    return sTestStatus;
  }
}
