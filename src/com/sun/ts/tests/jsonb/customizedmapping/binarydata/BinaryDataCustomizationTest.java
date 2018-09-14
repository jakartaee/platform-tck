/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsonb.customizedmapping.binarydata;

import java.util.Properties;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.config.BinaryDataStrategy;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.customizedmapping.binarydata.model.BinaryDataContainer;

/**
 * @test
 * @sources BinaryDataCustomizationTest.java
 * @executeClass com.sun.ts.tests.jsonb.customizedmapping.binarydata.BinaryDataCustomizationTest
 **/
public class BinaryDataCustomizationTest extends ServiceEETest {
  private static final long serialVersionUID = 10L;

  public static void main(String[] args) {
    EETest t = new BinaryDataCustomizationTest();
    Status s = t.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: testByteBinaryDataEncoding
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.10-1
   *
   * @test_Strategy: Assert that binary data is correctly encoded using BYTE
   * binary data encoding
   */
  public Status testByteBinaryDataEncoding() throws Fault {
    Jsonb jsonb = JsonbBuilder.create(
        new JsonbConfig().withBinaryDataStrategy(BinaryDataStrategy.BYTE));

    String jsonString = jsonb.toJson(new BinaryDataContainer());
    if (!jsonString.matches(
        "\\{\\s*\"data\"\\s*:\\s*\\[\\s*84\\s*,\\s*101\\s*,\\s*115\\s*,\\s*116\\s*,\\s*32\\s*,\\s*83\\s*,\\s*116\\s*,\\s*114\\s*,\\s*105\\s*,\\s*110\\s*,\\s*103\\s*]\\s*}")) {
      throw new Fault(
          "Failed to correctly marshal binary data using BYTE binary data encoding.");
    }

    BinaryDataContainer unmarshalledObject = jsonb.fromJson(
        "{ \"data\" : [ 84, 101, 115, 116, 32, 83, 116, 114, 105, 110, 103 ] }",
        BinaryDataContainer.class);
    if (!"Test String".equals(new String(unmarshalledObject.getData()))) {
      throw new Fault(
          "Failed to correctly unmarshal binary data using BYTE binary data encoding.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testBase64BinaryDataEncoding
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.10-1
   *
   * @test_Strategy: Assert that binary data is correctly encoded using BASE_64
   * binary data encoding
   */
  public Status testBase64BinaryDataEncoding() throws Fault {
    Jsonb jsonb = JsonbBuilder.create(
        new JsonbConfig().withBinaryDataStrategy(BinaryDataStrategy.BASE_64));

    String jsonString = jsonb.toJson(new BinaryDataContainer());
    if (!jsonString
        .matches("\\{\\s*\"data\"\\s*:\\s*\"VGVzdCBTdHJpbmc=\"\\s*}")) {
      throw new Fault(
          "Failed to correctly marshal binary data using BASE_64 binary data encoding.");
    }

    BinaryDataContainer unmarshalledObject = jsonb.fromJson(
        "{ \"data\" : \"VGVzdCBTdHJpbmc\" }", BinaryDataContainer.class);
    if (!"Test String".equals(new String(unmarshalledObject.getData()))) {
      throw new Fault(
          "Failed to correctly unmarshal binary data using BASE_64 binary data encoding.");
    }

    return Status.passed("OK");
  }

  /*
   * @testName: testBase64UrlBinaryDataEncoding
   *
   * @assertion_ids: JSONB:SPEC:JSB-4.10-1
   *
   * @test_Strategy: Assert that binary data is correctly encoded using
   * BASE_64_URL binary data encoding
   */
  public Status testBase64UrlBinaryDataEncoding() throws Fault {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig()
        .withBinaryDataStrategy(BinaryDataStrategy.BASE_64_URL));

    String jsonString = jsonb.toJson(new BinaryDataContainer());
    if (!jsonString
        .matches("\\{\\s*\"data\"\\s*:\\s*\"VGVzdCBTdHJpbmc=\"\\s*}")) {
      throw new Fault(
          "Failed to correctly marshal binary data using BASE_64_URL binary data encoding.");
    }

    BinaryDataContainer unmarshalledObject = jsonb.fromJson(
        "{ \"data\" : \"VGVzdCBTdHJpbmc=\" }", BinaryDataContainer.class);
    if (!"Test String".equals(new String(unmarshalledObject.getData()))) {
      throw new Fault(
          "Failed to correctly unmarshal binary data using BASE_64_URL binary data encoding.");
    }

    return Status.passed("OK");
  }
}
