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

/*
 * $Id$
 */

package com.sun.ts.tests.ejb30.bb.mdb.dest.topic.jarwar;

import javax.annotation.Resource;
import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import static com.sun.ts.tests.ejb30.common.messaging.Constants.PASSED;
import static com.sun.ts.tests.ejb30.common.messaging.Constants.FAILED;
import javax.jms.Topic;

public class TestServlet extends GenericServlet {

  @Resource(name = "replyTopic")
  private Topic replyTopic;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  public void service(ServletRequest req, ServletResponse res)
      throws ServletException, IOException {
    invokeTest(req, res);
  }

  protected void invokeTest(ServletRequest req, ServletResponse res)
      throws ServletException, IOException {
    PrintWriter pw = res.getWriter();
    String reason = "";
    boolean status = true;
    if (replyTopic == null) {
      reason += "replyTopic is null. ";
      status = false;
    }
    pw.println(status ? PASSED : FAILED);
    pw.println("replyTopic: " + replyTopic);

  }

}
