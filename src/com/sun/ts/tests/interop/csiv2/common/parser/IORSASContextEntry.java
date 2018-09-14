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

package com.sun.ts.tests.interop.csiv2.common.parser;

import java.io.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import java.util.*;

public class IORSASContextEntry extends Entry {
  public IORSASContextEntry(Element element) throws ParseException {
    if (!element.getTagName().equals("ior-sas-context")) {
      throw new ParseException("Unexpected tag: " + element.getTagName());
    }
    NodeList nodes = element.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeName().equals("target-supports")) {
        targetSupports = Integer.parseInt(getText(node));
      } else if (node.getNodeName().equals("target-requires")) {
        targetRequires = Integer.parseInt(getText(node));
      } else if (node.getNodeName().equals("privilege-authority")) {
        privilegeAuthorities
            .addElement(new PrivilegeAuthorityEntry((Element) node));
      } else if (node.getNodeName().equals("supported-naming-mechanism")) {
        supportedNamingMechanisms.addElement(parseBinHex(getText(node)));
      } else if (node.getNodeName().equals("supported-identity-types")) {
        supportedIdentityTypes = Integer.parseInt(getText(node));
      }
    }
  }

  public int getTargetSupports() {
    return targetSupports;
  }

  public int getTargetRequires() {
    return targetRequires;
  }

  public Vector getSupportedNamingMechanisms() {
    return supportedNamingMechanisms;
  }

  public int getSupportedIdentityTypes() {
    return supportedIdentityTypes;
  }

  public String toString() {
    String result = "<ior-sas-context>\n";
    result += "<target-supports>" + targetSupports + "</target-supports>\n";
    result += "<target-requires>" + targetRequires + "</target-requires>\n";
    for (int i = 0; i < privilegeAuthorities.size(); i++) {
      PrivilegeAuthorityEntry authority = (PrivilegeAuthorityEntry) privilegeAuthorities
          .elementAt(i);
      result += authority.toString();
    }
    for (int i = 0; i < supportedNamingMechanisms.size(); i++) {
      byte[] mechanism = (byte[]) supportedNamingMechanisms.elementAt(i);
      result += "<supported-naming-mechanism>" + binHex(mechanism)
          + "</supported-naming-mechanism>\n";
    }
    result += "<supported-identity-types>" + supportedIdentityTypes
        + "</supported-identity-types>\n";
    result += "</ior-sas-context>\n";
    return result;
  }

  public Vector getPrivilegeAuthorities() {
    return privilegeAuthorities;
  }

  private int targetSupports;

  private int targetRequires;

  private int supportedIdentityTypes;

  /**
   * @link aggregation
   * @associates <{PrivilegeAuthorityEntry}>
   * @supplierCardinality 0..*
   */
  private Vector privilegeAuthorities = new Vector();

  /** Elements of this Vector are byte[] */
  private Vector supportedNamingMechanisms = new Vector();
}
