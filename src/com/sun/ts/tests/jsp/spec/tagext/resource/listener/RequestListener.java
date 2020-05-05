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

package com.sun.ts.tests.jsp.spec.tagext.resource.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import javax.annotation.Resource;

import javax.sql.DataSource;
import jakarta.jms.QueueConnectionFactory;
import jakarta.jms.TopicConnectionFactory;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Queue;
import jakarta.jms.Topic;
import jakarta.mail.Session;
import java.net.URL;

public final class RequestListener implements ServletRequestListener {

  private ServletContext context = null;

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
  private jakarta.mail.Session mailSession;

  @Resource(name = "myUrl")
  private java.net.URL myUrl;

  public void requestDestroyed(ServletRequestEvent event) {
    return;
  }

  public void requestInitialized(ServletRequestEvent event) {
    this.context = event.getServletContext();

    StringBuffer pw = new StringBuffer();
    Boolean passed = true;

    pw.append("RequestListener requestInitialized");

    if (DB1 != null) {
      if (!(DB1 instanceof DataSource)) {
        passed = false;
        pw.append("wrong type DataSource");
      } else
        pw.append("passed DataSource");
    } else {
      passed = false;
      pw.append("DB1 is null");
    }

    if (qcFactory != null) {
      if (!(qcFactory instanceof jakarta.jms.QueueConnectionFactory)) {
        passed = false;
        pw.append("wrong type QueueConnectionFactory");
      } else
        pw.append("passed QueueConnectionFactory");
    } else {
      passed = false;
      pw.append("qcFactory is null");
    }

    if (tcFactory != null) {
      if (!(tcFactory instanceof jakarta.jms.TopicConnectionFactory)) {
        passed = false;
        pw.append("wrong type TopicConnectionFactory");
      } else
        pw.append("passed TopicConnectionFactory");
    } else {
      passed = false;
      pw.append("tcFactory is null");
    }

    if (cFactory != null) {
      if (!(cFactory instanceof jakarta.jms.ConnectionFactory)) {
        pw.append("wrong type ConnectionFactory");
        passed = false;
      } else
        pw.append("passed ConnectionFactory");
    } else {
      passed = false;
      pw.append("cFactory is null");
    }

    if (myQueue != null) {
      if (!(myQueue instanceof jakarta.jms.Queue)) {
        pw.append("wrong type Queue");
        passed = false;
      } else
        pw.append("passed Queue");
    } else {
      passed = false;
      pw.append("myQueue is null");
    }

    if (myTopic != null) {
      if (!(myTopic instanceof jakarta.jms.Topic)) {
        pw.append("wrong type Topic");
        passed = false;
      } else
        pw.append("passed Topic");
    } else {
      pw.append("myTopic is null");
      passed = false;
    }

    if (mailSession != null) {
      if (!(mailSession instanceof jakarta.mail.Session)) {
        passed = false;
        pw.append("wrong type .Session");
      } else
        pw.append("passed Session");
    } else {
      passed = false;
      pw.append("mailSession is null");
    }

    if (myUrl != null) {
      if (!(myUrl instanceof java.net.URL)) {
        passed = false;
        pw.append("wrong type URL ");
      } else
        pw.append("passed URL ");
    } else {
      passed = false;
      pw.append("myUrl is null");
    }

    context.setAttribute("CTSTestRequestListener", pw.toString());
  }
}
