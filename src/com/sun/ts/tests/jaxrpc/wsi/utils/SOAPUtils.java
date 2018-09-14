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

package com.sun.ts.tests.jaxrpc.wsi.utils;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPBody;
import javax.xml.rpc.soap.SOAPFaultException;

public class SOAPUtils implements javax.xml.soap.SOAPConstants {
  public static final String FAULT_DETAIL = "detail";

  public static final String FAULT_ACTOR = "faultactor";

  public static final String FAULT_STRING = "faultstring";

  public static final String FAULT_CODE = "faultcode";

  public static final String VERSION_MISMATCH = "VersionMismatch";

  public static final String CLIENT = "Client";

  public static final String MUST_UNDERSTAND = "MustUnderstand";

  public static final String[] FAULT_CHILDREN = { FAULT_ACTOR, FAULT_CODE,
      FAULT_DETAIL, FAULT_STRING };

  public static boolean isValidSoapFaultChildName(SOAPElement element) {
    for (int i = 0; i < FAULT_CHILDREN.length; i++) {
      if (FAULT_CHILDREN[i].equals(element.getElementName().getLocalName())) {
        return true;
      }
    }
    return false;
  }

  public static boolean isVersionMismatchFaultcode(SOAPMessage message)
      throws SOAPException {
    return isFaultcode(message, VERSION_MISMATCH);
  }

  public static boolean isMustUnderstandFaultcode(SOAPMessage message)
      throws SOAPException {
    return isFaultcode(message, MUST_UNDERSTAND);
  }

  public static boolean isClientFaultcode(SOAPMessage message)
      throws SOAPException {
    return isFaultcode(message, CLIENT);
  }

  public static boolean isMustUnderstandFaultcode(SOAPFaultException se) {
    return isFaultcode(se, MUST_UNDERSTAND);
  }

  private static boolean isFaultcode(SOAPFaultException se, String faultcode) {
    return se.getFaultCode().toString().endsWith(faultcode);
  }

  private static boolean isFaultcode(SOAPMessage message, String faultcode)
      throws SOAPException {
    SOAPBody body = message.getSOAPPart().getEnvelope().getBody();
    if (body.hasFault()) {
      return body.getFault().getFaultCode().endsWith(faultcode);
    } else {
      return false;
    }
  }

}
