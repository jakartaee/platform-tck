/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.api.rs.ext.runtimedelegate;

import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Link.Builder;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.Variant.VariantListBuilder;
import jakarta.ws.rs.ext.RuntimeDelegate;

public class TckRuntimeDelegate extends RuntimeDelegate {

  @Override
  public <T> T createEndpoint(Application application, Class<T> endpointType)
      throws IllegalArgumentException, UnsupportedOperationException {
    return null;
  }

  @Override
  public <T> HeaderDelegate<T> createHeaderDelegate(Class<T> type) {
    return null;
  }

  @Override
  public ResponseBuilder createResponseBuilder() {
    return null;
  }

  @Override
  public UriBuilder createUriBuilder() {
    return null;
  }

  @Override
  public VariantListBuilder createVariantListBuilder() {
    return null;
  }

  @Override
  public Builder createLinkBuilder() {
    return null;
  }

}
