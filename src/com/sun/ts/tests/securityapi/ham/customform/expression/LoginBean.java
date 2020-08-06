/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.securityapi.ham.customform.expression;

import static jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Named
@RequestScoped
public class LoginBean {

  private String pwd;

  private String user;

  @Inject
  private SecurityContext securityContext;

  public String getPwd() {
    return pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public void login() {

    FacesContext context = FacesContext.getCurrentInstance();
    Credential credential = new UsernamePasswordCredential(user,
        new Password(pwd));

    HttpServletRequest request = (HttpServletRequest) context
        .getExternalContext().getRequest();
    HttpServletResponse response = (HttpServletResponse) context
        .getExternalContext().getResponse();

    AuthenticationStatus status = securityContext.authenticate(request,
        response, withParams().credential(credential));

    if (status.equals(AuthenticationStatus.SUCCESS)) {
      // Authentication mechanism has send a redirect, should not
      // send anything to response from JSF now.
      context.responseComplete();
    } else if (status.equals(AuthenticationStatus.SEND_FAILURE)) {
      context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
          "Authentication failed", null));
    }

  }
}
