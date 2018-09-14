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
 * @(#)JAXRClient.java	1.17 03/05/16
 */

/*
 * @(#)JAXRClient.java	1.7 02/04/05
 */

package com.sun.ts.tests.jaxr.api.javax_xml_registry.ConnectionFactory;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxr.common.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import com.sun.javatest.Status;

import javax.xml.registry.*;
import javax.xml.registry.infomodel.*;

import java.security.cert.*;
import java.security.PrivateKey;
import javax.security.auth.*;
import java.security.KeyStore;

public class JAXRClient extends JAXRCommonClient // ServiceEETest
{

  private Properties props = null;

  // ==
  protected javax.xml.registry.Connection conn = null;

  protected javax.xml.registry.ConnectionFactory factory = null;

  public String supr = "JAXRCommonClient";

  // properties from the ts.jte file......
  protected static final String JAXRPASSWORD = "jaxrPassword";

  protected static final String JAXRUSER = "jaxrUser";

  protected static final String JAXRPASSWORD2 = "jaxrPassword2";

  protected static final String JAXRUSER2 = "jaxrUser2";

  protected static final String QUERYMANAGERURL = "queryManagerURL";

  protected static final String REGISTRYURL = "registryURL";

  protected static final String JAXRALIAS = "jaxrAlias";

  protected static final String JAXRALIAS2 = "jaxrAlias2";

  protected static final String JAXRALIAS_PASSWORD = "jaxrAliasPassword";

  protected static final String JAXRALIAS2_PASSWORD = "jaxrAlias2Password";

  protected static final String JAXR_CONNECTIONFACTORY_LOOKUP = "jaxrConnectionFactoryLookup";

  protected static final int USE_JNDI = 0;

  protected static final int USE_NEWINSTANCE = 1;

  protected static final String JAXR_SECURITY_TYPE = "jaxrSecurityCredentialType";

  protected static final int USE_USERNAME_PASSWORD = 0;

  protected static final int USE_DIGITAL_CERTIFICATES = 1;

  int jaxrConnectionFactoryLookup;

  int jaxrSecurityCredentialType;

  protected String jaxrPassword;

  protected String jaxrUser;

  protected String jaxrPassword2;

  protected String jaxrUser2;

  protected String jaxrAlias;

  protected String jaxrAlias2;

  protected String registryURL;

  protected String queryManagerURL;

  protected String jaxrLookup;

  protected Collection tracer = null;

  protected RegistryService rs = null;

  protected BusinessLifeCycleManager blm = null;

  protected CapabilityProfile cp = null;

  protected int capabilityLevel;

  // ================================================
  protected PasswordAuthentication passwdAuth = null;

  protected Collection debug = null;
  // ==

  // protected String registryURL;
  // protected String queryManagerURL;

  public static void main(String[] args) {
    JAXRClient theTests = new JAXRClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */
  /*
   * @class.setup_props: jaxrPassword; jaxrUser; jaxrPassword2; jaxrUser2;
   * registryURL; queryManagerURL; authenticationMethod;
   * jaxrSecurityCredentialType; jaxrJNDIResource; jaxrAlias; jaxrAlias2;
   * jaxrAliasPassword; jaxrAlias2Password; jaxrConnectionFactoryLookup;
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      jaxrSecurityCredentialType = Integer
          .parseInt(p.getProperty(JAXR_SECURITY_TYPE));
      jaxrConnectionFactoryLookup = Integer
          .parseInt(p.getProperty(JAXR_CONNECTIONFACTORY_LOOKUP));

      // if using username password combination.
      jaxrUser = p.getProperty(JAXRUSER);
      jaxrPassword = p.getProperty(JAXRPASSWORD);

      // some tests, for example extramural associations, will require a second
      // user
      jaxrUser2 = p.getProperty(JAXRUSER2);
      jaxrPassword2 = p.getProperty(JAXRPASSWORD2);

      // if using digital certificates for authorization need user alias
      jaxrAlias = p.getProperty(JAXRALIAS);
      jaxrAlias2 = p.getProperty(JAXRALIAS2);
      jaxrAliasPassword = p.getProperty(JAXRALIAS_PASSWORD);
      jaxrAlias2Password = p.getProperty(JAXRALIAS2_PASSWORD);

      queryManagerURL = p.getProperty("queryManagerURL");
      registryURL = p.getProperty("registryURL");
      authenticationMethod = p.getProperty("authenticationMethod");
      jaxrLookup = p.getProperty("jaxrJNDIResource");

    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: connectionFactory_createConnectionFactoryTest
   *
   * @assertion_ids: JAXR:JAVADOC:54;
   *
   * @test_Strategy: The newInstance method returns a JAXR ConnectionFactory. If
   * the javax.xml.registry.ConnectionFactoryClass system property is not set,
   * the JAXR provider must return a default ConnectionFactory instance. Verify
   * that newInstance will return a ConnectionFactory.
   *
   */
  public void connectionFactory_createConnectionFactoryTest() throws Fault {
    boolean pass = false;
    Connection conn = null;
    ConnectionFactory factory = null;
    try {
      Properties props = new Properties();
      props.setProperty("javax.xml.registry.queryManagerURL", queryManagerURL);
      props.setProperty("javax.xml.registry.lifeCycleManagerURL", registryURL);
      props.setProperty("javax.xml.registry.security.authenticationMethod",
          authenticationMethod);

      factory = ConnectionFactory.newInstance();
      if (factory == null)
        throw new Fault("ConnectionFactory.newInstance returned null");
      if (factory instanceof ConnectionFactory) {
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("createConnectionFactoryTest failed", e);
    }
    if (!pass)
      throw new Fault("createConnectionFactoryTest failed");
  }

  /*
   * @testName: connectionFactory_createConnectionTest
   *
   * @assertion_ids: JAXR:JAVADOC:50;
   *
   * @test_Strategy: Invoke the createConnection factory method and verify that
   * this will return a connection.
   *
   */
  public void connectionFactory_createConnectionTest() throws Fault {
    boolean pass = false;
    Connection conn = null;
    ConnectionFactory factory = null;
    try {
      Properties props = new Properties();
      props.setProperty("javax.xml.registry.queryManagerURL", queryManagerURL);
      props.setProperty("javax.xml.registry.lifeCycleManagerURL", registryURL);
      props.setProperty("javax.xml.registry.security.authenticationMethod",
          authenticationMethod);

      factory = ConnectionFactory.newInstance();

      factory.setProperties(props);
      conn = factory.createConnection();
      if (conn == null)
        throw new Fault("createConnection returned null");
      if (conn instanceof Connection)
        pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("createConnectionTest failed", e);
    } finally {
      if (conn != null)
        try {
          conn.close();
        } catch (Exception e) {
          TestUtil.logErr("Caught exception: " + e.getMessage());
          TestUtil.printStackTrace(e);
          throw new Fault("Error in close connection in cleanup", e);
        }
    }
    if (!pass)
      throw new Fault("createConnectionTest failed");
  }

  /*
   * @testName: connectionFactory_propertiesTest
   *
   * @assertion_ids: JAXR:JAVADOC:46; JAXR:JAVADOC:48; JAXR:SPEC:147;
   * JAXR:SPEC:148;JAXR:SPEC:144;
   * 
   * @test_Strategy: Create a ConnectionFactory. Set the properties for
   * queryManagerURL and lifeCycleManagerURL. Verify with getProperties
   *
   */
  public void connectionFactory_propertiesTest() throws Fault {
    Connection conn = null;
    ConnectionFactory factory = null;
    Properties retProperties = null;
    Properties props = null;
    String query = "javax.xml.registry.queryManagerURL";
    String lcm = "javax.xml.registry.lifeCycleManagerURL";
    try {
      props = new Properties();
      props.setProperty(query, queryManagerURL);
      props.setProperty(lcm, registryURL);

      factory = ConnectionFactory.newInstance();
      factory.setProperties(props);

      retProperties = factory.getProperties();
      TestUtil.logTrace("getProperties returned: queryManagerURL = "
          + retProperties.getProperty(query) + " and \n lifeCycleManagerURL = "
          + retProperties.getProperty(lcm));

      if (!(retProperties.getProperty(query).equals(queryManagerURL)
          && retProperties.getProperty(lcm).equals(registryURL))) {
        throw new Fault("unexpected results from getProperties");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("connectionFactory_propertiesTest failed", e);
    }
  }

  /*
   * @testName: connectionFactory_createConnectionInvalidTest
   *
   * @assertion_ids: JAXR:SPEC:154;
   *
   * @test_Strategy: An InvalidRequest exception must be thrown if the
   * queryManagerURL is not defined when calling createConnection. Call it with
   * out defining it.
   *
   */
  public void connectionFactory_createConnectionInvalidTest() throws Fault {
    boolean pass = false;
    Connection conn = null;
    ConnectionFactory factory = null;
    try {
      Properties props = new Properties();
      props.setProperty("javax.xml.registry.lifeCycleManagerURL", registryURL);
      props.setProperty("javax.xml.registry.security.authenticationMethod",
          authenticationMethod);

      factory = ConnectionFactory.newInstance();

      factory.setProperties(props);
      conn = factory.createConnection();
    } catch (InvalidRequestException ir) {
      TestUtil.printStackTrace(ir);
      pass = true;
      TestUtil.logTrace("InvalidRequestException was caught as expected!");
    } catch (Exception e) {
      TestUtil.logTrace(
          "Error: InvalidRequestException was NOT caught as expected!");

      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("createConnectionTest failed", e);
    } finally {
      if (conn != null)
        try {
          conn.close();
        } catch (Exception e) {
          TestUtil.logErr("Caught exception: " + e.getMessage());
          TestUtil.printStackTrace(e);
          throw new Fault("Error in close connection in cleanup", e);
        }
    }
    if (!pass)
      throw new Fault("createConnectionTest failed");
  }

  /*
   * @testName: connectionFactory_lifeCycleManagerPropertiesTest
   *
   * @assertion_ids: JAXR:SPEC:149;
   *
   * @test_Strategy: If lifeCycleManagerURL is not specified, then it must
   * default to the same setting as queryManagerURL. Create a ConnectionFactory.
   * Set the properties for queryManagerURL. Don't set lifeCycleManagerURL.
   * Verify lifeCycleManagerURL by publishing.
   * 
   *
   */
  public void connectionFactory_lifeCycleManagerPropertiesTest() throws Fault {
    Connection conn = null;
    ConnectionFactory factory = null;
    Properties retProperties = null;
    Properties props = null;
    String query = "javax.xml.registry.queryManagerURL";
    String lcm = "javax.xml.registry.lifeCycleManagerURL";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String testName = "connectionFactory_lifeCycleManagerPropertiesTest";

    // Check if the urls for the registry we are testing with are the same
    // for publish and query, if not leave
    if (queryManagerURL.equals(registryURL)) {
      try {
        props = new Properties();

        props.setProperty(query, queryManagerURL);
        props.setProperty("javax.xml.registry.security.authenticationMethod",
            authenticationMethod);

        switch (jaxrConnectionFactoryLookup) {
        case USE_JNDI:
          // need to do a lookup
          // # JNDI name JAXR ConnectionFactory
          // JAXRlookup = java:comp/env/eis/JAXR
          TSNamingContext nctx = null;
          nctx = new TSNamingContext();
          factory = (ConnectionFactory) nctx.lookup(jaxrLookup);
          break;
        case USE_NEWINSTANCE:
          // ==
          // use the alternate method the get the JAXR ConnectionFactory
          // newInstance static method on the abstract class ConnectionFactory
          // the JAXR provider must return a default ConnectionFactory instance.
          factory = ConnectionFactory.newInstance();
          break;
        default:
          // Error condition to handle here
          throw new Fault(
              "setup failed: jaxrConnectionFactoryLookup is invalid");
        } // end of switch

        factory.setProperties(props);
        conn = factory.createConnection();
        if (!(conn instanceof Connection))
          logMsg(
              "From JAXRCommonClient: Error did not get a connection object");

        rs = conn.getRegistryService();

        // ==
        // Check the authorization...
        // ==
        Set credentials = null;
        switch (jaxrSecurityCredentialType) {
        case USE_USERNAME_PASSWORD:
          credentials = getUsernamePasswordCredentials();
          break;
        case USE_DIGITAL_CERTIFICATES:
          credentials = getDigitalCertificateCredentials(jaxrAlias,
              jaxrAliasPassword);
          break;
        default:
          throw new Fault(
              "setup failed: jaxrSecurityCredentialType is invalid");
        } // end of switch

        conn.setCredentials(credentials);
        try {
          blm = rs.getBusinessLifeCycleManager();
        } catch (JAXRException je) {
          TestUtil.printStackTrace(je);
        }

        // Create an organization.
        InternationalString iorgName = blm.createInternationalString(orgName);
        Organization org = blm.createOrganization(iorgName);

        // Publish an organization and verify
        Collection orgs = new ArrayList();
        orgs.add(org);
        BulkResponse br = blm.saveOrganizations(orgs); // publish to registry
        if (br.getExceptions() != null) {
          TestUtil.logTrace("== Exception returned from BulkResponse!!\n");
          Collection ex = br.getExceptions();
          Iterator iter = ex.iterator();
          //
          while (iter.hasNext()) {
            JAXRException je = (JAXRException) iter.next();
            TestUtil.logTrace("== Detail Message for the JAXRException object: "
                + je.getMessage() + "\n");
          }
          throw new Fault(testName
              + " due to saveOrganizations errors, test did not complete!");
        }

        // If org saved get key
        Collection orgKeys = br.getCollection();
        // and remove it
        br = blm.deleteOrganizations(orgKeys);
        if (br.getExceptions() != null) {
          throw new Fault(testName
              + " due to deleteOrganizations errors, test cleanup not done!");
        }

      } catch (Exception e) {
        TestUtil.logErr("Caught exception: " + e.getMessage());
        TestUtil.printStackTrace(e);
        throw new Fault("connectionFactory_propertiesTest failed", e);
      }
    } // end of if queryManagerURL = lifeCycleManagerURL

  }

  /*
   * @testName: connectionFactory_propertiesMaxRowsTest
   *
   * @assertion_ids: JAXR:SPEC:152;
   * 
   * @test_Strategy: maxRows sets the maximum number of rows to be returned.
   * UDDI specific. set the maxrows property. Verify with getProperties
   *
   */
  public void connectionFactory_propertiesMaxRowsTest() throws Fault {
    Connection conn = null;
    ConnectionFactory factory = null;
    Properties retProperties = null;
    Properties props = null;
    String maxRows = "100";
    String maxRowsProp = "javax.xml.registry.uddi.maxRows";

    try {
      props = new Properties();
      props.setProperty(maxRowsProp, maxRows);

      factory = ConnectionFactory.newInstance();
      factory.setProperties(props);
      TestUtil.logTrace("max rows set to " + maxRows);
      retProperties = factory.getProperties();
      TestUtil.logTrace("getProperties returned: max Rows  = "
          + retProperties.getProperty(maxRowsProp));

      if (!(retProperties.getProperty(maxRowsProp).equals(maxRows))) {
        throw new Fault("max rows returned invalid");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("connectionFactory_propertiesTest failed", e);
    }
  }

  // ==========================

  /**
   * This method getUsernamePasswordCredentials is used to get the Credentials
   * for digital certificate and private key
   *
   * @param useralias
   *          - each entry in a keystore is identified by the alias string
   */
  public Set getUsernamePasswordCredentials() {
    Set credentials = new HashSet();
    passwdAuth = new PasswordAuthentication(jaxrUser,
        jaxrPassword.toCharArray());
    credentials.add(passwdAuth);
    return credentials;
  }
}
