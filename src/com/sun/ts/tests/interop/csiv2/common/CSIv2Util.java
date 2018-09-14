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

package com.sun.ts.tests.interop.csiv2.common;

import java.io.*;
import java.util.*;
import java.net.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.util.*;

/**
 * CSIv2Util is an utility class used to read and write Data to a
 * HttpsURLConnection
 * 
 * @author Raja Perumal
 *
 */
public final class CSIv2Util {
  public static TSHttpsURLConnection sendPostDataHttps(Properties p, URL url)
      throws IOException {
    TSHttpsURLConnection httpsURLConn = null;
    String argString = TestUtil.toEncodedString(p);
    httpsURLConn = new TSHttpsURLConnection();
    if (httpsURLConn != null) {
      // post the url with encoded properties
      // i.e Append the url with encoded properties(name & values)
      // URL newURL = new URL(url.toString()+"?"+argString);

      URL newURL = new URL(url.toString());
      TestUtil.logMsg("Openning https url connection to: " + newURL.toString());
      httpsURLConn.init(newURL);

      httpsURLConn.setDoInput(true);
      httpsURLConn.setDoOutput(true);
      httpsURLConn.setUseCaches(false);

      // set the conent length for the data
      httpsURLConn.setRequestProperty("CONTENT_LENGTH",
          "" + argString.length());

      TestUtil.logMsg("Encoded String=" + argString);

      // get Output stream
      OutputStream os = httpsURLConn.getOutputStream();
      OutputStreamWriter osw = new OutputStreamWriter(os);

      // write the data to the output stream
      osw.write(argString);
      osw.flush();
      osw.close();

    } else
      throw new IOException("httsURLConnection is null");

    return httpsURLConn;
  }

  /**
   * Get response Properties directly from a TSHttpsURLConnection.
   *
   */
  public static Properties getResponseProperties(
      TSHttpsURLConnection connection) throws IOException {
    Properties props;
    String input;

    StringBuffer content;
    BufferedReader in;

    // set up the streams / readers
    InputStream instream = connection.getInputStream();
    InputStreamReader inreader = new InputStreamReader(instream);
    in = new BufferedReader(inreader);

    // data structures
    content = new StringBuffer(1024);
    char[] chars = new char[1024];
    int length = 0;

    // pull the data into the content buffer
    while (length != -1) {
      content.append(chars, 0, length);
      length = in.read(chars, 0, chars.length);
    }

    // return
    instream.close();
    inreader.close();
    in.close();

    ByteArrayInputStream bais;
    byte[] bytes;

    props = new Properties();

    bytes = content.toString().getBytes();
    bais = new ByteArrayInputStream(bytes);
    props.load(bais);
    in.close();

    return props;
  }

}
