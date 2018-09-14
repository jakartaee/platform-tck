/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.spec.render.common;

public class EscapeBean {
  private String foo;

  private String bar;

  public EscapeBean() {
    // Initial settings done on creation of object.
    this.foo = "&foo";
    this.bar = "&bar";
  }

  /**
   * @param bar
   *          the bar to set
   */
  public void setFoo(String foo) {
    this.foo = foo;
  }

  /**
   * @return the bar
   */
  public String getFoo() {
    return foo;
  }

  /**
   * @param bar
   *          the bar to set
   */
  public void setBar(String bar) {
    this.bar = bar;
  }

  /**
   * @return the bar
   */
  public String getBar() {
    return bar;
  }
}
