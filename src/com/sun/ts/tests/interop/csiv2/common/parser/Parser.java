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
 * @(#)Parser.java	1.5 03/05/16
 */

package com.sun.ts.tests.interop.csiv2.common.parser;

import java.io.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.w3c.dom.*;

public class Parser {

  public static void main(String[] args) {
    String usage = "java Parser <filename>";
    if (args.length != 1) {
      System.err.println(usage);
      System.exit(1);
    }
    String filename = args[0];
    Parser p = new Parser();
    String data = "";
    try {
      BufferedReader in = new BufferedReader(new FileReader(filename));
      String line = "";
      while ((line = in.readLine()) != null) {
        data += line + "\n";
      }
      in.close();
      CSIv2LogEntry csiv2log = p.parse(data);
      System.out.println("---\nLog\n---\n" + csiv2log);
    } catch (IOException e) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(e);
    } catch (ParseException e) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(e);
    }
  }

  public CSIv2LogEntry parse(String data) throws ParseException {
    CSIv2LogEntry result;
    Document document;
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      document = builder.parse(new InputSource(new StringReader(data)));
      Element rootElement = document.getDocumentElement();
      result = parse(rootElement);
    } catch (SAXException e) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(e);
      Exception x = e;
      if (e.getException() != null) {
        x = e.getException();
      }
      throw new ParseException("SAXException: " + x);
    } catch (ParserConfigurationException e) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(e);
      throw new ParseException("ParserConfigurationException: " + e);
    } catch (IOException e) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(e);
      throw new ParseException("IOException: " + e);
    }
    return result;
  }

  /**
   * Parses the csiv2 log once it's been loaded.
   * 
   * @param root
   *          The root element of the log
   */
  private CSIv2LogEntry parse(Element root) throws ParseException {
    return new CSIv2LogEntry(root);
  }
}
