/*
 * Copyright (c) 2015, 2018 Oracle and/or its affiliates. All rights reserved.
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.mail.Session;
import javax.annotation.Resource;
import javax.inject.Inject;

import com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet;

@WebServlet(urlPatterns = { "/TestServlet/*" })
public class TestServlet extends HttpTCKServlet {

  @Resource(lookup = "java:comp/env/ResourceCompTestServlet_MailSession")
  private Session compSession;

  @Inject
  private Bean bean;

  @Inject
  private BeanResource beanResource;

  public void resRefTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    final String resource = "ResourceCompTestServlet_MailSession";
    ResourceUtil.test(response,
        // the default name of the res-ref from the field above
        ResourceUtil.getSession(
            "java:comp/env/" + this.getClass().getName() + "/compSession"),
        resource);
  }

  public void compTest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    final String resource = "ResourceCompTestServlet_MailSession";
    ResourceUtil.test(response,
        ResourceUtil.getSession("java:comp/env/" + resource), resource);
  }

  public void moduleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    final String resource = "ResourceModuleTestServlet_MailSession";
    ResourceUtil.test(response,
        ResourceUtil.getSession("java:module/env/" + resource), resource);
  }

  public void appTest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    final String resource = "ResourceAppTestServlet_MailSession";
    final String resource_repeatable = "ResourceAppTestServlet_MailSession_repeatable";
    ResourceUtil.test(response,
        ResourceUtil.getSession("java:app/env/" + resource), resource);
    ResourceUtil.test(response,
        ResourceUtil.getSession("java:app/env/" + resource_repeatable),
        resource_repeatable);
  }

  public void globalTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    final String resource = "ResourceGlobalTestServlet_MailSession";
    ResourceUtil.test(response,
        ResourceUtil.getSession("java:global/env/" + resource), resource);
  }

  public void compEqualsModuleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    final String resource = "ResourceCompTestServlet_MailSession";
    ResourceUtil.test(response,
        ResourceUtil.getSession("java:module/env/" + resource), resource);
  }

  public void moduleEqualsCompTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    final String resource = "ResourceModuleTestServlet_MailSession";
    ResourceUtil.test(response,
        ResourceUtil.getSession("java:comp/env/" + resource), resource);
  }

  public void beanTest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    final String resource = "Bean_MailSession";
    ResourceUtil.test(response,
        ResourceUtil.getSession("java:app/env/" + resource), resource);
  }

  public void beanCompTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    final String resource = "ResourceCompTestServlet_MailSession";
    ResourceUtil.test(response, bean.getSession("java:comp/env/" + resource),
        resource);
  }

  public void beanModuleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    final String resource = "ResourceModuleTestServlet_MailSession";
    ResourceUtil.test(response, bean.getSession("java:module/env/" + resource),
        resource);
  }

  public void beanAppTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    final String resource = "ResourceAppTestServlet_MailSession";
    ResourceUtil.test(response, bean.getSession("java:app/env/" + resource),
        resource);
  }

  public void beanGlobalTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    final String resource = "ResourceGlobalTestServlet_MailSession";
    ResourceUtil.test(response, bean.getSession("java:global/env/" + resource),
        resource);
  }

  public void beanResourceCompTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    final String resource = "ResourceCompTestServlet_MailSession";
    ResourceUtil.test(response, beanResource.getCompSession(), resource);
  }

  public void beanResourceModuleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    final String resource = "ResourceModuleTestServlet_MailSession";
    ResourceUtil.test(response, beanResource.getModuleSession(), resource);
  }

  public void beanResourceAppTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    final String resource = "ResourceAppTestServlet_MailSession";
    ResourceUtil.test(response, beanResource.getAppSession(), resource);
  }

  public void beanResourceGlobalTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    final String resource = "ResourceGlobalTestServlet_MailSession";
    ResourceUtil.test(response, beanResource.getGlobalSession(), resource);
  }

  public void beanRefCompTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    final String resource = "ResourceCompTestServlet_MailSession";
    ResourceUtil.test(response, bean.getSession("java:app/env/compSession"),
        resource);
  }

  public void beanRefModuleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    final String resource = "ResourceModuleTestServlet_MailSession";
    ResourceUtil.test(response, bean.getSession("java:app/env/moduleSession"),
        resource);
  }

  public void beanRefAppTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    final String resource = "ResourceAppTestServlet_MailSession";
    ResourceUtil.test(response, bean.getSession("java:app/env/appSession"),
        resource);
  }

  public void beanRefGlobalTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    final String resource = "ResourceGlobalTestServlet_MailSession";
    ResourceUtil.test(response, bean.getSession("java:app/env/globalSession"),
        resource);
  }
}
