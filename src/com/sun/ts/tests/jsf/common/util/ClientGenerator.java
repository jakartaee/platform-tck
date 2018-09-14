/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.common.util;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Utility class to generate clients for API tests.
 * </p>
 */
public class ClientGenerator {
  private static final String NL = System.getProperty("line.separator", "\n");

  Class testClass;

  String contextRoot;

  public ClientGenerator(Class testClass, String contextRoot) {
    this.testClass = testClass;
    this.contextRoot = contextRoot;
  }

  public static void main(String[] args) {
    if (args.length > 1) {
      Class testClass = loadClass(args[0]);
      if (testClass == null) {
        System.out.println("Unable to load specified class: '" + args[0] + "'");
        System.exit(1);

      }
      ClientGenerator generator = new ClientGenerator(testClass, args[1]);
      generator.generateClient();
    }
  }

  public void generateClient() {
    String packageName = testClass.getPackage().getName();
    String[] testMethods = getTestEntries();
    StringBuffer sb = new StringBuffer(2048);
    writeClassHeader(sb, packageName, contextRoot);
    for (String testMethod : testMethods) {
      writeTestEntry(sb, testMethod);
    }
    writeEndClass(sb);
    try {
      writeBytesToFile(sb.toString().getBytes("UTF-8"));
    } catch (Throwable t) {
      // ignore
    }

  }

  private static Class loadClass(String className) {
    Class clazz = null;
    String clsName = className;
    if (clsName.indexOf('/') != -1) {
      clsName = clsName.replace('/', '.');
    }
    try {
      clazz = Thread.currentThread().getContextClassLoader().loadClass(clsName);
    } catch (Throwable t) {
      System.err.println(t.toString());
      // do nothing...return null;
    }
    return clazz;
  }

  private void writeBytesToFile(byte[] bytes) {
    BufferedOutputStream out = null;
    try {
      out = new BufferedOutputStream(new FileOutputStream("URLClient.java"));
      out.write(bytes);
      out.flush();
    } catch (Throwable t) {
      System.err.println("Error writing client: " + t.toString());
      t.printStackTrace();
      System.exit(1);
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (Throwable t) {
          // ignore
        }
      }
    }
  }

  private String[] getTestEntries() {
    Method[] methods = testClass.getMethods();
    List<String> methodList = new ArrayList<String>();
    for (Method method : methods) {
      String methodName = method.getName();
      if (methodName.endsWith("Test")) {
        methodList.add(methodName);
      }
    }
    return methodList.toArray(new String[methodList.size()]);
  }

  private void writeClassHeader(StringBuffer buffer, String packageName,
      String contextRoot) {
    buffer.append("/*" + NL);
    buffer.append(
        " * Copyright (c) 2005, 2018 Oracle and/or its affiliates. All rights reserved."
            + NL);
    buffer.append(" *" + NL);
    buffer.append(
        " * This program and the accompanying materials are made available under the"
            + NL);
    buffer.append(
        " * terms of the Eclipse Public License v. 2.0, which is available at"
            + NL);
    buffer.append(" * http://www.eclipse.org/legal/epl-2.0." + NL);
    buffer.append(" *" + NL);
    buffer.append(
        " * This Source Code may also be made available under the following Secondary"
            + NL);
    buffer.append(
        " * Licenses when the conditions for such availability set forth in the"
            + NL);
    buffer.append(
        " * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,"
            + NL);
    buffer.append(
        " * version 2 with the GNU Classpath Exception, which is available at"
            + NL);
    buffer
        .append(" * https://www.gnu.org/software/classpath/license.html." + NL);
    buffer.append(" *" + NL);
    buffer.append(
        " * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0"
            + NL);
    buffer.append(" *" + NL);
    buffer.append("/*" + NL);
    buffer.append(NL);
    buffer.append(" * ").append('%').append("W% %").append('G')
        .append("%" + NL);
    buffer.append(" *" + NL);
    buffer.append(NL);
    buffer.append("package ").append(packageName).append(";" + NL);
    buffer.append(NL);
    buffer.append("import java.io.PrintWriter;" + NL);
    buffer.append("import com.sun.javatest.Status;" + NL);
    buffer.append(
        "import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;" + NL);
    buffer.append(NL);
    buffer.append(
        "public final class URLClient extends AbstractUrlClient {" + NL);
    buffer.append(NL);
    buffer.append("    private static final String CONTEXT_ROOT = \"");
    buffer.append(contextRoot).append("\";" + NL);
    buffer.append("    public static void main(String[] args) {" + NL);
    buffer.append("        URLClient theTests = new URLClient();" + NL);
    buffer.append("        Status s = theTests.run(args," + NL);
    buffer.append(
        "                                new PrintWriter(System.out, true),"
            + NL);
    buffer.append(
        "                                new PrintWriter(System.err, true));"
            + NL);
    buffer.append("        s.exit();" + NL);
    buffer.append("    }" + NL);
    buffer.append(
        "    public Status run(String[] args, PrintWriter out, PrintWriter err) {"
            + NL);
    buffer.append("        setContextRoot(CONTEXT_ROOT);" + NL);
    buffer.append("        setServletName(DEFAULT_SERVLET_NAME);" + NL);
    buffer.append("         return super.run(args, out, err);" + NL);
    buffer.append("    }" + NL);
    buffer.append("    /*" + NL);
    buffer.append(
        "     * @class.setup_props: webServerHost; webServerPort; ts_home;"
            + NL);
    buffer.append("     *" + NL);
    buffer.append("    /* Test Declarations *" + NL);
  }

  private void writeTestEntry(StringBuffer buffer, String testName) {
    buffer.append(NL);
    buffer.append("    /*" + NL);
    buffer.append("     * @").append("testName: ").append(testName).append(NL);
    buffer.append("     * @assertion_ids: PENDING: add assertion ID(s)" + NL);
    buffer.append("     * @test_Strategy: PENDING: add test strategy" + NL);
    buffer.append("     */" + NL);
    buffer.append("    public void ").append(testName)
        .append("() throws Fault {" + NL);
    buffer.append("        TEST_PROPS.setProperty(APITEST, \"").append(testName)
        .append("\");" + NL);
    buffer.append("        invoke();" + NL);
    buffer.append("    }" + NL);
  }

  private void writeEndClass(StringBuffer buffer) {
    buffer.append(NL);
    buffer.append("} // end of URLClient" + NL);
  }

}
