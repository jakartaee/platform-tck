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

package com.sun.ts.tests.securityapi.ham.customform.expression;

import static javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
