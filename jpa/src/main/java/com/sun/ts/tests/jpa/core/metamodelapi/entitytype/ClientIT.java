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

package com.sun.ts.tests.jpa.core.metamodelapi.entitytype;

import java.util.Date;
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

import jakarta.persistence.metamodel.Bindable;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.IdentifiableType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.Type;

@ExtendWith(ArquillianExtension.class)
@TestInstance(Lifecycle.PER_CLASS)

public class ClientIT extends PMClientBase {

  public ClientIT() {
  }

  @Deployment(testable = false, managed = false)
	public static JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = ClientIT.class.getPackageName();
		String pkgName = ClientIT.class.getPackageName() + ".";
		String[] classes = { pkgName + "A", pkgName + "Address", pkgName + "B", pkgName + "DID2Employee", pkgName + "DID2EmployeeId",
				pkgName + "ZipCode" };
		return createDeploymentJar("jpa_core_metamodelapi_entitytype.jar", pkgNameWithoutSuffix, classes);

	}

@BeforeAll
  public void setup() throws Exception {
    TestUtil.logTrace("setup");
    try {
      super.setup();
      removeTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Exception("Setup failed:", e);
    }
  }

  /*
   * @testName: getName
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1280
   *
   * @test_Strategy:
   * 
   */
@Test
  public void getName() throws Exception {
    boolean pass = false;

    String expected = "A";
    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EntityType<A> eType = metaModel.entity(A.class);
      if (eType != null) {
        String name = eType.getName();
        if (name.equals(expected)) {
          TestUtil.logTrace("Received:" + name);
          pass = true;
        } else {
          TestUtil.logErr("Expected: " + expected + ", actual:" + name);
        }
      } else {
        TestUtil.logErr("getEntity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getName failed");
    }
  }
  /*
   * @testName: getDeclaredId
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1283;
   * 
   * @test_Strategy:
   *
   */
@Test
  public void getDeclaredId() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EntityType<A> eType = metaModel.entity(A.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null EntityType");
        TestUtil.logTrace("entityType Name = A");
        SingularAttribute<A, String> idAttrib = eType
            .getDeclaredId(java.lang.String.class);
        String name = idAttrib.getType().getJavaType().getName();
        if (name.equals("java.lang.String")) {
          TestUtil.logTrace("Received:" + name);
          pass = true;
        } else {
          TestUtil.logErr("Expected java.lang.String, actual:" + name);
        }
      } else {
        TestUtil.logErr("getEntity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getDeclaredId failed");
    }
  }

  /*
   * @testName: getDeclaredIdIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1284;
   *
   * @test_Strategy:
   *
   */
