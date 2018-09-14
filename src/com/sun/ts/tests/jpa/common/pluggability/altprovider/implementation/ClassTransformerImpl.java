/*
 * Copyright (c) 2011, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.common.pluggability.altprovider.implementation;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javax.persistence.spi.ClassTransformer;

public class ClassTransformerImpl implements ClassTransformer {

  protected TSLogger logger;

  public ClassTransformerImpl() {
    logger = TSLogger.getInstance();
  }

  @Override
  public byte[] transform(ClassLoader arg0, String className,
      Class<?> classBeingRedefined, ProtectionDomain arg3, byte[] arg4)
      throws IllegalClassFormatException {
    logger.log("Called ClassTransformerImpl.transform()");

    return null;// indicates no transformation
  }

}
