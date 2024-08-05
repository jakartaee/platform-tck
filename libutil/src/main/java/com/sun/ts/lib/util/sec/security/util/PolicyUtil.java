/*
 * Copyright (c) 2004, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.lib.util.sec.security.util;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Arrays;

import com.sun.ts.lib.util.sec.net.www.ParseUtil;

/**
 * A utility class for getting a KeyStore instance from policy information. In
 * addition, a supporting getInputStream method.
 *
 */
public class PolicyUtil {

  // standard PKCS11 KeyStore type
  private static final String P11KEYSTORE = "PKCS11";

  // reserved word
  private static final String NONE = "NONE";

  /*
   * Fast path reading from file urls in order to avoid calling
   * FileURLConnection.connect() which can be quite slow the first time it is
   * called. We really should clean up FileURLConnection so that this is not a
   * problem but in the meantime this fix helps reduce start up time noticeably
   * for the new launcher. -- DAC
   */
  public static InputStream getInputStream(URL url) throws IOException {
    if ("file".equals(url.getProtocol())) {
      String path = url.getFile().replace('/', File.separatorChar);
      path = ParseUtil.decode(path);
      return new FileInputStream(path);
    } else {
      return url.openStream();
    }
  }

  /**
   * this is intended for use by policytool and the policy parser to instantiate
   * a KeyStore from the information in the GUI/policy file
   */
  public static KeyStore getKeyStore(URL policyUrl, // URL of policy file
      String keyStoreName, // input: keyStore URL
      String keyStoreType, // input: keyStore type
      String keyStoreProvider, // input: keyStore provider
      String storePassURL, // input: keyStore password
      Debug debug) throws KeyStoreException, MalformedURLException, IOException,
      NoSuchProviderException, NoSuchAlgorithmException,
      java.security.cert.CertificateException {

    if (keyStoreName == null) {
      throw new IllegalArgumentException("null KeyStore name");
    }

    char[] keyStorePassword = null;
    try {
      KeyStore ks;
      if (keyStoreType == null) {
        keyStoreType = KeyStore.getDefaultType();
      }

      if (P11KEYSTORE.equalsIgnoreCase(keyStoreType)
          && !NONE.equals(keyStoreName)) {
        throw new IllegalArgumentException("Invalid value (" + keyStoreName
            + ") for keystore URL.  If the keystore type is \"" + P11KEYSTORE
            + "\", the keystore url must be \"" + NONE + "\"");
      }

      if (keyStoreProvider != null) {
        ks = KeyStore.getInstance(keyStoreType, keyStoreProvider);
      } else {
        ks = KeyStore.getInstance(keyStoreType);
      }

      if (storePassURL != null) {
        URL passURL;
        try {
          passURL = new URL(storePassURL);
          // absolute URL
        } catch (MalformedURLException e) {
          // relative URL
          if (policyUrl == null) {
            throw e;
          }
          passURL = new URL(policyUrl, storePassURL);
        }

        if (debug != null) {
          debug.println("reading password" + passURL);
        }

        InputStream in = null;
        try {
          in = passURL.openStream();
          keyStorePassword = Password.readPassword(in);
        } finally {
          if (in != null) {
            in.close();
          }
        }
      }

      if (NONE.equals(keyStoreName)) {
        ks.load(null, keyStorePassword);
        return ks;
      } else {
        /*
         * location of keystore is specified as absolute URL in policy file, or
         * is relative to URL of policy file
         */
        URL keyStoreUrl = null;
        try {
          keyStoreUrl = new URL(keyStoreName);
          // absolute URL
        } catch (MalformedURLException e) {
          // relative URL
          if (policyUrl == null) {
            throw e;
          }
          keyStoreUrl = new URL(policyUrl, keyStoreName);
        }

        if (debug != null) {
          debug.println("reading keystore" + keyStoreUrl);
        }

        InputStream inStream = null;
        try {
          inStream = new BufferedInputStream(getInputStream(keyStoreUrl));
          ks.load(inStream, keyStorePassword);
        } finally {
          inStream.close();
        }
        return ks;
      }
    } finally {
      if (keyStorePassword != null) {
        Arrays.fill(keyStorePassword, ' ');
      }
    }
  }
}
