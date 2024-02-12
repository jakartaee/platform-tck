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

package com.sun.ts.tests.websocket.common.stringbean;

import java.io.IOException;
import java.io.Reader;

import com.sun.ts.tests.websocket.common.util.IOUtil;

import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder.TextStream;
import jakarta.websocket.EndpointConfig;

public class StringBeanTextStreamDecoder implements TextStream<StringBean> {

	@Override
	public void init(EndpointConfig config) {
	}

	@Override
	public StringBean decode(Reader r) throws DecodeException, IOException {
		String text = IOUtil.readFromReader(r);
		return new StringBean(text);
	}

	@Override
	public void destroy() {
	}
}
