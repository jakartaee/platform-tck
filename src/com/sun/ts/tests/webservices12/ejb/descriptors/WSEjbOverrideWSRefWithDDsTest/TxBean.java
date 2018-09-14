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

package com.sun.ts.tests.webservices12.ejb.descriptors.WSEjbOverrideWSRefWithDDsTest;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.jws.WebService;

@WebService(portName = "JunkJunkJunkPortName", serviceName = "JunkJunkJunkServiceName", targetNamespace = "http://Tx.org", wsdlLocation = "META-INF/wsdl/TxService.wsdl", endpointInterface = "com.sun.ts.tests.webservices12.ejb.descriptors.WSEjbOverrideWSRefWithDDsTest.Tx")
@Stateless(name = "WsFrontEjb")
public class TxBean implements SessionBean {

  private ChokeRemote choke;

  private ChokeHome home;

  private Object o;

  public void ejbCreate() {
    try {
      javax.naming.InitialContext ctx = new javax.naming.InitialContext();
      o = ctx.lookup("java:comp/env/ejb/wschokebean");
      home = (ChokeHome) javax.rmi.PortableRemoteObject.narrow(o,
          ChokeHome.class);
      choke = home.create();
    } catch (Exception e) {
      System.out.println("*** TxBean.ejbCreate: failed to find choke");
      e.printStackTrace();
    }
  }

  public void ejbActivate() {
    try {
      javax.naming.InitialContext ctx = new javax.naming.InitialContext();
      o = ctx.lookup("java:comp/env/ejb/wschokebean");
      home = (ChokeHome) javax.rmi.PortableRemoteObject.narrow(o,
          ChokeHome.class);
      choke = home.create();
    } catch (Exception e) {
      System.out.println("*** TxBean.ejbActivate: failed to find choke");
      e.printStackTrace();
    }
  }

  public void ejbRemove() {
  }

  public void ejbPassivate() {
  }

  public void setSessionContext(SessionContext sc) {
  }

  public String txRequired(String s) {
    try {
      choke.chokeMandatory();
      return s;
    } catch (Exception e) {
      throw new RuntimeException(
          "TxBean.txRequired choked on " + e.getMessage());
    }
  }

  public String txRequiresNew(String s) {
    try {
      choke.chokeMandatory();
      return s;
    } catch (Exception e) {
      throw new RuntimeException(
          "TxBean.txRequiresNew choked on " + e.getMessage());
    }
  }

  public String txSupports(String s) {
    try {
      choke.chokeNever();
      return s;
    } catch (Exception e) {
      throw new RuntimeException(
          "TxBean.txSupports choked on " + e.getMessage());
    }
  }

  public String txNotSupported(String s) {
    try {
      choke.chokeNever();
      return s;
    } catch (Exception e) {
      throw new RuntimeException(
          "TxBean.txNotSupported choked on " + e.getMessage());
    }
  }

  public String txNever(String s) {
    try {
      choke.chokeNever();
      return s;
    } catch (Exception e) {
      throw new RuntimeException("TxBean.txNever choked on " + e.getMessage());
    }
  }
}
