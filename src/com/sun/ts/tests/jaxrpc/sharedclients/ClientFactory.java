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

package com.sun.ts.tests.jaxrpc.sharedclients;

import java.lang.reflect.Constructor;
import java.util.Properties;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jaxrpc.wsi.constants.CTSConstants;
import com.sun.ts.tests.jaxrpc.wsi.utils.PropertyUtils;

public class ClientFactory {
  public static SOAPClient getClient(Class clazz, Properties properties)
      throws EETest.Fault {
    int mode;
    String property = properties.getProperty("platform.mode");
    if (property == null) {
      throw new EETest.Fault(
          "The 'platform.mode' property value is not defined");
    }
    if (property.equalsIgnoreCase("standalone")) {
      mode = SOAPClient.MODE_JAXRPC;
    } else if (property.equalsIgnoreCase("javaEE")) {
      mode = SOAPClient.MODE_JAVAEE;
    } else {
      throw new EETest.Fault(
          "The 'platform.mode' property value '" + property + "' is invalid");
    }
    String webServerHost = PropertyUtils.getProperty(properties,
        CTSConstants.WEBSERVER_HOST);
    int webServerPort;
    if (SecureClient.class.isAssignableFrom(clazz)) {
      webServerPort = PropertyUtils.getIntegerProperty(properties,
          CTSConstants.SECURE_WEBSERVER_PORT);
    } else {
      webServerPort = PropertyUtils.getIntegerProperty(properties,
          CTSConstants.WEBSERVER_PORT);
    }
    try {
      Constructor ctr = clazz
          .getConstructor(new Class[] { String.class, int.class, int.class });
      return (SOAPClient) ctr.newInstance(new Object[] { webServerHost,
          new Integer(webServerPort), new Integer(mode) });
    } catch (NoSuchMethodException e) {
      throw new EETest.Fault(
          "Client '" + clazz.getName() + "' does not have required constructor",
          e);
    } catch (ClassCastException e) {
      throw new EETest.Fault("Client '" + clazz.getName()
          + "' does not extend '" + SOAPClient.class.getName() + "'", e);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EETest.Fault("Unable to instantiate '" + clazz.getName() + "'",
          e);
    }
  }

  private ClientFactory() {
    super();
  }
}
