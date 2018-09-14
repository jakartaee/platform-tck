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

package com.sun.ts.tests.jsf.spec.render.common;

import java.util.Map;

import javax.faces.event.ActionListener;
import javax.faces.event.ActionEvent;
import javax.faces.event.AbortProcessingException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletResponse;

public class SimpleActionListener implements ActionListener {

  /**
   * <p>
   * Invoked when the action described by the specified
   * {@link javax.faces.event.ActionEvent} occurs.
   * </p>
   *
   * @param event
   *          The {@link javax.faces.event.ActionEvent} that has occurred
   *
   * @throws javax.faces.event.AbortProcessingException
   *           Signal the JavaServer Faces implementation that no further
   *           processing on the current event should be performed
   */
  public void processAction(ActionEvent event) throws AbortProcessingException {

    UIComponent component = event.getComponent();
    ExternalContext extContext = FacesContext.getCurrentInstance()
        .getExternalContext();
    HttpServletResponse response = (HttpServletResponse) extContext
        .getResponse();
    Map<String, String> requestParamMap = extContext.getRequestParameterMap();
    String expectedId = requestParamMap.get("expectedId");

    if (expectedId == null) {
      response.addHeader("actionEvent",
          "Test error.  Can't find expected" + " component ID.");
    } else {

      if (!expectedId.equals(component.getId())) {
        response.addHeader("actionEvent", "Expected component ID '" + expectedId
            + "', received: '" + component.getId() + '\'');
      } else {
        response.addHeader("actionEventOK", "PASSED");
      }
    }
  }
}
