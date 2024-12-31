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

package com.sun.ts.lib.implementation.sun.javaee;

import com.sun.ts.lib.porting.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

// Implementation Specific Classes
//import com.sun.net.ssl.HttpsURLConnection;
//import com.sun.net.ssl.HostnameVerifier;
//import com.sun.enterprise.security.KeyTool;

import com.sun.ts.lib.util.*;
import java.io.*;
import java.net.*;

/**
 * TSHttpsURLConnection provides the HTTPS specific featurs
 *
 */
public class SunRIHttpsURLConnection implements TSHttpsURLConnectionInterface {

  private HttpsURLConnection httpsURLConnection = null;

  private HostnameVerifier hostNameVerifier = null;

  private URL url = null;

  public void SunRIHttpsURLConnection() {
    // do nothing;
  }

  /**
   * This method is used to a) open a HttpsURLConnection from a given URL b) set
   * the HostNameVerifier to default MyHostNameVerifier
   *
   * @param url
   *          url used to open HttpsURLConnection
   */
  public void init(URL _url) throws IOException {
    url = _url;
    // KeyTool.initProvider();
    httpsURLConnection = (HttpsURLConnection) _url.openConnection();
    hostNameVerifier = new MyHostNameVerifier();
    httpsURLConnection.setHostnameVerifier(hostNameVerifier);
  }

  /**
   * Sets the value of the doInput field for this Connection
   *
   * @param doInput
   *          - the new value (the default is false)
   */
  public void setDoInput(boolean doInput) {
    httpsURLConnection.setDoInput(doInput);
  }

  /**
   * Sets the value of the doOutput field for this Connection
   *
   * @param doOutput
   *          - the new value (the default is false)
   */
  public void setDoOutput(boolean doOutput) {
    httpsURLConnection.setDoOutput(doOutput);
  }

  /**
   * Sets the value of the useCaches field for this Connection If the UseCaches
   * flag on the connection is true, the connection is allowed to use whatever
   * caches it can. If false, caches are to be ignored. The default value is set
   * to true
   *
   * @param usecaches
   *          - the new value (the default is true)
   */
  public void setUseCaches(boolean usecaches) {
    httpsURLConnection.setUseCaches(usecaches);
  }

  /**
   * Sets the general request property. If a property with the key already
   * exists, overwrite its value with the new value.
   *
   * @param key
   *          - the keyword by which the request is known
   * @param value
   *          - the value associated with it
   */
  public void setRequestProperty(String key, String value) {
    httpsURLConnection.setRequestProperty(key, value);
  }

  /**
   * Returns the value of the named header field. If called on a connection that
   * sets the same header multiple times only the last value is returned.
   * 
   * @param name
   *          - the name of the header field.
   * @return String - the value of the named header field, or null if there is
   *         no such field in the header.
   */
  public String getHeaderField(String name) {
    return httpsURLConnection.getHeaderField(name);
  }

  /**
   * Returns the value for the nth header field. It returns null if there are
   * fewer than n fields
   *
   * @ param num - Integer num @ return String - returns the value of the nth
   * header field
   */
  public String getHeaderField(int num) {
    return httpsURLConnection.getHeaderField(num);
  }

  /**
   * Disconnect connection
   */
  public void disconnect() {
    httpsURLConnection.disconnect();
  }

  /**
   * Returns an input stream that reads from the open connection
   *
   * @return InputStream - inputStream
   */
  public InputStream getInputStream() throws IOException {
    return httpsURLConnection.getInputStream();
  }

  /**
   * Returns an Output stream that writes to the open connection
   *
   * @return OutputStream - outputStream
   */
  public OutputStream getOutputStream() throws IOException {
    return httpsURLConnection.getOutputStream();
  }

  /**
   * This class is used to set HostNameVerifier
   * 
   * HostnameVerifier provides a callback mechanism so that implementers of this
   * interface can supply a policy for handling the case where the host to
   * connect to and the server name from the certificate mismatch
   */
  public class MyHostNameVerifier implements HostnameVerifier {

    /**
     * Verify that the hostname from the URL is an acceptable match with the
     * value from the common name entry in the server certificate's
     * distinguished name. (i.e the httpsHostName == name entry in the server
     * certificate)
     *
     * For all practical reasons a HttpsServer can have a certificate with any
     * name on it. Because of this reason this implementation doesn't verify the
     * httpsHoseName with the name entry in server ceritificate .
     * 
     * @param urlhostname
     *          - the https hostname in the url
     * @param certHostName
     *          - the name entry in the server certificate
     * @return boolean - this implementation returns true always.
     */
    public boolean verify(String urlhostname, String certHostName) {

      return true;
    }

    public boolean verify(String urlhostName, SSLSession sslSession) {
      return true;
    }

  }
}
