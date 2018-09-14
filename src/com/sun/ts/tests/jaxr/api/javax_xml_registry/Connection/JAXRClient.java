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
 * @(#)JAXRClient.java	1.22 03/05/16
 */

/*
 * @(#)JAXRClient.java	1.12 02/04/25
 */

package com.sun.ts.tests.jaxr.api.javax_xml_registry.Connection;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxr.common.JAXRCommonClient;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import com.sun.javatest.Status;

import javax.xml.registry.*;
import javax.xml.registry.infomodel.*;
import java.net.PasswordAuthentication;
import javax.security.auth.x500.X500PrivateCredential;

public class JAXRClient extends JAXRCommonClient {
  public static void main(String[] args) {
    JAXRClient theTests = new JAXRClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */
  /*
   * @class.setup_props: jaxrPassword; jaxrUser; jaxrPassword2; jaxrUser2;
   * registryURL; queryManagerURL; providerCapability; authenticationMethod;
   * jaxrConnectionFactoryLookup; jaxrSecurityCredentialType; jaxrJNDIResource;
   * jaxrAlias; jaxrAlias2; jaxrAliasPassword; jaxrAlias2Password;
   */
  public void setup(String[] args, Properties p) throws Fault {

    try {
      super.setup(args, p);

    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    try {
      logMsg("in cleanup");
      if (conn != null) {
        logTrace("Cleanup is closing the connection");
        conn.close();
      }

    } catch (Exception e) {
      TestUtil.logErr("Cleanup error: " + e.toString());
      TestUtil.printStackTrace(e);
    }

  }

  /*
   * @testName: ConnectionisSynchronousDefaultTest
   *
   * @assertion_ids: JAXR:JAVADOC:240;
   *
   * @assertion: Return true if client uses synchronous communication with JAXR
   * provider. Note that a JAXR provider must support both modes of
   * communication, while the client can choose which mode it wants to use.
   * Default is a return value of true (synchronous communication). JAXR javadoc
   *
   * @test_Strategy: Create a connection object. Test that the default for
   * isSynchonous is true.
   *
   */
  public void ConnectionisSynchronousDefaultTest() throws Fault {
    // get the Connection object conn from the super class
    String testName = "ConnectionisSynchronousDefaultTest";
    boolean pass = true;
    try {
      // Check default for synchronous
      if (!(conn.isSynchronous())) {
        logMsg("Error isSynchronous() should return true, it returned: "
            + conn.isSynchronous());
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "failed", e);
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: ConnectionsetSynchronousTest1
   *
   * @assertion_ids: JAXR:JAVADOC:242;
   *
   * @assertion: The JAXR client uses the setSynchronous method on a Connection
   * to dynamically alter whether it receives responses and exceptions from the
   * JAXR provider synchronously or not. The JAXR provider must use this
   * communication preference when processing requests on behalf of that client.
   *
   * @test_Strategy: Create a connection object. Use setSynchronous() to to set
   * synchronous to true. Verify that isSynchronous returns true.
   *
   */
  public void ConnectionsetSynchronousTest1() throws Fault {
    // get the Connection object conn from the super class
    String testName = "ConnectionsetSynchronousTest1";
    boolean pass = true;
    try {
      conn.setSynchronous(true);
      if (!(conn.isSynchronous())) {
        logMsg(
            "Error synchronous was set to true, its: " + conn.isSynchronous());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed", e);
    }
    if (!pass)
      throw new Fault(testName + " failed");
  }

  /*
   * @testName: ConnectionCloseTest
   *
   * @assertion_ids: JAXR:JAVADOC:236;JAXR:SPEC:174;
   *
   * @assertion: Since a provider typically allocates significant resources
   * outside the JVM on behalf of a Connection, clients should close them when
   * they are not needed. This interface is required to be implemented by all
   * JAXR Providers. Capability Level: 0
   *
   * @test_Strategy: Create a connection object. Use close() to to close the
   * connection. Verify that the Connection is closed by checking isClosed
   * method.
   *
   */
  public void ConnectionCloseTest() throws Fault {
    // get the Connection object conn from the super class
    String testName = "ConnectionCloseTest";
    boolean pass = true;
    try {

      conn.close();
      try {
        if (!(conn.isClosed())) {
          logMsg("Connection.close() has been called, but it is still open!");
          pass = false;
        }
      } catch (Exception ee) {
        TestUtil.printStackTrace(ee);
        logMsg("Connection is closed");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed", e);
    }

    if (!pass)
      throw new Fault(testName + " failed");
  }

  /*
   * @testName: ConnectionsetCredentialsTest
   *
   * @assertion_ids: JAXR:JAVADOC:244; JAXR:JAVADOC:246;
   *
   * @assertion: Sets the Credentials associated with this client. The
   * credentials is used to authenticate the client with the JAXR provider. A
   * JAXR client may dynamically change its identity by changing the credentials
   * associated with it.
   *
   * This interface is required to be implemented by all JAXR Providers.
   * Capability Level: 0
   *
   * @test_Strategy: Create a connection object. Create a PasswordAuthentication
   * object set to username and password from the ts.jte file. Use this to
   * create a credential set. Call setCredentials with this. Call getCredentials
   * to verify the new credentials have been set
   *
   */
  public void ConnectionsetCredentialsTest() throws Fault {
    // get the Connection object conn from the super class
    String testName = "ConnectionsetCredentialsTest";
    debug.clear();
    boolean pass = false;
    Set credentials = null;
    try {
      switch (jaxrSecurityCredentialType) {
      case USE_USERNAME_PASSWORD:
        credentials = new HashSet();
        PasswordAuthentication passwdAuth = new PasswordAuthentication(jaxrUser,
            jaxrPassword.toCharArray());
        credentials.add(passwdAuth);
        conn.setCredentials(credentials);
        for (Iterator i = credentials.iterator(); i.hasNext();) {
          Object credential = i.next();
          if (credential instanceof PasswordAuthentication) {
            // good!
            PasswordAuthentication pa = (PasswordAuthentication) credential;
            debug.add(
                "Setting password to: " + new String(pa.getPassword()) + "\n");
            debug.add("Setting username to:  " + pa.getUserName() + "\n");
          }
        }

        credentials = conn.getCredentials();
        for (Iterator i = credentials.iterator(); i.hasNext();) {
          Object credential = i.next();
          if (credential instanceof PasswordAuthentication) {
            // good!
            PasswordAuthentication pa = (PasswordAuthentication) credential;
            debug.add(
                "Password returned: " + new String(pa.getPassword()) + "\n");
            debug.add("Username returned:  " + pa.getUserName() + "\n");
            if ((pa.getUserName().equals(jaxrUser))
                && (new String(pa.getPassword()).equals(jaxrPassword))) {
              pass = true;
            }
          } else {
            debug.add("Error: returned unexpected object!\n");
            debug.add("Object is: " + credential.toString() + "\n");
          }
        }
        break;

      case USE_DIGITAL_CERTIFICATES:
        credentials = super.getDigitalCertificateCredentials(jaxrAlias,
            jaxrAliasPassword);
        conn.setCredentials(credentials);

        for (Iterator i = credentials.iterator(); i.hasNext();) {
          Object credential = i.next();
          if (credential instanceof javax.security.auth.x500.X500PrivateCredential) {
            // good!
            X500PrivateCredential cred = (javax.security.auth.x500.X500PrivateCredential) credential;
            debug.add("alias is: " + cred.getAlias());
          }
        }
        credentials = conn.getCredentials();
        for (Iterator i = credentials.iterator(); i.hasNext();) {
          Object credential = i.next();
          if (credential instanceof javax.security.auth.x500.X500PrivateCredential) {
            // good!
            X500PrivateCredential cred = (javax.security.auth.x500.X500PrivateCredential) credential;
            debug.add("alias returned: " + cred.getAlias());
            if (cred.getAlias().equals(jaxrAlias)) {
              pass = true;
            }
          } else {
            debug.add("Error: returned unexpected object!\n");
            debug.add("Object is: " + credential.toString() + "\n");
          }
        }
        break;

      default:
        throw new Fault("failed: jaxrSecurityCredentialType is invalid");
      } // end of switch

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed", e);
    } finally {
      // print out messages
      TestUtil.logTrace(debug.toString());
      if (!pass)
        throw new Fault(testName + " failed");
    }
  }

  /*
   * @testName: ConnectiongetRegistryServiceTest
   *
   * @assertion_ids: JAXR:JAVADOC:234;JAXR:SPEC:14;
   *
   * @assertion: getRegistryService Gets the RegistryService interface
   * associated with the Connection. This interface is required to be
   * implemented by all JAXR Providers. Capability Level: 0
   *
   * @test_Strategy: Create a connection object. Call getRegistryService and
   * ensure that a getRegistryService object was returned.
   *
   */
  public void ConnectiongetRegistryServiceTest() throws Fault {
    // get the Connection object conn from the super class
    String testName = "ConnectiongetRegistryServiceTest";
    boolean pass = true;
    RegistryService rs = null;
    try {
      rs = conn.getRegistryService();
      if (!(rs instanceof RegistryService)) {
        logMsg("Error we did not get a RegistryService object");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "failed", e);
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: ConnectionisClosedTest
   *
   * @assertion_ids: JAXR:JAVADOC:238;
   *
   * @assertion: isClosed() Return true if this Connection has been closed.
   *
   * @test_Strategy: Create a connection object. Call isClosed and ensure that
   * it returns true.
   *
   */
  public void ConnectionisClosedTest() throws Fault {
    // get the Connection object conn from the super class
    String testName = "ConnectionisClosedTest";
    boolean pass = true;
    debug.clear();
    try {
      if (conn.isClosed()) {
        debug.add("Error connection is open, yet isClosed method returns");
        debug.add(" " + conn.isClosed());
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "failed", e);
    } finally {
      // print out messages
      TestUtil.logTrace(debug.toString());
    }

    if (!pass)
      throw new Fault(testName + "failed ");
  }

  // end of test class
}
