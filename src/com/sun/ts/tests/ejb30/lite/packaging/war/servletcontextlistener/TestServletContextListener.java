/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.lite.packaging.war.servletcontextlistener;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.lite.basic.common.Basic1IF;

@WebListener
public class TestServletContextListener implements ServletContextListener {
  @EJB(name = "ejb/singletonBean")
  private SingletonBean singletonBean;

  @EJB(name = "ejb/statelessBean")
  private StatelessBean statelessBean;

  @EJB(name = "ejb/statefulBean")
  private StatefulBean statefulBean;

  private List<Basic1IF> beans = new ArrayList<Basic1IF>();

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    beans.add(singletonBean);
    beans.add(statelessBean);
    beans.add(statefulBean);
  }

  public void contextInitialized(ServletContextEvent sce) {
    Helper.getLogger().info(
        "About to exercise test logics in contextInitialized() of " + this);
    add(beans);
  }

  public static void add(List<Basic1IF> bs) {
    int m = 2, n = 3;
    for (Basic1IF b : bs) {
      int sum = b.add(m, n);
      Helper.getLogger().info(Helper
          .assertEquals("Check result of add operation on " + b, m + n, sum));
    }
  }

  public void contextDestroyed(ServletContextEvent sce) {
    Helper.getLogger().info(
        "About to destroy the ServletContext in contextDestroyed() of " + this);
  }
}
