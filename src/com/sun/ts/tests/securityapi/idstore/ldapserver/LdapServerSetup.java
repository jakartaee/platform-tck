/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.securityapi.idstore.ldapserver;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldif.LDIFReader;

public class LdapServerSetup {

  public static void main(String[] args) {
    InMemoryDirectoryServer ldapServer = null;
    try {
      InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(
          "dc=net");
      config.setListenerConfigs(new InMemoryListenerConfig("LdapForSecurityAPI",
          null, 11389, null, null, null));
      ldapServer = new InMemoryDirectoryServer(config);

      String ldif = System.getProperty("ldif.file");
      System.out.println("ldif file is:" + ldif);
      ldapServer.importFromLDIF(true, new LDIFReader(ldif));
      ldapServer.startListening();
      System.out.println("Ldap server started...");
    } catch (Exception ex) {
      System.out.println("Exception: " + ex.getMessage());
      ex.printStackTrace();
      throw new IllegalStateException(ex);
    }
  }

}
