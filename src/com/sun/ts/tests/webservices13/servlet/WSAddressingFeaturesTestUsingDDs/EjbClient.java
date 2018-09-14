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

package com.sun.ts.tests.webservices13.servlet.WSAddressingFeaturesTestUsingDDs;

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
import javax.naming.InitialContext;

import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.wsa.common.W3CAddressingConstants;
import com.sun.ts.tests.jaxws.wsa.common.WsaSOAPUtils;

@Stateless(name = "WSAddressingFeaturesTestUsingDDsClntBean")
@Remote({ EjbClientIF.class })
public class EjbClient implements EjbClientIF {

  private Properties harnessProps = null;

  private static final boolean debug = false;

  Echo defaultEchoPort = null;

  Echo enabledEchoPort = null;

  Echo requiredEchoPort = null;

  Echo disabledEchoPort = null;

  Echo2 defaultEcho2Port = null;

  Echo2 enabledEcho2Port = null;

  Echo2 requiredEcho2Port = null;

  Echo2 disabledEcho2Port = null;

  Echo3 anonymousEcho3Port = null;

  Echo4 nonanonymousEcho4Port = null;

  @PostConstruct
  public void postConstruct() {
    try {
      System.out.println("EjbClient:postConstruct()");
      InitialContext ctx = new InitialContext();
      System.out.println(
          "JNDI lookup java:comp/env/service/wsaddrfeaturestestusingddsdefaultechoport");
      defaultEchoPort = (Echo) ctx.lookup(
          "java:comp/env/service/wsaddrfeaturestestusingddsdefaultechoport");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsaddrfeaturestestusingddsenabledechoport");
      enabledEchoPort = (Echo) ctx.lookup(
          "java:comp/env/service/wsaddrfeaturestestusingddsenabledechoport");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsaddrfeaturestestusingddsrequiredechoport");
      requiredEchoPort = (Echo) ctx.lookup(
          "java:comp/env/service/wsaddrfeaturestestusingddsrequiredechoport");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsaddrfeaturestestusingddsdisabledechoport");
      disabledEchoPort = (Echo) ctx.lookup(
          "java:comp/env/service/wsaddrfeaturestestusingddsdisabledechoport");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsaddrfeaturestestusingddsdefaultecho2port");
      defaultEcho2Port = (Echo2) ctx.lookup(
          "java:comp/env/service/wsaddrfeaturestestusingddsdefaultecho2port");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsaddrfeaturestestusingddsenabledecho2port");
      enabledEcho2Port = (Echo2) ctx.lookup(
          "java:comp/env/service/wsaddrfeaturestestusingddsenabledecho2port");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsaddrfeaturestestusingddsrequiredecho2port");
      requiredEcho2Port = (Echo2) ctx.lookup(
          "java:comp/env/service/wsaddrfeaturestestusingddsrequiredecho2port");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsaddrfeaturestestusingddsdisabledecho2port");
      disabledEcho2Port = (Echo2) ctx.lookup(
          "java:comp/env/service/wsaddrfeaturestestusingddsdisabledecho2port");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsaddrfeaturestestusingddsanonymousecho3port");
      anonymousEcho3Port = (Echo3) ctx.lookup(
          "java:comp/env/service/wsaddrfeaturestestusingddsanonymousecho3port");
      System.out.println(
          "JNDI lookup java:comp/env/service/wsaddrfeaturestestusingddsnonanonymousecho4port");
      nonanonymousEcho4Port = (Echo4) ctx.lookup(
          "java:comp/env/service/wsaddrfeaturestestusingddsnonanonymousecho4port");
      System.out.println("EjbClient DEBUG: defaultEchoPort=" + defaultEchoPort);
      System.out.println("EjbClient DEBUG: enabledEchoPort=" + enabledEchoPort);
      System.out
          .println("EjbClient DEBUG: requiredEchoPort=" + requiredEchoPort);
      System.out
          .println("EjbClient DEBUG: disabledEchoPort=" + disabledEchoPort);
      System.out
          .println("EjbClient DEBUG: defaultEcho2Port=" + defaultEcho2Port);
      System.out
          .println("EjbClient DEBUG: enabledEcho2Port=" + enabledEcho2Port);
      System.out
          .println("EjbClient DEBUG: requiredEcho2Port=" + requiredEcho2Port);
      System.out
          .println("EjbClient DEBUG: disabledEcho2Port=" + disabledEcho2Port);
      System.out
          .println("EjbClient DEBUG: anonymousEcho3Port=" + anonymousEcho3Port);
      System.out.println(
          "EjbClient DEBUG: nonanonymousEcho4Port=" + nonanonymousEcho4Port);
    } catch (Exception e) {
      System.err.println("EjbClient:postConstruct() Exception: " + e);
      e.printStackTrace();
    }
    if (defaultEchoPort == null || enabledEchoPort == null
        || requiredEchoPort == null || disabledEchoPort == null
        || defaultEcho2Port == null || enabledEcho2Port == null
        || requiredEcho2Port == null || disabledEcho2Port == null
        || anonymousEcho3Port == null || nonanonymousEcho4Port == null) {
      throw new EJBException(
          "postConstruct failed: injection or JNDI lookup failure");
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
    else if (testName.equals("VerifyAddrHeadersMayExistForEnabledEcho2Port"))
      return VerifyAddrHeadersMayExistForEnabledEcho2Port();
    else if (testName.equals("testAnonymousResponsesAssertion"))
      return testAnonymousResponsesAssertion();
    else if (testName.equals("testNonAnonymousResponsesAssertion"))
      return testNonAnonymousResponsesAssertion();
    else
      return false;
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

  private boolean testAnonymousResponsesAssertion() {
    try {
      TestUtil.logMsg("EjbClient invoking EchoService echo() method");
      TestUtil.logMsg("testAnonymousResponsesAssertion");
      Holder<String> testName = new Holder("testAnonymousResponsesAssertion");
      anonymousEcho3Port.echo("Echo from EjbClient on anonymousEcho3Port",
          testName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean testNonAnonymousResponsesAssertion() {
    try {
      TestUtil.logMsg("EjbClient invoking EchoService echo() method");
      TestUtil.logMsg("testNonAnonymousResponsesAssertion");
      Holder<String> testName = new Holder(
          "testNonAnonymousResponsesAssertion");
      nonanonymousEcho4Port.echo("Echo from EjbClient on nonanonymousEcho4Port",
          testName);
      return true;
    } catch (Exception e) {
      return true;
    }
  }
}
