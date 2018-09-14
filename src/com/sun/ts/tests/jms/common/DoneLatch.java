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
package com.sun.ts.tests.jms.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.io.*;
import java.rmi.RemoteException;
import java.util.*;
import com.sun.javatest.Status;
import javax.jms.*;

/**
 * Monitor class for asynchronous examples. Producer signals end of message
 * stream; listener calls allDone() to notify consumer that the signal has
 * arrived, while consumer calls waitTillDone() to wait for this notification.
 */
public class DoneLatch {
  boolean done = false;

  /**
   * Waits until done is set to true.
   */
  public void waitTillDone() {
    synchronized (this) {
      while (!done) {
        try {
          this.wait();
        } catch (InterruptedException ie) {
        }
      }
    }
  }

  /**
   * Sets done to true.
   */
  public void allDone() {
    synchronized (this) {
      done = true;
      this.notify();
    }
  }

}
