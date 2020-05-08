/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jms.ee20.ra.common.messaging;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.commonee.Client;
import jakarta.jms.JMSException;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.QueueConnectionFactory;
import jakarta.jms.Session;
import jakarta.jms.Topic;
import jakarta.jms.TopicConnectionFactory;

abstract public class TopicClientBase extends QueueClientBase
    implements Constants {
  //////////////////////////////////////////////////////////////////////
  // Topic related stuff
  //////////////////////////////////////////////////////////////////////
  // These are declared in super class jms.commonee.Client
  // protected TopicConnection tConnect;
  // protected TopicSession tSession;
  // protected TopicConnectionFactory tFactory;
  // protected TopicPublisher tPub;

  protected Topic sendTopic;

  abstract protected void initSendTopic();

  protected Topic getSendTopic() {
    return sendTopic;
  }

  protected void setSendTopic(Topic topic) {
    this.sendTopic = topic;
  }

  abstract protected void initTopicConnectionFactory();

  protected TopicConnectionFactory getTopicConnectionFactory() {
    return tFactory;
  }

  protected void setTopicConnectionFactory(TopicConnectionFactory tf) {
    tFactory = tf;
  }

  //////////////////////////////////////////////////////////////////////
  // noop for Queue related methods
  //////////////////////////////////////////////////////////////////////
  final protected void initSendQueue() {
  }

  //////////////////////////////////////////////////////////////////////

  @Override
  protected MessageProducer getMessageProducer() throws JMSException {
    // TopicPublisher tPub declared in jms.commonee.Client
    // TopicSession tSession declared in jms.commonee.Client
    tPub = tSession.createPublisher(getSendTopic());
    return tPub;
  }

  @Override
  protected void configureTopic() throws JMSException {
    initTopicConnectionFactory();
    tConnect = tFactory.createTopicConnection(jmsUser, jmsPassword);
    tSession = tConnect.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
    tConnect.start();
    initSendTopic();
  }
}