@Test
  public void getDeclaredIdIllegalArgumentException() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EntityType<A> eType = metaModel.entity(A.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained A IdentifiableType");
        try {
          eType.getDeclaredId(Date.class);
          TestUtil.logTrace("Did not receive IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception e) {
          TestUtil.logErr("Received unexpected exception", e);
        }
      } else {
        TestUtil.logErr("getEntity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getDeclaredIdIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getDeclaredVersion
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1285;
   *
   * @test_Strategy:
   *
   */
@Test
  public void getDeclaredVersion() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EntityType<A> eType = metaModel.entity(A.class);
      if (eType != null) {
        SingularAttribute<A, Integer> idAttrib = eType
            .getDeclaredVersion(Integer.class);
        String name = idAttrib.getName();
        if (name.equals("value")) {
          TestUtil.logTrace("Received:" + name);
          pass = true;
        } else {
          TestUtil.logErr("Expected: value, actual:" + name);
        }
      } else {
        TestUtil.logErr("getEntity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getDeclaredVersion failed");
    }
  }

  /*
   * @testName: getDeclaredVersionIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1286;
   *
   * @test_Strategy:
   *
   */
@Test
  public void getDeclaredVersionIllegalArgumentException() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EntityType<A> eType = metaModel.entity(A.class);
      if (eType != null) {

        try {
          eType.getDeclaredVersion(Date.class);
          TestUtil.logTrace("Did not receive IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception e) {
          TestUtil.logErr("Received unexpected exception", e);
        }
      } else {
        TestUtil.logErr("getEntity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getDeclaredVersionIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getId
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1287;
   *
   * @test_Strategy:
   *
   */
@Test
  public void getId() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EntityType<A> eType = metaModel.entity(A.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null EntityType");
        SingularAttribute<? super A, String> idAttrib = eType
            .getId(String.class);
        String name = idAttrib.getType().getJavaType().getName();
        if (name.equals("java.lang.String")) {
          TestUtil.logTrace("Received expected: " + name);
          pass = true;
        } else {
          TestUtil.logErr("Expected java.lang.String, actual:" + name);
        }
      } else {
        TestUtil.logErr("getEntity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getId failed");
    }
  }

  /*
   * @testName: getIdIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1288;
   *
   * @test_Strategy:
   *
   */
@Test
  public void getIdIllegalArgumentException() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EntityType<A> eType = metaModel.entity(A.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained A Entity");
        try {
          eType.getId(Date.class);
          TestUtil.logTrace("Did not receive IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception e) {
          TestUtil.logErr("Received unexpected exception", e);
        }

      } else {
        TestUtil.logErr("getEntity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getIdIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getIdClassAttributes
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1289;
   *
   * @test_Strategy:
   *
   */
@Test
  public void getIdClassAttributes() throws Exception {
    boolean pass = false;

    Set<String> expected = new HashSet<String>();
    expected.add("firstName");
    expected.add("lastName");

    Set<String> actual = new HashSet<String>();

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EntityType<DID2Employee> eType = metaModel.entity(DID2Employee.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null EntityType");
        Set<SingularAttribute<? super DID2Employee, ?>> idClassAttribSet = eType
            .getIdClassAttributes();
        if (idClassAttribSet != null) {
          if (idClassAttribSet.size() > 0) {
            for (SingularAttribute<? super DID2Employee, ?> attrib : idClassAttribSet) {
              actual.add(attrib.getName());
            }

            if (expected.containsAll(actual) && actual.containsAll(expected)
                && expected.size() == actual.size()) {

              TestUtil.logTrace("Received expected attributes");
              for (String id : expected) {
                TestUtil.logTrace("id:" + id);
              }
              pass = true;
            } else {
              TestUtil.logErr("Received Unexpected ids");
              TestUtil.logErr("Expected:");
              for (String id : expected) {
                TestUtil.logErr("id:" + id);
              }
              TestUtil.logErr("Actual:");
              for (String attribName : actual) {
                TestUtil.logErr("attrib:" + attribName);
              }
            }
          } else {
            TestUtil.logErr("getIdClassAttributes() returned 0 items");
          }
        } else {
          TestUtil.logErr("getIdClassAttributes() returned null");
        }
      } else {
        TestUtil.logErr("getEntity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getIdClassAttributes failed");
    }
  }

  /*
   * @testName: getIdClassAttributesIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1290;
   *
   * @test_Strategy:
   *
   */
@Test
  public void getIdClassAttributesIllegalArgumentException() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained A Entity");
      EntityType<A> eType = metaModel.entity(A.class);
      if (eType != null) {
        try {
          eType.getIdClassAttributes();
          TestUtil.logTrace("Did not receive IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception e) {
          TestUtil.logErr("Received unexpected exception", e);
        }
      } else {
        TestUtil.logErr("getEntity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getIdClassAttributesIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getIdType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1291;
   *
   * @test_Strategy:
   *
   */
@Test
  public void getIdType() throws Exception {
    boolean pass = false;

    String expected = "java.lang.String";
    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EntityType<A> eType = metaModel.entity(A.class);
      if (eType != null) {
        Type type = eType.getIdType();
        String sType = type.getJavaType().getName();
        if (sType.equals(expected)) {
          TestUtil.logTrace("Received:" + sType);
          pass = true;
        } else {
          TestUtil.logErr("Expected: " + expected + ", actual:" + sType);
        }
      } else {
        TestUtil.logErr("getEntity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getIdType failed");
    }
  }

  /*
   * @testName: getSupertype
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1292;
   *
   * @test_Strategy:
   *
   */
@Test
  public void getSupertype() throws Exception {
    boolean pass = false;
    String expected = "com.sun.ts.tests.jpa.core.metamodelapi.entitytype.B";

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EntityType<A> eType = metaModel.entity(A.class);
      if (eType != null) {
        IdentifiableType<? super A> idType = eType.getSupertype();
        String name = idType.getJavaType().getName();
        if (name.equals(expected)) {
          TestUtil.logTrace("getSuperType() returned:" + name);
          pass = true;
        } else {
          TestUtil.logErr("Expected: " + expected + ", actual:" + name);
        }
      } else {
        TestUtil.logErr("getEntity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getSupertype failed");
    }
  }

  /*
   * @testName: getVersion
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1293;
   *
   * @test_Strategy:
   *
   */
@Test
  public void getVersion() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EntityType<A> eType = metaModel.entity(A.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null EntityType");
        SingularAttribute<? super A, Integer> idAttrib = eType
            .getVersion(java.lang.Integer.class);
        String name = idAttrib.getType().getJavaType().getName();
        if (name.equals("java.lang.Integer")) {
          pass = true;
        } else {
          TestUtil.logErr("Expected java.lang.Integer, actual:" + name);
        }
      } else {
        TestUtil.logErr("getEntity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getVersion failed");
    }
  }

  /*
   * @testName: getVersionIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1294;
   *
   * @test_Strategy:
   *
   */
@Test
  public void getVersionIllegalArgumentException() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EntityType<A> eType = metaModel.entity(A.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained A Entity");
        try {
          eType.getVersion(Date.class);
          TestUtil.logTrace("Did not receive IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception e) {
          TestUtil.logErr("Received unexpected exception", e);
        }
      } else {
        TestUtil.logErr("getEntity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getVersionIllegalArgumentException failed");
    }
  }

  /*
   * @testName: hasSingleIdAttribute
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1295;
   *
   * @test_Strategy:
   *
   */
@Test
  public void hasSingleIdAttribute() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EntityType<A> eType = metaModel.entity(A.class);
      if (eType != null) {
        TestUtil
            .logTrace("entityType Name = " + ((EntityType) eType).getName());
        boolean hasSingleIdAttribute = eType.hasSingleIdAttribute();
        if (hasSingleIdAttribute) {
          pass = true;
          TestUtil.logTrace(
              "hasSingleIdAttribute() returned" + hasSingleIdAttribute);
        } else {
          TestUtil.logErr("Expected: false, actual:" + hasSingleIdAttribute);

        }

      } else {
        TestUtil.logErr("getEntity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("hasSingleIdAttribute failed");
    }
  }

  /*
   * @testName: hasVersionAttribute
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1296;
   *
   * @test_Strategy:
   *
   */
@Test
  public void hasVersionAttribute() throws Exception {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EntityType<A> eType = metaModel.entity(A.class);
      if (eType != null) {
        TestUtil
            .logTrace("entityType Name = " + ((EntityType) eType).getName());
        boolean hasVersionAttribute = eType.hasVersionAttribute();
        if (hasVersionAttribute) {
          pass = true;
          TestUtil.logTrace(
              "hasSingleIdAttribute() returned" + hasVersionAttribute);
        } else {
          TestUtil.logErr("Expected: false, actual:" + hasVersionAttribute);

        }
      } else {
        TestUtil.logErr("getEntity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("hasVersionAttribute failed");
    }
  }

  /*
   * @testName: getBindableJavaType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1281;
   *
   * @test_Strategy:
   *
   */
@Test
  public void getBindableJavaType() throws Exception {
    boolean pass = false;

    String expected = "com.sun.ts.tests.jpa.core.metamodelapi.entitytype.A";
    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EntityType<A> eType = metaModel.entity(A.class);
      if (eType != null) {
        String bindableJavaType = eType.getBindableJavaType().getName();
        if (bindableJavaType != null) {
          if (bindableJavaType.equals(expected)) {
            TestUtil.logTrace("Received expected result: " + bindableJavaType);
            pass = true;
          } else {
            TestUtil.logTrace(
                "Expected:" + expected + ", actual:" + bindableJavaType);
          }
        } else {
          TestUtil.logErr("getName() returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getBindableJavaType failed");
    }
  }

  /*
   * @testName: getBindableType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1282;
   *
   * @test_Strategy:
   *
   */
@Test
  public void getBindableType() throws Exception {
    boolean pass = false;

    String expected = Bindable.BindableType.ENTITY_TYPE.name();
    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EntityType<A> eType = metaModel.entity(A.class);
      if (eType != null) {
        String bindableType = eType.getBindableType().name();
        if (bindableType != null) {
          if (bindableType.equals(expected)) {
            TestUtil.logTrace("Received expected result: " + bindableType);
            pass = true;
          } else {
            TestUtil
                .logTrace("Expected:" + expected + ", actual:" + bindableType);
          }
        } else {
          TestUtil.logErr("getBindableType() returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Exception("getBindableType failed");
    }
  }

@AfterAll
  public void cleanup() throws Exception {
    TestUtil.logTrace("Cleanup data");
    removeTestData();
    TestUtil.logTrace("cleanup complete, calling super.cleanup");
    super.cleanup();
  }

  private void removeTestData() {
    TestUtil.logTrace("removeTestData");
    if (getEntityTransaction().isActive()) {
      getEntityTransaction().rollback();
    }
  }
}
