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

package com.sun.ts.tests.jsp.spec.tagext.resource.taghandler;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

import javax.annotation.Resource;

import jakarta.jms.Queue;
import jakarta.jms.JMSException;

public class ResourceTimingSimpleTag extends SimpleTagSupport {

  @Resource(name = "myQueue")
  private Queue myQueue;

  private String queueName;

  public void setQueueName(String ignoreThis) throws JMSException {
    queueName = myQueue.getQueueName();
  }

  public void doTag() throws JspException, IOException {

    JspWriter out = getJspContext().getOut();

    try {
      if (queueName.equals("MY_QUEUE"))
        ;
      out.println("Test PASSED");
    } catch (Throwable t) {
      JspTestUtil.handleThrowable(t, out, "ResourceTimingSimpleTag");
    }
  }
}
