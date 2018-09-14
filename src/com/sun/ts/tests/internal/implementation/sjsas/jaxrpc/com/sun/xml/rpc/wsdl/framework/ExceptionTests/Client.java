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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.wsdl.framework.ExceptionTests;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.rpc.*;
import javax.xml.namespace.QName;

import com.sun.javatest.Status;

// Import implementation specific classes to test
import org.xml.sax.InputSource;
import com.sun.xml.rpc.wsdl.framework.*;
import com.sun.xml.rpc.wsdl.document.*;
import com.sun.xml.rpc.wsdl.parser.*;
import com.sun.xml.rpc.util.localization.*;

public class Client extends EETest {
  private Properties props = null;

  private final static String FS = System.getProperty("file.separator");

  private final static String WSDL_DOC = "MultiInterfaceTestService.wsdl";

  private final static String SERVICE_NAME = "MultiInterfaceTestService";

  private static final String baseDir = "src/com/sun/ts/tests/internal/implementation/sjsas/jaxrpc/com/sun/xml/rpc/wsdl/framework/ExceptionTests/";

  WSDLDocument document;

  Definitions definitions;

  Types types;

  PortType portType;

  private Object getFirstObject(Iterator i) {
    if (i.hasNext())
      return i.next();
    else
      return null;
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: ts_home;
   */

  public void setup(String[] args, Properties p) throws Fault {
    String tsHome, tsBase;
    try {
      tsHome = p.getProperty("ts_home");
      logMsg("tsHome=" + tsHome);
      tsBase = tsHome + "/" + baseDir.replaceAll("/", FS);
      logMsg("tsBase=" + tsBase);
      WSDLParser parser = new WSDLParser();
      InputSource inputSource = new InputSource(
          new File(tsBase + WSDL_DOC).toURL().toString());
      document = parser.parse(inputSource);
      definitions = document.getDefinitions();
      types = definitions.getTypes(); // Entity
      portType = (PortType) getFirstObject(definitions.portTypes()); // GlobalEntity
    } catch (Exception e) {
      throw new Fault("setup failed", e);
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: DuplicateEntityExceptionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void DuplicateEntityExceptionTest() throws Fault {
    TestUtil.logTrace("DuplicateEntityExceptionTest");
    boolean pass = true;
    String name = "myname";
    String rb;
    DuplicateEntityException v;
    try {
      TestUtil
          .logMsg("Call DuplicateEntityException(GloballyKnown) constructor");
      v = new DuplicateEntityException(portType);
      TestUtil.logMsg("Call getResourceBundleName() method");
      rb = v.getResourceBundleName();
      TestUtil
          .logMsg("Call DuplicateEntityException(Entity, String) constructor");
      v = new DuplicateEntityException(types, name);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("DuplicateEntityExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("DuplicateEntityExceptionTest failed");
  }

  /*
   * @testName: NoSuchEntityExceptionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void NoSuchEntityExceptionTest() throws Fault {
    TestUtil.logTrace("NoSuchEntityExceptionTest");
    boolean pass = true;
    QName name = new QName("uri:foo", "bar");
    String id = name.toString();
    NoSuchEntityException v;
    String rb;
    try {
      TestUtil.logMsg("Call NoSuchEntityException(QName name) constructor");
      v = new NoSuchEntityException(name);
      TestUtil.logMsg("Call getResourceBundleName() method");
      rb = v.getResourceBundleName();
      TestUtil.logMsg("Call NoSuchEntityException(String id) constructor");
      v = new NoSuchEntityException(id);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("NoSuchEntityExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("NoSuchEntityExceptionTest failed");
  }

  /*
   * @testName: ParseExceptionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void ParseExceptionTest() throws Fault {
    TestUtil.logTrace("ParseExceptionTest");
    boolean pass = true;
    String bn = "english";
    String rb;
    String key = "My Key";
    String arg = "My Arg";
    Object args[] = { "My Arg1", "My Arg2" };
    ParseException v;
    try {
      TestUtil.logMsg("Call ParseException(String key) constructor");
      v = new ParseException(key);
      TestUtil.logMsg("Call getResourceBundleName() method");
      rb = v.getResourceBundleName();
      TestUtil
          .logMsg("Call ParseException(String key, String arg) constructor");
      v = new ParseException(key, arg);
      TestUtil
          .logMsg("Call ParseException(String key, Object[] args) constructor");
      v = new ParseException(key, args);
      TestUtil.logMsg(
          "Call ParseException(String key, Localizable arg) constructor");
      v = new ParseException(key, new LocalizableMessage(bn, key));
      TestUtil.logMsg(
          "Call ParseException(String key, String arg, Localizable arg) constructor");
      v = new ParseException(key, arg, new LocalizableMessage(bn, key));
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("ParseExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("ParseExceptionTest failed");
  }

  /*
   * @testName: ValidationExceptionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void ValidationExceptionTest() throws Fault {
    TestUtil.logTrace("ValidationExceptionTest");
    boolean pass = true;
    String bn = "english";
    String rb;
    String key = "My Key";
    String arg = "My Arg";
    Object args[] = { "My Arg1", "My Arg2" };
    ValidationException v;
    try {
      TestUtil.logMsg("Call ValidationException(String key) constructor");
      v = new ValidationException(key);
      TestUtil.logMsg("Call getResourceBundleName() method");
      rb = v.getResourceBundleName();
      TestUtil.logMsg(
          "Call ValidationException(String key, String arg) constructor");
      v = new ValidationException(key, arg);
      TestUtil.logMsg(
          "Call ValidationException(String key, Object[] args) constructor");
      v = new ValidationException(key, args);
      TestUtil.logMsg(
          "Call ValidationException(String key, Localizable arg) constructor");
      v = new ValidationException(key, new LocalizableMessage(bn, key));
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("ValidationExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("ValidationExceptionTest failed");
  }
}
