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

/*
 * @(#)SyncTEI.java 1.1 11/06/02
 */

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.simpletagsupport;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;
import javax.servlet.jsp.tagext.TagData;

/**
 * Simple TEI to export AT_BEGIN and AT_END variables.
 */
public class SyncTEI extends TagExtraInfo {

  /**
   * Default constructor.
   */
  public SyncTEI() {
    super();
  }

  /**
   * Exports 'begin' with scope of AT_BEGIN, and 'end' with scope of AT_END.
   * 
   * @param data
   *          - TagData instance
   * @return an array of VariableInfos
   */
  public VariableInfo[] getVariableInfo(TagData data) {
    JspTestUtil.debug("[SyncTEI] in getVariableInfo()");
    return new VariableInfo[] {
        new VariableInfo("begin", "java.lang.Integer", true,
            VariableInfo.AT_BEGIN),
        new VariableInfo("end", "java.lang.Integer", true,
            VariableInfo.AT_END) };
  }
}
