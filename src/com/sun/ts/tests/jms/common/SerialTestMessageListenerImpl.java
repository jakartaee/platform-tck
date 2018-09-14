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
 * @(#)SerialTestMessageListenerImpl.java	1.10 03/05/16
 */
package com.sun.ts.tests.jms.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.io.*;
import java.rmi.RemoteException;
import java.util.*;
import com.sun.javatest.Status;
import javax.jms.*;
import javax.naming.*;

public class SerialTestMessageListenerImpl
    implements javax.jms.MessageListener {
  public boolean inUse = false;

  public boolean testFailed = false;

  public DoneLatch monitor = new DoneLatch();

  public void onMessage(Message m) {

    // first check for concurrent usage
    if (inUse == true) {
      TestUtil.logMsg("Error -- concurrent use of MessageListener");
      testFailed = true;
    }

    // set flag, then check for final message
    inUse = true;
    TestUtil.logMsg("*MessageListener: onMessage() called. "
        + "Forcing other message listeners to wait.");
    try {
      if (m.getBooleanProperty("COM_SUN_JMS_TEST_LASTMESSAGE") == true) {
        TestUtil.logMsg("*MessageListener: Received final message");
        monitor.allDone();
      } else {

        // wait to force next onMessage() to wait
        for (int i = 0; i < 10000; i++) {
        }
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logErr("Failure in message listener: " + e.getMessage());
      testFailed = true;
    }

    // unset flag
    inUse = false;
  }

}
