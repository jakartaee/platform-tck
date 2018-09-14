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

package com.sun.ts.tests.jpa.core.metamodelapi.mappedsuperclasstype;

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
   * @testName: mappedSuperclassType
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1428;
   *
   * @test_Strategy:
   *
   */
  public void mappedSuperclassType() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      pass = false;
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      Set<EntityType<?>> aSet = metaModel.getEntities();
      if (aSet != null) {
        TestUtil.logTrace("Obtained Non-null Set of EntityType");
        for (EntityType mType : aSet) {
          TestUtil.logTrace("EntityType:" + mType.getJavaType().getName());

          IdentifiableType<? super FullTimeEmployee> idType = mType
              .getSupertype();
          if (idType != null) {
            TestUtil
                .logTrace("IdentifiableType:" + idType.getJavaType().getName());
            if (idType instanceof MappedSuperclassType) {
              TestUtil.logTrace("type is instance of MappedSuperClassType:"
                  + idType.getJavaType().getName());
              pass = true;
            }
          }

        }
      }
    }

    getEntityTransaction().commit();

    if (!pass) {
      throw new Fault("mappedSuperclassType failed");
    }
  }

  /*
   * @testName: getDeclaredId
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1419;
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
      EntityType<FullTimeEmployee2> eType = metaModel
          .entity(FullTimeEmployee2.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null EntityType");
        IdentifiableType idType = eType.getSupertype();
        if (idType != null) {
          SingularAttribute<FullTimeEmployee, Integer> idAttrib = idType
              .getDeclaredId(String.class);
          if (idAttrib != null) {
            String name = idAttrib.getType().getJavaType().getName();
            if (name.equals("java.lang.String")) {
              TestUtil.logTrace("Received:" + name);
              pass = true;
            } else {
              TestUtil.logErr("Expected java.lang.String, actual:" + name);
            }
          } else {
            TestUtil.logErr("getDeclaredId(...) returned null");
          }
        } else {
          TestUtil.logErr("getSupertype() returned null");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1420;
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
      EntityType<FullTimeEmployee2> eType = metaModel
          .entity(FullTimeEmployee2.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null EntityType");
        IdentifiableType idType = eType.getSupertype();
        if (idType != null) {
          try {
            idType.getDeclaredId(Date.class);
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
            pass = true;
          } catch (Exception e) {
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          TestUtil.logErr("getSupertype() returned null");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1421;
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
      EntityType<FullTimeEmployee2> eType = metaModel
          .entity(FullTimeEmployee2.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null EntityType");
        IdentifiableType idType = eType.getSupertype();
        if (idType != null) {
          SingularAttribute<FullTimeEmployee, Integer> idAttrib = idType
              .getDeclaredVersion(Integer.class);
          if (idAttrib != null) {
            String name = idAttrib.getType().getJavaType().getName();
            if (name.equals("java.lang.Integer")) {
              TestUtil.logTrace("Received:" + name);
              pass = true;
            } else {
              TestUtil.logErr("Expected java.lang.Integer, actual:" + name);
            }
          } else {
            TestUtil.logErr("getDeclaredId(...) returned null");
          }
        } else {
          TestUtil.logErr("getSupertype() returned null");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1422;
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
      EntityType<FullTimeEmployee2> eType = metaModel
          .entity(FullTimeEmployee2.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null EntityType");
        IdentifiableType idType = eType.getSupertype();
        if (idType != null) {
          try {
            idType.getDeclaredVersion(Date.class);
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
            pass = true;
          } catch (Exception e) {
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          TestUtil.logErr("getSupertype() returned null");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1423;
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
      EntityType<FullTimeEmployee2> eType = metaModel
          .entity(FullTimeEmployee2.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null EntityType");
        IdentifiableType idType = eType.getSupertype();
        if (idType != null) {
          SingularAttribute<FullTimeEmployee, Integer> idAttrib = idType
              .getId(String.class);
          if (idAttrib != null) {
            String name = idAttrib.getType().getJavaType().getName();
            if (name.equals("java.lang.String")) {
              TestUtil.logTrace("Received:" + name);
              pass = true;
            } else {
              TestUtil.logErr("Expected java.lang.String, actual:" + name);
            }
          } else {
            TestUtil.logErr("getDeclaredId(...) returned null");
          }
        } else {
          TestUtil.logErr("getSupertype() returned null");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1424;
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
      EntityType<FullTimeEmployee2> eType = metaModel
          .entity(FullTimeEmployee2.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null EntityType");
        IdentifiableType idType = eType.getSupertype();
        if (idType != null) {
          try {
            idType.getId(Date.class);
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
            pass = true;
          } catch (Exception e) {
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          TestUtil.logErr("getSupertype() returned null");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1425;
   * 
   * @test_Strategy:
   *
   */

  public void getIdClassAttributes() throws Fault {
    boolean pass = false;

    List<String> expected = new ArrayList<String>();
    expected.add("id");
    Collections.sort(expected);

    List<String> actual = new ArrayList<String>();

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EntityType<FullTimeEmployee> eType = metaModel
          .entity(FullTimeEmployee.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null EntityType");
        IdentifiableType idType = eType.getSupertype();
        if (idType != null) {
          Set<SingularAttribute> idAttribSet = idType.getIdClassAttributes();
          if (idAttribSet != null) {
            if (idAttribSet.size() > 0) {
              for (Iterator i = idAttribSet.iterator(); i.hasNext();) {
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
                TestUtil.logErr("Expected:");
                for (String attribName : expected) {
                  TestUtil.logErr("attrib:" + attribName);
                }
                TestUtil.logErr("Actual:");
                for (String attribName : actual) {
                  TestUtil.logErr("attrib:" + attribName);
                }
              }
            } else {
              TestUtil.logErr("getIdClassAttributes() returned 0 results");
            }
          } else {
            TestUtil.logErr("getIdClassAttributes() returned null");
          }
        } else {
          TestUtil.logErr("getSupertype() returned null");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1426;
   * 
   * @test_Strategy:
   *
   */

  public void getIdClassAttributesIllegalArgumentException() throws Fault {
    boolean pass = false;

    getEntityTransaction().begin();
    Metamodel metaModel = getEntityManager().getMetamodel();
    if (metaModel != null) {
      TestUtil.logTrace("Obtained Non-null Metamodel from EntityManager");
      EntityType<FullTimeEmployee2> eType = metaModel
          .entity(FullTimeEmployee2.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null EntityType");
        IdentifiableType idType = eType.getSupertype();
        if (idType != null) {
          try {
            idType.getIdClassAttributes();
            TestUtil.logTrace("Did not receive IllegalArgumentException");
          } catch (IllegalArgumentException iae) {
            TestUtil.logTrace("Received expected IllegalArgumentException");
            pass = true;
          } catch (Exception e) {
            TestUtil.logErr("Received unexpected exception", e);
          }
        } else {
          TestUtil.logErr("getSupertype() returned null");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1427;
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
      EntityType<FullTimeEmployee2> eType = metaModel
          .entity(FullTimeEmployee2.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null EntityType");
        IdentifiableType idType = eType.getSupertype();
        if (idType != null) {
          String name = idType.getIdType().getJavaType().getName();

          if (name.equals("java.lang.String")) {
            TestUtil.logTrace("Received expected: " + name);
            pass = true;
          } else {
            TestUtil.logErr("Expected java.lang.String, actual:" + name);
          }

        } else {
          TestUtil.logErr("getSupertype() returned null");
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
   * @testName: getVersion
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1429;
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
      EntityType<FullTimeEmployee2> eType = metaModel
          .entity(FullTimeEmployee2.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null EntityType");
        IdentifiableType idType = eType.getSupertype();
        if (idType != null) {
          SingularAttribute idAttrib = eType.getVersion(Integer.class);
          String name = idAttrib.getType().getJavaType().getName();
          if (name.equals("java.lang.Integer")) {
            pass = true;
          } else {
            TestUtil.logErr("Expected java.lang.Integer, actual:" + name);
          }
        } else {
          TestUtil.logErr("getSupertype() returned null");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1430;
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
      EntityType<FullTimeEmployee2> eType = metaModel
          .entity(FullTimeEmployee2.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null EntityType");
        IdentifiableType idType = eType.getSupertype();
        if (idType != null) {
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
          TestUtil.logErr("getSupertype() returned null");
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
   * @testName: hasSingleIdAttribute
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:1431;
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
      EntityType<FullTimeEmployee2> eType = metaModel
          .entity(FullTimeEmployee2.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null EntityType");
        IdentifiableType idType = eType.getSupertype();
        if (idType != null) {
          boolean hasSingleIdAttribute = idType.hasSingleIdAttribute();
          if (hasSingleIdAttribute) {
            pass = true;
            TestUtil.logTrace(
                "hasSingleIdAttribute() returned" + hasSingleIdAttribute);
          } else {
            TestUtil.logErr("Expected: false, actual:" + hasSingleIdAttribute);

          }
        } else {
          TestUtil.logErr("getSupertype() returned null");
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
   * @assertion_ids: PERSISTENCE:JAVADOC:1432;
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
      EntityType<FullTimeEmployee2> eType = metaModel
          .entity(FullTimeEmployee2.class);
      if (eType != null) {
        TestUtil.logTrace("Obtained Non-null EntityType");
        IdentifiableType idType = eType.getSupertype();
        if (idType != null) {
          boolean hasVersionAttribute = idType.hasVersionAttribute();
          if (hasVersionAttribute) {
            pass = true;
            TestUtil.logTrace(
                "hasVersionAttribute() returned" + hasVersionAttribute);
          } else {
            TestUtil.logErr("Expected: false, actual:" + hasVersionAttribute);

          }
        } else {
          TestUtil.logErr("getSupertype() returned null");
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
