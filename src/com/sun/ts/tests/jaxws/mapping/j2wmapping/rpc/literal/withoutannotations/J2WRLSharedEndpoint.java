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

package com.sun.ts.tests.jaxws.mapping.j2wmapping.rpc.literal.withoutannotations;

@javax.jws.WebService
@javax.jws.soap.SOAPBinding(style = javax.jws.soap.SOAPBinding.Style.RPC)
public interface J2WRLSharedEndpoint {

  public java.lang.String arrayOperationFromClient(java.lang.String[] arg0);

  public com.sun.ts.tests.jaxws.mapping.j2wmapping.rpc.literal.withoutannotations.J2WRLSharedBean getBean();

  public java.lang.String[] arrayOperation();

  public java.lang.String stringOperation(java.lang.String arg0);

  public java.lang.String helloWorld();

  public void oneWayOperation();
}
