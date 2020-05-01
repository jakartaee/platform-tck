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

/*
 * $Id$
 */

package com.sun.ts.tests.jsonb.customizedmapping.dateformat.model.customized;

import java.util.Date;

import com.sun.ts.tests.jsonb.TypeContainer;

import jakarta.json.bind.annotation.JsonbDateFormat;

@JsonbDateFormat(value = "E DD MMM yyyy HH:mm:ss", locale = "de")
public class CustomizedPackageTypeOverrideDateContainer
    implements TypeContainer<Date> {
  private Date instance;

  @Override
  public Date getInstance() {
    return instance;
  }

  @Override
  public void setInstance(Date instance) {
    this.instance = instance;
  }
}
