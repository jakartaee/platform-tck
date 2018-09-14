/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.common.connector.whitebox.ibanno;

import java.lang.reflect.Method;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.UnavailableException;
import javax.resource.spi.work.Work;
import com.sun.ts.tests.common.connector.util.TSMessageListenerInterface;
import com.sun.ts.tests.common.connector.util.ConnectorStatus;
import com.sun.ts.tests.common.connector.util.AppException;
import com.sun.ts.tests.common.connector.whitebox.*;

public class IBAnnoMessageWork2 implements Work {

  private String name;

  private boolean stop = false;

  private MessageEndpointFactory factory;

  private LocalTxMessageXAResource msgxa = new LocalTxMessageXAResource(
      "LocalTxMessageXAResource2");

  public IBAnnoMessageWork2(String name, MessageEndpointFactory factory) {
    this.factory = factory;
    this.name = name;
    System.out.println("IBAnnoMessageWork2.constructor");
  }

  public void run() {

    while (!stop) {
      try {

        // creating xaep to check if the message delivery is transacted.
        MessageEndpoint xaep = factory.createEndpoint(msgxa);
        MessageEndpoint xaep1 = factory.createEndpoint(msgxa);
        MessageEndpoint xaep2 = factory.createEndpoint(msgxa);

        Method onMessagexa = getOnMessageMethod();
        ((TSMessageListenerInterface) xaep)
            .onMessage("IBAnnoMessageWork2 MDB2 Transacted Message1");
        ((TSMessageListenerInterface) xaep1)
            .onMessage("IBAnnoMessageWork2 MDB2 Transacted Message2");
        ((TSMessageListenerInterface) xaep2)
            .onMessage("IBAnnoMessageWork2 MDB2 Transacted Message3");

        Debug.trace("IBAnnoMessageWork2 MDB2 Transacted Message1");
        Debug.trace("IBAnnoMessageWork2 MDB2 Transacted Message2");
        Debug.trace("IBAnnoMessageWork2 MDB2 Transacted Message3");

        break;
      } catch (AppException ex) {
        ex.printStackTrace();
      } catch (UnavailableException ex) {
        try {
          Thread.currentThread().sleep(3000);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

  }

  public Method getOnMessageMethod() {

    Method onMessageMethod = null;
    try {
      Class msgListenerClass = TSMessageListenerInterface.class;
      Class[] paramTypes = { java.lang.String.class };
      onMessageMethod = msgListenerClass.getMethod("onMessage", paramTypes);

    } catch (NoSuchMethodException ex) {
      ex.printStackTrace();
    }
    return onMessageMethod;
  }

  public void release() {
  }

  public void stop() {
    this.stop = true;
  }

  public String toString() {
    return name;
  }

}
