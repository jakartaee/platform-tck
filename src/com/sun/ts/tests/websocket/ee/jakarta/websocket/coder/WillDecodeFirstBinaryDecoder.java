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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.coder;

import java.nio.ByteBuffer;

import jakarta.websocket.DecodeException;

import com.sun.ts.tests.websocket.common.stringbean.StringBean;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanBinaryDecoder;

public class WillDecodeFirstBinaryDecoder extends StringBeanBinaryDecoder {
  @Override
  public StringBean decode(ByteBuffer arg0) throws DecodeException {
    Logger.onCode(getClass());
    return super.decode(arg0);
  };

  @Override
  public boolean willDecode(ByteBuffer arg0) {
    Logger.onWillCode(getClass());
    return false;
  }
}
