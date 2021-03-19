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
 * $Id$
 */

package com.sun.ts.tests.jaxws.jaxws23.wsa.j2w.document.literal.anonymous;

import jakarta.xml.ws.*;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPException;
import java.util.Calendar;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.io.IOException;

/**
 * This class handles the non-anonymous ReplyTo responses
 * 
 * @author Alan Frechette
 */
@WebServiceProvider
@ServiceMode(value = Service.Mode.MESSAGE)
public class NonAnonymousRespProcessor implements Provider<SOAPMessage> {
  Exchanger<SOAPMessage> msgExchanger;

  public NonAnonymousRespProcessor() {
  }

  public NonAnonymousRespProcessor(Exchanger<SOAPMessage> msgExchanger) {
    this.msgExchanger = msgExchanger;
  }

  public SOAPMessage invoke(SOAPMessage request) {
    System.out.printf("====%s[start:%tc]====\n", getClass().getName(),
        Calendar.getInstance());
    try {
      request.writeTo(System.out);
    } catch (SOAPException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.printf("====%s[end:%tc]====\n", getClass().getName(),
        Calendar.getInstance());
    try {
      msgExchanger.exchange(request, 30L, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (TimeoutException e) {
      e.printStackTrace();
    }
    return null;
  }
}
