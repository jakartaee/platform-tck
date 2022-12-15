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

package com.sun.ts.lib.harness;

import com.sun.javatest.Command;
import com.sun.javatest.Status;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>
 * Allows the execution of CTS/TCK tests within the same JVM. This should only
 * be used for tests that can guarantee that running in such a configuration
 * will have no impact on the test results.
 */
public class ExecuteTSTestSameJVMCmd extends Command {

  public ExecuteTSTestSameJVMCmd() {
  }

  public Status run(String[] args, PrintWriter log, PrintWriter ref) {
    int repeat = 1;
    String className;
    String[] executeArgs = {};
    ClassLoader loader = getClassLoader();

    int i = 0;

    for (; i < args.length && args[i].startsWith("-"); i++) {
      if ("-loadDir".equals(args[i]) && i + 1 < args.length) {
        ; // ignore
      } else if ("-repeat".equals(args[i]) && i + 1 < args.length) {
        ; // ignore
      }
    }

    // Next must come the executeClass
    if (i < args.length) {
      className = args[i];
      i++;
    } else {
      return Status.failed("No executeClass specified");
    }

    // Finally, any optional args
    if (i < args.length) {
      executeArgs = new String[args.length - i];
      System.arraycopy(args, i, executeArgs, 0, executeArgs.length);
    }

    Status status = null;
    System.out.println("CLASSNAME: " + className);
    try {
      Class c;
      if (loader == null) {
        c = Class.forName(className);
      } else {
        c = loader.loadClass(className);
      }

      Status prevStatus = null;
      for (int j = 0; j < repeat; j++) {
        if (repeat > 1) {
          log.println("iteration: " + (j + 1));
        }

        Object t = c.newInstance();
        Class[] mainArgs = { Class.forName("[Ljava.lang.String;"),
            PrintWriter.class, PrintWriter.class };

        Method runMethod = c.getMethod("run", mainArgs);
        Object[] execArgs = new Object[3];
        execArgs[0] = executeArgs;
        execArgs[1] = log;
        execArgs[2] = ref;

        status = (Status) runMethod.invoke(t, execArgs);

        if (repeat > 1) {
          log.println("   " + status);
        }

        if ((prevStatus != null) && status.getType() != prevStatus.getType()) {
          status = Status
              .error("Return status type changed at repetition: " + (j + 1));
        }

        if (status.isError()) {
          return status;
        } else {
          prevStatus = status;
        }
      }
    } catch (ClassCastException e) {
      status = Status.failed("Can't load test: required interface not found");
    } catch (ClassNotFoundException e) {
      status = Status.failed("Can't load test: " + e);
    } catch (InstantiationException e) {
      status = Status.failed("Can't instantiate test: " + e);
    } catch (IllegalAccessException e) {
      status = Status.failed("Illegal access to test: " + e);
    } catch (VerifyError e) {
      return Status
          .failed("Class verification error while trying to load test class `"
              + className + "': " + e);
    } catch (LinkageError e) {
      return Status
          .failed("Class linking error while trying to load test class `"
              + className + "': " + e);
    } catch (NoSuchMethodException nsme) {
      return Status.failed("Unable to find 'main' method in test class '"
          + className + "': " + nsme);
    } catch (InvocationTargetException tie) {
      return Status.failed("Test Failed: " + tie);
    }
    return status;
  }
}
