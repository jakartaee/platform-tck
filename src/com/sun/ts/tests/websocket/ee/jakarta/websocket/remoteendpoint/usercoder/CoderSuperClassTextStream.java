/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates and others.
 * All rights reserved.
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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.remoteendpoint.usercoder;

import java.io.IOException;
import java.io.Writer;

import jakarta.websocket.Decoder;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;

public abstract class CoderSuperClassTextStream<T> extends CoderSuperClass
    implements Encoder.TextStream<T>, Decoder.TextStream<T> {

  @Override
  public void encode(T object, Writer writer) throws EncodeException {
    try {
      writer.write(COMMON_CODED_STRING);
      writer.close();
    } catch (IOException e) {
      throw new EncodeException(object, COMMON_CODED_STRING, e);
    }
  }

}
