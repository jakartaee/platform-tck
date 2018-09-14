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

package com.sun.ts.tests.jsp.spec.tagext.resource.taghandler;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

import javax.annotation.Resource;

import javax.sql.DataSource;
import javax.jms.QueueConnectionFactory;
import javax.jms.TopicConnectionFactory;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.mail.Session;
import java.net.URL;

public class ResourceTag extends TagSupport {

  @Resource(name = "DB1")
  private DataSource DB1;

  @Resource(name = "qcFactory")
  private QueueConnectionFactory qcFactory;

  @Resource(name = "tcFactory")
  private TopicConnectionFactory tcFactory;

  @Resource(name = "cFactory")
  private ConnectionFactory cFactory;

  @Resource(name = "myQueue")
  private Queue myQueue;

  @Resource(name = "myTopic")
  private Topic myTopic;

  @Resource(name = "mailSession")
  private javax.mail.Session mailSession;

  @Resource(name = "myUrl")
  private java.net.URL myUrl;

  public int doStartTag() throws JspException {

    boolean passed = true;
    JspWriter out = pageContext.getOut();

    try {

      if (DB1 != null) {
        if (!(DB1 instanceof DataSource)) {
          passed = false;
          out.println("wrong type DataSource");
        } else
          out.println("passed DataSource");
      } else {
        passed = false;
        out.println("DB1 is null");
      }

      if (qcFactory != null) {
        if (!(qcFactory instanceof javax.jms.QueueConnectionFactory)) {
          passed = false;
          out.println("wrong type QueueConnectionFactory");
        } else
          out.println("passed QueueConnectionFactory");
      } else {
        passed = false;
        out.println("qcFactory is null");
      }

      if (tcFactory != null) {
        if (!(tcFactory instanceof javax.jms.TopicConnectionFactory)) {
          passed = false;
          out.println("wrong type TopicConnectionFactory");
        } else
          out.println("passed TopicConnectionFactory");
      } else {
        passed = false;
        out.println("tcFactory is null");
      }

      if (cFactory != null) {
        if (!(cFactory instanceof javax.jms.ConnectionFactory)) {
          out.println("wrong type ConnectionFactory");
          passed = false;
        } else
          out.println("passed ConnectionFactory");
      } else {
        passed = false;
        out.println("cFactory is null");
      }

      if (myQueue != null) {
        if (!(myQueue instanceof javax.jms.Queue)) {
          out.println("wrong type Queue");
          passed = false;
        } else
          out.println("passed Queue");
      } else {
        passed = false;
        out.println("myQueue is null");
      }

      if (myTopic != null) {
        if (!(myTopic instanceof javax.jms.Topic)) {
          out.println("wrong type Topic");
          passed = false;
        } else
          out.println("passed Topic");
      } else {
        out.println("myTopic is null");
        passed = false;
      }

      if (mailSession != null) {
        if (!(mailSession instanceof javax.mail.Session)) {
          passed = false;
          out.println("wrong type .Session");
        } else
          out.println("passed Session");
      } else {
        passed = false;
        out.println("mailSession is null");
      }

      if (myUrl != null) {
        if (!(myUrl instanceof java.net.URL)) {
          passed = false;
          out.println("wrong type URL ");
        } else
          out.println("passed URL ");
      } else {
        passed = false;
        out.println("myUrl is null");
      }

      if (passed == true)
        out.println("Test PASSED.");

    } catch (IOException ioe) {
      throw new JspException("Unexpected Exception", ioe);
    }
    return SKIP_BODY;
  }
}
