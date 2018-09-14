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

/**
 * $Id$
 *
 * @author Raja Perumal
 *         07/12/02
 */

package com.sun.ts.tests.jacc.provider;

import java.util.logging.*;

/**
 * TSLogRecord is the custom LogRecord which has one additional logging field
 * ContextId, in addition to the regular Logging fields. The Log fields of
 * TSLogRecord are 1) sequence number 2) context Id (The logging context) 3)
 * message 4) class name (The class which logs the log message) 5) method name (
 * The method which logs the log message)
 **/
public class TSLogRecord extends LogRecord {

  /**
   * @serial The logging context Id
   */
  private String contextId;

  /**
   * Construct a LogRecord with the given level, message and context values.
   * 
   * @param level
   *          a logging level value
   * @param contextId
   *          the logging contextId
   * @param msg
   *          the raw non-localized logging message
   * 
   */
  TSLogRecord(Level level, String message, String contextId) {
    // set the rest of the fields using parent constructor
    super(level, message);
    this.contextId = contextId;

  }

  /**
   * Construct a LogRecord with the given level and message
   *
   * @param level
   *          a logging level value
   * @param msg
   *          the raw non-localized logging message
   *
   */
  TSLogRecord(Level level, String message) {
    super(level, message);
    // Add jacc_ctx for default contextId
    this.contextId = "jacc_ctx";
  }

  /**
   * Get the contextId
   *
   * @ return contextId
   */
  public String getContextId() {
    return contextId;
  }

  /**
   * Set the contextId
   * 
   * @param contextId
   *          the logging context Id
   */
  public void setContextId(String cId) {
    contextId = cId;
  }

}
