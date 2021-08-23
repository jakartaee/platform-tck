/*
 * Copyright (c) 2010, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.naming.context;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

public final class Test {
  public static final String NOT_ALLOWED_TO_MODIFY_ENV = "Application component instances are not allowed to modify the environment at runtime";

  private Test() {
  }

  public static InitialContext initialContext() throws NamingException {
    return new InitialContext();
  }

  public static Context javaCompContext() throws NamingException {
    return InitialContext.<Context> doLookup("java:comp");
  }

  public static Context javaCompEnvContext() throws NamingException {
    return InitialContext.<Context> doLookup("java:comp/env");
  }

  public static Context javaModuleContext() throws NamingException {
    return InitialContext.<Context> doLookup("java:module");
  }

  public static Context javaModuleEnvContext() throws NamingException {
    return InitialContext.<Context> doLookup("java:module/env");
  }

  public static Context javaAppContext() throws NamingException {
    return InitialContext.<Context> doLookup("java:app");
  }

  public static Context javaAppEnvContext() throws NamingException {
    return InitialContext.<Context> doLookup("java:app/env");
  }

  public static Context[] java3Contexts(boolean... excludeAppEnvs)
      throws NamingException {
    boolean excludeAppEnv = excludeAppEnvs.length == 0 ? false
        : excludeAppEnvs[0];
    if (excludeAppEnv) {
      return new Context[] { Test.javaCompContext(), Test.javaModuleContext(),
          Test.javaModuleEnvContext(), Test.javaAppContext() };
    }
    return new Context[] { Test.javaCompContext(), Test.javaModuleContext(),
        Test.javaModuleEnvContext(), Test.javaAppContext(),
        Test.javaAppEnvContext() };
  }

  public static Context[] envContexts(boolean... excludeAppEnvs)
      throws NamingException {
    boolean excludeAppEnv = excludeAppEnvs.length == 0 ? false
        : excludeAppEnvs[0];
    if (excludeAppEnv) {
      return new Context[] { Test.javaModuleEnvContext() };
    }
    return new Context[] { Test.javaModuleEnvContext(),
        Test.javaAppEnvContext() };
  }

  public static Context[] nonEnvContexts() throws NamingException {
    return new Context[] { Test.javaCompContext(), Test.javaModuleContext(),
        Test.javaAppContext() };
  }

  public static Context[] initialAndJava3Contexts(boolean... excludeAppEnvs)
      throws NamingException {
    boolean excludeAppEnv = excludeAppEnvs.length == 0 ? false
        : excludeAppEnvs[0];
    if (excludeAppEnv) {
      return new Context[] { Test.initialContext(), Test.javaCompContext(),
          Test.javaModuleContext(), Test.javaModuleEnvContext(),
          Test.javaAppContext() };
    }
    return new Context[] { Test.initialContext(), Test.javaCompContext(),
        Test.javaModuleContext(), Test.javaModuleEnvContext(),
        Test.javaAppContext(), Test.javaAppEnvContext() };
  }

  public static Object lookupInContextOrNull(Context context, String name) {
    try {
      return context.lookup(name);
    } catch (NamingException e) {
      return null;
    }
  }

  /**
   * Verifies java url context by invoking its methods. Application component
   * instances are not allowed to modify the environment at runtime.
   */
  public static String getEnvironment(Context... contexts)
      throws TestFailedException {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    for (Context context : contexts) {
      pw.println("Context under test: " + context);

      try {
        Hashtable<?, ?> environment = context.getEnvironment();
        if (environment == null) {
          pw.println("Expecting not null from getEnvironment, but got null.");
          throw new TestFailedException(sw.toString());
        }
        pw.println(
            "Got the expected result from getEnvironment: " + environment);
      } catch (TestFailedException e) {
        throw e;
      } catch (Throwable e) {
        throw new TestFailedException(sw.toString(), e);
      }
    }
    return sw.toString();
  }

  public static String bind(Context... contexts) throws TestFailedException {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    for (Context context : contexts) {
      pw.println("Context under test: " + context);

      String newEntryName = "newName";
      String newEntryValue = "newValue";
      try {
        context.bind(newEntryName, newEntryValue);
        Object obj = lookupInContextOrNull(context, newEntryName);
        if (obj != null) {
          pw.println("Expecting not exist, but got " + obj
              + ", from looking up " + newEntryName);
          throw new TestFailedException(sw.toString());
        }
        pw.println("Got the expected result: not exist.");
      } catch (TestFailedException e) {
        throw e;
      } catch (Throwable e) {
        pw.println("Got the expected exception: " + e);
      } finally {
        try {
          context.unbind(newEntryName);
        } catch (Exception ee) {
          // ignore
        }
      }
    }
    return sw.toString();
  }

  public static String rebind(Context... contexts) throws TestFailedException {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    for (Context context : contexts) {
      pw.println("Context under test: " + context);

      String newEntryName = "newName";
      String newEntryValue = "newValue";
      try {
        context.rebind(newEntryName, newEntryValue);
        Object obj = lookupInContextOrNull(context, newEntryName);
        if (obj != null) {
          pw.println("Expecting not exist, but got " + obj
              + ", from looking up " + newEntryName);
          throw new TestFailedException(sw.toString());
        }
        pw.println("Got the expected result: not exist.");
      } catch (TestFailedException e) {
        throw e;
      } catch (Throwable e) {
        pw.println("Got the expected exception: " + e);
      } finally {
        try {
          context.unbind(newEntryName);
        } catch (Exception ee) {
          // ignore
        }
      }
    }
    return sw.toString();
  }

  public static String unbind(Context... contexts) throws TestFailedException {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    for (Context context : contexts) {
      pw.println("Context under test: " + context);

      try {
        String entryName = "name";
        context.unbind(entryName);
        Object obj = lookupInContextOrNull(context, entryName);
        if (obj == null) {
          pw.println("Expecting still exist, but got null from looking up "
              + entryName);
          throw new TestFailedException(sw.toString());
        }
        pw.println("Got the expected result: entry not unbound: " + obj);
      } catch (TestFailedException e) {
        throw e;
      } catch (Throwable e) {
        pw.println("Got the expected exception: " + e);
      }
    }
    return sw.toString();
  }

  public static String rename(Context... contexts) throws TestFailedException {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    for (Context context : contexts) {
      pw.println("Context under test: " + context);

      try {
        String entryName = "name";
        String renameTo = "renameTo";
        context.rename(entryName, renameTo);
        Object obj = lookupInContextOrNull(context, entryName);
        if (obj == null) {
          pw.println("Expecting still exist, but got null from looking up "
              + entryName);
          throw new TestFailedException(sw.toString());
        }
        pw.println("Got the expected result: entry not renamed: " + obj);

        obj = lookupInContextOrNull(context, renameTo);
        if (obj != null) {
          pw.println("Expecting renameTo not exist, but got " + obj);
          throw new TestFailedException(sw.toString());
        }
        pw.println("Got the expected result: renameTo not exist.");
      } catch (TestFailedException e) {
        throw e;
      } catch (Throwable e) {
        pw.println("Got the expected exception: " + e);
        // e.printStackTrace(pw);
      }
    }
    return sw.toString();
  }

  public static String close(Context... contexts) throws TestFailedException {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    for (Context context : contexts) {
      pw.println("Context under test: " + context);
      try {
        for (int i = 0; i < 3; i++) {
          context.close();
          pw.println("Context closed successfully.");
        }
      } catch (Throwable e) {
        throw new TestFailedException(sw.toString(), e);
      }
    }
    return sw.toString();
  }

  public static String createSubcontext(Context... contexts)
      throws TestFailedException {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    for (Context context : contexts) {
      pw.println("Context under test: " + context);

      String newEntryName = "newName";
      try {
        context.createSubcontext(newEntryName);
        Object obj = lookupInContextOrNull(context, newEntryName);
        if (obj != null) {
          pw.println("Expecting not exist, but got " + obj
              + ", from looking up " + newEntryName);
          throw new TestFailedException(sw.toString());
        }
        pw.println("Got the expected result: not exist.");
      } catch (TestFailedException e) {
        throw e;
      } catch (Throwable e) {
        pw.println("Got the expected exception: " + e);
      } finally {
        try {
          context.destroySubcontext(newEntryName);
        } catch (Exception ee) {
          // ignore
        }
      }
    }
    return sw.toString();
  }

  public static String destroySubcontext(Context... contexts)
      throws TestFailedException {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    for (Context context : contexts) {
      pw.println("Context under test: " + context);

      try {
        String entryName = "sub";
        context.destroySubcontext(entryName);
        Object obj = lookupInContextOrNull(context, entryName);
        if (obj == null) {
          pw.println("Expecting still exist, but got null from looking up "
              + entryName);
          throw new TestFailedException(sw.toString());
        }
        pw.println("Got the expected result: entry not unbound: " + obj);
      } catch (TestFailedException e) {
        throw e;
      } catch (Throwable e) {
        pw.println("Got the expected exception: " + e);
      }
    }
    return sw.toString();
  }

  public static String lookup(Map<Context, String> context2LookupResult)
      throws TestFailedException, NamingException {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    for (Context context : context2LookupResult.keySet()) {
      pw.println("Context under test: " + context);

      String entryName = "name";
      Object obj = context.lookup(entryName);
      String expected = context2LookupResult.get(context);
      if (!expected.equals(obj)) {
        pw.println("Expecting " + expected + " from lookup " + entryName
            + ", but got " + obj);
        throw new TestFailedException(sw.toString());
      }
      pw.println(
          "Got the expected result " + expected + " from lookup: " + entryName);
    }
    return sw.toString();
  }

  public static String list(Context... contexts)
      throws TestFailedException, NamingException {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    for (Context context : contexts) {
      pw.println("Context under test: " + context);
      NamingEnumeration<NameClassPair> list = context.list("");
      int count = 0;
      int expectedCount = 2;
      while (list.hasMoreElements()) {
        count++;
        pw.println("NameClassPair: " + list.nextElement());
      }
      if (count == expectedCount) {
        pw.println("Got the expected NameClassPair.");
      } else {
        pw.println("Expecting # of NameClassPair: " + expectedCount
            + ", but actual " + count);
        throw new TestFailedException(sw.toString());
      }
    }
    return sw.toString();
  }

  public static String listBindings(Context... contexts)
      throws TestFailedException, NamingException {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    for (Context context : contexts) {
      pw.println("Context under test: " + context);
      NamingEnumeration<Binding> list = context.listBindings("");
      int count = 0;
      int expectedCount = 2;
      while (list.hasMoreElements()) {
        count++;
        pw.println("Binding: " + list.nextElement());
      }
      if (count == expectedCount) {
        pw.println("Got the expected Binding.");
      } else {
        pw.println("Expecting # of Binding: " + expectedCount + ", but actual "
            + count);
        throw new TestFailedException(sw.toString());
      }
    }
    return sw.toString();
  }
}
