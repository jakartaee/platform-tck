/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.jaxrpc.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.Iterator;
import javax.xml.rpc.JAXRPCException;
import javax.xml.namespace.QName;
import javax.xml.rpc.encoding.Deserializer;
import javax.xml.rpc.encoding.DeserializerFactory;

/**
 * Deserializer Factory for the OnePropBean javabean. Used for pluggable
 * serializer/deserializer.
 */
public class OnePropBeanDeserializerFactory implements DeserializerFactory {

  protected Deserializer deserializer;

  protected String JAX_RPC_RI_MECHANISM = "http://java.sun.com/jax-rpc-ri/1.0/streaming/";

  public OnePropBeanDeserializerFactory() {
    this.deserializer = new OnePropBeanSOAPDeserializer();
  }

  public Deserializer getDeserializerAs(String mechanismType)
      throws JAXRPCException {
    if (!JAX_RPC_RI_MECHANISM.equals(mechanismType)) {
      throw new JAXRPCException(
          "typemapping.mechanism.unsupported " + mechanismType);
    }
    return deserializer;
  }

  public Iterator getSupportedMechanismTypes() {
    return new SingleElementIterator(JAX_RPC_RI_MECHANISM);
  }

  public Iterator getDeserializers() {
    return new SingleElementIterator(deserializer);
  }
}
