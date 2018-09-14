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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.jstl.common.filters;

import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class SimpleXmlFilter extends XMLFilterImpl {

  /** Creates new SimpleXmlFilter */
  public SimpleXmlFilter() {
  }

  /*
   * public methods
   * ========================================================================
   */

  /**
   * When called, an new attribute, 'test', will be added to each element
   * processed.
   */
  public void startElement(String namespaceURI, String localName,
      String qualifiedName, Attributes atts) throws SAXException {
    AttributesImpl attributes = new AttributesImpl(atts);
    attributes.addAttribute("", "test", "test", "CDATA", "attrvalue");
    atts = attributes;
    super.startElement(namespaceURI, localName, qualifiedName, atts);
  }
}
