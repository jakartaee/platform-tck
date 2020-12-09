/*
 * Copyright (c) 2015, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.javaee.resource.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.ts.tests.servlet.common.util.ServletTestUtil;

import jakarta.mail.Session;
import jakarta.servlet.http.HttpServletResponse;

public class ResourceUtil {

  public static Session getSession(String jndiName) {
    try {
      InitialContext ic = new InitialContext();
      Object obj = ic.lookup(jndiName);
      if (obj instanceof Session)
        return (Session) obj;
      else
        return null;
    } catch (NamingException nex) {
      return null;
    }
  }

  public static void test(HttpServletResponse response, Session session,
      String expected) throws IOException {
    PrintWriter pw = response.getWriter();
    boolean passed = false;

    if (session == null) {
      pw.println("ERROR: resource was not found");
    } else {
      String actual = session.getProperty("test");
      if (actual == null) {
        pw.println("ERROR: property was not found");
      } else if (!actual.equals(expected)) {
        pw.println("expected result=" + expected);
        pw.println("actual result=" + actual);
      } else {
        passed = true;
      }
    }

    ServletTestUtil.printResult(pw, passed);
  }
}
