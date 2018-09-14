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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.tools.wscompile.wsdl_gen_test;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import com.sun.javatest.Status;

// Internal implementation specific classes to tes
import com.sun.xml.rpc.tools.wscompile.*;
import com.sun.xml.rpc.tools.wscompile.CompileTool;
import com.sun.xml.rpc.processor.generator.GeneratorException;

public class Client extends EETest {
  private final static String FS = System.getProperty("file.separator");

  private final static String PS = System.getProperty("path.separator");

  private final static String CONFIG_FILE = "config.xml";

  private final static String CLASSES = "classes";

  private String _tempDir;

  private String _dataDir;

  private String _srcDir;

  private String _classDir;

  private String _ndDir;

  private String _configFile;

  private String _classPath;

  private String _mapFile;

  private String _modelFile;

  private static final String baseDir = "src/com/sun/ts/tests/internal/implementation/sjsas/jaxrpc/com/sun/xml/rpc/wsdl";

  private void removeFile(File file) {
    try {
      if (file.isDirectory())
        removeDirectory(file);
      else
        file.delete();
    } catch (Exception e) {
      TestUtil.logMsg("Exception: " + e);
    }
  }

  private void removeDirectory(File directory) throws IOException {
    if (directory.exists() && !directory.delete()) {
      // must empty the directory
      File[] files = directory.listFiles();
      for (int i = 0; i < files.length; ++i) {
        if (files[i].isDirectory())
          removeDirectory(files[i]);
        else
          files[i].delete();
      }
      directory.delete();
    }
  }

  private void assertTrue(boolean b) {
    if (b)
      TestUtil.logMsg("PASSED ...");
    else
      TestUtil.logMsg("FAILED ...");

  }

  private void createConfigFile() throws Exception {
    File configFile = new File(_configFile);
    FileWriter writer = new FileWriter(configFile);
    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    writer.write(
        "<configuration xmlns=\"http://java.sun.com/xml/ns/jax-rpc/ri/config\">");
    writer.write("<service name=\"MarshallTestService\" ");
    writer.write("targetNamespace=\"http://marshalltestservice.org/wsdl\" ");
    writer.write("typeNamespace=\"http://marshalltestservice.org/types\" ");
    writer.write("packageName=\"test\">");
    writer.write(
        "<interface name=\"com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.tools.wscompile.wsdl_gen_test.MarshallTest\" ");
    writer.write(
        "servantName=\"com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.tools.wscompile.wsdl_gen_test.MarshallTestImpl\"/>");
    writer.write("</service>");
    writer.write("</configuration>");
    writer.close();
  }

  private boolean invokeWsCompileServer(String features) {
    return (new CompileTool(System.out, "wscompile")).run(new String[] {
        "-classpath", _classPath, "-d", _classDir, "-s", _srcDir, "-nd", _ndDir,
        "-gen:server", "-keep", "-f:" + features, "-mapping", _mapFile,
        "-model", _modelFile, "-Xprintstacktrace", "-verbose", _configFile });
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
    String tsHome;
    try {
      tsHome = p.getProperty("ts_home").replaceAll("/", FS);
      logMsg("tsHome=" + tsHome);
      _tempDir = tsHome + "/" + baseDir.replaceAll("/", FS) + FS + "tmp";
      logMsg("_tempDir=" + _tempDir);
      _classDir = _tempDir + FS + "classes";
      _srcDir = _tempDir + FS + "src";
      _ndDir = _tempDir + FS + "nd";
      _configFile = _tempDir + FS + CONFIG_FILE;
      _classPath = tsHome + FS + CLASSES;
      _mapFile = _ndDir + FS + "map.file";
      _modelFile = _ndDir + FS + "model.file";
      new File(_srcDir).mkdirs();
      new File(_classDir).mkdirs();
      new File(_ndDir).mkdirs();
      createConfigFile();
    } catch (Exception e) {
      throw new Fault("setup failed", e);
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    removeFile(new File(_srcDir));
    removeFile(new File(_classDir));
    removeFile(new File(_ndDir));
    removeFile(new File(_configFile));
    logMsg("cleanup ok");
  }

  /*
   * @testName: genRpcEncodedWSDL
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void genRpcEncodedWSDL() throws Fault {
    TestUtil.logTrace("genRpcEncodedWSDL()");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Invoke wscompile to generate rpc/encoded WSDL from SEI");
      assertTrue(invokeWsCompileServer("strict"));
    } catch (Exception e) {
      throw new Fault("genRpcEncodedWSDL() failed", e);
    }

    if (!pass)
      throw new Fault("genRpcEncodedWSDL() failed");
  }

  /*
   * @testName: genRpcLiteralWSDL
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void genRpcLiteralWSDL() throws Fault {
    TestUtil.logTrace("genRpcLiteralWSDL()");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg("Invoke wscompile to generate rpc/literal WSDL from SEI");
      assertTrue(invokeWsCompileServer("strict,rpcliteral"));
    } catch (Exception e) {
      throw new Fault("genRpcLiteralWSDL() failed", e);
    }

    if (!pass)
      throw new Fault("genRpcLiteralWSDL() failed");
  }

  /*
   * @testName: genDocumentLiteralWSDL
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void genDocumentLiteralWSDL() throws Fault {
    TestUtil.logTrace("genDocumentLiteralWSDL()");
    boolean pass = true;
    boolean status;
    try {
      TestUtil.logMsg(
          "Invoke wscompile to generate document/literal WSDL from SEI");
      assertTrue(invokeWsCompileServer("strict,documentliteral"));
    } catch (Exception e) {
      throw new Fault("genDocumentLiteralWSDL() failed", e);
    }

    if (!pass)
      throw new Fault("genDocumentLiteralWSDL() failed");
  }
}
