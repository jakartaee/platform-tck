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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.bodytagsupport;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;
import javax.servlet.jsp.tagext.TagData;

public class SyncTEI extends TagExtraInfo {

  /**
   * Default constructor.
   */
  public SyncTEI() {
    super();
  }

  /**
   * Sets the begin, nested, and end scripting variables for the
   * BodySynchronizationTag.
   * 
   * @param data
   *          - the TagData from the TLD.
   * @return a VariableInfo array.
   */
  public VariableInfo[] getVariableInfo(TagData data) {
    JspTestUtil.debug("[SyncTEI] in getVariableInfo()");
    return new VariableInfo[] {
        new VariableInfo("begin", "java.lang.Integer", true,
            VariableInfo.AT_BEGIN),
        new VariableInfo("nested", "java.lang.Integer", true,
            VariableInfo.NESTED),
        new VariableInfo("end", "java.lang.Integer", true,
            VariableInfo.AT_END) };
  }
}
