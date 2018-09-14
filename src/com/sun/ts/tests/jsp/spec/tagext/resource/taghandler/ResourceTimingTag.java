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
 * @(#)ResourceTimingTag.java	1.3 05/10/27
 */

package com.sun.ts.tests.jsp.spec.tagext.resource.taghandler;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

import javax.annotation.Resource;

import javax.jms.Queue;
import javax.jms.JMSException;

public class ResourceTimingTag extends TagSupport {

  @Resource(name = "myQueue")
  private Queue myQueue;

  private String queueName;

  public void setQueueName(String ignoreThis) throws JMSException {
    queueName = myQueue.getQueueName();
  }

  public int doStartTag() throws JspException {

    JspWriter out = pageContext.getOut();

    try {
      if (queueName.equals("MY_QUEUE"))
        ;
      out.println("Test PASSED");
    } catch (IOException ioe) {
      throw new JspException("Unexpected Exception", ioe);
    }
    return SKIP_BODY;
  }
}
