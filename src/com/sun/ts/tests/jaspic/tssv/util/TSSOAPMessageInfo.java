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

package com.sun.ts.tests.jaspic.tssv.util;

import javax.security.auth.message.MessageInfo;
import java.util.Map;
import java.lang.Object;

/**
 *
 * @author Raja Perumal
 */
public class TSSOAPMessageInfo implements MessageInfo {
  private Map map = null;

  private Object request = null;

  private Object response = null;

  /** Creates a new instance of TSMessageInfo */
  public TSSOAPMessageInfo() {

  }

  public TSSOAPMessageInfo(Map localMap) {
    map = localMap;
  }

  public Map getMap() {
    return map;
  }

  public void setResponseMessage(Object res) {
    response = res;
  }

  public void setRequestMessage(Object req) {
    request = req;
  }

  public Object getRequestMessage() {
    return request;
  }

  public Object getResponseMessage() {
    return response;
  }
}
