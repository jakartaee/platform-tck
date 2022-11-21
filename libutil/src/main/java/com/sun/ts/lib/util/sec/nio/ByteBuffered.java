/*
 * Copyright (c) 2003, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.lib.util.sec.nio;

import java.nio.ByteBuffer;
import java.io.IOException;

/**
 * This is an interface to adapt existing APIs to use {@link java.nio.ByteBuffer
 * <tt>ByteBuffers</tt>} as the underlying data format. Only the initial
 * producer and final consumer have to be changed.
 * <p>
 *
 * For example, the Zip/Jar code supports {@link java.io.InputStream
 * <tt>InputStreams</tt>}. To make the Zip code use
 * {@link java.nio.MappedByteBuffer <tt>MappedByteBuffers</tt>} as the
 * underlying data structure, it can create a class of InputStream that wraps
 * the ByteBuffer, and implements the ByteBuffered interface. A co-operating
 * class several layers away can ask the InputStream if it is an instance of
 * ByteBuffered, then call the {@link #getByteBuffer()} method.
 */
public interface ByteBuffered {

  /**
   * Returns the <tt>ByteBuffer</tt> behind this object, if this particular
   * instance has one. An implementation of <tt>getByteBuffer()</tt> is allowed
   * to return <tt>null</tt> for any reason.
   *
   * @return The <tt>ByteBuffer</tt>, if this particular instance has one, or
   *         <tt>null</tt> otherwise.
   *
   * @throws IOException
   *           If the ByteBuffer is no longer valid.
   *
   * @since 1.5
   */
  public ByteBuffer getByteBuffer() throws IOException;
}
