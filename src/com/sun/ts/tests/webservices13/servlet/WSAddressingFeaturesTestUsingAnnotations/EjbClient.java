/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.webservices13.servlet.WSAddressingFeaturesTestUsingAnnotations;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.xml.ws.*;
import javax.xml.ws.soap.*;
import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.annotation.Resource;
import javax.annotation.PostConstruct;

import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.wsa.common.W3CAddressingConstants;
import com.sun.ts.tests.jaxws.wsa.common.WsaSOAPUtils;

@Stateless(name = "WSAddressingFeaturesTestUsingAnnotationsClntBean")
@Remote({ EjbClientIF.class })
public class EjbClient implements EjbClientIF {

  private Properties harnessProps = null;

  private static final boolean debug = false;

  @Addressing
  @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsdefaultechoport", value = EchoService.class)
  Echo defaultEchoPort = null;

  @Addressing(enabled = true)
  @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsenabledechoport", value = EchoService.class)
  Echo enabledEchoPort = null;

  @Addressing(enabled = true, required = true)
  @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsrequiredechoport", value = EchoService.class)
  Echo requiredEchoPort = null;

  @Addressing(enabled = false)
  @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsdisabledechoport", value = EchoService.class)
  Echo disabledEchoPort = null;

  @Addressing
  @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsdefaultecho2port", value = EchoService.class)
  Echo2 defaultEcho2Port = null;

  @Addressing(enabled = true)
  @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsenabledecho2port", value = EchoService.class)
  Echo2 enabledEcho2Port = null;

  @Addressing(enabled = true, required = true)
  @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsrequiredecho2port", value = EchoService.class)
  Echo2 requiredEcho2Port = null;

  @Addressing(enabled = false)
  @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsdisabledecho2port", value = EchoService.class)
  Echo2 disabledEcho2Port = null;

  @WebServiceRef(name = "service/wsaddrfeaturestestusingannotationsservice")
  EchoService service = null;

  @PostConstruct
  public void postConstruct() {
    System.out.println("EjbClient:postConstruct()");
    System.out.println("EjbClient DEBUG: service=" + service);
    System.out.println("EjbClient DEBUG: defaultEchoPort=" + defaultEchoPort);
    System.out.println("EjbClient DEBUG: enabledEchoPort=" + enabledEchoPort);
    System.out.println("EjbClient DEBUG: requiredEchoPort=" + requiredEchoPort);
    System.out.println("EjbClient DEBUG: disabledEchoPort=" + disabledEchoPort);
    System.out.println("EjbClient DEBUG: defaultEcho2Port=" + defaultEcho2Port);
    System.out.println("EjbClient DEBUG: enabledEcho2Port=" + enabledEcho2Port);
    System.out
        .println("EjbClient DEBUG: requiredEcho2Port=" + requiredEcho2Port);
    System.out
        .println("EjbClient DEBUG: disabledEcho2Port=" + disabledEcho2Port);
    if (service == null || defaultEchoPort == null || enabledEchoPort == null
        || requiredEchoPort == null || disabledEchoPort == null
        || defaultEcho2Port == null || enabledEcho2Port == null
        || requiredEcho2Port == null || disabledEcho2Port == null) {
      throw new EJBException("postConstruct failed: injection failure");
    }
  }

  public void init(Properties p) {
    harnessProps = p;
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("init: failed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("init: failed");
    }
  }

  public boolean echo(String string, String testName) {
    if (testName.equals("VerifyAddrHeadersExistForRequiredEchoPort"))
      return VerifyAddrHeadersExistForRequiredEchoPort();
    else if (testName.equals("VerifyAddrHeadersDoNotExistForDisabledEchoPort"))
      return VerifyAddrHeadersDoNotExistForDisabledEchoPort();
    else if (testName.equals("VerifyAddrHeadersMayExistForEnabledEchoPort"))
      return VerifyAddrHeadersMayExistForEnabledEchoPort();
    else if (testName.equals("VerifyExceptionThrownForRequiredEcho2Port"))
      return VerifyExceptionThrownForRequiredEcho2Port();
    else if (testName.equals("VerifyAddrHeadersDoNotExistForDisabledEcho2Port"))
      return VerifyAddrHeadersDoNotExistForDisabledEcho2Port();
    else
      return VerifyAddrHeadersMayExistForEnabledEcho2Port();
  }

