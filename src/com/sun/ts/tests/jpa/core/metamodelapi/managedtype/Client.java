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

package com.sun.ts.tests.jpa.core.metamodelapi.managedtype;

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
   * @testName: managedtype
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1440
   *
   * @test_Strategy:
   *
   */
  public void managedtype() throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Order> mTypeOrder = metaModel.managedType(Order.class);
        if (mTypeOrder != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          pass = true;
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault("managedtype failed");
    }
  }

  /*
   * @testName: getAttributes
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1365
   *
   * @test_Strategy:
   *
   */
  public void getAttributes() throws Fault {
    boolean pass = true;

    Collection<String> expected = new ArrayList<String>();
    expected.add("id");
    expected.add("total");
    try {
      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Order> mTypeOrder = metaModel.managedType(Order.class);
        if (mTypeOrder != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          Set<Attribute<? super Order, ?>> attribSet = mTypeOrder
              .getAttributes();
          if (attribSet != null) {
            if (attribSet.size() != expected.size()) {
              pass = false;
              TestUtil.logErr("Received wrong number of results");
            }
            for (Attribute attrib : attribSet) {
              TestUtil.logTrace("attribute Name = " + attrib.getName());
              if (expected.contains(attrib.getName())) {
                TestUtil.logTrace("Received expected:" + attrib.getName());
              } else {
                TestUtil
                    .logErr("Received unexpected result" + attrib.getName());
                pass = false;
              }
            }
          } else {
            pass = false;
            TestUtil.logErr("getAttributes() returned null");
          }
        } else {
          pass = false;
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        pass = false;
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault("getAttributes failed");
    }
  }

  /*
   * @testName: getDeclaredAttributes
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1372
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredAttributes() throws Fault {
    boolean pass = true;

    Collection<String> expected = new ArrayList<String>();
    expected.add("id");
    expected.add("total");
    try {
      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Order> mTypeOrder = metaModel.managedType(Order.class);
        if (mTypeOrder != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          Set<Attribute<Order, ?>> attribSet = mTypeOrder
              .getDeclaredAttributes();
          if (attribSet != null) {
            if (attribSet.size() != expected.size()) {
              pass = false;
              TestUtil.logErr("Received wrong number of results");
            }
            for (Attribute attrib : attribSet) {
              TestUtil.logTrace("attribute Name = " + attrib.getName());
              if (expected.contains(attrib.getName())) {
                TestUtil.logTrace("Received expected:" + attrib.getName());
              } else {
                TestUtil
                    .logErr("Received unexpected result" + attrib.getName());
                pass = false;
              }
            }
          } else {
            pass = false;
            TestUtil.logErr("getAttributes() returned null");
          }
        } else {
          pass = false;
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        pass = false;
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault("getDeclaredAttributes failed");
    }
  }

  /*
   * @testName: getSingularAttributeStringClassTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1408; PERSISTENCE:JAVADOC:1413;
   *
   * @test_Strategy:
   *
   */
  public void getSingularAttributeStringClassTest() throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<B> mTypeB = metaModel.managedType(B.class);
        if (mTypeB != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          SingularAttribute<? super B, Address> singAttrib = mTypeB
              .getSingularAttribute("address",
                  com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address.class);
          Class addrClass = singAttrib.getType().getJavaType();
          if (addrClass.getName().equals(
              "com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address")) {
            TestUtil.logTrace("address class getName =" + addrClass.getName());
            pass = true;
          } else {
            TestUtil.logErr(
                "Expected: com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address, actual:"
                    + addrClass.getName());
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getSingularAttributeStringClassTest failed");
    }
  }

  /*
   * @testName: getSingularAttributeStringClassIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1409
   *
   * @test_Strategy:
   *
   */
  public void getSingularAttributeStringClassIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = true;

    try {
      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<B> mTypeB = metaModel.managedType(B.class);
        if (mTypeB != null) {
          TestUtil.logMsg("Testing invalid name");

          try {
            mTypeB.getSingularAttribute("doesnotexist",
                com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address.class);
            pass = false;
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
          } catch (Exception e) {
            pass = false;
            TestUtil.logErr("Received unexpected exception", e);
          }
          TestUtil.logMsg("Testing invalid type");

          try {
            mTypeB.getSingularAttribute("addreess",
                com.sun.ts.tests.jpa.core.metamodelapi.managedtype.ZipCode.class);
            pass = false;
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
          } catch (Exception e) {
            pass = false;
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          pass = false;
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        pass = false;
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault(
          "getSingularAttributeStringClassIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getSingularAttributeStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1410
   *
   * @test_Strategy:
   *
   */

  public void getSingularAttributeStringTest() throws Fault {
    boolean pass = false;
    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<B> mTypeB = metaModel.managedType(B.class);
        if (mTypeB != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          SingularAttribute<? super B, ?> singAttrib = mTypeB
              .getSingularAttribute("address");
          TestUtil.logTrace("singAttrib Type = " + singAttrib.getType());
          TestUtil
              .logTrace("singAttrib Java Type = " + singAttrib.getJavaType());
          if (singAttrib.getJavaType().getName().equals(
              "com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address")) {
            pass = true;
          } else {
            TestUtil.logErr("Expected: address, actual:"
                + singAttrib.getJavaType().getName());
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getSingularAttributeStringTest failed");
    }
  }

  /*
   * @testName: getSingularAttributeStringIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1411
   *
   * @test_Strategy:
   *
   */

  public void getSingularAttributeStringIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<B> mTypeB = metaModel.managedType(B.class);
        if (mTypeB != null) {
          TestUtil.logMsg("Testing invalid name");

          try {
            mTypeB.getSingularAttribute("doesnotexist");
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
            pass = true;
          } catch (Exception e) {
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault(
          "getSingularAttributeStringIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getDeclaredSingularAttributeStringClassTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1390
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredSingularAttributeStringClassTest() throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<B> mTypeB = metaModel.managedType(B.class);
        if (mTypeB != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          SingularAttribute<B, Address> singAttrib = mTypeB
              .getDeclaredSingularAttribute("address",
                  com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address.class);
          Class addrClass = singAttrib.getType().getJavaType();
          if (addrClass.getName().equals(
              "com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address")) {
            TestUtil.logTrace("address class getName =" + addrClass.getName());
            pass = true;
          } else {
            TestUtil.logErr(
                "Expected: com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address, actual:"
                    + addrClass.getName());
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getDeclaredSingularAttributeStringClassTest failed");
    }
  }

  /*
   * @testName:
   * getDeclaredSingularAttributeStringClassIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1391
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredSingularAttributeStringClassIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = true;

    try {
      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<B> mTypeB = metaModel.managedType(B.class);
        if (mTypeB != null) {
          TestUtil.logMsg("Testing invalid name");
          try {
            mTypeB.getDeclaredSingularAttribute("doesnotexist",
                com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address.class);
            pass = false;
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
          } catch (Exception e) {
            pass = false;
            TestUtil.logErr("Received unexpected exception", e);
          }

          TestUtil.logMsg("Testing invalid type");

          try {
            mTypeB.getDeclaredSingularAttribute("address",
                com.sun.ts.tests.jpa.core.metamodelapi.managedtype.ZipCode.class);
            pass = false;
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
          } catch (Exception e) {
            pass = false;
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          pass = false;
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        pass = false;
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault(
          "getDeclaredSingularAttributeStringClassIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getDeclaredSingularAttributeStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1392
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredSingularAttributeStringTest() throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<B> mTypeB = metaModel.managedType(B.class);
        if (mTypeB != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          SingularAttribute<B, ?> singAttrib = mTypeB
              .getDeclaredSingularAttribute("address");
          TestUtil.logTrace("singAttrib Type = " + singAttrib.getType());
          TestUtil
              .logTrace("singAttrib Java Type = " + singAttrib.getJavaType());
          if (singAttrib.getJavaType().getName().equals(
              "com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address")) {
            pass = true;
          } else {
            TestUtil.logErr(
                "Expected: com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address, actual:"
                    + singAttrib.getJavaType().getName());
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getDeclaredSingularAttributeStringTest failed");
    }
  }

  /*
   * @testName: getDeclaredSingularAttributeStringIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1393
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredSingularAttributeStringIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<B> mTypeB = metaModel.managedType(B.class);
        if (mTypeB != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          try {
            mTypeB.getDeclaredSingularAttribute("doesnotexist");
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
            pass = true;
          } catch (Exception e) {
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault(
          "getDeclaredSingularAttributeStringIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getDeclaredSingularAttributes
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1394
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredSingularAttributes() throws Fault {
    boolean pass = true;
    Collection<String> expected = new ArrayList<String>();
    expected.add("id");
    expected.add("address");
    expected.add("name");
    expected.add("value");

    try {
      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<B> mTypeB = metaModel.managedType(B.class);
        if (mTypeB != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          Set<SingularAttribute<B, ?>> attribSet = mTypeB
              .getDeclaredSingularAttributes();
          if (attribSet != null) {
            if (attribSet.size() != expected.size()) {
              pass = false;
              TestUtil.logErr("Received wrong number of results");
            }
            for (Attribute attrib : attribSet) {
              TestUtil.logTrace("attribute Name = " + attrib.getName());
              if (expected.contains(attrib.getName())) {
                TestUtil.logTrace("Received expected:" + attrib.getName());
              } else {
                TestUtil
                    .logErr("Received unexpected result" + attrib.getName());
                pass = false;
              }
            }
          } else {
            pass = false;
            TestUtil.logErr("getDeclaredSingularAttributes() returned null");
          }
        } else {
          pass = false;
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        pass = false;
        TestUtil.logErr("getMetamodel() returned null");
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault("getDeclaredSingularAttributes failed");
    }
  }

  /*
   * @testName: getSingularAttributes
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1412
   *
   * @test_Strategy:
   *
   */
  public void getSingularAttributes() throws Fault {
    boolean pass = true;
    Collection<String> expected = new ArrayList<String>();
    expected.add("id");
    expected.add("address");
    expected.add("name");
    expected.add("value");
    try {
      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<B> mTypeB = metaModel.managedType(B.class);
        if (mTypeB != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          Set<SingularAttribute<? super B, ?>> attribSet = mTypeB
              .getSingularAttributes();
          if (attribSet != null) {
            if (attribSet.size() != expected.size()) {
              pass = false;
              TestUtil.logErr("Received wrong number of results");
            }
            for (Attribute attrib : attribSet) {
              TestUtil.logTrace("attribute Name = " + attrib.getName());
              if (expected.contains(attrib.getName())) {
                TestUtil.logTrace("Received expected:" + attrib.getName());
              } else {
                TestUtil
                    .logErr("Received unexpected result" + attrib.getName());
                pass = false;
              }
            }
          } else {
            pass = false;
            TestUtil.logErr("getSingularAttributes() returned null");
          }
        } else {
          pass = false;
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        pass = false;
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault("getSingularAttributes failed");
    }
  }

  /*
   * @testName: getCollectionStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1368
   *
   * @test_Strategy:
   *
   */
  public void getCollectionStringTest() throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Uni1XMPerson> mType = metaModel
            .managedType(Uni1XMPerson.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          CollectionAttribute<? super Uni1XMPerson, ?> colAttrib = mType
              .getCollection("projects");
          Type t = colAttrib.getElementType();
          if (t != null) {
            TestUtil.logTrace("element Type  = " + t.getJavaType());
            if (t.getJavaType().getName().equals(
                "com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Uni1XMProject")) {
              TestUtil.logTrace(
                  "Received expected com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Uni1XMProject");
              pass = true;
            } else {
              TestUtil.logErr(
                  "Expected: com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Uni1XMProject, actual:"
                      + t.getJavaType().getName());
            }
          } else {
            TestUtil.logErr("getElementType() returned null");
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getCollectionStringTest failed");
    }
  }

  /*
   * @testName: getCollectionStringIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1369
   *
   * @test_Strategy:
   *
   */
  public void getCollectionStringIllegalArgumentExceptionTest() throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Uni1XMPerson> mType = metaModel
            .managedType(Uni1XMPerson.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          TestUtil.logMsg("Testing invalid name");
          try {
            mType.getCollection("doesnotexist");
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
            pass = true;
          } catch (Exception e) {
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getCollectionStringIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getCollectionStringClassTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1366
   *
   * @test_Strategy:
   *
   */
  public void getCollectionStringClassTest() throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Uni1XMPerson> mType = metaModel
            .managedType(Uni1XMPerson.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          CollectionAttribute<? super Uni1XMPerson, Uni1XMProject> colAttrib = mType
              .getCollection("projects",
                  com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Uni1XMProject.class);
          Type t = colAttrib.getElementType();
          if (t != null) {
            if (t.getJavaType().getName().equals(
                "com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Uni1XMProject")) {
              TestUtil.logTrace(
                  "Received expected com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Uni1XMProject");
              pass = true;
            } else {
              TestUtil.logErr(
                  "Expected: com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Uni1XMProject, actual:"
                      + t.getJavaType().getName());
            }
          } else {
            TestUtil.logErr("getElementType() returned null");
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getCollectionStringClassTest failed");
    }
  }

  /*
   * @testName: getCollectionStringClassIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1367
   *
   * @test_Strategy:
   *
   */
  public void getCollectionStringClassIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = true;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Uni1XMPerson> mType = metaModel
            .managedType(Uni1XMPerson.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          TestUtil.logMsg("Testing invalid name");
          try {
            mType.getCollection("doesnotexist",
                com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Uni1XMProject.class);
            pass = false;
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
          } catch (Exception e) {
            pass = false;
            TestUtil.logErr("Received unexpected exception", e);
          }
          TestUtil.logMsg("Testing invalid type");

          try {
            mType.getCollection("projects",
                com.sun.ts.tests.jpa.core.metamodelapi.managedtype.ZipCode.class);
            pass = false;
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
          } catch (Exception e) {
            pass = false;
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          pass = false;
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        pass = false;
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault(
          "getCollectionStringClassIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getSetStringClassTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1404
   *
   * @test_Strategy:
   *
   */
  public void getSetStringClassTest() throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<A> mType = metaModel.managedType(A.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          SetAttribute<? super A, Address> setAttrib = mType.getSet("address",
              com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address.class);
          Type t = setAttrib.getElementType();
          if (t != null) {
            TestUtil.logTrace("element Type  = " + t.getJavaType());
            if (t.getJavaType().getName().equals(
                "com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address")) {
              pass = true;
            } else {
              TestUtil.logErr(
                  "Expected: com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address, actual:"
                      + t.getJavaType().getName());
            }
          } else {
            TestUtil.logErr("getElementType() returned null");
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault("getSetStringClassTest failed");
    }
  }

  /*
   * @testName: getSetStringClassIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1405
   *
   * @test_Strategy:
   *
   */
  public void getSetStringClassIllegalArgumentExceptionTest() throws Fault {
    boolean pass = true;
    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<A> mType = metaModel.managedType(A.class);
        if (mType != null) {
          TestUtil.logMsg("Testing invalid name");
          try {
            mType.getSet("doesnotexist",
                com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address.class);
            pass = false;
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
          } catch (Exception e) {
            pass = false;
            TestUtil.logErr("Received unexpected exception", e);
          }

          TestUtil.logMsg("Testing invalid type");

          try {
            mType.getCollection("address",
                com.sun.ts.tests.jpa.core.metamodelapi.managedtype.ZipCode.class);
            pass = false;
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
          } catch (Exception e) {
            pass = false;
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          pass = false;
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        pass = false;
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault("getSetStringClassIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getSetStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1406
   *
   * @test_Strategy:
   *
   */
  public void getSetStringTest() throws Fault {
    boolean pass = false;
    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<A> mType = metaModel.managedType(A.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          SetAttribute<? super A, ?> setAttrib = mType.getSet("address");
          Type t = setAttrib.getElementType();
          TestUtil.logTrace("element Type  = " + setAttrib.getElementType());
          if (t != null) {
            TestUtil.logTrace("java Type  = " + t.getJavaType());
            if (t.getJavaType().getName().equals(
                "com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address")) {
              pass = true;
            } else {
              TestUtil.logErr(
                  "Expected: com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address, actual:"
                      + t.getJavaType().getName());
            }
          } else {
            TestUtil.logErr("getElementType() returned null");
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getSetStringTest failed");
    }
  }

  /*
   * @testName: getSetStringIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1407
   *
   * @test_Strategy:
   *
   */
  public void getSetStringIllegalArgumentExceptionTest() throws Fault {
    boolean pass = false;
    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<A> mType = metaModel.managedType(A.class);
        if (mType != null) {
          TestUtil.logMsg("Testing invalid name");
          try {
            mType.getSet("doesnotexist");
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
            pass = true;
          } catch (Exception e) {
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getSetStringIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getListStringClassTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1395
   *
   * @test_Strategy:
   *
   */
  public void getListStringClassTest() throws Fault {
    boolean pass = false;
    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<BiDirMX1Project> mType = metaModel
            .managedType(BiDirMX1Project.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          ListAttribute<? super BiDirMX1Project, BiDirMX1Person> listAttrib = mType
              .getList("biDirMX1Persons",
                  com.sun.ts.tests.jpa.core.metamodelapi.managedtype.BiDirMX1Person.class);
          Type t = listAttrib.getElementType();
          if (t != null) {
            TestUtil.logTrace("element Type  = " + t.getJavaType());
            if (t.getJavaType().getName().equals(
                "com.sun.ts.tests.jpa.core.metamodelapi.managedtype.BiDirMX1Person")) {
              pass = true;
            } else {
              TestUtil.logErr(
                  "Expected: com.sun.ts.tests.jpa.core.metamodelapi.managedtype.BiDirMX1Person, actual:"
                      + t.getJavaType().getName());
            }
          } else {
            TestUtil.logErr("getElementType() returned null");
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getListStringClassTest failed");
    }
  }

  /*
   * @testName: getListStringClassIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1396
   *
   * @test_Strategy:
   *
   */
  public void getListStringClassIllegalArgumentExceptionTest() throws Fault {
    boolean pass = true;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<BiDirMX1Project> mType = metaModel
            .managedType(BiDirMX1Project.class);
        if (mType != null) {
          TestUtil.logMsg("Testing invalid name");

          try {
            mType.getList("doesnotexist",
                com.sun.ts.tests.jpa.core.metamodelapi.managedtype.BiDirMX1Person.class);
            pass = false;
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
          } catch (Exception e) {
            pass = false;
            TestUtil.logErr("Received unexpected exception", e);
          }

          TestUtil.logMsg("Testing invalid type");

          try {
            mType.getCollection("biDirMX1Persons",
                com.sun.ts.tests.jpa.core.metamodelapi.managedtype.ZipCode.class);
            pass = false;
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
          } catch (Exception e) {
            pass = false;
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          pass = false;
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        pass = false;
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault("getListStringClassIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getListStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1397
   *
   * @test_Strategy:
   *
   */
  public void getListStringTest() throws Fault {
    boolean pass = false;
    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<BiDirMX1Project> mType = metaModel
            .managedType(BiDirMX1Project.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          ListAttribute<? super BiDirMX1Project, ?> listAttrib = mType
              .getList("biDirMX1Persons");
          Type t = listAttrib.getElementType();
          if (t != null) {
            TestUtil.logTrace("element Type  = " + t.getJavaType());
            if (t.getJavaType().getName().equals(
                "com.sun.ts.tests.jpa.core.metamodelapi.managedtype.BiDirMX1Person")) {
              pass = true;
            } else {
              TestUtil.logErr(
                  "Expected: com.sun.ts.tests.jpa.core.metamodelapi.managedtype.BiDirMX1Person, actual:"
                      + t.getJavaType().getName());
            }
          } else {
            TestUtil.logErr("getElementType() returned null");
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getListStringTest failed");
    }
  }

  /*
   * @testName: getListStringIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1398
   *
   * @test_Strategy:
   *
   */
  public void getListStringIllegalArgumentExceptionTest() throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<BiDirMX1Project> mType = metaModel
            .managedType(BiDirMX1Project.class);
        if (mType != null) {
          TestUtil.logMsg("Testing invalid name");
          try {
            mType.getList("doesnotexist");
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
            pass = true;
          } catch (Exception e) {
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getListStringIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getMapStringClassTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1399
   *
   * @test_Strategy:
   *
   */
  public void getMapStringClassTest() throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Department> mType = metaModel.managedType(Department.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          MapAttribute<? super Department, String, Employee> mapAttrib = mType
              .getMap("lastNameEmployees", java.lang.String.class,
                  Employee.class);
          Type t = mapAttrib.getElementType();
          if (t != null) {
            TestUtil.logTrace("element Type  = " + t.getJavaType());
            if (t.getJavaType().getName().equals(
                "com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Employee")) {
              pass = true;
            } else {
              TestUtil.logErr(
                  "Expected: com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Employee, actual:"
                      + t.getJavaType().getName());
            }
          } else {
            TestUtil.logErr("getElementType() returned null");
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getMapStringClassTest failed");
    }
  }

  /*
   * @testName: getMapStringClassIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1400
   *
   * @test_Strategy:
   *
   */
  public void getMapStringClassIllegalArgumentExceptionTest() throws Fault {
    boolean pass = true;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Department> mType = metaModel.managedType(Department.class);
        if (mType != null) {
          TestUtil.logMsg("Testing invalid name");
          try {
            mType.getMap("doesnotexist", java.lang.String.class,
                Employee.class);
            pass = false;
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
          } catch (Exception e) {
            pass = false;
            TestUtil.logErr("Received unexpected exception", e);
          }

          TestUtil.logMsg("Testing invalid type");

          try {
            mType.getMap("lastNameEmployees", String.class,
                com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Client.class);
            pass = false;
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
          } catch (Exception e) {
            pass = false;
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          pass = false;
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        pass = false;
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault("getMapStringClassIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getMapStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1401
   *
   * @test_Strategy:
   *
   */
  public void getMapStringTest() throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Department> mType = metaModel.managedType(Department.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          MapAttribute<? super Department, ?, ?> mapAttrib = mType
              .getMap("lastNameEmployees");
          Type t = mapAttrib.getElementType();
          if (t != null) {
            TestUtil.logTrace("element Type  = " + t.getJavaType());
            if (t.getJavaType().getName().equals(
                "com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Employee")) {
              pass = true;
            } else {
              TestUtil.logErr(
                  "Expected: com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Employee, actual:"
                      + t.getJavaType().getName());
            }
          } else {
            TestUtil.logErr("getElementType() returned null");
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getMapStringTest failed");
    }
  }

  /*
   * @testName: getMapStringIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1402
   *
   * @test_Strategy:
   *
   */
  public void getMapStringIllegalArgumentExceptionTest() throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Department> mType = metaModel.managedType(Department.class);
        if (mType != null) {
          TestUtil.logMsg("Testing invalid name");
          try {
            mType.getMap("doesnotexist");
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
            pass = true;
          } catch (Exception e) {
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getMapStringIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getDeclaredCollectionStringClassTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1373
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredCollectionStringClassTest() throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Uni1XMPerson> mType = metaModel
            .managedType(Uni1XMPerson.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          CollectionAttribute<Uni1XMPerson, Uni1XMProject> colAttrib = mType
              .getDeclaredCollection("projects",
                  com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Uni1XMProject.class);
          Type t = colAttrib.getElementType();
          if (t != null) {
            String type = t.getJavaType().getName();
            TestUtil.logTrace("element Type  = " + type);
            if (type.equals(
                "com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Uni1XMProject")) {
              pass = true;
            } else {
              TestUtil.logErr(
                  "Expected: com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Uni1XMProject, actual:"
                      + type);
            }
          } else {
            TestUtil.logErr("getElementType() returned null");
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getDeclaredCollectionStringClassTest failed");
    }
  }

  /*
   * @testName: getDeclaredCollectionStringClassIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1374
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredCollectionStringClassIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = true;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Uni1XMPerson> mType = metaModel
            .managedType(Uni1XMPerson.class);
        if (mType != null) {
          TestUtil.logMsg("Testing invalid name");
          try {
            mType.getDeclaredCollection("doesnotexist",
                com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Uni1XMProject.class);
            pass = false;
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
          } catch (Exception e) {
            pass = false;
            TestUtil.logErr("Received unexpected exception", e);
          }

          TestUtil.logMsg("Testing invalid type");

          try {
            mType.getCollection("projects",
                com.sun.ts.tests.jpa.core.metamodelapi.managedtype.ZipCode.class);
            pass = false;
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
          } catch (Exception e) {
            pass = false;
            TestUtil.logErr("Received unexpected exception", e);
          }

        } else {
          pass = false;
          TestUtil.logErr("managedType() returned null");
        }

      } else {
        pass = false;
        TestUtil.logErr("getMetamodel() returned null");
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault(
          "getDeclaredCollectionStringClassIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getDeclaredCollectionStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1375
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredCollectionStringTest() throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Uni1XMPerson> mType = metaModel
            .managedType(Uni1XMPerson.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          CollectionAttribute<Uni1XMPerson, ?> colAttrib = mType
              .getDeclaredCollection("projects");
          Type t = colAttrib.getElementType();
          if (t != null) {
            String type = t.getJavaType().getName();
            TestUtil.logTrace("element Type  = " + type);
            if (type.equals(
                "com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Uni1XMProject")) {
              pass = true;
            } else {
              TestUtil.logErr(
                  "Expected: com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Uni1XMProject, actual:"
                      + type);
            }
          } else {
            TestUtil.logErr("getElementType() returned null");
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getDeclaredCollectionStringTest failed");
    }
  }

  /*
   * @testName: getDeclaredCollectionStringIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1376
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredCollectionStringIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Uni1XMPerson> mType = metaModel
            .managedType(Uni1XMPerson.class);
        if (mType != null) {
          TestUtil.logMsg("Testing invalid name");
          try {
            mType.getDeclaredCollection("doesnotexist");
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
            pass = true;
          } catch (Exception e) {
            TestUtil.logErr("Received unexpected exception", e);
          }

        } else {
          TestUtil.logErr("managedType() returned null");
        }

      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault(
          "getDeclaredCollectionStringIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getDeclaredSetStringClassTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1386
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredSetStringClassTest() throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<A> mType = metaModel.managedType(A.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          SetAttribute<A, Address> setAttrib = mType.getDeclaredSet("address",
              com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address.class);
          Type t = setAttrib.getElementType();
          if (t != null) {
            TestUtil.logTrace("element Type  = " + t.getJavaType());
            if (t.getJavaType().getName().equals(
                "com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address")) {
              pass = true;
            } else {
              TestUtil.logErr(
                  "Expected: com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address, actual:"
                      + t.getJavaType().getName());
            }
          } else {
            TestUtil.logErr("getElementType() returned null");
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getDeclaredSetStringClassTest failed");
    }
  }

  /*
   * @testName: getDeclaredSetStringClassIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1387
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredSetStringClassIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = true;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<A> mType = metaModel.managedType(A.class);
        if (mType != null) {
          TestUtil.logMsg("Testing invalid name");
          try {
            mType.getDeclaredSet("doesnotexist",
                com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address.class);
            pass = false;
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
          } catch (Exception e) {
            pass = false;
            TestUtil.logErr("Received unexpected exception", e);
          }

          TestUtil.logMsg("Testing invalid type");

          try {
            mType.getCollection("address",
                com.sun.ts.tests.jpa.core.metamodelapi.managedtype.ZipCode.class);
            pass = false;
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
          } catch (Exception e) {
            pass = false;
            TestUtil.logErr("Received unexpected exception", e);
          }

        } else {
          pass = false;
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        pass = false;
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault(
          "getDeclaredSetStringClassIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getDeclaredSetStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1388
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredSetStringTest() throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<A> mType = metaModel.managedType(A.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          SetAttribute<A, ?> setAttrib = mType.getDeclaredSet("address");
          TestUtil.logTrace("element Type  = " + setAttrib.getElementType());
          Type t = setAttrib.getElementType();
          if (t != null) {
            TestUtil.logTrace("element Type  = " + t.getJavaType());
            if (t.getJavaType().getName().equals(
                "com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address")) {
              pass = true;
            } else {
              TestUtil.logErr(
                  "Expected: com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Address, actual:"
                      + t.getJavaType().getName());
            }
          } else {
            TestUtil.logErr("getElementType() returned null");
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getDeclaredSetStringTest failed");
    }
  }

  /*
   * @testName: getDeclaredSetStringIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1389
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredSetStringIllegalArgumentExceptionTest() throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<A> mType = metaModel.managedType(A.class);
        if (mType != null) {
          TestUtil.logMsg("Testing invalid name");
          try {
            mType.getDeclaredSet("doesnotexist");
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
            pass = true;
          } catch (Exception e) {
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault(
          "getDeclaredSetStringIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getDeclaredListStringClassTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1377
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredListStringClassTest() throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<BiDirMX1Project> mType = metaModel
            .managedType(BiDirMX1Project.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          ListAttribute<BiDirMX1Project, BiDirMX1Person> listAttrib = mType
              .getDeclaredList("biDirMX1Persons",
                  com.sun.ts.tests.jpa.core.metamodelapi.managedtype.BiDirMX1Person.class);
          Type t = listAttrib.getElementType();
          if (t != null) {
            TestUtil.logTrace("element Type  = " + t.getJavaType());
            if (t.getJavaType().getName().equals(
                "com.sun.ts.tests.jpa.core.metamodelapi.managedtype.BiDirMX1Person")) {
              pass = true;
            } else {
              TestUtil.logErr(
                  "Expected: com.sun.ts.tests.jpa.core.metamodelapi.managedtype.BiDirMX1Person, actual:"
                      + t.getJavaType().getName());
            }
          } else {
            TestUtil.logErr("getElementType() returned null");
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getDeclaredListStringClassTest failed");
    }
  }

  /*
   * @testName: getDeclaredListStringClassIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1378
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredListStringClassIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = true;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<BiDirMX1Project> mType = metaModel
            .managedType(BiDirMX1Project.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");

          TestUtil.logMsg("Testing invalid name");
          try {
            mType.getDeclaredList("doesnotexist",
                com.sun.ts.tests.jpa.core.metamodelapi.managedtype.BiDirMX1Person.class);
            pass = false;
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
          } catch (Exception e) {
            pass = false;
            TestUtil.logErr("Received unexpected exception", e);
          }

          TestUtil.logMsg("Testing invalid type");

          try {
            mType.getCollection("biDirMX1Persons",
                com.sun.ts.tests.jpa.core.metamodelapi.managedtype.ZipCode.class);
            pass = false;
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
          } catch (Exception e) {
            pass = false;
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          pass = false;
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        pass = false;
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault(
          "getDeclaredListStringClassIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getDeclaredListStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1379
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredListStringTest() throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<BiDirMX1Project> mType = metaModel
            .managedType(BiDirMX1Project.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          ListAttribute<BiDirMX1Project, ?> listAttrib = mType
              .getDeclaredList("biDirMX1Persons");
          Type t = listAttrib.getElementType();
          if (t != null) {
            TestUtil.logTrace("element Type  = " + t.getJavaType());
            if (t.getJavaType().getName().equals(
                "com.sun.ts.tests.jpa.core.metamodelapi.managedtype.BiDirMX1Person")) {
              pass = true;
            } else {
              TestUtil.logErr(
                  "Expected: com.sun.ts.tests.jpa.core.metamodelapi.managedtype.BiDirMX1Person, actual:"
                      + t.getJavaType().getName());
            }
          } else {
            TestUtil.logErr("getElementType() returned null");
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getDeclaredListStringTest failed");
    }
  }

  /*
   * @testName: getDeclaredListStringIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1380
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredListStringIllegalArgumentExceptionTest() throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<BiDirMX1Project> mType = metaModel
            .managedType(BiDirMX1Project.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          TestUtil.logMsg("Testing invalid name");
          try {
            mType.getDeclaredList("doesnotexist");
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
            pass = true;
          } catch (Exception e) {
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault(
          "getDeclaredListStringIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getDeclaredMapStringClassClassTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1381
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredMapStringClassClassTest() throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Department> mType = metaModel.managedType(Department.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          MapAttribute<Department, String, Employee> mapAttrib = mType
              .getDeclaredMap("lastNameEmployees", java.lang.String.class,
                  Employee.class);
          Type t = mapAttrib.getElementType();
          if (t != null) {
            TestUtil.logTrace("element Type  = " + t.getJavaType());
            if (t.getJavaType().getName().equals(
                "com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Employee")) {
              pass = true;
            } else {
              TestUtil.logErr(
                  "Expected: com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Employee, actual:"
                      + t.getJavaType().getName());
            }
          } else {
            TestUtil.logErr("getElementType() returned null");
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getDeclaredMapStringClassClassTest failed");
    }
  }

  /*
   * @testName: getDeclaredMapStringClassClassIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1382
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredMapStringClassClassIllegalArgumentExceptionTest()
      throws Fault {
    boolean pass = true;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Department> mType = metaModel.managedType(Department.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");

          TestUtil.logMsg("Testing invalid name");
          try {
            mType.getDeclaredMap("doesnotexist", java.lang.String.class,
                Employee.class);
            pass = false;
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
          } catch (Exception e) {
            pass = false;
            TestUtil.logErr("Received unexpected exception", e);
          }

          TestUtil.logMsg("Testing invalid type");

          try {
            mType.getDeclaredMap("lastNameEmployees", String.class,
                com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Client.class);
            pass = false;
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
          } catch (Exception e) {
            pass = false;
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          pass = false;
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        pass = false;
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault(
          "getDeclaredMapStringClassClassIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getDeclaredMapStringTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1383
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredMapStringTest() throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Department> mType = metaModel.managedType(Department.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          MapAttribute<Department, ?, ?> mapAttrib = mType
              .getDeclaredMap("lastNameEmployees");
          Type t = mapAttrib.getElementType();
          if (t != null) {
            TestUtil.logTrace("element Type  = " + t.getJavaType());
            if (t.getJavaType().getName().equals(
                "com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Employee")) {
              pass = true;
            } else {
              TestUtil.logErr(
                  "Expected: com.sun.ts.tests.jpa.core.metamodelapi.managedtype.Employee, actual:"
                      + t.getJavaType().getName());
            }
          } else {
            TestUtil.logErr("getElementType() returned null");
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getDeclaredMapStringTest failed");
    }
  }

  /*
   * @testName: getDeclaredMapStringIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1384
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredMapStringIllegalArgumentExceptionTest() throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Department> mType = metaModel.managedType(Department.class);
        if (mType != null) {
          TestUtil.logMsg("Testing invalid name");
          try {
            mType.getDeclaredMap("doesnotexist");
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
            pass = true;
          } catch (Exception e) {
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault(
          "getDeclaredMapStringIllegalArgumentExceptionTest failed");
    }
  }

  /*
   * @testName: getPluralAttributes
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1403
   *
   * @test_Strategy:
   *
   */
  public void getPluralAttributes() throws Fault {
    boolean pass = true;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Uni1XMPerson> mType = metaModel
            .managedType(Uni1XMPerson.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          Set<PluralAttribute<? super Uni1XMPerson, ?, ?>> pluralAttribSet = mType
              .getPluralAttributes();
          if (pluralAttribSet != null) {
            if (pluralAttribSet.size() != 1) {
              pass = false;
              TestUtil
                  .logErr("Expected size:1, actual:" + pluralAttribSet.size());
            }
            for (Attribute attrib : pluralAttribSet) {
              if (attrib.getName().equals("projects")) {
                TestUtil.logTrace("attribute Name = " + attrib.getName());
              } else {
                pass = false;
                TestUtil
                    .logErr("Expected: projects, actual:" + attrib.getName());
              }
            }
          } else {
            pass = false;
            TestUtil.logErr("getPluralAttributes() returned null");
          }
        } else {
          pass = false;
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        pass = false;
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault("getPluralAttributes failed");
    }
  }

  /*
   * @testName: getDeclaredPluralAttributes
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1385
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredPluralAttributes() throws Fault {
    boolean pass = true;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Uni1XMPerson> mType = metaModel
            .managedType(Uni1XMPerson.class);
        if (mType != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          Set<PluralAttribute<Uni1XMPerson, ?, ?>> pluralAttribSet = mType
              .getDeclaredPluralAttributes();
          if (pluralAttribSet != null) {
            if (pluralAttribSet.size() != 1) {
              pass = false;
              TestUtil
                  .logErr("Expected size:1, actual:" + pluralAttribSet.size());
            }
            for (Attribute attrib : pluralAttribSet) {
              if (attrib.getName().equals("projects")) {
                TestUtil.logTrace("attribute Name = " + attrib.getName());
              } else {
                pass = false;
                TestUtil
                    .logErr("Expected: projects, actual:" + attrib.getName());
              }
            }
          } else {
            pass = false;
            TestUtil.logErr("getPluralAttributes() returned null");
          }
        } else {
          pass = false;
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        pass = false;
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
      pass = false;
    }
    if (!pass) {
      throw new Fault("getDeclaredPluralAttributes failed");
    }
  }

  /*
   * @testName: getAttribute
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1363
   *
   * @test_Strategy:
   *
   */
  public void getAttribute() throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Order> mTypeOrder = metaModel.managedType(Order.class);
        if (mTypeOrder != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          Attribute<? super Order, ?> attrib = mTypeOrder.getAttribute("total");
          if (attrib != null) {

            TestUtil.logTrace("attribute Name = " + attrib.getName());
            TestUtil.logTrace("attribute Java Type =" + attrib.getJavaType());
            String name = attrib.getName();
            String type = attrib.getJavaType().getSimpleName();
            if (name.equals("total") && type.equals("int")) {
              pass = true;
            } else {
              TestUtil.logErr("Did not get either:");
              TestUtil.logErr("Expected name:total, actual:" + name);
              TestUtil.logErr("Expected type:int, actual:" + type);
            }
          } else {
            TestUtil.logErr("getAttribute return null");
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getAttribute failed");
    }
  }

  /*
   * @testName: getAttributeIllegalArgumentException
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1364
   *
   * @test_Strategy:
   *
   */
  public void getAttributeIllegalArgumentException() throws Fault {
    boolean pass = false;
    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Order> mTypeOrder = metaModel.managedType(Order.class);
        if (mTypeOrder != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          try {
            mTypeOrder.getAttribute("doesnotexist");
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
            pass = true;
          } catch (Exception e) {
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getAttributeIllegalArgumentException failed");
    }
  }

  /*
   * @testName: getDeclaredAttribute
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1370
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredAttribute() throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Order> mTypeOrder = metaModel.managedType(Order.class);
        if (mTypeOrder != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          Attribute<Order, ?> attrib = mTypeOrder.getDeclaredAttribute("total");
          if (attrib != null) {
            String name = attrib.getName();
            String type = attrib.getJavaType().getSimpleName();
            TestUtil.logTrace("attribute Name = " + name);
            TestUtil.logTrace("attribute Java Type =" + type);
            if (name.equals("total") && type.equals("int")) {
              pass = true;
            } else {
              TestUtil
                  .logErr("Expected: total/int, actual:" + name + "/" + type);
            }
          } else {
            TestUtil.logErr("getDeclaredAttribute() returned null");
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault("getDeclaredAttribute failed");
    }
  }

  /*
   * @testName: getDeclaredAttributeIllegalArgumentExceptionTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1371
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredAttributeIllegalArgumentExceptionTest() throws Fault {
    boolean pass = false;

    try {

      getEntityTransaction().begin();

      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        ManagedType<Order> mTypeOrder = metaModel.managedType(Order.class);
        if (mTypeOrder != null) {
          TestUtil.logTrace("Obtained Non-null ManagedType");
          try {
            mTypeOrder.getDeclaredAttribute("doesnotexist");
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
            pass = true;
          } catch (Exception e) {
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          TestUtil.logErr("managedType() returned null");
        }
      } else {
        TestUtil.logErr("getMetamodel() returned null");
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass) {
      throw new Fault(
          "getDeclaredAttributeIllegalArgumentExceptionTest failed");
    }

  }

  /*
   * @testName: getPersistenceType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1414;
   *
   * @test_Strategy:
   *
   */
  public void getPersistenceType() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      ManagedType<Order> mTypeOrder = metaModel.managedType(Order.class);
      if (mTypeOrder != null) {
        Type.PersistenceType type = mTypeOrder.getPersistenceType();
        TestUtil.logTrace("Obtained Non-null ManagedType");
        if (type.equals(Type.PersistenceType.ENTITY)) {
          pass = true;
        } else {
          TestUtil.logTrace("Persistence type = " + type.name());
        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("getPersistenceType Test  failed");
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
