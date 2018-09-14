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
 * $Id: Tests.java 59995 2009-10-14 12:05:29Z af70133 $
 */
package com.sun.ts.tests.jms.commonee;

import com.sun.ts.lib.util.*;

import java.util.Properties;
import java.util.ArrayList;
import javax.ejb.Remote;
import javax.jms.Queue;

@Remote
public interface Tests {
  public void initLogging(Properties p);

  public void remove();

  public ArrayList sendTextMessage_CQ(String tesname, String text);

  public ArrayList sendMessageP_CQ(String tesname, String text, boolean val);

  public ArrayList sendMessagePP_CQ(String tesname, String text, boolean val,
      String props, String value);

  public String receiveTextMessage_CQ();

  public String receiveMessageS_CQ(String selector);

  public int browseTextMessage_CQ(int num, String msg);

  public int browseMessageS_CQ(int num, String msg, String selector);

  public ArrayList sendTextMessage_CT(String tesname, String text);

  public String receiveTextMessage_CT();

  public int getAck_CQ();

  public int getAck_CT();

  public boolean getQueue();

  public boolean getSelector(String selector);

  public ArrayList sendTextMessage_Q(String tesname);

  public ArrayList sendTextMessage_Q(String tesname, boolean setDest);

  public ArrayList sendTextMessage_Q(String tesname, String text);

  public ArrayList sendTextMessage_Q(String tesname, String text,
      Queue testQueue);

  public ArrayList sendTextMessage_Q(String tesname, boolean setDest, int mode);

  public ArrayList sendFullBytesMessage_Q(String tesname);

  public ArrayList sendBytesMessage_Q(String tesname, boolean setDest);

  public ArrayList sendBytesMessage_Q(String tesname, boolean setDest,
      int mode);

  public boolean verifyFullBytesMessage();

  public ArrayList sendFullMapMessage_Q(String tesname);

  public ArrayList sendMapMessage_Q(String tesname, boolean setDest);

  public ArrayList sendMapMessage_Q(String tesname, boolean setDest, int mode);

  public boolean verifyFullMapMessage();

  public ArrayList sendFullStreamMessage_Q(String tesname);

  public ArrayList sendStreamMessage_Q(String tesname, boolean setDest);

  public ArrayList sendStreamMessage_Q(String tesname, boolean setDest,
      int mode);

  public boolean verifyFullStreamMessage();

  public ArrayList sendObjectMessage_Q(String tesname);

  public ArrayList sendObjectMessage_Q(String tesname, boolean setDest);

  public ArrayList sendObjectMessage_Q(String tesname, boolean setDest,
      int mode);

  public String getMessageID();

  public long getTimeStamp();

  public String getCorrelationID();

  public String getReplyTo();

  public String getDestination_1();

  public String getDestination();

  public String getType();

  public int getPriority();

  public long getExpiration();

  public int getDeliveryMode();

  public String getText();
}
