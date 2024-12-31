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
package com.sun.ts.tests.ejb30.lite.async.common.descriptor;

public interface DescriptorIF {
  public static final String EXCEPTION_MESSAGE = "From test app as part of the test logic.";

  /**
   * async method in all client views
   */
  void allViews();

  /**
   * async method only in local views
   */
  void localViews();

  /**
   * async method only in remote views
   */
  void remoteViews();

  /**
   * async method for all params types
   */
  void allParams();

  /**
   * async method for all params types
   */
  void allParams(int i);

  /**
   * async method for all params types
   */
  void allParams(String s);

  /**
   * async method for no-param method with the method name
   */
  void noParams();

  /**
   * not an async method
   */
  void noParams(int i);

  /**
   * async method for int-param method with the method name
   */
  void intParams(int i, int j);

  /**
   * not an async method
   */
  void intParams(int i, int j, int k);

  /**
   * async method only for int-param method and local views (including
   * no-interface view)
   */
  void intParamsLocalViews(int i, int j);

  /**
   * not an async method
   */
  void intParamsLocalViews();

}
