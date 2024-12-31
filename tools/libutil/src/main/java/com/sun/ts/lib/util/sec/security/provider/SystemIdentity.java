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

import java.io.Serializable;
import java.util.Enumeration;
import java.security.*;

/**
 * An identity with a very simple trust mechanism.
 *
 * @author Benjamin Renaud
 */

public class SystemIdentity extends Identity implements Serializable {

  /** use serialVersionUID from JDK 1.1. for interoperability */
  private static final long serialVersionUID = 9060648952088498478L;

  /* This should be changed to ACL */
  boolean trusted = false;

  /* Free form additional information about this identity. */
  private String info;

  public SystemIdentity(String name, IdentityScope scope)
      throws InvalidParameterException, KeyManagementException {
    super(name, scope);
  }

  /**
   * Is this identity trusted by sun.* facilities?
   */
  public boolean isTrusted() {
    return trusted;
  }

  /**
   * Set the trust status of this identity.
   */
  protected void setTrusted(boolean trusted) {
    this.trusted = trusted;
  }

  void setIdentityInfo(String info) {
    super.setInfo(info);
  }

  String getIndentityInfo() {
    return super.getInfo();
  }

  /**
   * Call back method into a protected method for package friends.
   */
  void setIdentityPublicKey(PublicKey key) throws KeyManagementException {
    setPublicKey(key);
  }

  /**
   * Call back method into a protected method for package friends.
   */
  void addIdentityCertificate(Certificate cert) throws KeyManagementException {
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
