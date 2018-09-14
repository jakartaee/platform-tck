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

import javax.mail.Session;
import javax.enterprise.context.ApplicationScoped;
import javax.annotation.Resource;

@ApplicationScoped
public class BeanResource {

  /*
   * Since this bean is app-scoped, these resource references need to have a
   * name in the java:app namespace; we can't use the default name.
   */

  @Resource(name = "java:app/env/compSession", lookup = "java:comp/env/ResourceCompTestServlet_MailSession")
  private Session compSession;

  @Resource(name = "java:app/env/moduleSession", lookup = "java:module/env/ResourceModuleTestServlet_MailSession")
  private Session moduleSession;

  @Resource(name = "java:app/env/appSession", lookup = "java:app/env/ResourceAppTestServlet_MailSession")
  private Session appSession;

  @Resource(name = "java:app/env/globalSession", lookup = "java:global/env/ResourceGlobalTestServlet_MailSession")
  private Session globalSession;

  public Session getCompSession() {
    return compSession;
  }

  public Session getModuleSession() {
    return moduleSession;
  }

  public Session getAppSession() {
    return appSession;
  }

  public Session getGlobalSession() {
    return globalSession;
  }
}
