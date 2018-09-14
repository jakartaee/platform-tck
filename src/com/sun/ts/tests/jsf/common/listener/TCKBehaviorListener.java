/*
 * Copyright (c) 2011, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.common.listener;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;

import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public final class TCKBehaviorListener implements AjaxBehaviorListener {
  private String id = null;

  private TCKBehaviorListener() {
  }

  // -------------------------------------------------- Public Methods

  /**
   * Create a BehaviorListener with with the given ID(id)
   * 
   * @param id
   *          - The id of the TestActionListener.
   * @return TestActionListener
   */
  public static TCKBehaviorListener withID(String id) {
    TCKBehaviorListener listener = new TCKBehaviorListener();
    listener.setId(id);

    return listener;

  }

  /**
   * Set the id of this TCKBehaviorListener
   * 
   * @param id
   *          - The id you want this TestActionListener Object to have.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Get the id of this AjaxBehaviorListener
   * 
   * @return The id that has been previously set.
   */
  public String getId() {
    return (this.id);
  }

  public void processAjaxBehavior(AjaxBehaviorEvent event)
      throws AbortProcessingException {
    trace(getId() + "@" + JSFTestUtil.getPhaseIdAsString(event.getPhaseId()));
  }

  public boolean equals(Object otherObj) {
    if (!(otherObj instanceof TCKBehaviorListener)) {
      return false;
    }
    TCKBehaviorListener other = (TCKBehaviorListener) otherObj;
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
