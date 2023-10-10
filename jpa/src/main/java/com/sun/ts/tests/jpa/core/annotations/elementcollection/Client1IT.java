/*
 * Copyright (c) 2009, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.annotations.elementcollection;

import java.util.HashSet;
import java.util.Set;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;
import com.sun.ts.tests.jpa.core.annotations.discriminatorValue.ClientIT;

@ExtendWith(ArquillianExtension.class)
@TestInstance(Lifecycle.PER_CLASS)

public class Client1IT extends PMClientBase {

  public Client1IT() {
  }
  
	@Deployment(testable = false, managed = false)
	public static JavaArchive createDeployment() throws Exception {
		String pkgNameWithoutSuffix = ClientIT.class.getPackageName();
		String pkgName = ClientIT.class.getPackageName() + ".";
		String[] classes = { pkgName + "A", pkgName + "Address" };
		return createDeploymentJar("jpa_core_annotations_elementcollection1.jar", pkgNameWithoutSuffix, classes);

	}



  @BeforeAll
  public void setupA() throws Exception {
    TestUtil.logTrace("setup");
    try {
      super.setup();
      removeATestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Exception("Setup failed:", e);

    }
  }

  /*
   * @testName: elementCollectionEmbeddableType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:318; PERSISTENCE:JAVADOC:319;
   * PERSISTENCE:SPEC:2007;
   * 
   * @test_Strategy: ElementCollection of an embeddable class
   *
   */
  @Test
  public void elementCollectionEmbeddableType() throws Exception {
    boolean pass = false;
    A aRef = null;
    try {

      final Address addr1 = new Address("1 Network Drive", "Burlington", "MA",
          "01801");
      final Address addr2 = new Address("Some Address", "Boston", "MA",
          "01803");

      Set<Address> s1 = new HashSet<Address>();
      s1.add(addr1);
      s1.add(addr2);

      aRef = new A("1", "bean1", 1);
      aRef.setAddress(s1);
      TestUtil.logTrace("Persisting A");
      getEntityTransaction().begin();
      getEntityManager().persist(aRef);
      getEntityTransaction().commit();

      getEntityTransaction().begin();
      A newA = findA("1");
      final Set<Address> newAddressSet = newA.getAddress();

      dumpAddresses(newAddressSet);

      boolean pass1 = false;
      boolean pass2 = false;
      boolean pass3 = true;
      for (Address addr : newAddressSet) {
        if (addr != null) {
          if (addr.getStreet().equals("1 Network Drive")
              && addr.getCity().equals("Burlington")
              && addr.getState().equals("MA")
              && addr.getZip().equals("01801")) {
            pass1 = true;
            TestUtil.logTrace("pass1 = " + pass1);
          }
          if (addr.getStreet().equals("Some Address")
              && addr.getCity().equals("Boston") && addr.getState().equals("MA")
              && addr.getZip().equals("01803")) {
            pass2 = true;
            TestUtil.logTrace("pass2 = " + pass2);
          }
        } else {
          TestUtil.logTrace("address=null");
          pass3 = false;
        }
      }

      if (pass1 && pass2 && pass3) {
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }

    }

    if (!pass) {
      throw new Exception("elementCollectionEmbeddableType failed");
    }
  }
  
  private A findA(final String id) {
	    return getEntityManager().find(A.class, id);
	  }

	  private void dumpAddresses(final Set<Address> addr) {
	    TestUtil.logTrace("address Data");
	    TestUtil.logTrace("---------------");
	    if (addr != null) {
	      TestUtil.logTrace("size=" + addr.size());
	      int elem = 1;
	      for (Address v : addr) {
	        TestUtil.logTrace("- Element #" + elem++);
	        TestUtil.logTrace("  street=" + v.getStreet() + ", city=" + v.getCity()
	            + ", state=" + v.getState() + ", zip=" + v.getZip());
	      }
	    } else {
	      TestUtil.logTrace("  address=NULL");
	    }
	  }

	 


  @AfterAll
  public void cleanupA() throws Exception {
    TestUtil.logTrace("cleanup");
    removeATestData();
    TestUtil.logTrace("cleanup complete, calling super.cleanup");
    super.cleanup();
  }
  
  private void removeATestData() {
	    TestUtil.logTrace("removeATestData");
	    if (getEntityTransaction().isActive()) {
	      getEntityTransaction().rollback();
	    }
	    try {
	      getEntityTransaction().begin();
	      getEntityManager().createNativeQuery("Delete from A_ADDRESS")
	          .executeUpdate();
	      getEntityManager().createNativeQuery("Delete from AEC").executeUpdate();
	      getEntityTransaction().commit();
	    } catch (Exception e) {
	      TestUtil.logErr("Exception encountered while removing entities:", e);
	    } finally {
	      try {
	        if (getEntityTransaction().isActive()) {
	          getEntityTransaction().rollback();
	        }
	      } catch (Exception re) {
	        TestUtil.logErr("Unexpected Exception in removeTestData:", re);
	      }
	    }
	  }


}
