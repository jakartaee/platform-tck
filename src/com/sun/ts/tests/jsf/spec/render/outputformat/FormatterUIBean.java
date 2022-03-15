/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsf.spec.render.outputformat;

import java.io.Serializable;
import jakarta.faces.component.html.HtmlOutputFormat;

@jakarta.inject.Named("score") @jakarta.enterprise.context.SessionScoped
public class FormatterUIBean implements Serializable {

  private static final long serialVersionUID = -956414308038088087L;

  private HtmlOutputFormat fscore;

  /** Creates a new instance of MessageBean */
  public FormatterUIBean() {
  }

  public HtmlOutputFormat getFscore() {
    return fscore;
  }

  public void setFscore(HtmlOutputFormat fscore) {
    fscore.setTitle("formatter7");
    fscore.setStyleClass("text");
    fscore.setValue("100-50");

    this.fscore = fscore;
  }
}
