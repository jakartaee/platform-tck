/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)ConformanceClaimHandler.java	1.1	03/04/15
 */

package com.sun.ts.tests.jaxws.sharedwebservices.simpleservice;

import jakarta.xml.ws.handler.*;
import jakarta.xml.ws.handler.soap.*;
import javax.xml.namespace.QName;
import jakarta.xml.soap.*;
import java.util.*;
import com.sun.ts.tests.jaxws.wsi.constants.WSIConstants;
import com.sun.ts.tests.jaxws.common.Handler_Util;
import com.sun.ts.tests.jaxws.common.Constants;

public class ConformanceClaimHandler
    implements SOAPHandler<SOAPMessageContext>, WSIConstants {
  public Set<QName> getHeaders() {
    Set<QName> headers = new HashSet<QName>();
    headers.add(new QName(WSI_CLAIM_NAMESPACE_URI, WSI_CLAIM_LOCAL_NAME));
    return headers;
  }

  public void init(Map<String, Object> config) {
  }

  public void destroy() {
  }

  public void close(MessageContext message) {
  }

  public boolean handleMessage(SOAPMessageContext context) {
    try {
      if (Handler_Util.getDirection(context).equals(Constants.OUTBOUND))
        addConformanceClaims(context.getMessage());
    } catch (SOAPException se) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(se);
    }

    return true;
  }

  public boolean handleFault(SOAPMessageContext message) {
    return true;
  }

  private void addConformanceClaims(SOAPMessage message) throws SOAPException {
    addBP10ConformanceClaim(message);
    addDummyConformanceClaim(message);
  }

  private void addBP10ConformanceClaim(SOAPMessage message)
      throws SOAPException {
    SOAPEnvelope env = message.getSOAPPart().getEnvelope();
    SOAPHeader conformanceClaim = getHeader(env);
    Name claimName = env.createName(WSI_CLAIM_LOCAL_NAME, WSI_CLAIM_PREFIX,
        WSI_CLAIM_NAMESPACE_URI);
    SOAPHeaderElement claim = conformanceClaim.addHeaderElement(claimName);
    claim.addAttribute(env.createName(WSI_CLAIM_CONFORMS_TO_ATTR),
        WSI_CLAIM_CONFORMS_TO_VALUE);
    message.saveChanges();
  }

  private void addDummyConformanceClaim(SOAPMessage message)
      throws SOAPException {
    SOAPEnvelope env = message.getSOAPPart().getEnvelope();
    SOAPHeader conformanceClaim = getHeader(env);
    Name claimName = env.createName(WSI_CLAIM_LOCAL_NAME, WSI_CLAIM_PREFIX,
        WSI_CLAIM_NAMESPACE_URI);
    SOAPHeaderElement claim = conformanceClaim.addHeaderElement(claimName);
    claim.addAttribute(env.createName(WSI_CLAIM_CONFORMS_TO_ATTR),
        "http://dummy/conformanceClaim");
    message.saveChanges();
  }

  private SOAPHeader getHeader(SOAPEnvelope env) throws SOAPException {
    SOAPHeader header = env.getHeader();
    if (header != null) {
      return header;
    } else {
      return env.addHeader();
    }
  }
}
