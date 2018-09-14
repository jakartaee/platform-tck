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

package com.sun.ts.tests.jsf.api.javax_faces.component.common;

import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;

public class TCKValueChangeListener implements ValueChangeListener {

  private String id = null;

  public TCKValueChangeListener() {
  }

  public TCKValueChangeListener(String id) {
    this.id = id;
  }

  // ----------------------------------------------------------- Pubic Methods

  public String getId() {
    return (this.id);
  }

  public void processValueChange(ValueChangeEvent event) {
    trace(getId() + '@' + JSFTestUtil.getPhaseIdAsString(event.getPhaseId()));
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

  // this needs to be named differently because other test methods
  // rely on the standard equal method.
  public boolean isEqual(Object otherObj) {
    if (!(otherObj instanceof TCKValueChangeListener)) {
      return false;
    }
    TCKValueChangeListener other = (TCKValueChangeListener) otherObj;
    if ((null != id && null == other.id) || (null == id && null != other.id)) {
      return false;
    }
    boolean idsAreEqual = true;
    if (null != id) {
      idsAreEqual = id.equals(other.id);
    }
    return idsAreEqual;
  }

}
