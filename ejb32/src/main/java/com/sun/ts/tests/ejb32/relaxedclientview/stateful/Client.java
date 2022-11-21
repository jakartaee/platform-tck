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

package com.sun.ts.tests.ejb32.relaxedclientview.stateful;

import com.sun.ts.tests.ejb32.relaxedclientview.common.ClientBase;

public class Client extends ClientBase {

  /*
   * @testName: noAnnotationTest
   * 
   * @test_Strategy: 2 interfaces without any annotations
   */

  /*
   * @testName: localAnnotationTest
   * 
   * @test_Strategy: 2 interfaces with local annotation on ejb
   */

  /*
   * @testName: remoteAnnotationTest
   * 
   * @test_Strategy: 2 interfaces with remote annotation on ejb
   */

  /*
   * @testName: oneRemoteAnnotationOnInterfaceTest
   * 
   * @test_Strategy: a remote annotation on one interface and another interface
   * has nothing
   */

  /*
   * @testName: oneRemoteAnnotationOnEjbTest
   * 
   * @test_Strategy: a remote annotation on ejb for one interface and another
   * interface has nothing
   */

  /*
   * @testName: noInterfaceViewTest
   * 
   * @test_Strategy: an ejb has no interface view, so all business interfaces
   * must be explicitly designated
   */

  /*
   * @testName: localDDTest
   * 
   * @test_Strategy: an ejb has dd local view
   */

}
