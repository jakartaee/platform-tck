/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder;

import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;

public abstract class CoderSuperClassText<T> extends CoderSuperClass
    implements Encoder.Text<T>, Decoder.Text<T> {

  @Override
  public boolean willDecode(String bytes) {
    return true;
  }

  @Override
  public String encode(T object) throws EncodeException {
    return COMMON_CODED_STRING;
  }

}
