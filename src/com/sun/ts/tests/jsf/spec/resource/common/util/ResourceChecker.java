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
package com.sun.ts.tests.jsf.spec.resource.common.util;

import java.io.PrintWriter;
import javax.faces.application.Resource;
import java.io.InputStream;
import java.io.StringWriter;
import javax.faces.application.ResourceHandler;

public class ResourceChecker {
  private static final String NL = System.getProperty("line.separator", "\n");

  /**
   * Validate that a JSF Resource can be generated and found in the webapp.
   * 
   * @param handler
   *          - The @ResourceHandle used to create the resource.
   * @param resource
   *          - The Name(@String) of which the resource is packaged as.
   * @param expected
   *          - The expected size of the resource.(in bytes)
   * @param lib
   *          - The name of the library(@String) that the resource is packaged
   *          under. excepts @null.
   * @param negativeTest
   *          - passing true in indicates this as a negative test case.
   * @param out
   *          - @PrintWriter to log test messages to.
   */
  public static void checkIndentifier(ResourceHandler handler, String resource,
      int expected, String lib, Boolean negativeTest, PrintWriter out) {

    if (handler != null) {
      try {
        Resource res = handler.createResource(resource, lib);

        if (res != null) {
          InputStream is = res.getInputStream();

          int result = 0;
          while (is.read() != -1) {
            result++;
          }

          if (expected == result) {
            out.println("NegativeTest = " + negativeTest + NL + "Test PASSED");

          } else if (negativeTest) {
            out.println("NegativeTest = " + negativeTest + NL + "Test FAILED."
                + NL + "NegativeTest = " + negativeTest + NL
                + "Should not have been able to get a handle " + NL + "to : "
                + resource + "' in  Library '" + lib + "'!");

          } else {
            out.println("Test FAILED." + NL + "Resource: " + resource + NL
                + "Expected: " + expected + NL + "Received: " + result);
          }
        } else {
          if (negativeTest) {
            out.println("NegativeTest = True" + NL + "Test PASSED");

          } else {
            out.println("Test FAILED." + NL + "Unable to Obtain Resource '"
                + resource + "' in  Library '" + lib + "'!");
          }
        }

      } catch (Exception e) {
        out.println("Test FAILED. See stacktrace below...");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        e.printStackTrace(pw);
        out.println(sw.toString());
      }

    } else {
      out.println(
          "Test FAILED.  Unable to obtain ResourceHandler " + "instance.");
    }
  }

  /**
   * Validate that a JSF Resource can be generated and found in the webapp.
   * Resource is not packaged under a Library, and this is for positive test
   * case only.
   * 
   * @param handler
   *          - The @ResourceHandle used to create the resource.
   * @param resource
   *          - The Name(@String) of which the resource is packaged as.
   * @param expected
   *          - expected size of the resource.(in bytes)
   * @param out
   *          - @PrintWriter to log test messages to.
   */
  public static void checkIndentifier(ResourceHandler handler, String resource,
      int expected, PrintWriter out) {

    checkIndentifier(handler, resource, expected, null, false, out);
  }

  /**
   * Validate that a JSF Resource can be generated and found in the webapp. This
   * is for positive test case only.
   * 
   * @param handler
   *          - The @ResourceHandle used to create the resource.
   * @param resource
   *          - The Name(@String) of which the resource is packaged as.
   * @param expected
   *          - The expected size of the resource.(in bytes)
   * @param lib
   *          - The name of the library(@String) that the resource is packaged
   *          under.
   * @param out
   *          - @PrintWriter to log test messages to.
   */
  public static void checkIndentifier(ResourceHandler handler, String resource,
      int expected, String lib, PrintWriter out) {

    checkIndentifier(handler, resource, expected, lib, false, out);
  }

  public static void doesExists(ResourceHandler handler, String resource,
      String lib, PrintWriter out) {

    if (handler != null) {
      try {
        Resource res = handler.createResource(resource, lib);

        if (res != null) {
          out.println("Test PASSED");
        } else {
          out.println("Test FAILED." + NL + "Unable to Obtain Resource '"
              + resource + "' in  Library '" + lib + "'!");
        }

      } catch (Exception e) {
        out.println("Test FAILED. See stacktrace below...");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        e.printStackTrace(pw);
        out.println(sw.toString());
      }

    } else {
      out.println(
          "Test FAILED.  Unable to obtain ResourceHandler " + "instance.");
    }
  }
}
