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

package com.sun.ts.tests.webservices.wsdlImport.http.Simple3;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import com.sun.ts.tests.jaxrpc.common.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.math.BigInteger;
import java.math.BigDecimal;

import javax.xml.namespace.QName;

// Service Implementation Class - as outlined in JAX-RPC Specification

public class TestsImpl implements Tests {
  public String invokeTest1(Boolean p1, Byte p2, Short p3, Integer p4, Long p5,
      Float p6, Double p7) throws RemoteException {
    boolean pass = true;

    if (!p1.equals(JAXRPC_Data.Boolean_data[0])) {
      TestUtil.logErr("Boolean failed, expected " + JAXRPC_Data.Boolean_data[0]
          + ", received " + p1);
      pass = false;
    }

    if (!p2.equals(JAXRPC_Data.Byte_data[0])) {
      TestUtil.logErr("Byte failed, expected " + JAXRPC_Data.Byte_data[0]
          + ", received " + p2);
      pass = false;
    }

    if (!p3.equals(JAXRPC_Data.Short_data[0])) {
      TestUtil.logErr("Short failed, expected " + JAXRPC_Data.Short_data[0]
          + ", received " + p3);
      pass = false;
    }

    if (!p4.equals(JAXRPC_Data.Integer_data[0])) {
      TestUtil.logErr("Integer failed, expected " + JAXRPC_Data.Integer_data[0]
          + ", received " + p4);
      pass = false;
    }

    if (!p5.equals(JAXRPC_Data.Long_data[0])) {
      TestUtil.logErr("Long failed, expected " + JAXRPC_Data.Long_data[0]
          + ", received " + p5);
      pass = false;
    }

    if (!p6.equals(JAXRPC_Data.Float_data[0])) {
      TestUtil.logErr("Float failed, expected " + JAXRPC_Data.Float_data[0]
          + ", received " + p6);
      pass = false;
    }

    if (!p7.equals(JAXRPC_Data.Double_data[0])) {
      TestUtil.logErr("Double failed, expected " + JAXRPC_Data.Double_data[0]
          + ", received " + p7);
      pass = false;
    }

    return ((pass) ? "passed" : "failed");
  }

  public String invokeTest2(BigInteger p1, BigDecimal p2, QName p3, String p4)
      throws RemoteException {
    boolean pass = true;

    if (!p1.equals(JAXRPC_Data.BigInteger_data[0])) {
      TestUtil.logErr("BigInteger failed, expected "
          + JAXRPC_Data.BigInteger_data[0] + ", received " + p1);
      pass = false;
    }

    if (!p2.equals(JAXRPC_Data.BigDecimal_data[0])) {
      TestUtil.logErr("BigDecimal failed, expected "
          + JAXRPC_Data.BigDecimal_data[0] + ", received " + p2);
      pass = false;
    }

    if (!p3.equals(JAXRPC_Data.QName_data[0])) {
      TestUtil.logErr("QName failed, expected " + JAXRPC_Data.QName_data[0]
          + ", received " + p3);
      pass = false;
    }

    if (!p4.equals(JAXRPC_Data.String_data[0])) {
      TestUtil.logErr("String failed, expected " + JAXRPC_Data.String_data[0]
          + ", received " + p4);
      pass = false;
    }

    return ((pass) ? "passed" : "failed");
  }

  public String invokeTest3() throws RemoteException {
    return "Hello";
  }

  public void invokeOneWayTest1(Boolean p1, Byte p2, Short p3, Integer p4,
      Long p5, Float p6, Double p7) throws RemoteException {
    return;
  }

  public void invokeOneWayTest2(BigInteger p1, BigDecimal p2, QName p3,
      String p4) throws RemoteException {
    return;
  }

  public void invokeOneWayTest3() throws RemoteException {
  }
}
