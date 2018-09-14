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

package com.sun.ts.tests.jaxrpc.sharedwebservices.faultservice;

public class DummyException extends Exception {

  /**
   * These dummy fields exist to testMessage Java -> WSDL mapping for
   * Exceptions. JAXRPC spec says that fields map to extra elements in the
   * soap:Fault. Basic Profile R1000 says extra fields aren't allowed in
   * soap:Faults
   */
  private String dummyField1;

  private String dummyField2;

  public DummyException(String dummyField1, String dummyField2) {
    super();
    this.dummyField1 = dummyField1;
    this.dummyField2 = dummyField2;
  }

  public String getDummyField1() {
    return dummyField1;
  }

  public void setDummyField1(String dummyField1) {
    this.dummyField1 = dummyField1;
  }

  public String getDummyField2() {
    return dummyField2;
  }

  public void setDummyField2(String dummyField2) {
    this.dummyField2 = dummyField2;
  }
}
