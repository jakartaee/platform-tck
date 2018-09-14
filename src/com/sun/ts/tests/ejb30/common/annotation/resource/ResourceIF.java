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

package com.sun.ts.tests.ejb30.common.annotation.resource;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

public interface ResourceIF {
  public void remove(); // for sfsb only

  public void testEJBContext() throws TestFailedException;

  public void testUserTransaction() throws TestFailedException;

  public void testDataSource() throws TestFailedException;

  public void testDataSource2() throws TestFailedException;

  public void testMailSession() throws TestFailedException;

  public void testUrl() throws TestFailedException;

  public void testTopicConnectionFactory() throws TestFailedException;

  public void testQueueConnectionFactory() throws TestFailedException;

  public void testConnectionFactoryQ() throws TestFailedException;

  public void testConnectionFactoryT() throws TestFailedException;

  public void testDestinationQ() throws TestFailedException;

  public void testDestinationT() throws TestFailedException;

  public void testQueue() throws TestFailedException;

  public void testTopic() throws TestFailedException;

  public void testOrb() throws TestFailedException;

  public void testCustomResourceInjected() throws TestFailedException;

  public void testCustomResourceLookup() throws TestFailedException;

  public void testTransactionSynchronizationRegistryInjected()
      throws TestFailedException;

  public void testTransactionSynchronizationRegistryLookup()
      throws TestFailedException;

  // not valid for stateful beans
  public void testTimerServiceInjected() throws TestFailedException;

  public void testTimerServiceLookup() throws TestFailedException;

}
