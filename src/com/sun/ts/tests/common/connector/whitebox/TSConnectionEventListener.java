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

import javax.resource.spi.*;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionEvent;
import java.util.*;
import com.sun.ts.tests.common.connector.util.*;

public class TSConnectionEventListener implements ConnectionEventListener {

  private Vector listeners;

  private ManagedConnection mcon;

  /*
   * @name TSConnectionEventListener
   * 
   * @desc TSConnectionEventListener constructor
   * 
   * @param ManagedConnection mcon
   */

  public TSConnectionEventListener(ManagedConnection mcon) {
    listeners = new Vector();
    ConnectorStatus.getConnectorStatus()
        .logAPI("TSConnectionEventListener.constructor", "mcon", "");
    this.mcon = mcon;
  }

  /*
   * @name sendEvent
   * 
   * @desc send event notifications
   * 
   * @param int eventType, Exception ex, Object connectionHandle
   */
  public void sendEvent(int eventType, Exception ex, Object connectionHandle) {
    Vector list = (Vector) listeners.clone();
    ConnectionEvent ce = null;
    if (ex == null) {
      ce = new ConnectionEvent(mcon, eventType);
    } else {
      ce = new ConnectionEvent(mcon, eventType, ex);
    }
    if (connectionHandle != null) {
      ce.setConnectionHandle(connectionHandle);
    }
    int size = list.size();
    for (int i = 0; i < size; i++) {
      ConnectionEventListener l = (ConnectionEventListener) list.elementAt(i);
      switch (eventType) {
      case ConnectionEvent.CONNECTION_CLOSED:
        l.connectionClosed(ce);
        ConnectorStatus.getConnectorStatus().logAPI(
            "TSConnectionEventListener.sendEvent", "CONNECTION_CLOSED", "");
        System.out
            .println("TSConnectionEventListener.sendEvent:CONNECTION_CLOSED");
        break;
      case ConnectionEvent.LOCAL_TRANSACTION_STARTED:
        l.localTransactionStarted(ce);
        ConnectorStatus.getConnectorStatus().logAPI(
            "TSConnectionEventListener.sendEvent", "LOCAL_TRANSACTION_STARTED",
            "");
        break;
      case ConnectionEvent.LOCAL_TRANSACTION_COMMITTED:
        l.localTransactionCommitted(ce);
        ConnectorStatus.getConnectorStatus().logAPI(
            "TSConnectionEventListener.sendEvent", "LOCAL_TRANSACTION_COMMITED",
            "");
        break;
      case ConnectionEvent.LOCAL_TRANSACTION_ROLLEDBACK:
        l.localTransactionRolledback(ce);
        ConnectorStatus.getConnectorStatus().logAPI(
            "TSConnectionEventListener.sendEvent",
            "LOCAL_TRANSACTION_ROLLEDBACK", "");
        break;
      case ConnectionEvent.CONNECTION_ERROR_OCCURRED:
        l.connectionErrorOccurred(ce);
        ConnectorStatus.getConnectorStatus().logAPI(
            "TSConnectionEventListener.sendEvent", "CONNECTION_ERROR_OCCURED",
            "");
        break;
      default:
        throw new IllegalArgumentException("Illegal eventType: " + eventType);
      }
    }
  }

  public void localTransactionRolledback(ConnectionEvent event) {

    ConnectorStatus.getConnectorStatus()
        .logAPI("TSConnectionEventListener.localTransactionRolledBack", "", "");
    System.out.println("TSConnectionEventListener.localTransactionRolledback");
  }

  public void localTransactionCommitted(ConnectionEvent event) {

    ConnectorStatus.getConnectorStatus()
        .logAPI("TSConnectionEventListener.localTransactionCommitted", "", "");
    System.out.println("TSConnectionEventListener.localTransactionCommited");
  }

  public void localTransactionStarted(ConnectionEvent event) {

    ConnectorStatus.getConnectorStatus()
        .logAPI("TSConnectionEventListener.localTransactionStarted", "", "");
    System.out.println("TSConnectionEventListener.localTransactionStarted");
  }

  /*
   * @name addConnectorListener
   * 
   * @desc add a connector event listener
   * 
   * @param ConnectionEventListener
   */
  public void addConnectorListener(ConnectionEventListener l) {
    ConnectorStatus.getConnectorStatus().logAPI(
        "TSConnectionEventListener.addConnectorListener",
        "connectionEventListener", "");
    listeners.addElement(l);
  }

  /*
   * @name removeConnectorListener
   * 
   * @desc remove a connector event listener
   * 
   * @param ConnectionEventListener
   */
  public void removeConnectorListener(ConnectionEventListener l) {
    ConnectorStatus.getConnectorStatus().logAPI(
        "TSConnectionEventListener.removeConnectorListener",
        "connectionEventListener", "");
    listeners.removeElement(l);
  }

  /*
   * @name connectionClosed
   * 
   * @desc
   * 
   * @param ConnectionEvent
   */
  public void connectionClosed(ConnectionEvent event) {
    // do nothing. The event is sent by the TSEISConnection wrapper
  }

  /*
   * @name connectionErrorOccured
   * 
   * @desc add a connector event listener
   * 
   * @param ConnectionEvent
   */
  public void connectionErrorOccurred(ConnectionEvent event) {
    ConnectorStatus.getConnectorStatus().logAPI(
        "TSEISConnectionEventListener.connectionErrorOccured",
        "connectionEvent", "");
    sendEvent(ConnectionEvent.CONNECTION_ERROR_OCCURRED, event.getException(),
        null);
  }
}
