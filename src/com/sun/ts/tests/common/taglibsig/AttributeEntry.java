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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.common.taglibsig;

public class AttributeEntry {

  public static final String NO_ATTRIBUTE_NAME = "no attribute name";

  private String name = NO_ATTRIBUTE_NAME;

  private String type = "java.lang.String";

  private String rtexpr = "false";

  private String required = "false";

  public AttributeEntry() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    if (name != null) {
      this.name = name;
    }
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    if (type != null) {
      this.type = type;
    }
  }

  public String getRtexpr() {
    return rtexpr;
  }

  public void setRtexpr(String rtexpr) {
    if (rtexpr != null) {
      this.rtexpr = rtexpr;
    }
  }

  public String getRequired() {
    return required;
  }

  public void setRequired(String required) {
    if (required != null) {
      this.required = required;
    }
  }
}
