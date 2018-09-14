/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.spec.coretags.common;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;

@RequestScoped
public class BookBean implements Serializable {

  private String authorName;

  private boolean result;

  @PostConstruct
  public void postConstruct() {
  }

  public String checkAuthor() {
    if ("Orsen Scott Card".equals(authorName)) {
      this.setResult(true);
      return "Test Passed!";
    }

    this.setResult(false);
    return "Test Failed!";
  }

  public String getAuthorName() {
    return authorName;
  }

  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }

  public boolean getResult() {
    return result;
  }

  public void setResult(boolean result) {
    this.result = result;
  }
}
