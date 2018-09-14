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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sun.ts.tests.jsf.spec.render.grid;

import javax.faces.application.Application;
import javax.faces.component.html.HtmlColumn;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;

public class GridUIBean {

  private HtmlPanelGrid gps;

  public GridUIBean() {

  }

  public HtmlPanelGrid getGps() {
    return gps;
  }

  public void setGps(HtmlPanelGrid hpg) {
    this.gps = hpg;
    gps.setId("coordinates");
    gps.setTitle("coordinates");
    gps.setColumns(2);
    gps.setColumnClasses("odd,even");

    gps.getChildren().add(genColumn("North-Long", 3));
    gps.getChildren().add(genColumn("East-Lat", 7));
  }

  private HtmlColumn genColumn(String id, int coor) {
    Application application = FacesContext.getCurrentInstance()
        .getApplication();
    HtmlColumn column = (HtmlColumn) application
        .createComponent(HtmlColumn.COMPONENT_TYPE);
    HtmlOutputText header = (HtmlOutputText) application
        .createComponent(HtmlOutputText.COMPONENT_TYPE);

    column.setId(id);
    header.setValue(coor);
    column.getChildren().add(header);

    return column;
  }
}
