/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.ee.rs.ext.paramconverter;

import javax.activation.DataSource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ParamConverter;

import com.sun.ts.tests.jaxrs.common.impl.StringDataSource;

public class DataSourceParamConverter implements ParamConverter<DataSource> {

  @Override
  public DataSource fromString(String value) throws IllegalArgumentException {
    StringDataSource sds = new StringDataSource(value,
        MediaType.TEXT_PLAIN_TYPE);
    return sds;
  }

  @Override
  public String toString(DataSource value) throws IllegalArgumentException {
    return value.getName();
  }

}
