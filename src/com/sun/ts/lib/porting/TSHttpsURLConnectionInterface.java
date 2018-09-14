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

package com.sun.ts.lib.porting;

import java.io.*;
import java.net.*;

/**
 * This is the TSHttpsURLConnectionInterface. An implementation of this
 * interface must be provided by each Java EE implementation, to support https
 * url connection.
 *
 */
public interface TSHttpsURLConnectionInterface {
  /**
   * Sets the value of the doInput field for this URLConnection to the specified
   * value.
   * 
   * A URL connection can be used for input and/or output. Set the DoInput flag
   * to true if you intend to use the URL connection for input, false if not.
   * The default is true.
   * 
   * 
   * @param doInput
   *          a boolean indicating whether the URL connection is used for input
   *          or not
   */
  public void setDoInput(boolean doInput);

  /**
   * Sets the value of the doOutput field for this URLConnection to the
   * specified value.
   * 
   * A URL connection can be used for input and/or output. Set the DoOutput flag
   * to true if you intend to use the URL connection for output, false if not.
   * The default is false.
   *
   * @param doOutput
   *          a boolean indicating whether the URL connection is used for output
   *          or not
   */
  public void setDoOutput(boolean doOutput);

  /**
   * Sets the value of the useCaches field of this URLConnection to the
   * specified value.
   * 
   * Some protocols do caching of documents. Occasionally, it is important to be
   * able to "tunnel through" and ignore the caches (e.g., the "reload" button
   * in a browser). If the UseCaches flag on a connection is true, the
   * connection is allowed to use whatever caches it can. If false, caches are
   * to be ignored. The default value is true
   *
   * @param usecaches
   *          a boolean indicating whether or not to allow caching
   */
  public void setUseCaches(boolean usecaches);

  /**
   * Sets the general request property. If a property with the key already
   * exists, overwrite its value with the new value.
   *
   * @param key
   *          the keyword by which the request is known
   * @param value
   *          the value associated with it
   */
  public void setRequestProperty(String key, String value);

  /**
   * Returns the value of the named header field. If called on a connection that
   * sets the same header multiple times only the last value is returned.
   * 
   * @param name
   *          the name of the header field.
   * @return String the value of the named header field, or null if there is no
   *         such field in the header.
   */
  public String getHeaderField(String name);

  /**
   * Returns the value for the nth header field. It returns null if there are
   * fewer than n fields
   * 
   * @param num
   *          Integer num
   * @return String returns the value of the nth header field
   */
  public String getHeaderField(int num);

  /**
   * Disconnects connection
   */
  public void disconnect();

  /**
   * Returns an input stream that reads from the open connection
   * 
   * @return InputStream inputStream
   */
  public InputStream getInputStream() throws IOException;

  /**
   * Returns an Output stream that writes to the open connection
   * 
   * @return OutputStream - OutputStream
   */
  public OutputStream getOutputStream() throws IOException;

  /**
   * Initializes HttpsURLConnection
   * 
   * @param url
   *          url used to open HttpsURLConnection
   */
  public void init(URL url) throws IOException;

}
