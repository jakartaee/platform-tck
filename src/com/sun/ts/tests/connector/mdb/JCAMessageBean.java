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
 * $Id: JCAMessageBean.java 58899 2009-07-31 12:56:17Z phendley $
 */

package com.sun.ts.tests.connector.mdb;

import javax.ejb.*;
import javax.naming.*;
import com.sun.ts.tests.common.connector.util.*;
import java.security.Principal;

/**
 * The JCAMessageBean class is a message-driven bean. It implements the
 * javax.ejb.MessageDrivenBean and TSMessageListenerInterface. It is defined as
 * public (but not final or abstract). It defines a constructor and the methods
 * setMessageDrivenContext, ejbCreate, onMessage, and ejbRemove.
 */
public class JCAMessageBean
    implements MessageDrivenBean, TSMessageListenerInterface {

  private transient MessageDrivenContext mdc = null;

  private Context context;

  private final String MDBROLE = "ADM"; // as defined in
                                        // msginflow_mdb_msginflow_ejb.xml

  /**
   * Constructor, which is public and takes no arguments.
   */
  public JCAMessageBean() {
    System.out.println("In JCAMessageBean.JCAMessageBean()");
  }

  /**
   * setMessageDrivenContext method, declared as public (but not final or
   * static), with a return type of void, and with one argument of type
   * javax.ejb.MessageDrivenContext.
   *
   * @param mdc
   *          the context to set
   */
  public void setMessageDrivenContext(MessageDrivenContext mdc) {
    this.mdc = mdc;
  }

  /**
   * ejbCreate method, declared as public (but not final or static), with a
   * return type of void, and with no arguments.
   */
  public void ejbCreate() {

  }

  /**
   * onMessage method, declared as public (but not final or static), with a
   * return type of void, and with one argument of type javax.jms.Message.
   *
   * @param inMessage
   *          the incoming message
   */
  public void onMessage(String inMessage) throws AppException {

    System.out.println("enterred JCAMessageBean.onMessage()");
    if (inMessage != null) {
      System.out.println("JCAMessageBean:  inMessage = " + inMessage);
    }

    if (!inMessage.equals("")) {
      if (inMessage.equals("Throw SysException from IBAnnoMessageWork")) {
        sysMessage();
        sendReply(inMessage);
        System.out.println("MESSAGE BEAN: Message " + inMessage);
      }

      if (inMessage.equals("Throw AppException from IBAnnoMessageWork")) {
        appMessage();
        sendReply(inMessage);
        System.out.println("MESSAGE BEAN: Message " + inMessage);
      }
    }
  }

  private void sysMessage() {
    System.out.println("Inside sysMessage");
    throw new SysException("Throw SysException");
  }

  private void appMessage() throws AppException {
    System.out.println("Inside appMessage");
    throw new AppException("Throw AppException");
  }

  private void sendReply(String msg) {

    System.out.println(msg);
  }

  /**
   * ejbRemove method, declared as public (but not final or static), with a
   * return type of void, and with no arguments.
   */
  public void ejbRemove() {
    System.out.println("In JCAMessageBean.remove()");
  }
}
