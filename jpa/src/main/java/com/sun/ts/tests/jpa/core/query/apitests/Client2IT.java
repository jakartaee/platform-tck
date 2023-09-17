/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.query.apitests;

import java.sql.Date;
import java.text.DecimalFormat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

public class Client2IT extends PMClientBase {

  final Department deptRef[] = new Department[5];

  public Client2IT() {
  }

@BeforeEach
  public void setupNoData() throws Exception {
    TestUtil.logTrace("setup");
    try {
      super.setup();
      TestUtil.logTrace("Done creating test data");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught in Setup: ", e);
      throw new Exception("Setup failed:", e);

    }
  }

@AfterEach
  public void cleanupNoData() throws Exception {
    TestUtil.logTrace("in cleanupNoData");
    super.cleanup();
  }


  /*
   * @testName: getParameterIntIllegalArgumentException2Test
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:562; PERSISTENCE:JAVADOC:652
   * 
   * @test_Strategy: create query and set a positional parameter. Verify calling
   * getParameter with a class that is not assignable to the type throws
   * IllegalArgumentException create TypedQuery and set a positional parameter.
   * Verify calling getParameter with a class that is not assignable to the type
   * throws IllegalArgumentException
   */
@Test
  public void getParameterIntIllegalArgumentException2Test() throws Exception {
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      TestUtil.logMsg("Testing Query version");
      Query query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = ?1")
          .setParameter(1, "Tom");
      query.getParameter(1, java.util.List.class);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass1 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    TestUtil.logMsg("Testing TypedQuery version");

    try {
      TypedQuery<Employee> query = getEntityManager()
          .createQuery("select e from Employee e where e.firstName = ?1",
              Employee.class)
          .setParameter(1, "Tom");
      query.getParameter(1, java.util.List.class);
      TestUtil.logErr("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      TestUtil.logTrace("Received expected IllegalArgumentException");
      pass2 = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }
    if (!pass1 || !pass2) {
      throw new Exception("getParameterIntIllegalArgumentException2Test failed");
    }
  }
}
