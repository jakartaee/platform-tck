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

/*
 * @(#)MessageBeanOne.java	1.3  03/05/16
 */

package com.sun.ts.tests.connector.mdb;

import javax.ejb.*;
import javax.naming.*;
import com.sun.ts.tests.common.connector.util.*;

/**
 * The MessageBean class is a message-driven bean. It implements the
 * javax.ejb.MessageDrivenBean and TSMessageListenerInterface. It is defined as
 * public (but not final or abstract). It defines a constructor and the methods
 * setMessageDrivenContext, ejbCreate, onMessage, and ejbRemove.
 */
public class MessageBeanOne
    implements MessageDrivenBean, TSMessageListenerInterface {

  private transient MessageDrivenContext mdc = null;

  private Context context;

  /**
   * Constructor, which is public and takes no arguments.
   */
  public MessageBeanOne() {
    System.out.println("In MessageBean.MessageBean()");
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
   * Casts the incoming Message to a TextMessage and displays the text.
   *
   * @param inMessage
   *          the incoming message
   */
  public void onMessage(String inMessage) throws AppException {

    if (!inMessage.equals("")) {
      if (inMessage.equals("Throw EJBException from Required")) {
        sysMessage();
      }

      if (inMessage.equals("Throw AppException from Required")) {
        appMessage();
      }

      sendReply(inMessage);
      System.out.println("MESSAGE BEAN: Message " + inMessage);
    }
  }

  private void sysMessage() {
    System.out
        .println("Inside sysMessage intentionally throwing an exception.");
    throw new SysException("Throw SysException");
  }

  private void appMessage() throws AppException {
    System.out.println("Inside appMessage");
    throw new AppException("Throw AppException");
  }

  private void sendReply(String msg) {
    try {
      System.out.println(msg);
    } catch (Exception e) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(e);
    }
  }

  /**
   * ejbRemove method, declared as public (but not final or static), with a
   * return type of void, and with no arguments.
   */
  public void ejbRemove() {
    System.out.println("In MessageBean.remove()");
  }
}
