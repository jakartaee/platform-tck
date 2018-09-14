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

/*
 * $Id$
 */
package com.sun.ts.tests.jsf.spec.ajax.common;

import javax.faces.event.ActionEvent;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Arrays;

public class AjaxTagValuesBean {

  private Integer count = 0;

  private Boolean checked = false;

  private String text = "";

  private String[] outArray = { "out1", ":form2:out2", ":out3" };

  private Collection<String> outSet = new LinkedHashSet<String>(
      Arrays.asList(outArray));

  private String render = "out1";

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Boolean getChecked() {
    return checked;
  }

  public void setChecked(Boolean checked) {
    this.checked = checked;
  }

  public Integer getCount() {
    return count++;
  }

  public void reset(ActionEvent ae) {
    count = 0;
    checked = false;
    text = "";
  }

  public Collection<String> getRenderList() {
    return outSet;
  }

  public String getRenderOne() {
    return render;
  }
}
