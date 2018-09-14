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
 * @(#)DescriptionClient.java	1.2 03/05/16
 */

package com.sun.ts.tests.jaxrpc.sharedclients;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.ts.lib.harness.EETest;

public class DescriptionClient {

  private String url;

  public String getURL() {
    return url;
  }

  public void setURL(String url) {
    this.url = url;
  }

  public Document getDocument() throws EETest.Fault {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      InputSource is = new InputSource(new URL(url).openStream());
      return builder.parse(is);
    } catch (FactoryConfigurationError e) {
      throw new EETest.Fault(
          "XML document builder factory configuration problem", e);
    } catch (ParserConfigurationException e) {
      throw new EETest.Fault("XML parser configuration problem", e);
    } catch (IOException e) {
      throw new EETest.Fault("Unable to read description from '" + url + "'",
          e);
    } catch (SAXException e) {
      throw new EETest.Fault("Unable to parse description from '" + url + "'",
          e);
    }
  }
}
