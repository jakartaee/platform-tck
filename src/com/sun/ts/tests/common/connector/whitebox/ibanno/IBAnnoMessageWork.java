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
import java.util.ArrayList;
import java.util.List;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.UnavailableException;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkContextProvider;
import javax.resource.spi.work.WorkContext;
import javax.resource.ResourceException;
import com.sun.ts.tests.common.connector.util.TSMessageListenerInterface;
import com.sun.ts.tests.common.connector.util.ConnectorStatus;
import com.sun.ts.tests.common.connector.util.AppException;
import com.sun.ts.tests.common.connector.whitebox.Debug;
import com.sun.ts.tests.common.connector.whitebox.XAMessageXAResource;

public class IBAnnoMessageWork implements Work, WorkContextProvider {

  private String name;

  private boolean stop = false;

  private MessageEndpointFactory factory;

  private XAMessageXAResource msgxa = new XAMessageXAResource();

  private MessageEndpoint ep2;

  private List<WorkContext> contextsList = new ArrayList<WorkContext>();

  public IBAnnoMessageWork(String name, MessageEndpointFactory factory) {
    this.factory = factory;
    this.name = name;

    Debug.trace("IBAnnoMessageWork constructor");
  }

  public void run() {

    while (!stop) {
      try {

        // Createing ep and ep1 for comparison
        MessageEndpoint ep1 = factory.createEndpoint(null);
        ep2 = factory.createEndpoint(null);

        // creating xaep to check if the message delivery is transacted.
        MessageEndpoint xaep = factory.createEndpoint(msgxa);

        if (!ep2.equals(ep1)) {
          Debug.trace("IBAnnoMessageWork XA Unique MessageEndpoint returned");
        }

        chkMessageEndpointImpl(ep1);

        Method onMessage = getOnMessageMethod();
        ep1.beforeDelivery(onMessage);
        ((TSMessageListenerInterface) ep1)
            .onMessage("IBAnnoMessageWork XA Message To MDB");
        ep1.afterDelivery();

        Method onMessagexa = getOnMessageMethod();
        xaep.beforeDelivery(onMessagexa);
        ((TSMessageListenerInterface) xaep)
            .onMessage("IBAnnoMessageWork XA Non Transacted Message To MDB1");
        xaep.afterDelivery();

        boolean de = factory.isDeliveryTransacted(onMessagexa);

        callSysExp();
        callAppExp();

        if (!de) {
          Debug.trace("IBAnnoMessageWork XA MDB1 delivery is not transacted");
        } else {
          Debug.trace("IBAnnoMessageWork XA MDB1 delivery is transacted");
        }

        break;

      } catch (AppException ex) {
        ex.printStackTrace();

      } catch (UnavailableException ex) {
        try {
          Thread.currentThread().sleep(3000);
        } catch (Exception e) {
          e.printStackTrace();
        }

      } catch (NoSuchMethodException ns) {
        ns.printStackTrace();

      } catch (ResourceException re) {
        re.printStackTrace();
      }
    }

  }

  public void callSysExp() {

    try {
      Debug.trace(" in  IBAnnoMessageWork.callSysExp()");
      Method onMessage = getOnMessageMethod();
      ep2.beforeDelivery(onMessage);
      ((TSMessageListenerInterface) ep2)
          .onMessage("Throw SysException from IBAnnoMessageWork");

    } catch (NoSuchMethodException e) {
      System.out.println("IBAnnoMessageWork: NoSuchMethodException");
      e.getMessage();
      e.printStackTrace();
    } catch (UnavailableException e) {
      System.out.println("IBAnnoMessageWork: UnavailableException");
      e.printStackTrace();
    } catch (ResourceException re) {
      System.out.println("IBAnnoMessageWork: ResourceException");
      re.printStackTrace();
    } catch (AppException ae) {
      System.out.println("IBAnnoMessageWork: AppException");
      ae.printStackTrace();
    } catch (Exception e) {
      // if we are in here, we will assume that our exception was of type ejb
      // but we
      // should not code only to ejb as the messaging could be POJO's and not
      // ejb....
      System.out.println("EJBException thrown by NotSupported MDB");
      ConnectorStatus.getConnectorStatus()
          .logState("EJBException thrown by NotSupported");
      e.printStackTrace();
    } finally {
      try {
        // this ensures that before and
        // after delivery calls are properly matched.
        ep2.afterDelivery();
      } catch (ResourceException re2) {
        re2.printStackTrace();
      }
    }
  }

  public void callAppExp() {

    try {
      Debug.trace(" in  IBAnnoMessageWork.callAppExp()");
      Method onMessage = getOnMessageMethod();
      ep2.beforeDelivery(onMessage);
      ((TSMessageListenerInterface) ep2)
          .onMessage("Throw AppException from IBAnnoMessageWork");

    } catch (AppException ejbe) {
      System.out.println("AppException thrown by NotSupported MDB");
      ConnectorStatus.getConnectorStatus()
          .logState("AppException thrown by NotSupported");

    } catch (NoSuchMethodException ns) {
      ns.printStackTrace();

    } catch (ResourceException re) {
      re.printStackTrace();

    } finally {
      try {
        // this ensures that before and
        // after delivery calls are properly matched.
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

  private void chkMessageEndpointImpl(MessageEndpoint ep) {
    if ((ep instanceof MessageEndpoint)
        && (ep instanceof TSMessageListenerInterface)) {
      Debug.trace("IBANNO XA MessageEndpoint interface implemented");
      Debug.trace("IBANNO XA TSMessageListener interface implemented");
    } else {
      Debug.trace(
          "IBANNO XA MessageEndpoint and TSMessageListenerInterface not implemented");
    }

  }

  public List<WorkContext> getWorkContexts() {
    return contextsList;
  }

  public void addWorkContext(WorkContext ic) {
    contextsList.add(ic);
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
