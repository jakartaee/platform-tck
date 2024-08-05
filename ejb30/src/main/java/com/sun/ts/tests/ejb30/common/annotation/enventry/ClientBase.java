/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * $Id$
 */

package com.sun.ts.tests.ejb30.common.annotation.enventry;

import java.util.Properties;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

abstract public class ClientBase extends EETest {
  protected Properties props;

  // 2 remote beans directly accessed by client
  abstract protected EnvEntryIF getEnvEntrySetterBean();

  abstract protected EnvEntryIF getEnvEntryFieldBean();

  abstract protected EnvEntryIF getEnvEntryTypeBean();

  /*
   * @class.setup_props:
   */
  public void setup(String[] args, Properties p) throws Exception {
    props = p;
  }

  /**
   * Removes all beans used in this client. It should only be used by sfsb,
   * though other bean types may also have a remove business method.
   */
  protected void remove() {
    if (getEnvEntrySetterBean() != null) {
      try {
        getEnvEntrySetterBean().remove();
        TLogger.log("EnvEntrySetterBean removed successfully.");
      } catch (Exception e) {
        // ignore
        TLogger.log("failed to remove EnvEntrySetterBean.");
      }
    }

    if (getEnvEntryFieldBean() != null) {
      try {
        getEnvEntryFieldBean().remove();
        TLogger.log("EnvEntryFieldBean removed successfully.");
      } catch (Exception e) {
        // ignore
        TLogger.log("failed to remove EnvEntryFieldBean.");
      }
    }
  }

  public void cleanup() throws Exception {
  }

  /*
   * testName: stringTest
   * 
   * @test_Strategy:
   * 
   */
  public void stringTest() throws Exception {
    try {
      getEnvEntryFieldBean().stringTest();
      getEnvEntrySetterBean().stringTest();
      getEnvEntryTypeBean().stringTest();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: charTest
   * 
   * @test_Strategy:
   * 
   */
  public void charTest() throws Exception {
    try {
      getEnvEntryFieldBean().charTest();
      getEnvEntrySetterBean().charTest();
      getEnvEntryTypeBean().charTest();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: intTest
   * 
   * @test_Strategy:
   * 
   */
  public void intTest() throws Exception {
    try {
      getEnvEntryFieldBean().intTest();
      getEnvEntrySetterBean().intTest();
      getEnvEntryTypeBean().intTest();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: booleanTest
   * 
   * @test_Strategy:
   * 
   */
  public void booleanTest() throws Exception {
    try {
      getEnvEntryFieldBean().booleanTest();
      getEnvEntrySetterBean().booleanTest();
      getEnvEntryTypeBean().booleanTest();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: doubleTest
   * 
   * @test_Strategy:
   * 
   */
  public void doubleTest() throws Exception {
    try {
      getEnvEntryFieldBean().doubleTest();
      getEnvEntrySetterBean().doubleTest();
      getEnvEntryTypeBean().doubleTest();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: byteTest
   * 
   * @test_Strategy:
   * 
   */
  public void byteTest() throws Exception {
    try {
      getEnvEntryFieldBean().byteTest();
      getEnvEntrySetterBean().byteTest();
      getEnvEntryTypeBean().byteTest();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: shortTest
   * 
   * @test_Strategy:
   * 
   */
  public void shortTest() throws Exception {
    try {
      getEnvEntryFieldBean().shortTest();
      getEnvEntrySetterBean().shortTest();
      getEnvEntryTypeBean().shortTest();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: longTest
   * 
   * @test_Strategy:
   * 
   */
  public void longTest() throws Exception {
    try {
      getEnvEntryFieldBean().longTest();
      getEnvEntrySetterBean().longTest();
      getEnvEntryTypeBean().longTest();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: floatTest
   * 
   * @test_Strategy:
   * 
   */
  public void floatTest() throws Exception {
    try {
      getEnvEntryFieldBean().floatTest();
      getEnvEntrySetterBean().floatTest();
      getEnvEntryTypeBean().floatTest();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: stringDeepTest
   * 
   * @test_Strategy:
   * 
   */
  public void stringDeepTest() throws Exception {
    try {
      getEnvEntryFieldBean().stringDeepTest();
      getEnvEntrySetterBean().stringDeepTest();
      getEnvEntryTypeBean().stringDeepTest();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: charDeepTest
   * 
   * @test_Strategy:
   * 
   */
  public void charDeepTest() throws Exception {
    try {
      getEnvEntryFieldBean().charDeepTest();
      getEnvEntrySetterBean().charDeepTest();
      getEnvEntryTypeBean().charDeepTest();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: intDeepTest
   * 
   * @test_Strategy:
   * 
   */
  public void intDeepTest() throws Exception {
    try {
      getEnvEntryFieldBean().intDeepTest();
      getEnvEntrySetterBean().intDeepTest();
      getEnvEntryTypeBean().intDeepTest();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: booleanDeepTest
   * 
   * @test_Strategy:
   * 
   */
  public void booleanDeepTest() throws Exception {
    try {
      getEnvEntryFieldBean().booleanDeepTest();
      getEnvEntrySetterBean().booleanDeepTest();
      getEnvEntryTypeBean().booleanDeepTest();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: doubleDeepTest
   * 
   * @test_Strategy:
   * 
   */
  public void doubleDeepTest() throws Exception {
    try {
      getEnvEntryFieldBean().doubleDeepTest();
      getEnvEntrySetterBean().doubleDeepTest();
      getEnvEntryTypeBean().doubleDeepTest();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: byteDeepTest
   * 
   * @test_Strategy:
   * 
   */
  public void byteDeepTest() throws Exception {
    try {
      getEnvEntryFieldBean().byteDeepTest();
      getEnvEntrySetterBean().byteDeepTest();
      getEnvEntryTypeBean().byteDeepTest();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: shortDeepTest
   * 
   * @test_Strategy:
   * 
   */
  public void shortDeepTest() throws Exception {
    try {
      getEnvEntryFieldBean().shortDeepTest();
      getEnvEntrySetterBean().shortDeepTest();
      getEnvEntryTypeBean().shortDeepTest();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: longDeepTest
   * 
   * @test_Strategy:
   * 
   */
  public void longDeepTest() throws Exception {
    try {
      getEnvEntryFieldBean().longDeepTest();
      getEnvEntrySetterBean().longDeepTest();
      getEnvEntryTypeBean().longDeepTest();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }

  /*
   * testName: floatDeepTest
   * 
   * @test_Strategy:
   * 
   */
  public void floatDeepTest() throws Exception {
    try {
      getEnvEntryFieldBean().floatDeepTest();
      getEnvEntrySetterBean().floatDeepTest();
      getEnvEntryTypeBean().floatDeepTest();
    } catch (TestFailedException e) {
      throw new Exception("Test Failed", e);
    }
  }
}
