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

package com.sun.ts.tests.jaspic.tssv.util;

import java.io.File;
import java.util.logging.XMLFormatter;
import java.util.logging.Handler;

/**
 *
 * @author Raja Perumal
 */
public class TSXMLFormatter extends XMLFormatter {

  /** Creates a new instance of TSXMLFormatter */
  public TSXMLFormatter() {
    super();
  }

  /**
   * Override getHead method of XMLFormatter. This is done to avoid specifying
   * external entity such as "logger.dtd" in the generated log file.
   *
   * Occurance of "<!DOCTYPE log SYSTEM "logger.dtd" > causes runtime
   * performance hit due to dtd validation at runtime.
   *
   * @param h
   *          The target handler.
   * @return header string
   */
  public String getHead(Handler h) {
    String logFileLocation = System.getProperty("log.file.location");

    // Check if the log file already exists
    File file = new File(logFileLocation + "/" + JASPICData.DEFAULT_LOG_FILE);
    if (file.exists()) {
      // if the log file exists and the file length is more than 1 byte
      // then don't return any header otherwise return header info.
      if (file.length() > 1) {
        // Don't write any header info
        return "";
      }
    }

    StringBuffer sb = new StringBuffer();
    sb.append("<?xml version=\"1.0\"");
    String encoding = "UTF-8";

    sb.append(" encoding=\"");
    sb.append(encoding);
    sb.append("\"");
    sb.append(" standalone=\"no\"?>\n");
    // sb.append("<!DOCTYPE log SYSTEM \"logger.dtd\">\n");
    sb.append("<log>\n");
    return sb.toString();
  }

  /**
   * Return the tail string for a set of XML formatted records.
   *
   * This method will be called only during the end of logger lifecycle.
   * LogProcessor already has the built in logic to handle non well-formed XML
   * (i.e missing </log> element)
   *
   * @param h
   *          The target handler (can be null)
   * @return a valid XML string
   */
  public String getTail(Handler h) {
    // return "</log>\n";
    return "";
  }

}
