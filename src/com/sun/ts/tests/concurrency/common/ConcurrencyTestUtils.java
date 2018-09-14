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

package com.sun.ts.tests.concurrency.common;

import java.util.*;
import java.net.*;
import java.util.concurrent.*;
import javax.enterprise.concurrent.*;
import javax.naming.*;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.porting.*;

public class ConcurrencyTestUtils {

  public static final String SCHEDULED_MANAGED_EXECUTOR_SVC_JNDI_NAME = "java:comp/DefaultManagedScheduledExecutorService";

  public static final String MANAGED_THREAD_FACTORY_SVC_JNDI_NAME = "java:comp/DefaultManagedThreadFactory";

  public static final long COMMON_CHECK_INTERVAL = 5 * 1000;

  public static final long COMMON_TASK_TIMEOUT = 30 * 1000;

  public static final int COMMON_CHECK_INTERVAL_IN_SECOND = 5;

  public static final int COMMON_TASK_TIMEOUT_IN_SECOND = 30;

  public static final String SERVLET_OP_ATTR_NAME = "opName";

  public static String SERVLET_OP_COUNTER_GETCOUNT = "getCount";

  public static String SERVLET_OP_COUNTER_INC = "inc";

  public static String SERVLET_OP_COUNTER_RESET = "reset";

  public static final String SERVLET_RETURN_SUCCESS = "success";

  public static final String SERVLET_RETURN_FAIL = "fail";

  public static final String SERVLET_OP_INHERITEDAPI_TESTAPISUBMIT = "testApiSubmit";

  public static final String SERVLET_OP_INHERITEDAPI_TESTAPIEXECUTE = "testApiExecute";

  public static final String SERVLET_OP_INHERITEDAPI_TESTAPIINVOKEALL = "testApiInvokeAll";

  public static final String SERVLET_OP_INHERITEDAPI_TESTAPIINVOKEANY = "testApiInvokeAny";

  public static final String SERVLET_OP_INHERITEDAPI_TESTAPISCHEDULE = "testApiSchedule";

  public static final String SERVLET_OP_INHERITEDAPI_TESTAPISCHEDULEATFIXEDRATE = "testApiScheduleAtFixedRate";

  public static final String SERVLET_OP_INHERITEDAPI_TESTAPISCHEDULEWITHFIXEDDELAY = "testApiScheduleWithFixedDelay";

  public static final String SERVLET_OP_FORBIDDENAPI_TESTAWAITTERMINATION = "testAwaitTermination";

  public static final String SERVLET_OP_FORBIDDENAPI_TESTISSHUTDOWN = "testIsShutdown";

  public static final String SERVLET_OP_FORBIDDENAPI_TESTISTERMINATED = "testIsTerminated";

  public static final String SERVLET_OP_FORBIDDENAPI_TESTSHUTDOWN = "testShutdown";

  public static final String SERVLET_OP_FORBIDDENAPI_TESTSHUTDOWNNOW = "testShutdownNow";

  public static void assertEquals(Object expected, Object actual) {
    String msg = "expected " + expected + " but you got " + actual;
    if (expected == null && actual == null) {
      return;
    }
    if (expected == null || actual == null) {
      throw new RuntimeException(msg);
    }
    if (!expected.equals(actual)) {
      throw new RuntimeException(msg);
    }
  }

  public static void assertInRange(Object[] range, Object actual) {
    String expected = "";
    for (Object each : range) {
      expected += each.toString();
      expected += ",";
    }
    expected = expected.substring(0, expected.length() - 1);
    String msg = "expected in " + expected + " but you got " + actual;
    for (Object each : range) {
      if (each.equals(actual)) {
        return;
      }
    }
    throw new RuntimeException(msg);
  }

  public static void asserIntInRange(int low, int high, int actual) {
    String msg = "expected in range " + low + " , " + high;
    msg += " but you got " + actual;
    if (actual < low || actual > high) {
      throw new RuntimeException(msg);
    }
  }

  public static ManagedScheduledExecutorService getManagedScheduledExecutorService() {
    try {
      InitialContext context = new InitialContext();
      ManagedScheduledExecutorService executorService = (ManagedScheduledExecutorService) context
          .lookup(SCHEDULED_MANAGED_EXECUTOR_SVC_JNDI_NAME);
      return executorService;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * The difference between this method and waitForTaskComplete is that some
   * scheduled task will return values for multiple times, in this situation
   * waitForTaskComplete does not work.
   */
  public static void waitTillFutureIsDone(Future future) {
    long start = System.currentTimeMillis();

    while (!future.isDone()) {
      try {
        Thread.sleep(COMMON_CHECK_INTERVAL);
      } catch (InterruptedException ignore) {
      }

      if ((System.currentTimeMillis() - start) > COMMON_TASK_TIMEOUT) {
        throw new RuntimeException("wait task timeout");
      }
    }
  }

  public static void sendClientRequest2Url(String protocol, String hostname,
      int portnum, String urlString, String testName) throws Exception {
    TSURL ctsurl = new TSURL();
    URL url = ctsurl.getURL(protocol, hostname, portnum, urlString);
    Properties prop = new Properties();
    prop.put(ConcurrencyTestUtils.SERVLET_OP_ATTR_NAME, testName);
    URLConnection urlConn = TestUtil.sendPostData(prop, url);
    String result = TestUtil.getResponse(urlConn);
    ConcurrencyTestUtils.assertEquals(
        ConcurrencyTestUtils.SERVLET_RETURN_SUCCESS, result.trim());
  }

}
