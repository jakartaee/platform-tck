/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.api.rs.processingexception;

import java.io.IOException;

import javax.ws.rs.ProcessingException;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = -9156519616224592459L;

  public JAXRSClient() {
    setContextRoot("/jaxrs_api_rs_processingexception_web");
  }

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
   * @testName: constructorWithRuntimeExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1010;
   * 
   * @test_Strategy: Constructs a new JAX-RS runtime processing exception with
   * the specified cause and a detail message of (cause==null ? null :
   * cause.toString())
   */
  public void constructorWithRuntimeExceptionTest() throws Fault {
    IllegalStateException ile = new IllegalStateException("TCK exception");
    ProcessingException mpe = new ProcessingException(ile);
    assertFault(mpe.getCause().equals(ile),
        "getCause does not work for ProcessingException and RuntimeException cause");
    assertFault(mpe.getMessage().equals(ile.toString()),
        "getMessage does not work for ProcessingException and RuntimeException cause");
  }

  /*
   * @testName: constructorWithCheckedExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1010;
   * 
   * @test_Strategy: Constructs a new JAX-RS runtime processing exception with
   * the specified cause and a detail message of (cause==null ? null :
   * cause.toString())
   */
  public void constructorWithCheckedExceptionTest() throws Fault {
    IOException ioe = new IOException("TCK exception");
    ProcessingException mpe = new ProcessingException(ioe);
    assertFault(mpe.getCause().equals(ioe),
        "getCause does not work for ProcessingException and CheckedException cause");
    assertFault(mpe.getMessage().equals(ioe.toString()),
        "getMessage does not work for ProcessingException and CheckedException cause");
  }

  /*
   * @testName: constructorWithNullThrowableTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1010;
   * 
   * @test_Strategy: Constructs a new JAX-RS runtime processing exception with
   * the specified cause and a detail message of (cause==null ? null :
   * cause.toString())
   */
  public void constructorWithNullThrowableTest() throws Fault {
    ProcessingException mpe = new ProcessingException((Throwable) null);
    assertFault(mpe.getCause() == null,
        "getCause does not work for ProcessingException and null cause");
    assertFault(mpe.getMessage() == null,
        "getMessage does not work for ProcessingException and null cause");
  }

  /*
   * @testName: constructorWithNullThrowableNullMessageTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1011;
   * 
   * @test_Strategy: Constructs a new JAX-RS runtime processing exception with
   * the specified detail message and cause.
   */
  public void constructorWithNullThrowableNullMessageTest() throws Fault {
    ProcessingException mpe = new ProcessingException((String) null,
        (Throwable) null);
    assertFault(mpe.getCause() == null,
        "getCause does not work for ProcessingException and null cause and null message");
    assertFault(mpe.getMessage() == null,
        "getMessage does not work for ProcessingException and null cause and null message");
  }

  /*
   * @testName: constructorWithNullThrowableNotNullMessageTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1011;
   * 
   * @test_Strategy: Constructs a new JAX-RS runtime processing exception with
   * the specified detail message and cause.
   */
  public void constructorWithNullThrowableNotNullMessageTest() throws Fault {
    String msg = "TCK Message";
    ProcessingException mpe = new ProcessingException(msg, (Throwable) null);
    assertFault(mpe.getCause() == null,
        "getCause does not work for ProcessingException and null cause and not null message");
    assertFault(mpe.getMessage().equals(msg),
        "getMessage does not work for ProcessingException and null cause and not null message");
  }

  /*
   * @testName: constructorWithRuntimeExceptionNullMessageTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1011;
   * 
   * @test_Strategy: Constructs a new JAX-RS runtime processing exception with
   * the specified detail message and cause.
   */
  public void constructorWithRuntimeExceptionNullMessageTest() throws Fault {
    IllegalStateException ise = new IllegalStateException(
        "JAXRS TCK exception");
    ProcessingException mpe = new ProcessingException((String) null, ise);
    assertFault(mpe.getCause().equals(ise),
        "getCause does not work for ProcessingException and RuntimeException and null message");
    assertFault(mpe.getMessage() == null,
        "getMessage does not work for ProcessingException and RuntimeException and null message");
  }

  /*
   * @testName: constructorWithCheckedExceptionNullMessageTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1011;
   * 
   * @test_Strategy: Constructs a new JAX-RS runtime processing exception with
   * the specified detail message and cause.
   */
  public void constructorWithCheckedExceptionNullMessageTest() throws Fault {
    IOException ioe = new IOException("JAXRS TCK exception");
    ProcessingException mpe = new ProcessingException((String) null, ioe);
    assertFault(mpe.getCause().equals(ioe),
        "getCause does not work for ProcessingException and CheckedException and null message");
    assertFault(mpe.getMessage() == null,
        "getMessage does not work for ProcessingException and CheckedException and null message");
  }

  /*
   * @testName: constructorWithRuntimeExceptionAndNotNullMessageTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1011;
   * 
   * @test_Strategy: Constructs a new JAX-RS runtime processing exception with
   * the specified detail message and cause.
   */
  public void constructorWithRuntimeExceptionAndNotNullMessageTest()
      throws Fault {
    String msg = "TCK Message";
    IllegalStateException ise = new IllegalStateException(
        "JAXRS TCK exception");
    ProcessingException mpe = new ProcessingException(msg, ise);
    assertFault(mpe.getCause().equals(ise),
        "getCause does not work for ProcessingException and RuntimeException and not null message");
    assertFault(mpe.getMessage().equals(msg),
        "getMessage does not work for ProcessingException and RuntimeException and not null message");
  }

  /*
   * @testName: constructorWithCheckedExceptionAndNotNullMessageTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1011;
   * 
   * @test_Strategy: Constructs a new JAX-RS runtime processing exception with
   * the specified detail message and cause.
   */
  public void constructorWithCheckedExceptionAndNotNullMessageTest()
      throws Fault {
    String msg = "TCK Message";
    IOException ioe = new IOException("JAXRS TCK exception");
    ProcessingException mpe = new ProcessingException(msg, ioe);
    assertFault(mpe.getCause().equals(ioe),
        "getCause does not work for ProcessingException and CheckedException and not null message");
    assertFault(mpe.getMessage().equals(msg),
        "getMessage does not work for ProcessingException and CheckedException and not null message");
  }

  /*
   * @testName: constructorWithNotNullMessageTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1012;
   * 
   * @test_Strategy: Constructs a new JAX-RS runtime processing exception with
   * the specified detail message.
   */
  public void constructorWithNotNullMessageTest() throws Fault {
    String msg = "TCK Message";
    ProcessingException mpe = new ProcessingException(msg);
    assertFault(mpe.getCause() == null,
        "getCause does not work for ProcessingException and not null message");
    assertFault(mpe.getMessage().equals(msg),
        "getMessage does not work for ProcessingException and not null message");
  }

  /*
   * @testName: constructorWithNullMessageTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1012;
   * 
   * @test_Strategy: Constructs a new JAX-RS runtime processing exception with
   * the specified detail message.
   */
  public void constructorWithNullMessageTest() throws Fault {
    ProcessingException mpe = new ProcessingException((String) null);
    assertFault(mpe.getCause() == null,
        "getCause does not work for ProcessingException and null message");
    assertFault(mpe.getMessage() == null,
        "getMessage does not work for ProcessingException and null message");
  }
}
