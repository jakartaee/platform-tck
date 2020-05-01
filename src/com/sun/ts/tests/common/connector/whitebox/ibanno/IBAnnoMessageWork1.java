/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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
import java.util.Iterator;
import jakarta.resource.spi.endpoint.MessageEndpoint;
import jakarta.resource.spi.endpoint.MessageEndpointFactory;
import jakarta.resource.spi.UnavailableException;
import jakarta.resource.spi.work.Work;
import com.sun.ts.tests.common.connector.util.TSMessageListenerInterface;
import com.sun.ts.tests.common.connector.util.ConnectorStatus;
import com.sun.ts.tests.common.connector.util.AppException;
import jakarta.resource.ResourceException;
import com.sun.ts.tests.common.connector.whitebox.*;

public class IBAnnoMessageWork1 implements Work {

  private String name;

  private boolean stop = false;

  private MessageEndpointFactory factory;

  private IBAnnoMessageXAResource1 msgxa = new IBAnnoMessageXAResource1();

  private MessageEndpoint xaep;

  private MessageEndpoint ep2;

  public IBAnnoMessageWork1(String name, MessageEndpointFactory factory) {
    this.factory = factory;
    this.name = name;
    System.out.println("IBAnnoMessageWork1.constructor");
  }

  public void run() {

    while (!stop) {
      try {
        System.out.println("Inside the IBAnnoMessageWork1 run ");
        // creating xaep to check if the message delivery is transacted.
        xaep = factory.createEndpoint(msgxa);

        ep2 = factory.createEndpoint(null);

        Method onMessagexa = getOnMessageMethod();
        xaep.beforeDelivery(onMessagexa);
        ((TSMessageListenerInterface) xaep)
            .onMessage("IBAnno MDB2 Transacted Message To MDB");
        xaep.afterDelivery();
        Debug.trace("IBAnno MDB2 Transacted Message To MDB");

        callSysExp();
        callAppExp();

        boolean de = factory.isDeliveryTransacted(onMessagexa);

        if (de) {
          Debug.trace("IBAnno MDB2 delivery is transacted");
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
          .onMessage("Throw IBAnnoSysException from Required");
      // this has been moved to finally clause to ensure that before and
      // after delivery calls are properly matched.
      // ep2.afterDelivery();
    } catch (NoSuchMethodException e) {
      System.out.println("IBAnnoMessageWork1: NoSuchMethodException");
      e.getMessage();
      e.printStackTrace();
    } catch (UnavailableException e) {
      System.out.println("IBAnnoMessageWork1: UnavailableException");
      e.printStackTrace();
    } catch (ResourceException re) {
      System.out.println("IBAnnoMessageWork1: ResourceException");
      re.printStackTrace();
    } catch (AppException ae) {
      System.out.println("IBAnnoMessageWork1: AppException");
      ae.printStackTrace();
    } catch (Exception e) {
      // if we are in here, we will assume that our exception was of type ejb
      // but it
      // could also be from a non-ejb POJO - thus we use this Exception type.
      Debug.trace("IBAnnoEJBException thrown but Required");
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
          .onMessage("Throw IBAnnoAppException from Required");
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

  public void release() {
  }

  public void stop() {
    this.stop = true;
  }

  public String toString() {
    return name;
  }

}