  private boolean VerifyAddrHeadersExistForRequiredEchoPort() {
    try {
      TestUtil.logMsg("EjbClient invoking EchoService echo() method");
      TestUtil.logMsg(
          "Addressing headers MUST be present on the SOAPRequest/SOAPResponse");
      TestUtil.logMsg("VerifyAddrHeadersExistForRequiredEchoPort");
      Holder<String> testName = new Holder(
          "VerifyAddrHeadersExistForRequiredEchoPort");
      requiredEchoPort.echo("Echo from EjbClient on requiredEchoPort",
          testName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean VerifyAddrHeadersDoNotExistForDisabledEchoPort() {
    try {
      TestUtil.logMsg("EjbClient invoking EchoService echo() method");
      TestUtil.logMsg(
          "Addressing headers MUST NOT be present on the SOAPRequest/SOAPResponse");
      TestUtil.logMsg("VerifyAddrHeadersDoNotExistForDisabledEchoPort");
      Holder<String> testName = new Holder(
          "VerifyAddrHeadersDoNotExistForDisabledEchoPort");
      disabledEchoPort.echo("Echo from EjbClient on disabledEchoPort",
          testName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean VerifyAddrHeadersMayExistForEnabledEchoPort() {
    try {
      TestUtil.logMsg("EjbClient invoking EchoService echo() method");
      TestUtil.logMsg(
          "Addressing headers MAY be present on the SOAPRequest/SOAPResponse");
      TestUtil.logMsg("VerifyAddrHeadersMayExistForEnabledEchoPort");
      Holder<String> testName = new Holder(
          "VerifyAddrHeadersMayExistForEnabledEchoPort");
      enabledEchoPort.echo("Echo from EjbClient on enabledEchoPort", testName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean VerifyExceptionThrownForRequiredEcho2Port() {
    try {
      TestUtil.logMsg("EjbClient invoking EchoService echo() method");
      TestUtil.logMsg("Expect a WebServiceException to be thrown back");
      TestUtil.logMsg("VerifyExceptionThrownForRequiredEcho2Port");
      Holder<String> testName = new Holder(
          "VerifyExceptionThrownForRequiredEcho2Port");
      requiredEcho2Port.echo("Echo from EjbClient on requiredEcho2Port",
          testName);
      TestUtil.logErr("WebServiceException was not thrown back");
      return false;
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught expected WebServiceException: " + e.getMessage());
      return true;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      return false;
    }
  }

  private boolean VerifyAddrHeadersDoNotExistForDisabledEcho2Port() {
    try {
      TestUtil.logMsg("EjbClient invoking EchoService echo() method");
      TestUtil.logMsg(
          "Addressing headers MUST NOT be present on the SOAPRequest/SOAPResponse");
      TestUtil.logMsg("VerifyAddrHeadersDoNotExistForDisabledEcho2Port");
      Holder<String> testName = new Holder(
          "VerifyAddrHeadersDoNotExistForDisabledEcho2Port");
      disabledEcho2Port.echo("Echo from EjbClient on disabledEcho2Port",
          testName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean VerifyAddrHeadersMayExistForEnabledEcho2Port() {
    try {
      TestUtil.logMsg("EjbClient invoking EchoService echo() method");
      TestUtil.logMsg(
          "Addressing headers MAY be present on the SOAPRequest/SOAPResponse");
      TestUtil.logMsg("VerifyAddrHeadersMayExistForEnabledEcho2Port");
      Holder<String> testName = new Holder(
          "VerifyAddrHeadersMayExistForEnabledEcho2Port");
      enabledEcho2Port.echo("Echo from EjbClient on enabledEcho2Port",
          testName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
