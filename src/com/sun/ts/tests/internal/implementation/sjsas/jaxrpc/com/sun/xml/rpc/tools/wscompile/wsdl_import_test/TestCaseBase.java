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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.tools.wscompile.wsdl_import_test;

import java.io.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

// Internal implementation specific classes to tes
import com.sun.xml.rpc.tools.wscompile.*;
import com.sun.xml.rpc.tools.wscompile.CompileTool;
import com.sun.xml.rpc.processor.generator.GeneratorException;

/**
 *
 * @author JAX-RPC RI Development Team
 */
public class TestCaseBase {

  protected final static String FS = System.getProperty("file.separator");

  protected final static String PS = System.getProperty("path.separator");

  private static final String BASEDIR = "src/com/sun/ts/tests/internal/implementation/sjsas/jaxrpc/com/sun/xml/rpc/wsdl";

  private final static String WSDL_DIR = "test" + FS + "etc" + FS + "wsdl";

  private final static String CONFIG_FILE = "config.xml";

  private final static String BUILD_CLASSES = "build" + FS + "classes";

  private final static String BUILD_TEST_CLASSES = "build" + FS + "test" + FS
      + "classes";

  private String _tsHome;

  private String _baseDir;

  private String _tempDir;

  private String _dataDir;

  private String _srcDir;

  private String _classDir;

  private String _ndDir;

  private String _configFile;

  private String _testFileName;

  private String _dataFileName;

  protected boolean _useSearchSchemaOption;

  public TestCaseBase(String tsHome) {
    _tsHome = tsHome;
    _baseDir = _tsHome + "/" + BASEDIR.replaceAll("/", FS);
    _tempDir = _baseDir + FS + "tmp";
    _dataDir = _baseDir + FS + "data";
    _srcDir = _tempDir + FS + "src";
    _classDir = _tempDir + FS + "classes";
    _ndDir = _tempDir + FS + "nd";
    _configFile = _tempDir + FS + CONFIG_FILE;
  }

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

  protected void doTestSetup() {
    TestUtil.logMsg("Begin Import Test for: " + _dataFileName);
    new File(_srcDir).mkdirs();
    new File(_classDir).mkdirs();
    new File(_ndDir).mkdirs();
    _useSearchSchemaOption = false;
  }

  protected void doTestCleanup() {
    TestUtil.logMsg("End Import Test for: " + _dataFileName);
    removeFile(new File(_srcDir));
    removeFile(new File(_classDir));
    removeFile(new File(_ndDir));
    removeFile(new File(_configFile));
  }

  private void initFile(String fileName) {
    _dataFileName = fileName.replaceAll("/", FS);
    _testFileName = _dataDir + FS + WSDL_DIR + FS + _dataFileName;
    doTestSetup();
  }

