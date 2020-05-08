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

package com.sun.ts.lib.porting;

import jakarta.jms.*;

/**
 * This is the TSJMSObjectsInterface. An implementation of this interface must
 * be provided by each JMS implementation to support their own implementation of
 * getting administered objects.
 *
 */
public interface TSJMSObjectsInterface {
  /**
   * This method allows JMS implementation to get Queue
   */

  public Queue getQueue(String name) throws Exception;

  /**
   * This method allows JMS implementation to get Topic
   */

  public Topic getTopic(String name) throws Exception;

  /**
   * This method allows JMS implementation to get TopicConnectionFactory
   */

  public TopicConnectionFactory getTopicConnectionFactory(String name)
      throws Exception;

  /**
   * This method allows JMS implementation to get QueueConnectionFactory
   */

  public QueueConnectionFactory getQueueConnectionFactory(String name)
      throws Exception;

  /**
   * This method allows JMS implementation to get ConnectionFactory
   */

  public ConnectionFactory getConnectionFactory(String name) throws Exception;

}
