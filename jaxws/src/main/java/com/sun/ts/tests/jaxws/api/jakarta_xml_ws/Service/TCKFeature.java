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
 * $Id: Client.java 56891 2009-02-24 15:46:00Z af70133 $
 */

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws.Service;

import jakarta.xml.ws.WebServiceFeature;

public final class TCKFeature extends WebServiceFeature {
  public static final String ID = "http://www.w3.org/2004/08/soap/features/tck-feature";

  public TCKFeature() {
    this.enabled = true;
  }

  public TCKFeature(boolean enabled) {
    this.enabled = enabled;
  }

  public String getID() {
    return ID;
  }
}
