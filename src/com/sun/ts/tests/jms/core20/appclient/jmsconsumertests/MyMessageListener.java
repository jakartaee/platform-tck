/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jms.core20.appclient.jmsconsumertests;

import com.sun.ts.lib.util.TestUtil;
import javax.jms.Message;
import javax.jms.MessageListener;

public class MyMessageListener implements MessageListener {

  private String name = null;

  private Message message = null;

  boolean complete = false;

  public MyMessageListener() {
    this("MyMessageListener");
  }

  public MyMessageListener(String name) {
    this.name = name;
  }

  // getters/setters
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Message getMessage() {
    return message;
  }

  public void setMessage(Message message) {
    this.message = message;
  }

  public boolean isComplete() {
    return complete;
  }

  public void setComplete(boolean complete) {
    this.complete = complete;
  }

  public void onMessage(Message message) {
    TestUtil.logMsg("Got Message: " + message);
    this.message = message;
    complete = true;
  }

}
