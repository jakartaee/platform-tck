/*
 * Copyright (c) 2007, 2025 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)Client.java	1.13 03/05/16
 */

package com.sun.ts.tests.assembly.altDD;

import java.util.Properties;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.lib.util.TSNamingContext;

import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.asset.UrlAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.impl.base.path.BasicPath;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;

@Tag("assembly")
@Tag("platform")
@Tag("tck-appclient")
@ExtendWith(ArquillianExtension.class)
public class Client extends EETest {

  private static final String prefix = "java:comp/env/";

  private static final String entryLookup = prefix + "myCountry";

  private static final String beanLookup = prefix + "ejb/myPainter";

  /* Expected values for bean name */
  private static final String entryNameRef = "France";

  private static final String beanNameRef = "Gaughin";

  private Properties props = null;

  private TSNamingContext nctx = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   */
  public void setup(String[] args, Properties props) throws Fault {

    try {
      this.props = props;

      logMsg("[Client] Getting Naming Context...");
      nctx = new TSNamingContext();
      logMsg("[Client] Setup completed!");
    } catch (Exception e) {
      logErr("[Client] Failed to obtain Naming Context:" + e);
      throw new Fault("[Client] Setup failed:" + e, e);
    }
  }


  @TargetsContainer("tck-appclient")
  @OverProtocol("appclient")
  @Deployment(name = "assembly_altDD", order = 2)
  public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
    JavaArchive assembly_altDD_client = ShrinkWrap.create(JavaArchive.class, "assembly_altDD_client.jar");
    assembly_altDD_client.addClasses(
        Fault.class,
        EETest.class,
        SetupException.class,
        com.sun.ts.tests.assembly.altDD.Client.class,
        com.sun.ts.tests.assembly.altDD.PainterBean.class);
    URL resURL = Client.class.getResource("assembly_altDD_client.xml");
    if (resURL != null) {
      assembly_altDD_client.addAsManifestResource(resURL, "application-client.xml");
    }
    assembly_altDD_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"),
        "MANIFEST.MF");

    resURL = Client.class.getResource("assembly_altDD_client.jar.sun-application-client.xml");
    if (resURL != null) {
      assembly_altDD_client.addAsManifestResource(resURL, "sun-application-client.xml");
    }
    archiveProcessor.processClientArchive(assembly_altDD_client, Client.class, resURL);

    JavaArchive assembly_altDD_ejb = ShrinkWrap.create(JavaArchive.class, "assembly_altDD_ejb.jar");
    assembly_altDD_ejb.addClasses(
        com.sun.ts.tests.common.ejb.wrappers.Stateless3xWrapper.class,
        com.sun.ts.lib.util.RemoteLoggingInitException.class,
        com.sun.ts.tests.assembly.util.shared.ejbref.common.ReferencedBeanCode.class,
        com.sun.ts.tests.assembly.altDD.PainterBean.class,
        com.sun.ts.tests.assembly.altDD.PainterBeanEJB.class);
    // The ejb-jar.xml descriptor
    URL ejbResURL = Client.class.getResource("assembly_altDD_ejb.xml");
    if (ejbResURL != null) {
      assembly_altDD_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
    }
    ejbResURL = Client.class.getResource("assembly_altDD_ejb.jar.sun-ejb-jar.xml");
    if (ejbResURL != null) {
      assembly_altDD_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
    }

    archiveProcessor.processEjbArchive(assembly_altDD_ejb, Client.class, ejbResURL);


    EnterpriseArchive assembly_altDD_ear = ShrinkWrap.create(EnterpriseArchive.class, "assembly_altDD.ear");
    assembly_altDD_ear.addAsModule(assembly_altDD_client);
    assembly_altDD_ear.addAsModule(assembly_altDD_ejb);

    URL earResURL = Client.class.getResource("altDD_client.xml");
    assembly_altDD_ear.add(new UrlAsset(earResURL), new BasicPath("/altDD_client.xml"));
    earResURL = Client.class.getResource("altDD_ejb.xml");
    assembly_altDD_ear.add(new UrlAsset(earResURL), new BasicPath("/altDD_ejb.xml"));

    earResURL = Client.class.getResource("application.xml");
    assembly_altDD_ear.addAsManifestResource(earResURL, "application.xml");

    archiveProcessor.processEarArchive(assembly_altDD_ear, Client.class, null);

    return assembly_altDD_ear;
  }

  /**
   * @testName: testAppClient
   *
   * @assertion_ids: JavaEE:SPEC:10260
   *
   * @test_Strategy: Package an application containing:
   *
   *                 - An application client jar file including its own DD: DD2.
   *                 DD2 declares one String environment entry, named
   *                 'myCountry' whose value is 'Spain'.
   *
   *                 - An alternate DD: DD4. DD4 is almost identical to DD2.
   *                 Nevertheless it changes the value for the 'myCountry'
   *                 environment entry: the new value is 'France'.
   *
   *                 - An application DD including the application client module
   *                 and using an alt-dd element to define DD4 as an alternate
   *                 DD for the application client.
   *
   *                 We check that:
   *
   *                 - We can deploy the application.
   *
   *                 - The application client can lookup the 'ejb/myCountry'
   *                 environment entry.
   *
   *                 - The runtime value is 'France', validating the use of DD4
   *                 at deployment time.
   */
  @Test
  public void testAppClient() throws Fault {
    String entryValue;
    boolean pass = false;

    try {
      logTrace("[Client] Looking up " + entryLookup);

      entryValue = (String) nctx.lookup(entryLookup);

      pass = entryValue.equals(entryNameRef);
      if (!pass) {
        logErr("[Client] Expected " + entryLookup + " name to be "
            + entryNameRef + ", not " + entryValue);

        throw new Fault("Alternative DD test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: " + e);
      throw new Fault("Alternative DD test failed!" + e, e);
    }
  }

  /**
   * @testName: testEJB
   *
   * @assertion_ids: JavaEE:SPEC:255
   *
   * @test_Strategy: Package an application containing:
   *
   *                 - An ejb-jar file including its own DD: DD1. This ejb-jar
   *                 contains 2 beans sharing the same Home and Remote
   *                 interfaces. According to DD1:
   *
   *                 . The two ejb-name's are Bean1 and Bean2.
   *
   *                 . Bean1 declares a String environment entry named myName
   *                 whose value is 'Dali '
   * 
   *                 . Bean2 declares a String environment entry named myName
   *                 whose value is 'Picasso'
   *
   *                 - An application client jar file including its own DD: DD2.
   *                 DD2 declares one EJB reference using ejb-ref-name
   *                 'ejb/myPainter' and an ejb-link element targeting Bean1.
   *
   *                 - An alternate DD: DD3. DD3 is almost identical to DD1.
   *                 Nevertheless it changes the values for the myName
   *                 environment entries: Bean1 is 'Gaughin' and Bean2 is
   *                 'Matisse'.
   *
   *                 - An application DD including the EJB jar module and the
   *                 application jar module, but also using an alt-dd element to
   *                 define DD3 as an alternate DD for the ejb-jar.
   *
   *                 We check that:
   *
   *                 - We can deploy the application.
   *
   *                 - The application client can lookup 'ejb/myPainter' and
   *                 create a Bean instance.
   *
   *                 - The client can call a business method on that instance
   *                 that return the value of the myName environment entry in
   *                 the bean environment.
   *
   *                 - The returned value is 'Matisse', validating the use of
   *                 DD3 at deployment time.
   */
  @Test
  public void testEJB() throws Fault {
    PainterBean bean = null;
    String nameValue;
    boolean pass = false;

    try {
      logTrace("[Client] Looking up " + beanLookup);
      bean = (PainterBean) nctx.lookup(beanLookup, PainterBean.class);
      bean.createNamingContext();
      
      bean.initLogging(props);

      logTrace("[Client] Checking referenced EJB...");
      nameValue = bean.whoAreYou();

      pass = nameValue.equals(beanNameRef);
      if (!pass) {
        logErr("[Client] Expected " + beanLookup + " name to be " + beanNameRef
            + ", not " + nameValue);

        throw new Fault("Alternative DD test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: " + e);
      throw new Fault("Alternative DD test failed!" + e, e);
    }
  }

  public void cleanup() {
    logTrace("[Client] Cleanup.");
  }

}
