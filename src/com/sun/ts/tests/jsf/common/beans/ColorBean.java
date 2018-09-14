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

package com.sun.ts.tests.jsf.common.beans;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "color")
@ApplicationScoped
public class ColorBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private String color;

  public void editColor(ActionEvent event) {
    String buttonId = event.getComponent().getId();

    if (buttonId.contains("Red")) {
      this.setColor("Red");
    } else {
      this.setColor("Blue");
    }
  }

  public String result() {
    return color;
  }

  public String getColor() {
    return color;
  }

  private void setColor(String color) {
    this.color = color;
  }

}
