/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.jaxrpc.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.xml.rpc.*;
import javax.xml.namespace.QName;
import javax.xml.rpc.soap.*;
import javax.xml.rpc.handler.*;
import javax.xml.rpc.handler.soap.*;

public class MyHandlerChainImpl extends ArrayList implements HandlerChain {
  protected List handlerInfos = new ArrayList();

  String[] roles = null;

  public MyHandlerChainImpl(List handlerInfos) {
    this.handlerInfos = handlerInfos;
    createHandlerInstances();
  }

  public MyHandlerChainImpl() {
  }

  private void createHandlerInstances() {
    for (int i = 0; i < handlerInfos.size(); i++)
      add(newHandler(getHandlerInfo(i)));
  }

  public boolean handleFault(MessageContext _context) {
    SOAPMessageContext context = (SOAPMessageContext) _context;

    for (int i = size() - 1; i >= 0; i--)
      if (getHandlerInstance(i).handleFault(context) == false)
        return false;
    return true;
  }

  public boolean handleRequest(MessageContext _context) {
    SOAPMessageContext context = (SOAPMessageContext) _context;

    boolean processFault = false;

    for (int i = 0; i < size(); i++) {
      Handler currentHandler = getHandlerInstance(i);
      try {
        if (currentHandler.handleRequest(context) == false) {
          return false;
        }
      } catch (SOAPFaultException sfe) {
        throw sfe;
      }
    }
    return true;
  }

  public boolean handleResponse(MessageContext context) {
    for (int i = size() - 1; i >= 0; i--)
      if (getHandlerInstance(i).handleResponse(context) == false)
        return false;
    return true;
  }

  boolean initialized = false;

  public void init(java.util.Map config) {
    // TODO: How to implement this?
  }

  public void destroy() {
    for (int i = 0; i < size(); i++)
      getHandlerInstance(i).destroy();
    clear();
  }

  Handler getHandlerInstance(int index) {
    return (Handler) castToHandler(get(index));
  }

  HandlerInfo getHandlerInfo(int index) {
    return (HandlerInfo) handlerInfos.get(index);
  }

  Handler newHandler(HandlerInfo handlerInfo) {
    try {
      Handler handler = (Handler) handlerInfo.getHandlerClass().newInstance();
      handler.init(handlerInfo);
      return handler;
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new JAXRPCException("Unable to instantiate handler: " + ex);
    }
  }

  public void setRoles(String[] soapActorNames) {
    this.roles = soapActorNames;
  }

  public String[] getRoles() {
    return roles;
  }

  protected Handler castToHandler(Object o) {
    if (!(o instanceof Handler)) {
      throw new JAXRPCException("handler.chain.contains.handler.only"
          + ": bad object is: " + o.toString());
    }
    return (Handler) o;
  }
}
