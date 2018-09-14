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
import java.util.ArrayList;
import java.util.List;

import com.sun.ts.tests.common.connector.util.TSMessageListenerInterface;
import com.sun.ts.tests.common.connector.util.ConnectorStatus;
import com.sun.ts.tests.common.connector.util.AppException;

import javax.resource.ResourceException;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.UnavailableException;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkContext;
import javax.resource.spi.work.WorkManager;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.XATerminator;
import javax.resource.spi.work.WorkContextProvider;

public class LocalTxMessageWork implements Work, WorkContextProvider {

  private String name;

  private boolean stop = false;

  private MessageEndpointFactory factory;

  private LocalTxMessageXAResource msgxa = new LocalTxMessageXAResource(
      "LocalTxMessageXAResource");

  private MessageEndpoint ep2;

  private List<WorkContext> contextsList = new ArrayList<WorkContext>();

  private BootstrapContext ctx = null;

  /*
   * XXXX private WorkManager wm = null; private XATerminator xa; private String
   * sicUser = ""; // this should correspond to ts.jte's 'user' property private
   * String sicPwd = ""; // this should correspond to ts.jte's 'password'
   * property private String eisUser = ""; // this should correspond to ts.jte's
   * 'user1' property private String eisPwd = ""; // this should correspond to
   * ts.jte's 'password' property
   */
  private final String SICFAIL = "mdb not executed with proper SIC principal";

  private final String SICPASS = "mdb executed with proper SIC principal";

  public LocalTxMessageWork(String name, MessageEndpointFactory factory) {
    this.factory = factory;
    this.name = name;

    /*
     * XXXX this.sicUser = System.getProperty("j2eelogin.name"); this.sicPwd =
     * System.getProperty("j2eelogin.password"); this.eisUser =
     * System.getProperty("eislogin.name"); this.eisPwd =
     * System.getProperty("eislogin.password");
     */
    debug("LocalTxMessageWork.constructor");
  }

  public void setBootstrapContext(BootstrapContext bsc) {
    this.ctx = bsc;
    /*
     * XXXX this.wm = bsc.getWorkManager(); this.xa = ctx.getXATerminator();
     */
  }

  /*
   * This is a privaet convenience method that is use for sending a message that
   * contains some role information to the mdb. The mdb will be looking for a
   * msg that begins with the string "ROLE". Once the mdb encounters this msg,
   * it will perform a specific test and then send back a response to us via a
   * AppException that we will want to log. This method is used to assist with
   * checking assertions Connector:SPEC:232 and Connector:SPEC:233. (This is
   * used in conjunction with connector/mdb/MessageBean.java)
   */
  private void doSICMsgCheck(MessageEndpoint ep, Method onMessage) {

    try {

      ep.beforeDelivery(onMessage);
      String message = "ROLE: ADM";

      ((TSMessageListenerInterface) ep).onMessage(message);
    } catch (AppException ex) {
      String str = ex.getMessage();
      debug("str = " + str);
      if ((str != null) && (str.equals("MDB-SIC SUCCESS"))) {
        debug(SICPASS);
        ConnectorStatus.getConnectorStatus().logState(SICPASS);
      } else {
        debug(
            "MDB-SIC FAILED due to AppException with msg: " + ex.getMessage());
        debug(SICFAIL);
        ex.printStackTrace();
        ConnectorStatus.getConnectorStatus().logState(SICFAIL);
      }
    } catch (Exception e) {
      // problem if here - we had some problem with msg exchange with MDB.
      debug("MDB-SIC FAILED due to Exception with msg: " + e.getMessage());
      debug(SICFAIL);
      e.printStackTrace();
      ConnectorStatus.getConnectorStatus().logState(SICFAIL);
    } finally {
      try {
        ep.afterDelivery();
      } catch (ResourceException re2) {
        re2.printStackTrace();
      }
    }
  }

