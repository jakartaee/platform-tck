/*
 * Copyright (c) 2006, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaspic.util;

import com.sun.ts.lib.util.TestUtil;
import java.lang.reflect.Constructor;
import java.net.URL;
import javax.xml.namespace.QName;

/**
 *
 * @author Raja Perumal
 */
public class WebServiceUtils {

  public static Object getPort(URL wsdlurl, QName siName, Class siClass,
      QName portName, Class seiClass) throws Exception {
    TestUtil.logMsg("getPort(URL, QName, Class, QName, Class)");

    javax.xml.ws.Service svc = (javax.xml.ws.Service) getService(wsdlurl,
        siName, siClass);
    TestUtil.logMsg("Get stub/proxy for seiClass -> " + seiClass.getName()
        + ", port ->" + portName);
    Object stub = null;
    stub = svc.getPort(portName, seiClass);
    if (stub == null) {
      TestUtil.logErr(
          "FATAL: getPort(URL, QName, Class, QName, Class) returned stub/proxy=null");
    } else
      TestUtil.logMsg("Obtained stub/proxy=" + stub);
    return stub;
  }

  public static javax.xml.ws.Service getService(URL wsdlurl, QName siName,
      Class siClass) throws Exception {
    TestUtil.logMsg("getService(URL, QName, Class)");
    javax.xml.ws.Service service = null;
    if (wsdlurl != null)
      TestUtil.logMsg("URL=" + wsdlurl.toString());
    TestUtil.logMsg("siName=" + siName);
    TestUtil.logMsg("siClass=" + siClass.getName());
    Constructor ctr = siClass.getConstructor(URL.class, QName.class);
    service = (javax.xml.ws.Service) ctr.newInstance(wsdlurl, siName);
    if (service == null)
      TestUtil
          .logErr("FATAL: getService(URL, QName, Class) returned service=null");
    return service;
  }

}
