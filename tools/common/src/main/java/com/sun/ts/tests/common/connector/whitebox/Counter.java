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

package com.sun.ts.tests.common.connector.whitebox;

/*
 * This class is used to assist with testing of work context notifications
 * and the calling order.   There is a static counter in this class that will
 * get incremented everytime someone creates an instance of this class.
 * The  idea is to print the call order out during notifications of work 
 * context releated calls of:  workAccepted(), workStarted(), workCompleted(),
 * and contextSetupComplete(). 
 * 
 * This class should NOT be used by other tests as it could throw the 
 * count off.  So unless you are sure you know what you're doing, refrain 
 * from using this class.
 *
 * @see  com.sun.ts.tests.com.sun.common.connector.whitebox.WorkListenerImpl2
 * @see  com.sun.ts.tests.com.sun.common.connector.whitebox.TSSICWithListener
 *
 */
public class Counter {

  public enum Action {
    INCREMENT, DECREMENT, DO_NOTHING
  };

  private static int count = 0;

  private static Action action = Action.DO_NOTHING;

  public Counter() {
  }

  /*
   * We are forcing the users to explicitly indicate if they want to increment,
   * decrement, or do nothing. The reasoning is to ensure that the user knows
   * exactly what they are doing when using this class/method.
   *
   * Since the primary use of this class/method is to record the current count
   * (usually during a work context notification call) we expect the users to
   * call this as: getCount(Counter.INCREMENT) so that the count is incremented
   * each time its called/used. The other Action types are only there for
   * completeness but no current use of them is expected.
   *
   */
  public int getCount(Action val) {

    Counter.action = val;

    if (action == Action.INCREMENT) {
      count++;
    } else if (action == Action.DECREMENT) {
      // this is allowed - but not likely so offer caution
      debug(
          "CAUTION:  user invoked Counter(Action.DECREMENT) - verify this is correct.");
      count--;
    } else if (action == Action.DO_NOTHING) {
      // this is allowed - but not likely so offer caution
      debug(
          "CAUTION:  user invoked Counter(Action.NOTHING) - verify this is correct.");
    }

    return count;
  }

  public void setCount(int val) {
    count = val;
  }

  public int increment() {
    return count++;
  }

  public int decrement() {
    return count--;
  }

  public static void resetCount() {
    count = 0;
    action = Action.DO_NOTHING;
  }

  private void debug(String str) {
    Debug.trace(str);
  }

}
