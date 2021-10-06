/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.securityapi.ham.customform.base;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/servlet2")
@ServletSecurity(@HttpConstraint(rolesAllowed = "Administrator"))
public class Servlet2 extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    System.out.println("Inside  Servlet2 ....." + "<BR>");

    response.getWriter().write("This is a servlet \n");

    try {
      BeanManager bm = CDI.current().getBeanManager();

      Set<Bean<?>> beans = bm.getBeans(HttpAuthenticationMechanism.class,
          new AnnotationLiteral<Any>() {
          });
      for (Bean<?> bean : beans) {
        response.getWriter().println("Class: " + bean.getBeanClass().getName());
        response.getWriter().println("Types: " + bean.getTypes().toString());
        response.getWriter()
            .println("Qualifier: " + split(bean.getQualifiers()));
        response.getWriter()
            .println("Scope: " + bean.getScope().getSimpleName());
        response.getWriter().println("Have qualifier @Default: "
            + haveQualifierDefault(bean.getQualifiers()));
        response.getWriter().println("Have scope @ApplicationScoped: "
            + (bean.getScope() == ApplicationScoped.class));
      }
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  private String split(Set<Annotation> set) {
    StringBuilder sb = new StringBuilder();
    for (Annotation a : set) {
      sb.append(a.annotationType().getSimpleName()).append(",");
    }
    return sb.substring(0, sb.length() - 1);
  }

  private boolean haveQualifierDefault(Set<Annotation> set) {

    for (Annotation a : set) {
      if (a.annotationType() == Default.class) {
        return true;
      }
    }
    return false;
  }

}
