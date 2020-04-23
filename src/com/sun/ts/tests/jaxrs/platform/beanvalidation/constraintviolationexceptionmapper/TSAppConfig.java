/*
 * Copyright (c) 2015, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.platform.beanvalidation.constraintviolationexceptionmapper;

import java.util.HashSet;
import java.util.Set;

import jakarta.ws.rs.core.Application;

import com.sun.ts.tests.jaxrs.common.provider.StringBeanEntityProvider;
import com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation.ConstraintDeclarationResource;
import com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation.ConstraintDefinitionResource;
import com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation.NotShortNorFiveEntityProvider;
import com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation.Resource;
import com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation.ValidateExecutableResource;

public class TSAppConfig extends Application {

  public java.util.Set<java.lang.Class<?>> getClasses() {
    Set<Class<?>> resources = new HashSet<Class<?>>();
    resources.add(Resource.class);
    resources.add(ConstraintDeclarationResource.class);
    resources.add(ConstraintDefinitionResource.class);
    resources.add(ValidateExecutableResource.class);
    resources.add(NotShortNorFiveEntityProvider.class);
    resources.add(StringBeanEntityProvider.class);
    resources.add(ConstraintViolationExceptionMapper.class);
    return resources;
  }
}
