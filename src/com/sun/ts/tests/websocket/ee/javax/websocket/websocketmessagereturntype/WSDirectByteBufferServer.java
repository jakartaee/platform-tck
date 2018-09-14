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

package com.sun.ts.tests.websocket.ee.javax.websocket.websocketmessagereturntype;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.sun.ts.tests.websocket.common.util.IOUtil;

@ServerEndpoint("/directbytebuffer")
public class WSDirectByteBufferServer {
  private File f;

  @OnClose
  public void onClose() {
    f.delete();
  }

  private static File createTempFile(String data) throws IOException {
    File f = File.createTempFile("tcktemp", "file");
    FileOutputStream fos = new FileOutputStream(f);
    fos.write(data.getBytes());
    fos.close();
    return f;
  }

  @OnMessage
  public ByteBuffer echo(String data) throws IOException {
    f = createTempFile(data);

    FileInputStream fis = new FileInputStream(f);
    MappedByteBuffer mbb = fis.getChannel().map(MapMode.READ_ONLY, 0,
        data.length());
    fis.close();
    return mbb;
  }

  @OnError
  public void onError(Session session, Throwable t) throws IOException {
    t.printStackTrace(); // Write to error log, too
    String message = "Exception: " + IOUtil.printStackTrace(t);
    session.getBasicRemote().sendText(message);
  }
}
