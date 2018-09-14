/*
 * Copyright (c) 2014, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.api.rs.core.nocontentexception;

import java.io.IOException;

import javax.ws.rs.core.NoContentException;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = 3227433857540172402L;

  private static final String MESSAGE = "Any NoContentException message";

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    new JAXRSClient().run(args);
  }

  /* Run test */

  /*
   * @testName: constructorStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1127;
   * 
   * @test_Strategy: Construct a new NoContentException instance. message - the
   * detail message (which is saved for later retrieval by the
   * Throwable.getMessage() method).
   */
  public void constructorStringTest() throws Fault {
    NoContentException e = new NoContentException(MESSAGE);
    assertMessage(MESSAGE, e);
  }

  /*
   * @testName: constructorNullStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1127;
   * 
   * @test_Strategy: Construct a new NoContentException instance. message - the
   * detail message (which is saved for later retrieval by the
   * Throwable.getMessage() method).
   */
  public void constructorNullStringTest() throws Fault {
    NoContentException e = new NoContentException((String) null);
    assertNullMessage(e);
  }

  /*
   * @testName: constructorStringThrowableTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1128;
   * 
   * @test_Strategy: Construct a new NoContentException instance.
   */
  public void constructorStringThrowableTest() throws Fault {
    Throwable[] throwables = new Throwable[] { new RuntimeException(),
        new IOException(), new Error(), new Throwable() };
    for (Throwable t : throwables) {
      NoContentException e = new NoContentException(MESSAGE, t);
      assertMessage(MESSAGE, e);
      assertCause(e, t);

      e = new NoContentException(null, t);
      assertNullMessage(e);
      assertCause(e, t);
    }
  }

  /*
   * @testName: constructorThrowableTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1129;
   * 
   * @test_Strategy: Construct a new NoContentException instance.
   */
  public void constructorThrowableTest() throws Fault {
    Throwable[] throwables = new Throwable[] { new RuntimeException(),
        new IOException(), new Error(), new Throwable() };
    for (Throwable t : throwables) {
      NoContentException e = new NoContentException(t);
      assertCause(e, t);
    }
  }

  // /////////////////////////////////////////////////////////////
  private static void //
      assertMessage(String message, Exception exception) throws Fault {
    assertEquals(message, exception.getMessage(),
        "Unexpected message in exception", exception.getMessage(),
        "expected was", message);
    logMsg("The exception contains expected", "#getMessage()");
  }

  private static void //
      assertNullMessage(Exception exception) throws Fault {
    assertNull(exception.getMessage(), "Unexpected message in exception",
        exception.getMessage(), "expected was null");
    logMsg("The exception contains expected", "#getMessage()");
  }

  private static void //
      assertCause(NoContentException e, Throwable expected) throws Fault {
    assertEquals(e.getCause(), expected, "#getCause does not contain expected",
        expected, "but", e.getCause());
    logMsg("getCause contains expected", expected);
  }
}
