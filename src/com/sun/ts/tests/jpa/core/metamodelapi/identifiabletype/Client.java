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

package com.sun.ts.tests.jpa.core.metamodelapi.identifiabletype;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import javax.persistence.metamodel.*;
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
   * @testName: getId
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1301
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
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null EntityType");
        SingularAttribute<? super A, String> idAttrib = iType
            .getId(String.class);
        String name = idAttrib.getType().getJavaType().getName();
        if (name.equals("java.lang.String")) {
          TestUtil.logTrace("Received expected: " + name);
          pass = true;
        } else {
          TestUtil.logErr("Expected java.lang.String, actual:" + name);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1302
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
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained A Entity");
        try {
          iType.getId(Date.class);
          TestUtil.logTrace("Did not receive IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception e) {
          TestUtil.logErr("Received unexpected exception", e);
        }

      } else {
        TestUtil.logErr("entity(...) returned null");
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
   * @testName: getVersion
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1307
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
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null EntityType");
        SingularAttribute<? super A, Integer> idAttrib = iType
            .getVersion(java.lang.Integer.class);
        String name = idAttrib.getType().getJavaType().getName();
        if (name.equals("java.lang.Integer")) {
          pass = true;
        } else {
          TestUtil.logErr("Expected java.lang.Integer, actual:" + name);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1308
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
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained A Entity");
        try {
          iType.getVersion(Date.class);
          TestUtil.logTrace("Did not receive IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception e) {
          TestUtil.logErr("Received unexpected exception", e);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
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
   * @testName: getDeclaredId
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1297
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
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null EntityType");
        TestUtil.logTrace("entityType Name = A");
        SingularAttribute<A, String> idAttrib = iType
            .getDeclaredId(java.lang.String.class);
        String name = idAttrib.getType().getJavaType().getName();
        if (name.equals("java.lang.String")) {
          TestUtil.logTrace("Received expected name:" + name);
          pass = true;
        } else {
          TestUtil.logErr("Expected java.lang.String, actual:" + name);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1298
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
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained A IdentifiableType");
        try {
          iType.getDeclaredId(Date.class);
          TestUtil.logTrace("Did not receive IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception e) {
          TestUtil.logErr("Received unexpected exception", e);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1299
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
      Set<EntityType<?>> aSet = metaModel.getEntities();
      if (aSet != null) {
        TestUtil.logTrace("Obtained Non-null Set of EntityType");
        IdentifiableType<A> iType = metaModel.entity(A.class);

        TestUtil
            .logTrace("entityType Name = " + ((EntityType) iType).getName());
        SingularAttribute<A, Integer> idAttrib = iType
            .getDeclaredVersion(Integer.class);
        String name = idAttrib.getName();
        if (name.equals("value")) {
          TestUtil.logTrace("Received:" + name);
          pass = true;
        } else {
          TestUtil.logErr("Expected: value, actual:" + name);
        }
      } else {
        TestUtil.logErr("getEntities(...) returned null");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1300
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
      Set<EntityType<?>> aSet = metaModel.getEntities();
      if (aSet != null) {
        TestUtil.logTrace("Obtained Non-null Set of EntityType");
        IdentifiableType<A> iType = metaModel.entity(A.class);
        TestUtil
            .logTrace("entityType Name = " + ((EntityType) iType).getName());
        try {
          iType.getDeclaredVersion(Date.class);
          TestUtil.logTrace("Did not receive IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception e) {
          TestUtil.logErr("Received unexpected exception", e);
        }

      } else {
        TestUtil.logErr("getEntities(...) returned null");
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
   * @testName: getSupertype
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1306
   *
   * @test_Strategy:
   *
   */
  public void getSupertype() throws Fault {
    boolean pass = false;
    String expected = "com.sun.ts.tests.jpa.core.metamodelapi.identifiabletype.B";

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil
            .logTrace("entityType Name = " + ((EntityType) iType).getName());
        IdentifiableType<? super A> idType = iType.getSupertype();
        String name = idType.getJavaType().getName();
        if (name.equals(expected)) {
          TestUtil.logTrace("getSuperType() returned:" + name);
          pass = true;
        } else {
          TestUtil.logErr("Expected: " + expected + ", actual:" + name);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
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
   * @testName: hasSingleIdAttribute
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1309
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
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {

        TestUtil
            .logTrace("entityType Name = " + ((EntityType) iType).getName());
        boolean hasSingleIdAttribute = iType.hasSingleIdAttribute();
        if (hasSingleIdAttribute) {
          pass = true;
          TestUtil.logTrace(
              "hasSingleIdAttribute() returned" + hasSingleIdAttribute);
        } else {
          TestUtil.logErr("Expected: false, actual:" + hasSingleIdAttribute);

        }

      } else {
        TestUtil.logErr("entity(...) returned null");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1310
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
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {

        TestUtil
            .logTrace("entityType Name = " + ((EntityType) iType).getName());
        boolean hasVersionAttribute = iType.hasVersionAttribute();
        if (hasVersionAttribute) {
          pass = true;
          TestUtil.logTrace(
              "hasSingleIdAttribute() returned" + hasVersionAttribute);
        } else {
          TestUtil.logErr("Expected: false, actual:" + hasVersionAttribute);

        }
      } else {
        TestUtil.logErr("entity(...) returned null");
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
   * @testName: getIdClassAttributes
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1303
   *
   * @test_Strategy:
   *
   */
  public void getIdClassAttributes() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      // Set<EntityType<?>> aSet = metaModel.getEntities();
      IdentifiableType<DID2Employee> iType = metaModel
          .entity(DID2Employee.class);
      if (iType != null) {

        Set<SingularAttribute<? super DID2Employee, ?>> idClassAttribSet = iType
            .getIdClassAttributes();
        if (idClassAttribSet != null) {
          for (SingularAttribute<? super DID2Employee, ?> attrib : idClassAttribSet) {
            TestUtil.logTrace("attribute Name = " + attrib.getName());
          }
          pass = true;
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1304
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
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {

        try {
          iType.getIdClassAttributes();
          TestUtil.logTrace("Did not receive IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception e) {
          TestUtil.logErr("Received unexpected exception", e);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1305
   *
   * @test_Strategy:
   *
   */
  public void getIdType() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        Type idType = iType.getIdType();
        if (idType != null) {
          TestUtil.logTrace("idType Name = " + idType.getJavaType());
          String name = idType.getJavaType().getName();

          if (name.equals("java.lang.String")) {
            TestUtil.logTrace("Received expected: " + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected java.lang.String, actual:" + name);
          }

        } else {
          TestUtil.logErr("getIdType() returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
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
   * @testName: getAttribute
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1311;
   *
   * @test_Strategy:
   *
   */
  public void getAttribute() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        Attribute attrib = iType.getAttribute("id");
        if (attrib != null) {
          TestUtil.logTrace("attribute Name = " + attrib.getName());
          String name = attrib.getName();
          if (name.equals("id")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: id, actual:" + name);
          }
        } else {
          TestUtil.logErr("getAttribute(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getAttribute failed");
    }
  }

  /*
   * @testName: getAttributeIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1312;
   *
   * @test_Strategy:
   *
   */
  public void getAttributeIllegalArgumentException() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        try {
          iType.getAttribute("doesnotexist");
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getAttributeIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getAttributes
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1313;
   *
   * @test_Strategy:
   *
   */
  public void getAttributes() throws Fault {
    boolean pass = false;

    List<String> expected = new ArrayList<String>();

    expected.add("cAddress");
    expected.add("cAddress_inherited");
    expected.add("id");
    expected.add("lAddress");
    expected.add("lAddress_inherited");
    expected.add("mAddress");
    expected.add("mAddress_inherited");
    expected.add("name");
    expected.add("sAddress");
    expected.add("sAddress_inherited");
    expected.add("value");
    Collections.sort(expected);

    List<String> actual = new ArrayList<String>();

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        Set set = iType.getAttributes();
        if (set != null) {
          if (set.size() > 0) {
            for (Iterator i = set.iterator(); i.hasNext();) {
              Attribute attrib = (Attribute) i.next();
              actual.add(attrib.getName());
            }
            Collections.sort(actual);

            if (expected.containsAll(actual) && actual.containsAll(expected)
                && expected.size() == actual.size()) {

              TestUtil.logTrace("Received expected attributes");
              for (String attribName : expected) {
                TestUtil.logTrace("attrib:" + attribName);
              }
              pass = true;
            } else {
              TestUtil.logErr("Received unexpected attributes");
              TestUtil.logErr("Expected(" + expected.size() + "):");
              for (String attribName : expected) {
                TestUtil.logErr("attrib:" + attribName);
              }
              TestUtil.logErr("Actual(" + actual.size() + "):");
              for (String attribName : actual) {
                TestUtil.logErr("attrib:" + attribName);
              }
            }
          } else {
            TestUtil.logErr("getAttributes() returned 0 results");
          }
        } else {
          TestUtil.logErr("getAttributes() returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getAttributes failed");
    }
  }

  /*
   * @testName: getCollectionStringClass
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1314;
   *
   * @test_Strategy:
   *
   */
  public void getCollectionStringClass() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        CollectionAttribute cAttrib = iType.getCollection("cAddress",
            Address.class);
        if (cAttrib != null) {
          TestUtil.logTrace("attribute Name = " + cAttrib.getName());
          String name = cAttrib.getName();
          if (name.equals("cAddress")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: cAddress, actual:" + name);
          }
        } else {
          TestUtil.logErr("getCollection(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getCollectionStringClass failed");
    }
  }

  /*
   * @testName: getCollectionStringClassIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1315;
   *
   * @test_Strategy:
   *
   */
  public void getCollectionStringClassIllegalArgumentException() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        try {
          iType.getCollection("doesnotexist", Address.class);
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault(
          "getCollectionStringClassIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getCollectionString
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1316;
   *
   * @test_Strategy:
   *
   */
  public void getCollectionString() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        CollectionAttribute cAttrib = iType.getCollection("cAddress");
        if (cAttrib != null) {
          TestUtil.logTrace("attribute Name = " + cAttrib.getName());
          String name = cAttrib.getName();
          if (name.equals("cAddress")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: cAddress, actual:" + name);
          }
        } else {
          TestUtil.logErr("getCollection(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getCollectionString failed");
    }
  }

  /*
   * @testName: getCollectionStringIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1317;
   *
   * @test_Strategy:
   *
   */
  public void getCollectionStringIllegalArgumentException() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        try {
          iType.getCollection("doesnotexist");
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getCollectionStringIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getDeclaredAttribute
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1318;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredAttribute() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        Attribute attrib = iType.getDeclaredAttribute("cAddress");
        if (attrib != null) {
          TestUtil.logTrace("attribute Name = " + attrib.getName());
          String name = attrib.getName();
          if (name.equals("cAddress")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: cAddress, actual:" + name);
          }
        } else {
          TestUtil.logErr("getDeclaredAttribute(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getDeclaredAttribute failed");
    }
  }

  /*
   * @testName: getDeclaredAttributeIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1319;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredAttributeIllegalArgumentException() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        try {
          iType.getDeclaredAttribute("cAddress_inherited");
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getDeclaredAttributeIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getDeclaredAttributes
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1320;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredAttributes() throws Fault {
    boolean pass = false;

    List<String> expected = new ArrayList<String>();

    expected.add("id");
    expected.add("cAddress");
    expected.add("lAddress");
    expected.add("mAddress");
    expected.add("sAddress");
    expected.add("value");
    Collections.sort(expected);

    List<String> actual = new ArrayList<String>();

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        Set set = iType.getDeclaredAttributes();
        if (set != null) {
          if (set.size() > 0) {
            for (Iterator i = set.iterator(); i.hasNext();) {
              Attribute attrib = (Attribute) i.next();
              actual.add(attrib.getName());
            }
            Collections.sort(actual);

            if (expected.containsAll(actual) && actual.containsAll(expected)
                && expected.size() == actual.size()) {

              TestUtil.logTrace("Received expected attributes");
              for (String attribName : expected) {
                TestUtil.logTrace("attrib:" + attribName);
              }
              pass = true;
            } else {
              TestUtil.logErr("Received unexpected attributes");
              TestUtil.logErr("Expected(" + expected.size() + "):");
              for (String attribName : expected) {
                TestUtil.logErr("attrib:" + attribName);
              }
              TestUtil.logErr("Actual(" + actual.size() + "):");
              for (String attribName : actual) {
                TestUtil.logErr("attrib:" + attribName);
              }
            }
          } else {
            TestUtil.logErr("getAttributes() returned 0 results");
          }
        } else {
          TestUtil.logErr("getDeclaredAttributes() returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getDeclaredAttributes failed");
    }
  }

  /*
   * @testName: getDeclaredCollectionStringClass
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1321;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredCollectionStringClass() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        CollectionAttribute cAttrib = iType.getCollection("cAddress",
            Address.class);
        if (cAttrib != null) {
          TestUtil.logTrace("attribute Name = " + cAttrib.getName());
          String name = cAttrib.getName();
          if (name.equals("cAddress")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: cAddress, actual:" + name);
          }
        } else {
          TestUtil.logErr("getCollection(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getDeclaredCollectionStringClass failed");
    }
  }

  /*
   * @testName: getDeclaredCollectionStringClassIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1322;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredCollectionStringClassIllegalArgumentException()
      throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        try {
          iType.getDeclaredCollection("cAddress_inherited", Address.class);
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault(
          "getDeclaredCollectionStringClassIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getDeclaredCollectionString
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1323;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredCollectionString() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        CollectionAttribute cAttrib = iType.getCollection("cAddress",
            Address.class);
        if (cAttrib != null) {
          TestUtil.logTrace("attribute Name = " + cAttrib.getName());
          String name = cAttrib.getName();
          if (name.equals("cAddress")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: cAddress, actual:" + name);
          }
        } else {
          TestUtil.logErr("getCollection(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getDeclaredCollectionString failed");
    }
  }

  /*
   * @testName: getDeclaredCollectionStringIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1324;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredCollectionStringIllegalArgumentException()
      throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        try {
          iType.getDeclaredCollection("cAddress_inherited", Address.class);
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault(
          "getDeclaredCollectionStringIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getDeclaredListStringClass
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1325;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredListStringClass() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        ListAttribute lAttrib = iType.getDeclaredList("lAddress",
            Address.class);
        if (lAttrib != null) {
          TestUtil.logTrace("attribute Name = " + lAttrib.getName());
          String name = lAttrib.getName();
          if (name.equals("lAddress")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: lAddress, actual:" + name);
          }
        } else {
          TestUtil.logErr("getDeclaredList(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getDeclaredListStringClass failed");
    }
  }

  /*
   * @testName: getDeclaredListStringClassIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1326;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredListStringClassIllegalArgumentException()
      throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        try {
          iType.getDeclaredList("lAddress_inherited", Address.class);
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault(
          "getDeclaredListStringClassIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getDeclaredListString
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1327;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredListString() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        ListAttribute lAttrib = iType.getDeclaredList("lAddress");
        if (lAttrib != null) {
          TestUtil.logTrace("attribute Name = " + lAttrib.getName());
          String name = lAttrib.getName();
          if (name.equals("lAddress")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: lAddress, actual:" + name);
          }
        } else {
          TestUtil.logErr("getDeclaredList(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getDeclaredListString failed");
    }
  }

  /*
   * @testName: getDeclaredListStringIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1328;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredListStringIllegalArgumentException() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        try {
          iType.getDeclaredList("lAddress_inherited");
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getDeclaredListStringIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getDeclaredMapStringClassClass
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1329;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredMapStringClassClass() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        MapAttribute mAttrib = iType.getDeclaredMap("mAddress", Address.class,
            String.class);
        if (mAttrib != null) {
          TestUtil.logTrace("attribute Name = " + mAttrib.getName());
          String name = mAttrib.getName();
          if (name.equals("mAddress")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: mAddress, actual:" + name);
          }
        } else {
          TestUtil.logErr("getDeclaredMap(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getDeclaredMapStringClassClass failed");
    }
  }

  /*
   * @testName: getDeclaredMapStringClassClassIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1330;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredMapStringClassClassIllegalArgumentException()
      throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        try {
          iType.getDeclaredMap("mAddress_inherited", Address.class,
              String.class);
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault(
          "getDeclaredMapStringClassClassIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getDeclaredMapString
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1331;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredMapString() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        MapAttribute mAttrib = iType.getDeclaredMap("mAddress");
        if (mAttrib != null) {
          TestUtil.logTrace("attribute Name = " + mAttrib.getName());
          String name = mAttrib.getName();
          if (name.equals("mAddress")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: mAddress, actual:" + name);
          }
        } else {
          TestUtil.logErr("getDeclaredMap(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getDeclaredMapString failed");
    }
  }

  /*
   * @testName: getDeclaredMapStringIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1332;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredMapStringIllegalArgumentException() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        try {
          iType.getDeclaredMap("mAddress_inherited");
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getDeclaredMapStringIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getDeclaredSetStringClass
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1334;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredSetStringClass() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        SetAttribute sAttrib = iType.getDeclaredSet("sAddress", Address.class);
        if (sAttrib != null) {
          TestUtil.logTrace("attribute Name = " + sAttrib.getName());
          String name = sAttrib.getName();
          if (name.equals("sAddress")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: sAddress, actual:" + name);
          }
        } else {
          TestUtil.logErr("getDeclaredSet(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getDeclaredSetStringClass failed");
    }
  }

  /*
   * @testName: getDeclaredSetStringClassIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1335;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredSetStringClassIllegalArgumentException() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        try {
          iType.getDeclaredSet("sAddress_inherited", Address.class);
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault(
          "getDeclaredSetStringClassIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getDeclaredSetString
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1336;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredSetString() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        SetAttribute sAttrib = iType.getDeclaredSet("sAddress");
        if (sAttrib != null) {
          TestUtil.logTrace("attribute Name = " + sAttrib.getName());
          String name = sAttrib.getName();
          if (name.equals("sAddress")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: sAddress, actual:" + name);
          }
        } else {
          TestUtil.logErr("getDeclaredSet(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getDeclaredSetString failed");
    }
  }

  /*
   * @testName: getDeclaredSetStringIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1337;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredSetStringIllegalArgumentException() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        try {
          iType.getDeclaredSet("sAddress_inherited");
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getDeclaredSetStringIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getDeclaredSingularAttributeStringClass
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1338;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredSingularAttributeStringClass() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        SingularAttribute<A, Integer> singAttrib = iType
            .getDeclaredSingularAttribute("value", Integer.class);
        if (singAttrib != null) {
          TestUtil.logTrace("attribute Name = " + singAttrib.getName());
          String name = singAttrib.getName();
          if (name.equals("value")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: value, actual:" + name);
          }
        } else {
          TestUtil.logErr("getDeclaredSingularAttribute(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getDeclaredSingularAttributeStringClass failed");
    }
  }

  /*
   * @testName: getDeclaredSingularAttributeStringClassIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1339;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredSingularAttributeStringClassIllegalArgumentException()
      throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        try {
          iType.getDeclaredSingularAttribute("name", String.class);
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault(
          "getDeclaredSingularAttributeStringClassIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getDeclaredSingularAttributeString
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1340;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredSingularAttributeString() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        SingularAttribute singAttrib = iType
            .getDeclaredSingularAttribute("value");
        if (singAttrib != null) {
          TestUtil.logTrace("attribute Name = " + singAttrib.getName());
          String name = singAttrib.getName();
          if (name.equals("value")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: value, actual:" + name);
          }
        } else {
          TestUtil.logErr("getDeclaredSingularAttribute(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getDeclaredSingularAttributeString failed");
    }
  }

  /*
   * @testName: getDeclaredSingularAttributeStringIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1341;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredSingularAttributeStringIllegalArgumentException()
      throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        try {
          iType.getDeclaredSingularAttribute("name");
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault(
          "getDeclaredSingularAttributeStringIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getDeclaredSingularAttributes
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1342;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredSingularAttributes() throws Fault {
    boolean pass = false;

    List<String> expected = new ArrayList<String>();

    expected.add("id");
    expected.add("name");
    expected.add("value");
    Collections.sort(expected);

    List<String> actual = new ArrayList<String>();

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        Set set = iType.getDeclaredSingularAttributes();
        if (set != null) {
          if (set.size() > 0) {
            for (Iterator i = set.iterator(); i.hasNext();) {
              Attribute attrib = (Attribute) i.next();
              actual.add(attrib.getName());
            }
            Collections.sort(actual);

            if (expected.containsAll(actual) && actual.containsAll(expected)
                && expected.size() == actual.size()) {

              TestUtil.logTrace("Received expected attributes");
              for (String attribName : expected) {
                TestUtil.logTrace("attrib:" + attribName);
              }
              pass = true;
            } else {
              TestUtil.logErr("Received unexpected attributes");
              TestUtil.logErr("Expected(" + expected.size() + "):");
              for (String attribName : expected) {
                TestUtil.logErr("attrib:" + attribName);
              }
              TestUtil.logErr("Actual(" + actual.size() + "):");
              for (String attribName : actual) {
                TestUtil.logErr("attrib:" + attribName);
              }
            }
          } else {
            TestUtil
                .logErr("getDeclaredSingularAttributes() returned 0 results");
          }
        } else {
          TestUtil.logErr("getDeclaredSingularAttributes() returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getDeclaredSingularAttributes failed");
    }
  }

  /*
   * @testName: getListStringClass
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1343;
   *
   * @test_Strategy:
   *
   */
  public void getListStringClass() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        ListAttribute lAttrib = iType.getList("lAddress", Address.class);
        if (lAttrib != null) {
          TestUtil.logTrace("attribute Name = " + lAttrib.getName());
          String name = lAttrib.getName();
          if (name.equals("lAddress")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: lAddress, actual:" + name);
          }
        } else {
          TestUtil.logErr("getList(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getListStringClass failed");
    }
  }

  /*
   * @testName: getListStringClassIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1344;
   *
   * @test_Strategy:
   *
   */
  public void getListStringClassIllegalArgumentException() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        try {
          iType.getList("doesnotexist", Address.class);
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getListStringClassIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getListString
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1345;
   *
   * @test_Strategy:
   *
   */
  public void getListString() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        ListAttribute lAttrib = iType.getList("lAddress");
        if (lAttrib != null) {
          TestUtil.logTrace("attribute Name = " + lAttrib.getName());
          String name = lAttrib.getName();
          if (name.equals("lAddress")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: lAddress, actual:" + name);
          }
        } else {
          TestUtil.logErr("getList(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getListString failed");
    }
  }

  /*
   * @testName: getListStringIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1346;
   *
   * @test_Strategy:
   *
   */
  public void getListStringIllegalArgumentException() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        try {
          iType.getDeclaredList("doesnotexist");
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getListStringIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getMapStringClassClass
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1347;
   *
   * @test_Strategy:
   *
   */
  public void getMapStringClassClass() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        MapAttribute mAttrib = iType.getMap("mAddress", Address.class,
            String.class);
        if (mAttrib != null) {
          TestUtil.logTrace("attribute Name = " + mAttrib.getName());
          String name = mAttrib.getName();
          if (name.equals("mAddress")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: mAddress, actual:" + name);
          }
        } else {
          TestUtil.logErr("getMap(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getMapStringClassClass failed");
    }
  }

  /*
   * @testName: getMapStringClassClassIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1348;
   *
   * @test_Strategy:
   *
   */
  public void getMapStringClassClassIllegalArgumentException() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        try {
          iType.getMap("doesnotexist", Address.class, String.class);
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getMapStringClassClassIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getMapString
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1349;
   *
   * @test_Strategy:
   *
   */
  public void getMapString() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        MapAttribute mAttrib = iType.getMap("mAddress");
        if (mAttrib != null) {
          TestUtil.logTrace("attribute Name = " + mAttrib.getName());
          String name = mAttrib.getName();
          if (name.equals("mAddress")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: mAddress, actual:" + name);
          }
        } else {
          TestUtil.logErr("getMap(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getMapString failed");
    }
  }

  /*
   * @testName: getMapStringIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1350;
   *
   * @test_Strategy:
   *
   */
  public void getMapStringIllegalArgumentException() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        try {
          iType.getMap("doesnotexist");
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getMapStringIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getPluralAttributes
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1351;
   *
   * @test_Strategy:
   *
   */
  public void getPluralAttributes() throws Fault {
    boolean pass = false;

    List<String> expected = new ArrayList<String>();

    expected.add("cAddress");
    expected.add("cAddress_inherited");
    expected.add("lAddress");
    expected.add("lAddress_inherited");
    expected.add("mAddress");
    expected.add("mAddress_inherited");
    expected.add("sAddress");
    expected.add("sAddress_inherited");
    Collections.sort(expected);

    List<String> actual = new ArrayList<String>();

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        Set set = iType.getPluralAttributes();
        if (set != null) {
          if (set.size() > 0) {
            for (Iterator i = set.iterator(); i.hasNext();) {
              Attribute attrib = (Attribute) i.next();
              actual.add(attrib.getName());
            }
            Collections.sort(actual);

            if (expected.containsAll(actual) && actual.containsAll(expected)
                && expected.size() == actual.size()) {

              TestUtil.logTrace("Received expected attributes");
              for (String attribName : expected) {
                TestUtil.logTrace("attrib:" + attribName);
              }
              pass = true;
            } else {
              TestUtil.logErr("Received unexpected attributes");
              TestUtil.logErr("Expected(" + expected.size() + "):");
              for (String attribName : expected) {
                TestUtil.logErr("attrib:" + attribName);
              }
              TestUtil.logErr("Actual(" + actual.size() + "):");
              for (String attribName : actual) {
                TestUtil.logErr("attrib:" + attribName);
              }
            }
          } else {
            TestUtil.logErr("getPluralAttributes() returned 0 results");
          }
        } else {
          TestUtil.logErr("getPluralAttributes() returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getPluralAttributes failed");
    }
  }

  /*
   * @testName: getDeclaredPluralAttributes
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1333;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredPluralAttributes() throws Fault {
    boolean pass = false;

    List<String> expected = new ArrayList<String>();

    expected.add("cAddress");
    expected.add("lAddress");
    expected.add("mAddress");
    expected.add("sAddress");
    Collections.sort(expected);

    List<String> actual = new ArrayList<String>();

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        Set set = iType.getDeclaredPluralAttributes();
        if (set != null) {
          if (set.size() > 0) {
            for (Iterator i = set.iterator(); i.hasNext();) {
              Attribute attrib = (Attribute) i.next();
              actual.add(attrib.getName());
            }
            Collections.sort(actual);

            if (expected.containsAll(actual) && actual.containsAll(expected)
                && expected.size() == actual.size()) {

              TestUtil.logTrace("Received expected attributes");
              for (String attribName : expected) {
                TestUtil.logTrace("attrib:" + attribName);
              }
              pass = true;
            } else {
              TestUtil.logErr("Received unexpected attributes");
              TestUtil.logErr("Expected(" + expected.size() + "):");
              for (String attribName : expected) {
                TestUtil.logErr("attrib:" + attribName);
              }
              TestUtil.logErr("Actual(" + actual.size() + "):");
              for (String attribName : actual) {
                TestUtil.logErr("attrib:" + attribName);
              }
            }
          } else {
            TestUtil.logErr("getPluralAttributes() returned 0 results");
          }
        } else {
          TestUtil.logErr("getPluralAttributes() returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getDeclaredPluralAttributes failed");
    }
  }

  /*
   * @testName: getSetStringClass
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1352;
   *
   * @test_Strategy:
   *
   */
  public void getSetStringClass() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        SetAttribute sAttrib = iType.getSet("sAddress", Address.class);
        if (sAttrib != null) {
          TestUtil.logTrace("attribute Name = " + sAttrib.getName());
          String name = sAttrib.getName();
          if (name.equals("sAddress")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: sAddress, actual:" + name);
          }
        } else {
          TestUtil.logErr("getSet(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getSetStringClass failed");
    }
  }

  /*
   * @testName: getSetStringClassIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1353;
   *
   * @test_Strategy:
   *
   */
  public void getSetStringClassIllegalArgumentException() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        try {
          iType.getSet("doesnotexist", Address.class);
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getSetStringClassIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getSetString
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1354;
   *
   * @test_Strategy:
   *
   */
  public void getSetString() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        SetAttribute sAttrib = iType.getSet("sAddress");
        if (sAttrib != null) {
          TestUtil.logTrace("attribute Name = " + sAttrib.getName());
          String name = sAttrib.getName();
          if (name.equals("sAddress")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: sAddress, actual:" + name);
          }
        } else {
          TestUtil.logErr("getSet(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getSetString failed");
    }
  }

  /*
   * @testName: getSetStringIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1355;
   *
   * @test_Strategy:
   *
   */
  public void getSetStringIllegalArgumentException() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        try {
          iType.getSet("doesnotexist");
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getSetStringIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getSingularAttributeStringClass
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1356;
   *
   * @test_Strategy:
   *
   */
  public void getSingularAttributeStringClass() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        SingularAttribute singAttrib = iType.getSingularAttribute("name",
            String.class);
        if (singAttrib != null) {
          TestUtil.logTrace("attribute Name = " + singAttrib.getName());
          String name = singAttrib.getName();
          if (name.equals("name")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: name, actual:" + name);
          }
        } else {
          TestUtil.logErr("getSingularAttribute(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getSingularAttributeStringClass failed");
    }
  }

  /*
   * @testName: getSingularAttributeStringClassIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1357;
   *
   * @test_Strategy:
   *
   */
  public void getSingularAttributeStringClassIllegalArgumentException()
      throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        try {
          iType.getSingularAttribute("doesnotexist", Address.class);
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault(
          "getSingularAttributeStringClassIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getSingularAttributeString
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1358;
   *
   * @test_Strategy:
   *
   */
  public void getSingularAttributeString() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        SingularAttribute singAttrib = iType.getSingularAttribute("name");
        if (singAttrib != null) {
          TestUtil.logTrace("attribute Name = " + singAttrib.getName());
          String name = singAttrib.getName();
          if (name.equals("name")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: name, actual:" + name);
          }
        } else {
          TestUtil.logErr("getSingularAttribute(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getSingularAttributeString failed");
    }
  }

  /*
   * @testName: getSingularAttributeStringIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1359;
   *
   * @test_Strategy:
   *
   */
  public void getSingularAttributeStringIllegalArgumentException()
      throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        try {
          iType.getSingularAttribute("doesnotexist");
          TestUtil.logErr("Did not receive expected IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
          TestUtil.logTrace("Received expected IllegalArgumentException");
          pass = true;
        } catch (Exception ex) {
          TestUtil.logErr("Received unexpected exception:", ex);
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault(
          "getSingularAttributeStringIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getSingularAttributes
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1360;
   *
   * @test_Strategy:
   *
   */
  public void getSingularAttributes() throws Fault {
    boolean pass = false;

    List<String> expected = new ArrayList<String>();

    expected.add("id");
    expected.add("name");
    expected.add("value");
    Collections.sort(expected);

    List<String> actual = new ArrayList<String>();

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      IdentifiableType<A> iType = metaModel.entity(A.class);
      if (iType != null) {
        TestUtil.logTrace("Obtained Non-null Entity A");
        Set set = iType.getSingularAttributes();
        if (set != null) {
          if (set.size() > 0) {
            for (Iterator i = set.iterator(); i.hasNext();) {
              Attribute attrib = (Attribute) i.next();
              actual.add(attrib.getName());
            }
            Collections.sort(actual);

            if (expected.containsAll(actual) && actual.containsAll(expected)
                && expected.size() == actual.size()) {

              TestUtil.logTrace("Received expected attributes");
              for (String attribName : expected) {
                TestUtil.logTrace("attrib:" + attribName);
              }
              pass = true;
            } else {
              TestUtil.logErr("Received unexpected attributes");
              TestUtil.logErr("Expected(" + expected.size() + "):");
              for (String attribName : expected) {
                TestUtil.logErr("attrib:" + attribName);
              }
              TestUtil.logErr("Actual(" + actual.size() + "):");
              for (String attribName : actual) {
                TestUtil.logErr("attrib:" + attribName);
              }
            }
          } else {
            TestUtil.logErr("getSingularAttributes() returned 0 results");
          }
        } else {
          TestUtil.logErr("getSingularAttributes(...) returned null");
        }
      } else {
        TestUtil.logErr("entity(...) returned null");
      }
    } else {
      TestUtil.logErr("getMetamodel() returned null");
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getSingularAttributes failed");
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
