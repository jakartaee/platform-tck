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

package com.sun.ts.tests.webservices.deploy.CompScopedRefs;

import java.rmi.RemoteException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.sun.ts.lib.util.*;

import javax.naming.InitialContext;
import javax.xml.rpc.Service;

public class HelloClientBean implements SessionBean {

  public void ejbCreate() {
  }

  public void ejbActivate() {
  }

  public void ejbRemove() {
  }

  public void ejbPassivate() {
  }

  public void setSessionContext(SessionContext sc) {
  }

  public String callHello() {
    String result = null;
    try {
      InitialContext ctx = new InitialContext();
      TestUtil.logMsg("JNDI lookup for shared_service_ref Service");
      Service svc = (javax.xml.rpc.Service) ctx
          .lookup("java:comp/env/service/shared_service_ref");
      TestUtil.logMsg("Service found is " + svc);
      Hello port = (Hello) svc.getPort(Hello.class);
      TestUtil.logMsg("Port obtained");
      result = port.hello();
    } catch (Exception e) {
      System.out.println("*** Exception in HelloClientBean");
      e.printStackTrace();
    }
    return result;
  }
}
