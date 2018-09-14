/*
 * Copyright (c) 2014, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.ee.javax.websocket.session11.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.sun.ts.tests.websocket.common.util.IOUtil;

public class AlternativeInputStreamDecoder
    implements Decoder.Binary<InputStream> {

  public static final String DECODER_SAYS = "InputStream decoder";

  @Override
  public void init(EndpointConfig config) {
  }

  @Override
  public void destroy() {
  }

  @Override
  public boolean willDecode(ByteBuffer bytes) {
    return true;
  }

  @Override
  public InputStream decode(ByteBuffer bytes) throws DecodeException {
    String s = DECODER_SAYS + IOUtil.byteBufferToString(bytes);
    ByteArrayInputStream bais = new ByteArrayInputStream(s.getBytes());
    return bais;
  }
}
