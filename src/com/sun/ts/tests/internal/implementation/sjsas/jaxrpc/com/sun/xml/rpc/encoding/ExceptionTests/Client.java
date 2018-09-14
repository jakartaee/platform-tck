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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.encoding.ExceptionTests;

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
import com.sun.xml.rpc.encoding.*;
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
   * @testName: EncodingExceptionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void EncodingExceptionTest() throws Fault {
    TestUtil.logTrace("EncodingExceptionTest");
    boolean pass = true;
    String bn = "english";
    String rb;
    String key = "My Key";
    String arg = "My Arg";
    Object args[] = { "My Arg1", "My Arg2" };
    EncodingException v;
    try {
      TestUtil.logMsg("Call EncodingException(String key) constructor");
      v = new EncodingException(key);
      TestUtil.logMsg("Call getResourceBundleName() method");
      rb = v.getResourceBundleName();
      TestUtil
          .logMsg("Call EncodingException(String key, String arg) constructor");
      v = new EncodingException(key, arg);
      TestUtil.logMsg(
          "Call EncodingException(String key, Object[] args) constructor");
      v = new EncodingException(key, args);
      TestUtil.logMsg(
          "Call EncodingException(String key, Localizable arg) constructor");
      v = new EncodingException(key, new LocalizableMessage(bn, key));
      TestUtil.logMsg("Call EncodingException(Localizable arg) constructor");
      v = new EncodingException(new LocalizableMessage(bn, key));
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("EncodingExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("EncodingExceptionTest failed");
  }

  /*
   * @testName: MissingTrailingBlockIDExceptionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void MissingTrailingBlockIDExceptionTest() throws Fault {
    TestUtil.logTrace("MissingTrailingBlockIDExceptionTest");
    boolean pass = true;
    String bn = "english";
    String rb;
    String key = "My Key";
    String arg = "My Arg";
    Object args[] = { "My Arg1", "My Arg2" };
    MissingTrailingBlockIDException v;
    try {
      TestUtil.logMsg(
          "Call MissingTrailingBlockIDException(String key) constructor");
      v = new MissingTrailingBlockIDException(key);
      TestUtil.logMsg("Call getResourceBundleName() method");
      rb = v.getResourceBundleName();
      TestUtil.logMsg(
          "Call MissingTrailingBlockIDException(String key, String arg) constructor");
      v = new MissingTrailingBlockIDException(key, arg);
      TestUtil.logMsg(
          "Call MissingTrailingBlockIDException(String key, Object[] args) constructor");
      v = new MissingTrailingBlockIDException(key, args);
      TestUtil.logMsg(
          "Call MissingTrailingBlockIDException(String key, Localizable arg) constructor");
      v = new MissingTrailingBlockIDException(key,
          new LocalizableMessage(bn, key));
      TestUtil.logMsg(
          "Call MissingTrailingBlockIDException(Localizable arg) constructor");
      v = new MissingTrailingBlockIDException(new LocalizableMessage(bn, key));
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MissingTrailingBlockIDExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("MissingTrailingBlockIDExceptionTest failed");
  }

  /*
   * @testName: SerializationExceptionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void SerializationExceptionTest() throws Fault {
    TestUtil.logTrace("SerializationExceptionTest");
    boolean pass = true;
    String bn = "english";
    String rb;
    String key = "My Key";
    String arg = "My Arg";
    Object args[] = { "My Arg1", "My Arg2" };
    SerializationException v;
    try {
      TestUtil.logMsg("Call SerializationException(String key) constructor");
      v = new SerializationException(key);
      TestUtil.logMsg("Call getResourceBundleName() method");
      rb = v.getResourceBundleName();
      TestUtil.logMsg(
          "Call SerializationException(String key, String arg) constructor");
      v = new SerializationException(key, arg);
      TestUtil.logMsg(
          "Call SerializationException(String key, Object[] args) constructor");
      v = new SerializationException(key, args);
      TestUtil.logMsg(
          "Call SerializationException(String key, Localizable arg) constructor");
      v = new SerializationException(key, new LocalizableMessage(bn, key));
      TestUtil
          .logMsg("Call SerializationException(Localizable arg) constructor");
      v = new SerializationException(new LocalizableMessage(bn, key));
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SerializationExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("SerializationExceptionTest failed");
  }

  /*
   * @testName: DeserializationExceptionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void DeserializationExceptionTest() throws Fault {
    TestUtil.logTrace("DeserializationExceptionTest");
    boolean pass = true;
    String bn = "english";
    String rb;
    String key = "My Key";
    String arg = "My Arg";
    Object args[] = { "My Arg1", "My Arg2" };
    DeserializationException v;
    try {
      TestUtil.logMsg("Call DeserializationException(String key) constructor");
      v = new DeserializationException(key);
      TestUtil.logMsg("Call getResourceBundleName() method");
      rb = v.getResourceBundleName();
      TestUtil.logMsg(
          "Call DeserializationException(String key, String arg) constructor");
      v = new DeserializationException(key, arg);
      TestUtil.logMsg(
          "Call DeserializationException(String key, Object[] args) constructor");
      v = new DeserializationException(key, args);
      TestUtil.logMsg(
          "Call DeserializationException(String key, Localizable arg) constructor");
      v = new DeserializationException(key, new LocalizableMessage(bn, key));
      TestUtil
          .logMsg("Call DeserializationException(Localizable arg) constructor");
      v = new DeserializationException(new LocalizableMessage(bn, key));
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("DeserializationExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("DeserializationExceptionTest failed");
  }

  /*
   * @testName: TypeMappingExceptionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void TypeMappingExceptionTest() throws Fault {
    TestUtil.logTrace("TypeMappingExceptionTest");
    boolean pass = true;
    String bn = "english";
    String rb;
    String key = "My Key";
    String arg = "My Arg";
    Object args[] = { "My Arg1", "My Arg2" };
    TypeMappingException v;
    try {
      TestUtil.logMsg("Call TypeMappingException(String key) constructor");
      v = new TypeMappingException(key);
      TestUtil.logMsg("Call getResourceBundleName() method");
      rb = v.getResourceBundleName();
      TestUtil.logMsg(
          "Call TypeMappingException(String key, String arg) constructor");
      v = new TypeMappingException(key, arg);
      TestUtil.logMsg(
          "Call TypeMappingException(String key, Object[] args) constructor");
      v = new TypeMappingException(key, args);
      TestUtil.logMsg(
          "Call TypeMappingException(String key, Localizable arg) constructor");
      v = new TypeMappingException(key, new LocalizableMessage(bn, key));
      TestUtil.logMsg("Call TypeMappingException(Localizable arg) constructor");
      v = new TypeMappingException(new LocalizableMessage(bn, key));
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("TypeMappingExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("TypeMappingExceptionTest failed");
  }
}
