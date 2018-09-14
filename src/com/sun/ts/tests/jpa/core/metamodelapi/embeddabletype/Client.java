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

package com.sun.ts.tests.jpa.core.metamodelapi.embeddabletype;

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
   * @testName: embeddableTest
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1433
   *
   * @test_Strategy:
   *
   */
  public void embeddableTest() throws Fault {
    boolean pass = true;
    Collection<String> expected = new ArrayList<String>();
    expected.add("cZipcode");
    expected.add("street");
    expected.add("state");
    expected.add("city");
    expected.add("lZipcode");
    expected.add("mZipcode");
    expected.add("sZipcode");

    try {

      getEntityTransaction().begin();
      Metamodel metaModel = getEntityManager().getMetamodel();
      if (metaModel != null) {
        TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
        EmbeddableType<Address> eTypeOrder = metaModel
            .embeddable(Address.class);
        if (eTypeOrder != null) {
          TestUtil.logTrace("Obtained Non-null EmbeddableType");
          Set<Attribute<Address, ?>> attribSet = eTypeOrder
              .getDeclaredAttributes();
          if (attribSet != null) {
            if (attribSet.size() != expected.size()) {
              pass = false;
              TestUtil.logErr("Received wrong number of results");
            }
            for (Attribute attrib : attribSet) {
              String name = attrib.getName();
              if (expected.contains(name)) {
                TestUtil.logTrace("Received expected result:" + name);
              } else {
                TestUtil.logErr("Received unexpected result:" + name);
                pass = false;
              }
            }
          } else {
            pass = false;
            TestUtil.logErr("getDeclaredAttributes() returned null");
          }
        } else {
          pass = false;
          TestUtil.logErr("embeddable() returned null");
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
      throw new Fault("embeddableTest failed");
    }
  }

  /*
   * @testName: getAttribute
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1230
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        Attribute attrib = eType.getAttribute("street");
        if (attrib != null) {
          TestUtil.logTrace("attribute Name = " + attrib.getName());
          String name = attrib.getName();
          if (name.equals("street")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: street, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1231;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        try {
          eType.getAttribute("doesnotexist");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1232;
   *
   * @test_Strategy:
   *
   */
  public void getAttributes() throws Fault {
    boolean pass = false;

    List<String> expected = new ArrayList<String>();

    expected.add("street");
    expected.add("state");
    expected.add("city");
    expected.add("cZipcode");
    expected.add("lZipcode");
    expected.add("mZipcode");
    expected.add("sZipcode");

    Collections.sort(expected);

    List<String> actual = new ArrayList<String>();

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        Set set = eType.getAttributes();
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1233;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        CollectionAttribute cAttrib = eType.getCollection("cZipcode",
            ZipCode.class);
        if (cAttrib != null) {
          TestUtil.logTrace("attribute Name = " + cAttrib.getName());
          String name = cAttrib.getName();
          if (name.equals("cZipcode")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: cZipcode, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1234;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        try {
          eType.getCollection("doesnotexist", Address.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1235;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        CollectionAttribute cAttrib = eType.getCollection("cZipcode");
        if (cAttrib != null) {
          TestUtil.logTrace("attribute Name = " + cAttrib.getName());
          String name = cAttrib.getName();
          if (name.equals("cZipcode")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: cZipcode, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1236;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        try {
          eType.getCollection("doesnotexist");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1237;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        Attribute attrib = eType.getDeclaredAttribute("cZipcode");
        if (attrib != null) {
          TestUtil.logTrace("attribute Name = " + attrib.getName());
          String name = attrib.getName();
          if (name.equals("cZipcode")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: cZipcode, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1238;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        try {
          eType.getDeclaredAttribute("doesnotexist");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1239;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredAttributes() throws Fault {
    boolean pass = false;

    List<String> expected = new ArrayList<String>();

    expected.add("street");
    expected.add("state");
    expected.add("city");
    expected.add("cZipcode");
    expected.add("lZipcode");
    expected.add("mZipcode");
    expected.add("sZipcode");
    Collections.sort(expected);

    List<String> actual = new ArrayList<String>();

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        Set set = eType.getDeclaredAttributes();
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
              TestUtil.logErr("Received unexpected attributes:");
              TestUtil.logErr("Expected (" + expected.size() + "):");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1240;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        CollectionAttribute cAttrib = eType.getCollection("cZipcode",
            ZipCode.class);
        if (cAttrib != null) {
          TestUtil.logTrace("attribute Name = " + cAttrib.getName());
          String name = cAttrib.getName();
          if (name.equals("cZipcode")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: cZipcode, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1241;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        try {
          eType.getDeclaredCollection("doesnotexist", Address.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1242;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        CollectionAttribute cAttrib = eType.getCollection("cZipcode",
            ZipCode.class);
        if (cAttrib != null) {
          TestUtil.logTrace("attribute Name = " + cAttrib.getName());
          String name = cAttrib.getName();
          if (name.equals("cZipcode")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: cZipcode, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1243;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        try {
          eType.getDeclaredCollection("doesnotexist", Address.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1244;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        ListAttribute lAttrib = eType.getDeclaredList("lZipcode",
            ZipCode.class);
        if (lAttrib != null) {
          TestUtil.logTrace("attribute Name = " + lAttrib.getName());
          String name = lAttrib.getName();
          if (name.equals("lZipcode")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: lZipcode, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1245;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        try {
          eType.getDeclaredList("doesnotexist", Address.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1246;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        ListAttribute lAttrib = eType.getDeclaredList("lZipcode");
        if (lAttrib != null) {
          TestUtil.logTrace("attribute Name = " + lAttrib.getName());
          String name = lAttrib.getName();
          if (name.equals("lZipcode")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: lZipcode, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1247;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        try {
          eType.getDeclaredList("doesnotexist");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1248;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        MapAttribute mAttrib = eType.getDeclaredMap("mZipcode", ZipCode.class,
            String.class);
        if (mAttrib != null) {
          TestUtil.logTrace("attribute Name = " + mAttrib.getName());
          String name = mAttrib.getName();
          if (name.equals("mZipcode")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: mZipcode, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1249;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        try {
          eType.getDeclaredMap("doesnotexist", Address.class, String.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1250;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        MapAttribute mAttrib = eType.getDeclaredMap("mZipcode");
        if (mAttrib != null) {
          TestUtil.logTrace("attribute Name = " + mAttrib.getName());
          String name = mAttrib.getName();
          if (name.equals("mZipcode")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: mZipcode, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1251;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        try {
          eType.getDeclaredMap("doesnotexist");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1253;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        SetAttribute sAttrib = eType.getDeclaredSet("sZipcode", ZipCode.class);
        if (sAttrib != null) {
          TestUtil.logTrace("attribute Name = " + sAttrib.getName());
          String name = sAttrib.getName();
          if (name.equals("sZipcode")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: sZipcode, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1254;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        try {
          eType.getDeclaredSet("doesnotexist", Address.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1255;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        SetAttribute sAttrib = eType.getDeclaredSet("sZipcode");
        if (sAttrib != null) {
          TestUtil.logTrace("attribute Name = " + sAttrib.getName());
          String name = sAttrib.getName();
          if (name.equals("sZipcode")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: sZipcode, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1256;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        try {
          eType.getDeclaredSet("doesnotexist");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1257;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        SingularAttribute singAttrib = eType
            .getDeclaredSingularAttribute("city", String.class);
        if (singAttrib != null) {
          TestUtil.logTrace("attribute city = " + singAttrib.getName());
          String name = singAttrib.getName();
          if (name.equals("city")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: name, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1258;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        try {
          eType.getDeclaredSingularAttribute("value", Integer.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1259;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        SingularAttribute singAttrib = eType
            .getDeclaredSingularAttribute("street");
        if (singAttrib != null) {
          TestUtil.logTrace("attribute Name = " + singAttrib.getName());
          String name = singAttrib.getName();
          if (name.equals("street")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: street, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1260;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        try {
          eType.getDeclaredSingularAttribute("value");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1261;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredSingularAttributes() throws Fault {
    boolean pass = false;

    List<String> expected = new ArrayList<String>();

    expected.add("city");
    expected.add("state");
    expected.add("street");
    Collections.sort(expected);

    List<String> actual = new ArrayList<String>();

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        Set set = eType.getDeclaredSingularAttributes();
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1262;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        ListAttribute lAttrib = eType.getList("lZipcode", ZipCode.class);
        if (lAttrib != null) {
          TestUtil.logTrace("attribute Name = " + lAttrib.getName());
          String name = lAttrib.getName();
          if (name.equals("lZipcode")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: lZipcode, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1263;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        try {
          eType.getList("doesnotexist", Address.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1264;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        ListAttribute lAttrib = eType.getList("lZipcode");
        if (lAttrib != null) {
          TestUtil.logTrace("attribute Name = " + lAttrib.getName());
          String name = lAttrib.getName();
          if (name.equals("lZipcode")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: lZipcode, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1265;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        try {
          eType.getDeclaredList("doesnotexist");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1266;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        MapAttribute mAttrib = eType.getMap("mZipcode", ZipCode.class,
            String.class);
        if (mAttrib != null) {
          TestUtil.logTrace("attribute Name = " + mAttrib.getName());
          String name = mAttrib.getName();
          if (name.equals("mZipcode")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: mZipcode, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1267;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        try {
          eType.getMap("doesnotexist", Address.class, String.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1268;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        MapAttribute mAttrib = eType.getMap("mZipcode");
        if (mAttrib != null) {
          TestUtil.logTrace("attribute Name = " + mAttrib.getName());
          String name = mAttrib.getName();
          if (name.equals("mZipcode")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: mZipcode, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1269;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        try {
          eType.getMap("doesnotexist");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1270;
   *
   * @test_Strategy:
   *
   */
  public void getPluralAttributes() throws Fault {
    boolean pass = false;

    List<String> expected = new ArrayList<String>();

    expected.add("cZipcode");
    expected.add("lZipcode");
    expected.add("mZipcode");
    expected.add("sZipcode");
    Collections.sort(expected);

    List<String> actual = new ArrayList<String>();

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        Set set = eType.getPluralAttributes();
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
              TestUtil.logErr("Received Unexpected attributes");
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
   * @testName: getSetStringClass
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1271;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        SetAttribute sAttrib = eType.getSet("sZipcode", ZipCode.class);
        if (sAttrib != null) {
          TestUtil.logTrace("attribute Name = " + sAttrib.getName());
          String name = sAttrib.getName();
          if (name.equals("sZipcode")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: sZipcode, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1272;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        try {
          eType.getSet("doesnotexist", Address.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1273;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        SetAttribute sAttrib = eType.getSet("sZipcode");
        if (sAttrib != null) {
          TestUtil.logTrace("attribute Name = " + sAttrib.getName());
          String name = sAttrib.getName();
          if (name.equals("sZipcode")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: sZipcode, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1274;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        try {
          eType.getSet("doesnotexist");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1275;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        SingularAttribute singAttrib = eType.getSingularAttribute("street",
            String.class);
        if (singAttrib != null) {
          TestUtil.logTrace("attribute Name = " + singAttrib.getName());
          String name = singAttrib.getName();
          if (name.equals("street")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: street, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1276;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        try {
          eType.getSingularAttribute("doesnotexist", Address.class);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1277;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        SingularAttribute singAttrib = eType.getSingularAttribute("street");
        if (singAttrib != null) {
          TestUtil.logTrace("attribute Name = " + singAttrib.getName());
          String name = singAttrib.getName();
          if (name.equals("street")) {
            TestUtil.logTrace("Received expected result:" + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected: street, actual:" + name);
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1278;
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
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        try {
          eType.getSingularAttribute("doesnotexist");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1279;
   *
   * @test_Strategy:
   *
   */
  public void getSingularAttributes() throws Fault {
    boolean pass = false;

    List<String> expected = new ArrayList<String>();

    expected.add("city");
    expected.add("state");
    expected.add("street");
    Collections.sort(expected);

    List<String> actual = new ArrayList<String>();

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null Entity Address");
        Set set = eType.getSingularAttributes();
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
              TestUtil.logErr("Received Unexpected attributes");
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

  /*
   * @testName: getDeclaredPluralAttributes
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1252;
   *
   * @test_Strategy:
   *
   */
  public void getDeclaredPluralAttributes() throws Fault {
    boolean pass = false;

    List<String> expected = new ArrayList<String>();

    expected.add("cZipcode");
    expected.add("lZipcode");
    expected.add("mZipcode");
    expected.add("sZipcode");
    Collections.sort(expected);

    List<String> actual = new ArrayList<String>();

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EmbeddableType<Address> eType = metaModel.embeddable(Address.class);
      if (eType != null) {
        Set set = eType.getDeclaredPluralAttributes();
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
              TestUtil.logErr("Received Unexpected attributes");
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
