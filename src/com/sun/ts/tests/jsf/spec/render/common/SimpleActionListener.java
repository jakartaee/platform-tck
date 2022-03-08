/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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
import java.io.Serializable;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.ActionListener;
import jakarta.servlet.http.HttpServletResponse;

@jakarta.inject.Named("ActionListener") @jakarta.enterprise.context.SessionScoped
public class SimpleActionListener implements ActionListener, Serializable {

  private static final long serialVersionUID = -2123380871083456327L;

  /**
   * <p>
   * Invoked when the action described by the specified
   * {@link jakarta.faces.event.ActionEvent} occurs.
   * </p>
   *
   * @param event
   *          The {@link jakarta.faces.event.ActionEvent} that has occurred
   *
   * @throws jakarta.faces.event.AbortProcessingException
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
