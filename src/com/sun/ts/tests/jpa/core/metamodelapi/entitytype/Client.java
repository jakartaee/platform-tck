/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.metamodel.*;
import java.lang.reflect.Array;
import java.util.*;

public class Client extends PMClientBase {

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      super.setup(args, p);
      removeTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
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
  public void getName() throws Fault {
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
      throw new Fault("getName failed");
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

  public void getDeclaredId() throws Fault {
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
      throw new Fault("getDeclaredId failed");
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

  public void getDeclaredIdIllegalArgumentException() throws Fault {
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
      throw new Fault("getDeclaredIdIllegalArgumentException failed");
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
  public void getDeclaredVersion() throws Fault {
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
      throw new Fault("getDeclaredVersion failed");
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
  public void getDeclaredVersionIllegalArgumentException() throws Fault {
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
      throw new Fault("getDeclaredVersionIllegalArgumentException failed");
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
  public void getId() throws Fault {
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
      throw new Fault("getId failed");
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
  public void getIdIllegalArgumentException() throws Fault {
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
      throw new Fault("getIdIllegalArgumentException failed");
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
  public void getIdClassAttributes() throws Fault {
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
      throw new Fault("getIdClassAttributes failed");
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

  public void getIdClassAttributesIllegalArgumentException() throws Fault {
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
      throw new Fault("getIdClassAttributesIllegalArgumentException failed");
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
  public void getIdType() throws Fault {
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
      throw new Fault("getIdType failed");
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
  public void getSupertype() throws Fault {
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
      throw new Fault("getSupertype failed");
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
  public void getVersion() throws Fault {
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
      throw new Fault("getVersion failed");
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
  public void getVersionIllegalArgumentException() throws Fault {
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
      throw new Fault("getVersionIllegalArgumentException failed");
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
  public void hasSingleIdAttribute() throws Fault {
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
      throw new Fault("hasSingleIdAttribute failed");
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
  public void hasVersionAttribute() throws Fault {
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
      throw new Fault("hasVersionAttribute failed");
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
  public void getBindableJavaType() throws Fault {
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
      throw new Fault("getBindableJavaType failed");
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
  public void getBindableType() throws Fault {
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
      throw new Fault("getBindableType failed");
    }
  }

  public void cleanup() throws Fault {
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
