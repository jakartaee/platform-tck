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
 * $Id$
 */

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.streaming.exceptiontests;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.rpc.*;
import javax.xml.rpc.handler.*;

import com.sun.javatest.Status;

// Import implementation specific classes to test
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.sp.*;
import com.sun.xml.rpc.util.localization.*;

public class Client extends EETest {
  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props:
   */

  public void setup(String[] args, Properties p) throws Fault {
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: StreamingExceptionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void StreamingExceptionTest() throws Fault {
    TestUtil.logTrace("StreamingExceptionTest");
    boolean pass = true;
    String bn = "english";
    String rb;
    String key = "My Key";
    String arg = "My Arg";
    Object args[] = { "My Arg1", "My Arg2" };
    IOException ioe = new IOException("My IO Exception");
    ParseException pe = new ParseException("My Parse Exception");
    StreamingException v;
    try {
      TestUtil.logMsg("Call StreamingException(String key) constructor");
      v = new StreamingException(key);
      TestUtil.logMsg("Call getResourceBundleName() method");
      rb = v.getResourceBundleName();
      TestUtil.logMsg(
          "Call StreamingException(String key, String arg) constructor");
      v = new StreamingException(key, arg);
      TestUtil.logMsg(
          "Call StreamingException(String key, Object[] args) constructor");
      v = new StreamingException(key, args);
      TestUtil.logMsg(
          "Call StreamingException(String key, Localizable arg) constructor");
      v = new StreamingException(key, new LocalizableMessage(bn, key));
      TestUtil.logMsg("Call StreamingException(IOException) constructor");
      v = new StreamingException(ioe);
      TestUtil.logMsg("Call StreamingException(ParseException) constructor");
      v = new StreamingException(pe);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("StreamingExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("StreamingExceptionTest failed");
  }

  /*
   * @testName: XMLReaderExceptionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void XMLReaderExceptionTest() throws Fault {
    TestUtil.logTrace("XMLReaderExceptionTest");
    boolean pass = true;
    String bn = "english";
    String rb;
    String key = "My Key";
    String arg = "My Arg";
    Object args[] = { "My Arg1", "My Arg2" };
    XMLReaderException v;
    try {
      TestUtil.logMsg("Call XMLReaderException(String key) constructor");
      v = new XMLReaderException(key);
      TestUtil.logMsg("Call getResourceBundleName() method");
      rb = v.getResourceBundleName();
      TestUtil.logMsg(
          "Call XMLReaderException(String key, String arg) constructor");
      v = new XMLReaderException(key, arg);
      TestUtil.logMsg(
          "Call XMLReaderException(String key, Object[] args) constructor");
      v = new XMLReaderException(key, args);
      TestUtil.logMsg(
          "Call XMLReaderException(String key, Localizable arg) constructor");
      v = new XMLReaderException(key, new LocalizableMessage(bn, key));
      TestUtil.logMsg("Call XMLReaderException(Localizable arg) constructor");
      v = new XMLReaderException(new LocalizableMessage(bn, key));
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("XMLReaderExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("XMLReaderExceptionTest failed");
  }

  /*
   * @testName: XMLWriterExceptionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void XMLWriterExceptionTest() throws Fault {
    TestUtil.logTrace("XMLWriterExceptionTest");
    boolean pass = true;
    String bn = "english";
    String rb;
    String key = "My Key";
    String arg = "My Arg";
    Object args[] = { "My Arg1", "My Arg2" };
    XMLWriterException v;
    try {
      TestUtil.logMsg("Call XMLWriterException(String key) constructor");
      v = new XMLWriterException(key);
      TestUtil.logMsg("Call getResourceBundleName() method");
      rb = v.getResourceBundleName();
      TestUtil.logMsg(
          "Call XMLWriterException(String key, String arg) constructor");
      v = new XMLWriterException(key, arg);
      TestUtil.logMsg(
          "Call XMLWriterException(String key, Object[] args) constructor");
      v = new XMLWriterException(key, args);
      TestUtil.logMsg(
          "Call XMLWriterException(String key, Localizable arg) constructor");
      v = new XMLWriterException(key, new LocalizableMessage(bn, key));
      TestUtil.logMsg("Call XMLWriterException(Localizable arg) constructor");
      v = new XMLWriterException(new LocalizableMessage(bn, key));
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("XMLWriterExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("XMLWriterExceptionTest failed");
  }
}