  public void run() {

    while (!stop) {
      try {
        debug("Inside the LocalTxMessageWork run ");
        // Createing ep and ep1 for comparison

        MessageEndpoint ep = factory.createEndpoint(null);
        MessageEndpoint ep1 = factory.createEndpoint(null);

        ep2 = factory.createEndpoint(null);
        // creating xaep to check if the message delivery is transacted.
        MessageEndpoint xaep = factory.createEndpoint(msgxa);

        if ((ep != null) && (!ep.equals(ep1))) {
          ConnectorStatus.getConnectorStatus()
              .logState("LocalTx Unique MessageEndpoint returned");
        }

        chkMessageEndpointImpl(ep);

        Method onMessage = getOnMessageMethod();
        ep.beforeDelivery(onMessage);
        ((TSMessageListenerInterface) ep).onMessage("LocalTx Message To MDB");
        ep.afterDelivery();
        ConnectorStatus.getConnectorStatus().logState("LocalTx Message To MDB");

        doSICMsgCheck(ep, onMessage);

        Method onMessagexa = getOnMessageMethod();
        xaep.beforeDelivery(onMessagexa);
        ((TSMessageListenerInterface) xaep)
            .onMessage("LocalTx Non Transacted Message To MDB1");
        xaep.afterDelivery();

        ConnectorStatus.getConnectorStatus()
            .logState("LocalTx Non Transacted Message To MDB1");

        System.out.println("Calling sysExp()");

        callSysExp();
        callAppExp();

        boolean de = factory.isDeliveryTransacted(onMessagexa);

        if (!de) {
          System.out.println("MDB1 delivery is not transacted");
          ConnectorStatus.getConnectorStatus()
              .logState("LocalTx MDB1 delivery is not transacted");
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
      } catch (NoSuchMethodException nse) {
        nse.printStackTrace();
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
          .onMessage("Throw EJBException from NotSupported");
      // this has been moved to finally clause to ensure that before and
      // after delivery calls are properly matched.
      // ep2.afterDelivery();
    } catch (NoSuchMethodException e) {
      debug("LocalTxMessageWork: NoSuchMethodException");
      e.getMessage();
      e.printStackTrace();
    } catch (UnavailableException e) {
      debug("LocalTxMessageWork: UnavailableException");
      e.printStackTrace();
    } catch (ResourceException re) {
      debug("LocalTxMessageWork: ResourceException");
      re.printStackTrace();
    } catch (AppException ae) {
      debug("LocalTxMessageWork: AppException");
      ae.printStackTrace();
    } catch (Exception e) {
      // if we are in here, we assume our exception is expected and is of type
      // ejb
      // but it could also be from a non-ejb POJO - thus we use this Exception
      // type.
      debug("EJBException thrown by NotSupported MDB");
      ConnectorStatus.getConnectorStatus()
          .logState("EJBException thrown by NotSupported");
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
          .onMessage("Throw AppException from NotSupported");
      // this has been moved to finally clause to ensure that before and
      // after delivery calls are properly matched.
      // ep2.afterDelivery();
    } catch (AppException ejbe) {
      debug("AppException thrown by NotSupported MDB");
      ConnectorStatus.getConnectorStatus()
          .logState("AppException thrown by NotSupported");
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

  private void chkMessageEndpointImpl(MessageEndpoint ep) {
    if (ep instanceof MessageEndpoint
        && ep instanceof TSMessageListenerInterface) {
      ConnectorStatus.getConnectorStatus()
          .logState("LocalTx MessageEndpoint interface implemented");
      ConnectorStatus.getConnectorStatus()
          .logState("LocalTx TSMessageListener interface implemented");
    } else {
      ConnectorStatus.getConnectorStatus().logState(
          "MessageEndpoint and TSMessageListenerInterface not implemented");
    }
  }

  @Override
  public List<WorkContext> getWorkContexts() {
    return contextsList;
  }

  public void addWorkContext(WorkContext ic) {
    contextsList.add(ic);
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

  private void debug(String val) {
    Debug.trace(val);
  }

}
