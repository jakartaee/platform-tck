/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2022 Contributors to Eclipse Foundation. All rights reserved.
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

package com.sun.ts.tests.jms.ee20.resourcedefs.annotations;

import jakarta.jms.JMSConnectionFactoryDefinition;
import jakarta.jms.JMSConnectionFactoryDefinitions;
import jakarta.jms.JMSDestinationDefinition;
import jakarta.jms.JMSDestinationDefinitions;

//-------------------------------------
// JMS Destination Resource Definitions
//-------------------------------------
@JMSDestinationDefinition(
      description="Define Queue JSPMyTestQueue",
      interfaceName="jakarta.jms.Queue",
      name="java:global/env/JSPMyTestQueue",
      destinationName="JSPMyTestQueue"
 )

@JMSDestinationDefinition(
      description="Define Topic JSPMyTestTopic",
      interfaceName="jakarta.jms.Topic",
      name="java:app/env/JSPMyTestTopic",
      destinationName="JSPMyTestTopic"
 )

//-------------------------------------------
// JMS ConnectionFactory Resource Definitions
//-------------------------------------------
@JMSConnectionFactoryDefinition(
      description="Define ConnectionFactory JSPMyTestConnectionFactory",
      interfaceName="jakarta.jms.ConnectionFactory",
      name="java:global/JSPMyTestConnectionFactory",
      user = "j2ee",
      password = "j2ee"
 )

@JMSConnectionFactoryDefinition(
      description="Define QueueConnectionFactory JSPMyTestQueueConnectionFactory",
      interfaceName="jakarta.jms.QueueConnectionFactory",
      name="java:app/JSPMyTestQueueConnectionFactory",
      user = "j2ee",
      password = "j2ee"
 )

@JMSConnectionFactoryDefinition(
      description="Define TopicConnectionFactory JSPMyTestTopicConnectionFactory",
      interfaceName="jakarta.jms.TopicConnectionFactory",
      name="java:module/JSPMyTestTopicConnectionFactory",
      user = "j2ee",
      password = "j2ee"
 )

@JMSConnectionFactoryDefinition(
      description="Define Durable TopicConnectionFactory JSPMyTestDurableTopicConnectionFactory",
      interfaceName="jakarta.jms.TopicConnectionFactory",
      name="java:comp/env/jms/JSPMyTestDurableTopicConnectionFactory",
      user = "j2ee",
      password = "j2ee",
      clientId = "MyClientID",
      properties = { "Property1=10", "Property2=20" },
      transactional = false,
      maxPoolSize = 30,
      minPoolSize = 20
 )

public class MyBean {
  public MyBean() {
  }
}
