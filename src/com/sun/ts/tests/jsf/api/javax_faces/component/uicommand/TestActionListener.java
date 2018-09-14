/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.javax_faces.component.uicommand;

import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

public final class TestActionListener implements ActionListener {
  private String id = null;

  private TestActionListener() {
  }

  // ----------------------------------------------------------- Public Methods

  /**
   * Create a TestActionListener with with the given ID(id)
   * 
   * @param id
   *          - The id of the TestActionListener.
   * @return TestActionListener
   */
  public static TestActionListener withID(String id) {
    TestActionListener tal = new TestActionListener();
    tal.setId(id);

    return tal;

  }

  /**
   * Set the id of this TestActionListener
   * 
   * @param id
   *          - The id you want this TestActionListener Object to have.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Get the id of this TestActionListener
   * 
   * @return The id that has been previously set.
   */
  public String getId() {
    return (this.id);
  }

  public void processAction(ActionEvent event) {
    trace(getId() + "@" + JSFTestUtil.getPhaseIdAsString(event.getPhaseId()));
  }

  public boolean equals(Object otherObj) {
    if (!(otherObj instanceof TestActionListener)) {
      return false;
    }
    TestActionListener other = (TestActionListener) otherObj;
    if ((null != id && null == other.id) || (null == id && null != other.id)) {
      return false;
    }
    boolean idsAreEqual = true;
    if (null != id) {
      idsAreEqual = id.equals(other.id);
    }

    return idsAreEqual;
  }

  public int hashCode() {
    assert false : "hashCode not designed";
    return 42; // any arbitrary constant will do
  }

  // ---------------------------------------------------- Static Trace Methods

  // Accumulated trace log
  private static StringBuffer trace = new StringBuffer();

  // Append to the current trace log (or clear if null)
  public static void trace(String text) {
    if (text == null) {
      trace.setLength(0);
    } else {
      trace.append('/');
      trace.append(text);
    }
  }

  // Retrieve the current trace log
  public static String trace() {
    return (trace.toString());
  }

}
