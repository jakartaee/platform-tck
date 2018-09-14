/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.securityapi.ham.sam.obtainbean;

import java.io.IOException;
import java.util.Map;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.module.ServerAuthModule;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServerAuthModule implements ServerAuthModule {

  @Override
  public void initialize(MessagePolicy requestPolicy,
      MessagePolicy responsePolicy, CallbackHandler handler, Map map)
      throws AuthException {
  }

  @Override
  public Class<?>[] getSupportedMessageTypes() {
    return new Class<?>[] { HttpServletRequest.class,
        HttpServletResponse.class };
  }

  @Override
  public AuthStatus validateRequest(MessageInfo messageInfo,
      Subject clientSubject, Subject serviceSubject) throws AuthException {

    HttpAuthenticationMechanism ham = CDI.current()
        .select(HttpAuthenticationMechanism.class).get();

    BeanManager bm = CDI.current().getBeanManager();

    Bean<HttpAuthenticationMechanism> bean = (Bean<HttpAuthenticationMechanism>) bm
        .getBeans(HttpAuthenticationMechanism.class).iterator().next();
    CreationalContext<HttpAuthenticationMechanism> ctx = bm
        .createCreationalContext(bean);
    ham = (HttpAuthenticationMechanism) bm.getReference(bean,
        HttpAuthenticationMechanism.class, ctx);

    if (ham != null) {
      HttpServletResponse response = (HttpServletResponse) messageInfo
          .getResponseMessage();
      try {
        response.getWriter().println(
            "The CDI services is fully available, ServerAuthModule method can obtain bean through CDI.");
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    return AuthStatus.SUCCESS;
  }

  @Override
  public AuthStatus secureResponse(MessageInfo messageInfo,
      Subject serviceSubject) throws AuthException {
    return AuthStatus.SEND_SUCCESS;
  }

  @Override
  public void cleanSubject(MessageInfo messageInfo, Subject subject)
      throws AuthException {

  }

}
