/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.misc.sameejbclass;

import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.lib.deliverable.cts.resource.Dog;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.servlet.common.util.Data;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/TestServlet", loadOnStartup = 1)
public class TestServlet extends HttpTCKServlet {
  @EJB(beanName = "SameEJBClassBean1", name = "ejb/SameEJBClassBean1")
  private SameEJBClassIF sameEJBClassBean1;

  @EJB(beanName = "SameEJBClassBean2", name = "ejb/SameEJBClassBean2")
  private SameEJBClassIF sameEJBClassBean2;

  @EJB(beanName = "SameEJBClassBean3", name = "ejb/SameEJBClassBean3")
  private SameEJBClassIF sameEJBClassBean3;

  @EJB(beanName = "SameEJBClassBean4", name = "ejb/SameEJBClassBean4")
  private SameEJBClassIF sameEJBClassBean4;

  @EJB(beanName = "SameEJBClassBean1", name = "ejb/sameEJBClassBeanRemote1")
  private SameEJBClassRemoteIF sameEJBClassBeanRemote1;

  public void checkEnvEntry(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      pw.println(sameEJBClassBean1.checkEnvEntry("sameEJBClassBean1"));
      pw.println(sameEJBClassBean2.checkEnvEntry("sameEJBClassBean2"));
      pw.println(sameEJBClassBean3.checkEnvEntry("sameEJBClassBean3"));
      pw.println(
          Data.PASSED + sameEJBClassBean4.checkEnvEntry("sameEJBClassBean4"));
    } catch (TestFailedException ex) {
      pw.println(Data.FAILED + ex.getMessage());
    }
  }

  public void testDTO(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      // obj and dogCopy are equal
      Dog dogCopy = new Dog();
      Object obj = Dog.getInstance();
      ;

      // obj2 to hold return result
      Object obj2 = sameEJBClassBeanRemote1.testDTO(10, "object", obj);
      // remote call should not modify the param
      if (!obj.equals(dogCopy)) {
        throw new TestFailedException(
            "The param to a remote call was modified: " + obj);
      }
      // return value dog2 should contain the new name set in the bean class
      if (obj2.equals(obj)) {
        throw new TestFailedException("Unexpected return value: " + obj2);
      }
      pw.println(Data.PASSED);
    } catch (Exception ex) {
      pw.println(Data.FAILED + ex.getMessage());
    }
  }
}