  private void assertTrue(boolean b) {
    if (b)
      TestUtil.logMsg("PASSED ...");
    else
      TestUtil.logMsg("FAILED ...");
    doTestCleanup();

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

  /* added into confirm working of search schema with WSDLModeler */
  /* this is for a negative test */
  protected void importSearchSchemaWSDL(String filename) throws Exception {
    initFile(filename);
    File configFile = new File(_configFile);
    FileWriter writer = new FileWriter(_configFile);
    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    writer.write(
        "<configuration xmlns=\"http://java.sun.com/xml/ns/jax-rpc/ri/config\">");
    writer.write("<wsdl location=\"");
    writer.write(new File(_testFileName).toURL().toString());
    writer.write("\" packageName=\"test\" />");
    writer.write("</configuration>");
    writer.close();
    assertTrue(!invokeWsCompileServer(configFile.getAbsolutePath()));
  }

  protected void importWSDL(String filename) throws Exception {
    initFile(filename);
    File configFile = new File(_configFile);
    FileWriter writer = new FileWriter(configFile);
    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    writer.write(
        "<configuration xmlns=\"http://java.sun.com/xml/ns/jax-rpc/ri/config\">");
    writer.write("<wsdl location=\"");
    writer.write(new File(_testFileName).toURL().toString());
    writer.write("\" packageName=\"test\" />");
    writer.write("</configuration>");
    writer.close();
    assertTrue(invokeWsCompileServer(configFile.getAbsolutePath()));
  }

  protected void importWSDLClientOnly(String filename) throws Exception {
    initFile(filename);
    File configFile = new File(_configFile);
    FileWriter writer = new FileWriter(configFile);
    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    writer.write(
        "<configuration xmlns=\"http://java.sun.com/xml/ns/jax-rpc/ri/config\">");
    writer.write("<wsdl location=\"");
    writer.write(new File(_testFileName).toURL().toString());
    writer.write("\" packageName=\"test\" />");
    writer.write("</configuration>");
    writer.close();
    assertTrue(invokeWsCompileClient(configFile.getAbsolutePath()));
  }

  protected void isWSDLValid(String filename) throws Exception {
    initFile(filename);
    File configFile = new File(_configFile);
    FileWriter writer = new FileWriter(configFile);
    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    writer.write(
        "<configuration xmlns=\"http://java.sun.com/xml/ns/jax-rpc/ri/config\">");
    writer.write("<wsdl location=\"");
    writer.write(new File(_testFileName).toURL().toString());
    writer.write("\" packageName=\"test\" />");
    writer.write("</configuration>");
    writer.close();
    TestUtil.logMsg("\n Expect failure on invalid WSDL");
    try {
      assertTrue(!invokeWsCompileImport(configFile.getAbsolutePath()));
    } catch (Exception e) {
      assertTrue(true);
    }
  }

  protected void importWSDLWsCompileImport(String filename) throws Exception {
    initFile(filename);
    File configFile = new File(_configFile);
    FileWriter writer = new FileWriter(configFile);
    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    writer.write(
        "<configuration xmlns=\"http://java.sun.com/xml/ns/jax-rpc/ri/config\">");
    writer.write("<wsdl location=\"");
    writer.write(new File(_testFileName).toURL().toString());
    writer.write("\" packageName=\"test\" />");
    writer.write("</configuration>");
    writer.close();
    assertTrue(invokeWsCompileImport(configFile.getAbsolutePath()));
  }

  protected void importWSIWSDLWsCompileImport(String filename)
      throws Exception {
    initFile(filename);
    File configFile = new File(_configFile);
    FileWriter writer = new FileWriter(configFile);
    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    writer.write(
        "<configuration xmlns=\"http://java.sun.com/xml/ns/jax-rpc/ri/config\">");
    writer.write("<wsdl location=\"");
    writer.write(new File(_testFileName).toURL().toString());
    writer.write("\" packageName=\"test\" />");
    writer.write("</configuration>");
    writer.close();
    assertTrue(invokeWSIWsCompileImport(configFile.getAbsolutePath()));
  }

  private boolean invokeWsCompileServer(String configFileName) {
    return (new CompileTool(System.out, "wscompile")).run(
        new String[] { "-classpath", BUILD_CLASSES + PS + BUILD_TEST_CLASSES,
            "-d", _classDir, "-s", _srcDir, "-nd", _ndDir, "-gen:server",
            "-keep", "-f:searchschema", "-Xprintstacktrace", configFileName });
  }

  private boolean invokeWsCompileClient(String configFileName) {
    return (new CompileTool(System.out, "wscompile")).run(
        new String[] { "-classpath", BUILD_CLASSES + PS + BUILD_TEST_CLASSES,
            "-d", _classDir, "-s", _srcDir, "-nd", _ndDir, "-gen:client",
            "-keep", "-f:searchschema", "-Xprintstacktrace", configFileName });
  }

  private boolean invokeWsCompileImport(String configFileName) {
    return (new CompileTool(System.out, "wscompile")).run(
        new String[] { "-classpath", BUILD_CLASSES + PS + BUILD_TEST_CLASSES,
            "-d", _classDir, "-s", _srcDir, "-nd", _ndDir, "-import", "-keep",
            "-Xprintstacktrace", configFileName });
  }

  private boolean invokeWSIWsCompileImport(String configFileName) {
    return (new CompileTool(System.out, "wscompile")).run(
        new String[] { "-classpath", BUILD_CLASSES + PS + BUILD_TEST_CLASSES,
            "-d", _classDir, "-s", _srcDir, "-nd", _ndDir, "-import", "-f:wsi",
            "-keep", "-Xprintstacktrace", configFileName });
  }
}
