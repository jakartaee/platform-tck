/*
 * Copyright (c) 1996, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.lib.util.sec.security.provider;

import java.util.*;
import java.security.*;

/**
 * SunSecurity signer. Like SystemIdentity, it has a trust bit, which can be set
 * by SunSecurity classes, and a set of accessors for other classes in
 * sun.security.*.
 *
 * @author Benjamin Renaud
 */

public class SystemSigner extends Signer {

  /** use serialVersionUID from JDK 1.1. for interoperability */
  private static final long serialVersionUID = -2127743304301557711L;

  /* Is this signer trusted */
  private boolean trusted = false;

  /**
   * Construct a signer with a given name.
   */
  public SystemSigner(String name) {
    super(name);
  }

  /**
   * Construct a signer with a name and a scope.
   *
   * @param name
   *          the signer's name.
   *
   * @param scope
   *          the scope for this signer.
   */
  public SystemSigner(String name, IdentityScope scope)
      throws KeyManagementException {

    super(name, scope);
  }

  /* Set the trust status of this signer */
  void setTrusted(boolean trusted) {
    this.trusted = trusted;
  }

  /**
   * Returns true if this signer is trusted.
   */
  public boolean isTrusted() {
    return trusted;
  }

  /* friendly callback for set keys */
  void setSignerKeyPair(KeyPair pair)
      throws InvalidParameterException, KeyException {
    setKeyPair(pair);
  }

  /* friendly callback for getting private keys */
  PrivateKey getSignerPrivateKey() {
    return getPrivateKey();
  }

  void setSignerInfo(String s) {
    setInfo(s);
  }

  /**
   * Call back method into a protected method for package friends.
   */
  void addSignerCertificate(Certificate cert) throws KeyManagementException {
    addCertificate(cert);
  }

  void clearCertificates() throws KeyManagementException {
    Certificate[] certs = certificates();
    for (int i = 0; i < certs.length; i++) {
      removeCertificate(certs[i]);
    }
  }

  public String toString() {
    String trustedString = "not trusted";
    if (trusted) {
      trustedString = "trusted";
    }
    return super.toString() + "[" + trustedString + "]";
  }
}
