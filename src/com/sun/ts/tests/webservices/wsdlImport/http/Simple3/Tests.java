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

public interface Tests extends java.rmi.Remote {
  public void invokeOneWayTest1(java.lang.Boolean boolean_1,
      java.lang.Byte byte_2, java.lang.Short short_3,
      java.lang.Integer integer_4, java.lang.Long long_5,
      java.lang.Float float_6, java.lang.Double double_7)
      throws java.rmi.RemoteException;

  public void invokeOneWayTest2(java.math.BigInteger bigInteger_1,
      java.math.BigDecimal bigDecimal_2, javax.xml.namespace.QName QName_3,
      java.lang.String string_4) throws java.rmi.RemoteException;

  public void invokeOneWayTest3() throws java.rmi.RemoteException;

  public java.lang.String invokeTest1(java.lang.Boolean boolean_1,
      java.lang.Byte byte_2, java.lang.Short short_3,
      java.lang.Integer integer_4, java.lang.Long long_5,
      java.lang.Float float_6, java.lang.Double double_7)
      throws java.rmi.RemoteException;

  public java.lang.String invokeTest2(java.math.BigInteger bigInteger_1,
      java.math.BigDecimal bigDecimal_2, javax.xml.namespace.QName QName_3,
      java.lang.String string_4) throws java.rmi.RemoteException;

  public java.lang.String invokeTest3() throws java.rmi.RemoteException;
}
