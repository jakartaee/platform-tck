/*
 * Copyright (c) 2012, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.spec.client.instance;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Configurable;
import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;
import com.sun.ts.tests.jaxrs.common.provider.StringBeanEntityProvider;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = 3031443305675236388L;

  private int registeredProviderCnt = -1;

  private int registeredPropertyCnt = -1;

  public static void main(String[] args) {
    new JAXRSClient().run(args);
  }

  public JAXRSClient() {
    Client client = ClientBuilder.newClient();
    Configuration config = client.getConfiguration();
    registeredPropertyCnt = config.getProperties().size();
    registeredProviderCnt = config.getInstances().size();
    logMsg("Already registered", registeredProviderCnt, "providers");
    logMsg("Already registered", registeredPropertyCnt, "properties");

  }

  /* Run test */
  /*
   * @testName: defaultClientConfigurationPresetTest
   * 
   * @assertion_ids: JAXRS:SPEC:73;
   * 
   * @test_Strategy: This interface supports configuration of: Features,
   * Properties, Providers
   */
  public void defaultClientConfigurationPresetTest() throws Fault {
    Client client = ClientBuilder.newClient();
    checkConfig(client, registeredProviderCnt, registeredPropertyCnt);
  }

  /*
   * @testName: clientConfiguredTest
   * 
   * @assertion_ids: JAXRS:SPEC:73;
   * 
   * @test_Strategy: This interface supports configuration of: Features,
   * Properties, Providers
   */
  public void clientConfiguredTest() throws Fault {
    Client client = ClientBuilder.newClient();
    client.register(new StringBeanEntityProvider());
    checkConfig(client, 1 + registeredProviderCnt, registeredPropertyCnt);

    client = ClientBuilder.newClient();
    client.property(getClass().getName(), getClass().getName());
    checkConfig(client, registeredProviderCnt, 1 + registeredPropertyCnt);

    client = ClientBuilder.newClient();
    client.register(new Feature() {
      @Override
      public boolean configure(FeatureContext context) {
        return true;
      }
    });
    checkConfig(client, 1 + registeredProviderCnt, registeredPropertyCnt);
  }

  // /////////////////////////////////////////////////////////////////////////

  void checkConfig(Configurable<?> configurable, int providersCount,
      int propertiesCount) throws Fault {

    Configuration config = configurable.getConfiguration();

    boolean check;
    Set<Object> providers = config.getInstances();
    check = checkCollectionSize(providers, providersCount);
    assertFault(check, "Unexpected Instances List in Client:",
        providers.size());
    TestUtil.logTrace("Test of Providers passed");

    Map<String, Object> properties = config.getProperties();
    check = checkCollectionSize(properties, propertiesCount);
    assertFault(check, "Unexpected Properties List in Client:",
        properties.size());
    TestUtil.logTrace("Test of Properties passed");
  }

  static boolean checkCollectionSize(Collection<?> collection, int size) {
    return (collection == null && size == 0)
        || (collection != null && collection.size() == size);
  }

  static boolean checkCollectionSize(Map<?, ?> collection, int size) {
    return (collection == null && size == 0)
        || (collection != null && collection.size() == size);
  }
}
