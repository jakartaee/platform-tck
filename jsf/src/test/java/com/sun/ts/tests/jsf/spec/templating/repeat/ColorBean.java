/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id:
 */

package com.sun.ts.tests.jsf.spec.templating.repeat;

import java.util.ArrayList;
import java.util.List;

public class ColorBean {

  private List<String> colors = new ArrayList<String>();

  public ColorBean() {
    this.initialSetup();
  }

  private void initialSetup() {
    // ArrayList
    colors.add("Red");
    colors.add("Green");
    colors.add("Blue");
    colors.add("Violet");
    colors.add("Pink");
  }

  /**
   * @return the colors
   */
  public List getColors() {
    return colors;
  }

  /**
   * @param colors
   */
  public void setColors(List<String> colors) {
    this.colors = colors;
  }
}
