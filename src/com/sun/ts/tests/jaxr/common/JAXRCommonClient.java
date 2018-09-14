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
 * @(#)JAXRCommonClient.java	1.16 03/05/16
 */

/*
 * @(#)JAXRCommonClient.java	1.9 02/04/05
 */

package com.sun.ts.tests.jaxr.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

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

public class JAXRCommonClient extends ServiceEETest implements Serializable

{
  protected Properties props = null;

  protected javax.xml.registry.Connection conn = null;

  protected javax.xml.registry.ConnectionFactory factory = null;

  public String supr = "JAXRCommonClient";

  // properties from the ts.jte file......

  protected static final String WEBSERVERPORT = "webServerPort";

  protected static final String WEBSERVERHOST = "webServerHost";

  protected static final String JAXRPASSWORD = "jaxrPassword";

  protected static final String JAXRUSER = "jaxrUser";

  protected static final String JAXRPASSWORD2 = "jaxrPassword2";

  protected static final String JAXRUSER2 = "jaxrUser2";

  protected static final String QUERYMANAGERURL = "queryManagerURL";

  protected static final String REGISTRYURL = "registryURL";

  protected static final String AUTHENTICATIONMETHOD = "authenticationMethod";

  protected static final String JAXRALIAS = "jaxrAlias";

  protected static final String JAXRALIAS2 = "jaxrAlias2";

  protected static final String JAXRALIAS_PASSWORD = "jaxrAliasPassword";

  protected static final String JAXRALIAS2_PASSWORD = "jaxrAlias2Password";

  protected static final String JAXR_WEBCONTEXT = "jaxrWebContext";

  protected static final String JAXR_CONNECTIONFACTORY_LOOKUP = "jaxrConnectionFactoryLookup";

  protected static final int USE_JNDI = 0;

  protected static final int USE_NEWINSTANCE = 1;

  protected static final String JAXR_SECURITY_TYPE = "jaxrSecurityCredentialType";

  protected static final int USE_USERNAME_PASSWORD = 0;

  protected static final int USE_DIGITAL_CERTIFICATES = 1;

  protected static final String TS_HOME = "ts_home";

  protected String tshome;

  protected int jaxrConnectionFactoryLookup;

  protected int jaxrSecurityCredentialType;

  protected String webServerHost;

  protected String webServerPort;

  protected String jaxrPassword;

  protected String jaxrUser;

  protected String jaxrPassword2;

  protected String jaxrUser2;

  protected String jaxrAlias;

  protected String jaxrAliasPassword;

  protected String jaxrAlias2;

  protected String jaxrAlias2Password;

  protected String jaxrWebContext;

  protected String registryURL;

  protected String queryManagerURL;

  protected String authenticationMethod;

  protected String baseuri;

  protected RegistryService rs = null;

  protected BusinessLifeCycleManager blm = null;

  protected CapabilityProfile cp = null;

  protected int capabilityLevel;

  // ================================================
  protected PasswordAuthentication passwdAuth = null;

  protected Collection debug = null;
  /* Test setup */

  /*
   * @class.setup_props: jaxrPassword; jaxrUser; jaxrPassword2; jaxrUser2;
   * registryURL; queryManagerURL; authenticationMethod;
   * jaxrConnectionFactoryLookup; jaxrSecurityCredentialType; jaxrJNDIResource;
   * jaxrAlias; jaxrAliasPassword; jaxrAlias2; jaxrAliasPassword2;
   * webServerHost; webServerPort; jaxrWebContext; ts_home;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    debug = null;
    try {
      debug = new ArrayList();
      tshome = p.getProperty(TS_HOME);

      jaxrSecurityCredentialType = Integer
          .parseInt(p.getProperty(JAXR_SECURITY_TYPE));
      jaxrConnectionFactoryLookup = Integer
          .parseInt(p.getProperty(JAXR_CONNECTIONFACTORY_LOOKUP));

      // if using username password combination.
      jaxrUser = p.getProperty(JAXRUSER);
      jaxrPassword = p.getProperty(JAXRPASSWORD);
      webServerPort = p.getProperty(WEBSERVERPORT);
      webServerHost = p.getProperty(WEBSERVERHOST);

      // some tests, for example extramural associations, will require a second
      // user
      jaxrUser2 = p.getProperty(JAXRUSER2);
      jaxrPassword2 = p.getProperty(JAXRPASSWORD2);

      // if using digital certificates for authorization need user alias
      jaxrAlias = p.getProperty(JAXRALIAS);
      jaxrAlias2 = p.getProperty(JAXRALIAS2);
      // html test pages for external link tests
      jaxrWebContext = p.getProperty(JAXR_WEBCONTEXT);

      baseuri = "http://" + webServerHost + ":" + webServerPort + "/"
          + jaxrWebContext + "/";

      jaxrAliasPassword = p.getProperty(JAXRALIAS_PASSWORD);
      jaxrAlias2Password = p.getProperty(JAXRALIAS2_PASSWORD);
      // must have URL for query
      props.setProperty("javax.xml.registry.queryManagerURL",
          p.getProperty(QUERYMANAGERURL));
      // must have URL for publishing
      props.setProperty("javax.xml.registry.lifeCycleManagerURL",
          p.getProperty(REGISTRYURL));

      props.setProperty("javax.xml.registry.security.authenticationMethod",
          p.getProperty(AUTHENTICATIONMETHOD));

      // ==
      // Check the JAXR ConnectionFactory property... determine if using
      // newInstance or JNDI lookup.
      // ==

      switch (jaxrConnectionFactoryLookup) {
      case USE_JNDI:
        // need to do a lookup
        // # JNDI name JAXR ConnectionFactory
        String jaxrLookup = p.getProperty("jaxrJNDIResource");
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
        throw new Fault("setup failed: jaxrConnectionFactoryLookup is invalid");
      } // end of switch

      factory.setProperties(props);
      conn = factory.createConnection();
      if (!(conn instanceof Connection))
        logMsg("From JAXRCommonClient: Error did not get a connection object");

      rs = conn.getRegistryService();
      if (!(rs instanceof RegistryService))
        logMsg("From JAXRCommonClient: Error did not get a rs object");
      cp = rs.getCapabilityProfile();
      capabilityLevel = cp.getCapabilityLevel();

      // ==
      // Check the authorization...
      // ==
      Set credentials = null;
      switch (jaxrSecurityCredentialType) {
      case USE_USERNAME_PASSWORD:
        credentials = getUsernamePasswordCredentials();
        break;
      case USE_DIGITAL_CERTIFICATES:
        // ==
        credentials = getDigitalCertificateCredentials(jaxrAlias,
            jaxrAliasPassword);
        break;
      default:
        throw new Fault("setup failed: jaxrSecurityCredentialType is invalid");
      } // end of switch
      conn.setCredentials(credentials);
      try {

        blm = rs.getBusinessLifeCycleManager();
      } catch (JAXRException je) {
        TestUtil.logMsg("JAXRException");
        TestUtil.printStackTrace(je);
      }

    } catch (Exception e) {
      TestUtil.logMsg("exception getMessage: " + e.getMessage());
      TestUtil.logMsg("exception getCause: " + e.getCause());
      e.printStackTrace();
      TestUtil.logErr(" exception" + e);
      TestUtil.printStackTrace(e);
      throw new Fault("setup failed:", e);
    } finally {
      // print out messages
      TestUtil.logTrace(debug.toString());
      debug.clear();
    }
  }

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

  /**
   * This method getDigitalCertificateCredentials is used to get the Credentials
   * for digital certificate and private key
   *
   * @param useralias
   *          - each entry in a keystore is identified by the alias string
   */
  public Set getDigitalCertificateCredentials(String useralias,
      String aliasPassword) throws Fault {
    String keystoreType = "JKS"; // type of keystore implementation provided by
                                 // SUN.
    Set credentials = new HashSet();

    try {

      // Get the keystore file - this is passed in on the command line - see
      // ts.jte
      String keystoreFile = System.getProperty("javax.net.ssl.keyStore");

      // Get the keyStorePassword - this is passed in on the command line - see
      // ts.jte
      String keystorePassword = System
          .getProperty("javax.net.ssl.keyStorePassword");

      // get keystore password
      char[] keystorePasswordBytes = null;
      keystorePasswordBytes = keystorePassword.toCharArray();

      // get keystore instance
      KeyStore ks = KeyStore.getInstance(keystoreType);

      TestUtil.logMsg(" ===============> userAlias=" + useralias
          + " aliasPassword=" + aliasPassword + " keyStorePassword="
          + keystorePassword + "keystoreFile=" + keystoreFile);

      // load keystore
      ks.load(new FileInputStream(keystoreFile), keystorePasswordBytes);

      // useralias tells me which certificate to get from the keystore
      Certificate certificate = ks.getCertificate(useralias);

      // I need to get the private key.
      java.security.PrivateKey key = (java.security.PrivateKey) ks
          .getKey(useralias, aliasPassword.toCharArray());

      // ok now I need an X500PrivateCredential
      X509Certificate cert = (X509Certificate) certificate;

      javax.security.auth.x500.X500PrivateCredential privateCredential = null;

      privateCredential = new javax.security.auth.x500.X500PrivateCredential(
          cert, key, useralias);
      credentials.add(privateCredential);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault(
          "setup failed: Unable to create credential for digital certificate",
          e);
    }
    return credentials;
  }

  public void cleanup() throws Fault {
    try {
      logMsg("JAXRCommonClient.cleanup:Closing connection");
      if (conn != null) {
        conn.close();
      }
    } catch (Exception e) {
      TestUtil.logErr("Cleanup error: " + e.toString());
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * remove registry objects created by the test
   */
  public boolean cleanUpRegistry(Collection keys, String objectType) {
    try {
      blm = rs.getBusinessLifeCycleManager();
      // if removing an org, check for services and users

      if (objectType.equals(LifeCycleManager.ORGANIZATION)) {
        BusinessQueryManager bqm = rs.getBusinessQueryManager();
        BulkResponse br = bqm.getRegistryObjects(keys, objectType);
        if (br.getExceptions() != null) {
          debug.add("Error:    cleanUpRegistry failed \n");
          return false;
        }
        Collection orgs = br.getCollection();
        Organization org = null;
        Iterator iter = orgs.iterator();
        while (iter.hasNext()) {
          org = (Organization) iter.next();
        }
        Collection services = org.getServices();
        if (services.size() > 0)
          org.removeServices(services);
        // deletes users from this organization
        Collection users = org.getUsers();
        if (users.size() > 0)
          org.removeUsers(users);
      }

      BulkResponse br = blm.deleteObjects(keys, objectType);
      if (!(JAXR_Util.checkBulkResponse("saveOrganizations", br, debug))) {
        debug.add("Error:    cleanUpRegistry failed \n");
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Cleanup error: " + e.toString());
      TestUtil.printStackTrace(e);
      return false;
    }
    return true;

  }

  public boolean cleanUpRegistry() {
    return cleanUpRegistry(JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME);
  }

  public boolean cleanUpRegistry(String orgName) {
    try {
      // query registry
      Collection names = new ArrayList();
      names.add(orgName);
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      // == Search for org....
      BulkResponse br = bqm.findOrganizations(null, names, null, null, null,
          null);

      if (!(JAXR_Util.checkBulkResponse("findOrganizations", br, debug))) {
        debug.add(
            "Error reported in cleanUpRegistry:  findOrganizations failed \n");
        return false;
      }

      // == Check if there is something to delete
      Collection orgs = br.getCollection();
      if (orgs.size() == 0) {
        debug.add(" OK: nothing in the registry to clean! \n");
        return true;
      }
      // == Get the key for the org so it can be deleted...
      Iterator iter = orgs.iterator();
      Organization org;
      Collection orgKeys = new ArrayList();
      javax.xml.registry.infomodel.Key key = null;
      while (iter.hasNext()) {
        org = (Organization) iter.next();
        orgKeys.add(org.getKey());
        Collection services = org.getServices();
        if (services.size() > 0)
          org.removeServices(services);
        // deletes users from this organization
        Collection users = org.getUsers();
        if (users.size() > 0)
          org.removeUsers(users);
      }
      // == Got the key, now we can delete it....
      br = blm.deleteOrganizations(orgKeys);
      if (!(JAXR_Util.checkBulkResponse("deleteOrganizations", br, debug))) {
        debug.add(
            "Error reported in cleanUpRegistry:  deleteOrganizations failed \n");
        return false;
      }
      return true;
    } catch (JAXRException e) {
      TestUtil.printStackTrace(e);
      return false;
    } finally {
      TestUtil.logTrace(debug.toString());
      debug.clear();
    }
  } // end of clean method

  public Key saveMyOrganization(Organization org) {
    Key key = null;
    Collection orgs = new ArrayList();
    orgs.add(org);
    try {
      BulkResponse br = blm.saveOrganizations(orgs);
      if (br.getExceptions() != null) {
        debug.add("Error:   saveOrganizations failed \n");
        return null;
      }
      Collection orgKeys = br.getCollection();
      Iterator iter = orgKeys.iterator();
      while (iter.hasNext()) {
        key = (Key) iter.next();
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    return key;
  } // end of saveMyOrganization method

}
