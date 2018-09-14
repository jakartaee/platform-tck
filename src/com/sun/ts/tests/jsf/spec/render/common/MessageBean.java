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

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class MessageBean {
  private static String INFO_SUMMARY = "INFO: Summary Message";

  private static String INFO_DETAIL = "INFO: Detailed Message";

  private static String WARN_SUMMARY = "WARN: Summary Message";

  private static String WARN_DETAIL = "WARN: Detailed Message";

  private static String ERROR_SUMMARY = "ERROR: Summary Message";

  private static String ERROR_DETAIL = "ERROR: Detailed Message";

  private static String FATAL_SUMMARY = "FATAL: Summary Message";

  private static String FATAL_DETAIL = "FATAL: Detailed Message";

  private FacesMessage message;

  private FacesMessage inputOneMessage;

  private FacesMessage inputTwoMessage;

  private String severity;

  private String id;

  /** Creates a new instance of MessageBean */
  public MessageBean() {
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setSeverity(String severity) {

    FacesContext context = FacesContext.getCurrentInstance();

    if ("INFO".equals(severity)) {
      message = new FacesMessage(FacesMessage.SEVERITY_INFO, INFO_SUMMARY,
          INFO_DETAIL);

      context.addMessage(id, message);

    } else if ("WARN".equals(severity)) {
      message = new FacesMessage(FacesMessage.SEVERITY_WARN, WARN_SUMMARY,
          WARN_DETAIL);

      context.addMessage(id, message);

    } else if ("ERROR".equals(severity)) {
      message = new FacesMessage(FacesMessage.SEVERITY_ERROR, ERROR_SUMMARY,
          ERROR_DETAIL);

      context.addMessage(id, message);

    } else if ("FATAL".equals(severity)) {
      message = new FacesMessage(FacesMessage.SEVERITY_FATAL, FATAL_SUMMARY,
          FATAL_DETAIL);

      context.addMessage(id, message);

    } else if ("MESSAGES_INFO".equals(severity)) {
      inputOneMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,
          INFO_SUMMARY + "_One ", INFO_DETAIL + "_One ");

      inputTwoMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,
          INFO_SUMMARY + "_Two ", INFO_DETAIL + "_Two ");

      context.addMessage(null, inputOneMessage);
      context.addMessage(null, inputTwoMessage);

    } else if ("MESSAGES_WARN".equals(severity)) {
      inputOneMessage = new FacesMessage(FacesMessage.SEVERITY_WARN,
          WARN_SUMMARY + "_One ", WARN_DETAIL + "_One ");

      inputTwoMessage = new FacesMessage(FacesMessage.SEVERITY_WARN,
          WARN_SUMMARY + "_Two ", WARN_DETAIL + "_Two ");

      context.addMessage(null, inputOneMessage);
      context.addMessage(null, inputTwoMessage);

    } else if ("MESSAGES_ERROR".equals(severity)) {
      inputOneMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
          ERROR_SUMMARY + "_One ", ERROR_DETAIL + "_One ");

      inputTwoMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
          ERROR_SUMMARY + "_Two ", ERROR_DETAIL + "_Two ");

      context.addMessage(null, inputOneMessage);
      context.addMessage(null, inputTwoMessage);

    } else if ("MESSAGES_FATAL".equals(severity)) {
      inputOneMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL,
          FATAL_SUMMARY + "_One ", FATAL_DETAIL + "_One ");

      inputTwoMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL,
          FATAL_SUMMARY + "_Two ", FATAL_DETAIL + "_Two ");

      context.addMessage(null, inputOneMessage);
      context.addMessage(null, inputTwoMessage);
    }

    this.severity = severity;
  }

  public String getSeverity() {
    return severity;
  }

  public String getId() {
    return id;
  }

}
