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

package com.sun.ts.tests.common.connector.whitebox;

import java.lang.reflect.Method;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.UnavailableException;
import javax.resource.spi.work.Work;
import com.sun.ts.tests.common.connector.util.TSMessageListenerInterface;
import com.sun.ts.tests.common.connector.util.ConnectorStatus;
import com.sun.ts.tests.common.connector.util.AppException;
import javax.resource.ResourceException;

public class LocalTxMessageWork1 implements Work {

  private String name;

  private boolean stop = false;

  private MessageEndpointFactory factory;

  private LocalTxMessageXAResource msgxa = new LocalTxMessageXAResource(
      "LocalTxMessageXAResource1");

  private MessageEndpoint xaep;

  private MessageEndpoint ep2;

  public LocalTxMessageWork1(String name, MessageEndpointFactory factory) {
    this.factory = factory;
    this.name = name;
    System.out.println("LocalTxMessageWork1.constructor");

  }

  public void run() {

    while (!stop) {
      try {
        System.out.println("Inside the LocalTxMessageWork1 run ");
        // creating xaep to check if the message delivery is transacted.
        xaep = factory.createEndpoint(msgxa);

        ep2 = factory.createEndpoint(null);

        Method onMessagexa = getOnMessageMethod();
        xaep.beforeDelivery(onMessagexa);
        ((TSMessageListenerInterface) xaep)
            .onMessage("LocalTx MDB2 Transacted Message To MDB");
        xaep.afterDelivery();
        ConnectorStatus.getConnectorStatus()
            .logState("LocalTx MDB2 Transacted Message To MDB");

        callSysExp();
        callAppExp();

        System.out.println("LocalTx MDB2 Transacted Message To MDB");

        boolean de = factory.isDeliveryTransacted(onMessagexa);

        if (de) {
          ConnectorStatus.getConnectorStatus()
              .logState("LocalTx MDB2 delivery is transacted");
          System.out.println("LocalTx MDB2 delivery is transacted");
        }
        break;
      } catch (AppException ex) {
        ex.printStackTrace();
      }

      catch (NoSuchMethodException e) {
        e.printStackTrace();
      }

      catch (UnavailableException ex) {
        try {
          Thread.currentThread().sleep(3000);
        } catch (Exception e) {
          e.printStackTrace();
        }
      } catch (ResourceException re) {
        re.printStackTrace();
      }

    }

  }

  public void callSysExp() {

    try {
      Method onMessage = getOnMessageMethod();
      ep2.beforeDelivery(onMessage);
      ((TSMessageListenerInterface) ep2)
          .onMessage("Throw EJBException from Required");
      // this has been moved to finally clause to ensure that before and
      // after delivery calls are properly matched.
      // ep2.afterDelivery();
    } catch (NoSuchMethodException e) {
      System.out.println("LocalTxMessageWork1: NoSuchMethodException");
      e.getMessage();
      e.printStackTrace();
    } catch (UnavailableException e) {
      System.out.println("LocalTxMessageWork1: UnavailableException");
      e.printStackTrace();
    } catch (ResourceException re) {
      System.out.println("LocalTxMessageWork1: ResourceException");
      re.printStackTrace();
    } catch (AppException ae) {
      System.out.println("LocalTxMessageWork1: AppException");
      ae.printStackTrace();
    } catch (Exception e) {
      // if we are in here, we will assume that our exception was of type ejb
      // but it
      // could also be from a non-ejb POJO - thus we use this Exception type.
      System.out.println("EJBException thrown by Required");
      ConnectorStatus.getConnectorStatus()
          .logState("EJBException thrown by Required");
      e.printStackTrace();
    } finally {
      try {
        ep2.afterDelivery();
      } catch (ResourceException re2) {
        re2.printStackTrace();
      }
    }

  }

  public void callAppExp() {

    try {
      Method onMessage = getOnMessageMethod();
      ep2.beforeDelivery(onMessage);
      ((TSMessageListenerInterface) ep2)
          .onMessage("Throw AppException from Required");
      // this has been moved to finally clause to ensure that before and
      // after delivery calls are properly matched.
      // ep2.afterDelivery();
    } catch (AppException ejbe) {
      System.out.println("AppException thrown by Required MDB");
      ConnectorStatus.getConnectorStatus()
          .logState("AppException thrown by Required");
    } catch (NoSuchMethodException ns) {
      ns.printStackTrace();
    } catch (ResourceException re) {
      re.printStackTrace();
    } finally {
      try {
        ep2.afterDelivery();
      } catch (ResourceException re2) {
        re2.printStackTrace();
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

  @Override
  public void release() {
  }

  public void stop() {
    this.stop = true;
  }

  public String toString() {
    return name;
  }

}
