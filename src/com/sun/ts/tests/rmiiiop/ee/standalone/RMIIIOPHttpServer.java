/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */

package com.sun.ts.tests.rmiiiop.ee.standalone;

import java.util.*;
import java.io.*;
import java.net.*;

public class RMIIIOPHttpServer extends Thread {
  private ServerSocket ssock = null;

  private String ior = null;

  private boolean verbose = false;

  // Inner class ProcessClientRequest
  static class ProcessClientRequest extends Thread {

    // Instance variables
    private Socket csock = null;

    private String ior = null;

    private boolean verbose = false;

    public ProcessClientRequest(Socket s, String ior, boolean verbose) {
      this.csock = s;
      this.ior = ior;
      this.verbose = verbose;
    }

    public void run() {
      try {
        if (verbose)
          PrintUtil.logMsg("Processing http client request....");
        InputStream is = csock.getInputStream();
        PrintWriter os = new PrintWriter(csock.getOutputStream(), true);
        if (verbose)
          PrintUtil.logMsg(
              "Send HTTP_OK response and IOR string back to the client....");
        os.print("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\n" + ior);
        os.flush();
        byte[] buffer = new byte[1024];
        is.read(buffer);
        os.close();
      } catch (IOException e) {
        ;
      } finally {
        try {
          csock.close();
        } catch (IOException e2) {
        }
      }
    }
  }

  public RMIIIOPHttpServer(String ior, int port, boolean verbose)
      throws Exception {
    this.ior = ior;
    this.ssock = new ServerSocket(port);
    this.verbose = verbose;
  }

  public void run() {
    try {
      while (true) {
        if (verbose)
          PrintUtil.logMsg("RMIIIOPHttpServer ready and waiting....");
        ProcessClientRequest p = new ProcessClientRequest(ssock.accept(), ior,
            verbose);
        p.start();
      }
    } catch (IOException e) {
    }
  }
}
