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

package com.sun.ts.tests.jsonb.defaultmapping.basictypes;

import static com.sun.ts.tests.jsonb.MappingTester.combine;

import java.math.BigDecimal;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jsonb.MappingTester;
import com.sun.ts.tests.jsonb.SimpleMappingTester;
import com.sun.ts.tests.jsonb.defaultmapping.basictypes.model.BooleanContainer;
import com.sun.ts.tests.jsonb.defaultmapping.basictypes.model.ByteContainer;
import com.sun.ts.tests.jsonb.defaultmapping.basictypes.model.CharacterContainer;
import com.sun.ts.tests.jsonb.defaultmapping.basictypes.model.DoubleContainer;
import com.sun.ts.tests.jsonb.defaultmapping.basictypes.model.FloatContainer;
import com.sun.ts.tests.jsonb.defaultmapping.basictypes.model.IntegerContainer;
import com.sun.ts.tests.jsonb.defaultmapping.basictypes.model.LongContainer;
import com.sun.ts.tests.jsonb.defaultmapping.basictypes.model.NumberContainer;
import com.sun.ts.tests.jsonb.defaultmapping.basictypes.model.ShortContainer;
import com.sun.ts.tests.jsonb.defaultmapping.basictypes.model.StringContainer;

/**
 * @test
 * @sources BasicJavaTypesMappingTest.java
 * @executeClass com.sun.ts.tests.jsonb.defaultmapping.basictypes.BasicJavaTypesMappingTest
 **/
public class BasicJavaTypesMappingTest extends ServiceEETest {

  private static final long serialVersionUID = 10L;

  public static void main(String[] args) {
    EETest t = new BasicJavaTypesMappingTest();
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
   * @testName: testStringMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.1-5; JSONB:SPEC:JSB-3.3-1;
   * JSONB:SPEC:JSB-3.3.1-1; JSONB:SPEC:JSB-3.3.1-2
   *
   * @test_Strategy: Assert that String type is correctly handled, encodings
   * other than UTF-8 are supported and that UTF-8 BOM does not produce an error
   */
  public Status testStringMapping() throws Exception {
    MappingTester<String> stringMappingTester = new MappingTester<>(
        StringContainer.class);
    return combine(stringMappingTester.test("Test String", "\"Test String\""),
        stringMappingTester.test(
            new String(new byte[] { (byte) 0xef, (byte) 0xbb, (byte) 0xbf },
                "UTF-8"),
            "\"" + new String(
                new byte[] { (byte) 0xef, (byte) 0xbb, (byte) 0xbf }, "UTF-8")
                + "\""),
        stringMappingTester.test(new String("Test String".getBytes(), "UTF-8"),
            "\"" + new String("Test String".getBytes(), "UTF-8") + "\""));
  }

  /*
   * @testName: testCharacterMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.3-1; JSONB:SPEC:JSB-3.3.1-1
   *
   * @test_Strategy: Assert that Character type is correctly handled
   */
  public Status testCharacterMapping() throws Fault {
    MappingTester<Character> characterMappingTester = new MappingTester<>(
        CharacterContainer.class);
    return combine(characterMappingTester.test('c', "\"c\""),
        // RFC 4627, paragraph 2.5: the control characters U+0000 must be
        // escaped
        characterMappingTester.test(Character.MIN_VALUE, "\"\\u0000\""),
        characterMappingTester.test(Character.MAX_VALUE,
            "\"" + Character.MAX_VALUE + "\""));
  }

  /*
   * @testName: testByteMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.3-1; JSONB:SPEC:JSB-3.3.2-1;
   * JSONB:SPEC:JSB-3.3.2-2
   *
   * @test_Strategy: Assert that Byte type is correctly handled in accordance
   * with toString and parseByte methods
   */
  public Status testByteMapping() throws Fault {
    MappingTester<Byte> byteMappingTester = new MappingTester<>(
        ByteContainer.class);
    return combine(byteMappingTester.test((byte) 0, "0"),
        byteMappingTester.test(Byte.MIN_VALUE, String.valueOf(Byte.MIN_VALUE)),
        byteMappingTester.test(Byte.MAX_VALUE, String.valueOf(Byte.MAX_VALUE)));
  }

  /*
   * @testName: testShortMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.3-1; JSONB:SPEC:JSB-3.3.2-1;
   * JSONB:SPEC:JSB-3.3.2-2
   *
   * @test_Strategy: Assert that Short type is correctly handled in accordance
   * with toString and parseShort methods
   */
  public Status testShortMapping() throws Fault {
    MappingTester<Short> shortMappingTester = new MappingTester<>(
        ShortContainer.class);
    return combine(shortMappingTester.test((short) 0, "0"),
        shortMappingTester.test(Short.MIN_VALUE,
            String.valueOf(Short.MIN_VALUE)),
        shortMappingTester.test(Short.MAX_VALUE,
            String.valueOf(Short.MAX_VALUE)));
  }

  /*
   * @testName: testIntegerMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.3-1; JSONB:SPEC:JSB-3.3.2-1;
   * JSONB:SPEC:JSB-3.3.2-2
   *
   * @test_Strategy: Assert that Integer type is correctly handled in accordance
   * with toString and parseInteger methods
   */
  public Status testIntegerMapping() throws Fault {
    MappingTester<Integer> integerMappingTester = new MappingTester<>(
        IntegerContainer.class);
    return combine(integerMappingTester.test(0, "0"),
        integerMappingTester.test(Integer.MIN_VALUE,
            String.valueOf(Integer.MIN_VALUE)),
        integerMappingTester.test(Integer.MAX_VALUE,
            String.valueOf(Integer.MAX_VALUE)));
  }

  /*
   * @testName: testLongMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.3-1; JSONB:SPEC:JSB-3.3.2-1;
   * JSONB:SPEC:JSB-3.3.2-2
   *
   * @test_Strategy: Assert that Long type is correctly handled in accordance
   * with toString and parseLong methods
   */
  public Status testLongMapping() throws Fault {
    MappingTester<Long> longMappingTester = new MappingTester<>(
        LongContainer.class);
    return combine(longMappingTester.test(0L, "0"),
        longMappingTester.test(Long.MIN_VALUE, String.valueOf(Long.MIN_VALUE)),
        longMappingTester.test(Long.MAX_VALUE, String.valueOf(Long.MAX_VALUE)));
  }

  /*
   * @testName: testFloatMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.3-1; JSONB:SPEC:JSB-3.3.2-1;
   * JSONB:SPEC:JSB-3.3.2-2
   *
   * @test_Strategy: Assert that Float type is correctly handled in accordance
   * with toString and parseFloat methods
   */
  public Status testFloatMapping() throws Fault {
    MappingTester<Float> floatMappingTester = new MappingTester<>(
        FloatContainer.class);
    return combine(floatMappingTester.test(0f, "0.0"),
        floatMappingTester.test(0.5f, "0.5"),
        floatMappingTester.test(Float.MIN_VALUE,
            String.valueOf(Float.MIN_VALUE)),
        floatMappingTester.test(Float.MAX_VALUE,
            String.valueOf(Float.MAX_VALUE)));
  }

  /*
   * @testName: testDoubleMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.3-1; JSONB:SPEC:JSB-3.3.2-1;
   * JSONB:SPEC:JSB-3.3.2-2
   *
   * @test_Strategy: Assert that Double type is correctly handled in accordance
   * with toString and parseDouble methods
   */
  public Status testDoubleMapping() throws Fault {
    MappingTester<Double> doubleMappingTester = new MappingTester<>(
        DoubleContainer.class);
    return combine(doubleMappingTester.test(0.0, "0.0"),
        doubleMappingTester.test(Double.MIN_VALUE,
            String.valueOf(Double.MIN_VALUE)),
        doubleMappingTester.test(Double.MAX_VALUE,
            String.valueOf(Double.MAX_VALUE)));
  }

  /*
   * @testName: testBooleanMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.3-1; JSONB:SPEC:JSB-3.3.3-1;
   * JSONB:SPEC:JSB-3.3.3-1; JSONB:SPEC:JSB-3.3.3-2
   *
   * @test_Strategy: Assert that Boolean type is correctly handled in accordance
   * with toString and parseBoolean methods
   */
  public Status testBooleanMapping() throws Fault {
    MappingTester<Boolean> booleanMappingTester = new MappingTester<>(
        BooleanContainer.class);
    return combine(booleanMappingTester.test(true, "true"),
        booleanMappingTester.test(false, "false"),
        booleanMappingTester.test(Boolean.TRUE, Boolean.TRUE.toString()),
        booleanMappingTester.test(Boolean.FALSE, Boolean.FALSE.toString()));
  }

  /*
   * @testName: testNumberMapping
   *
   * @assertion_ids: JSONB:SPEC:JSB-3.3.4-1; JSONB:SPEC:JSB-3.3.4-2;
   * JSONB:SPEC:JSB-3.10-1
   *
   * @test_Strategy: Assert that Number type is correctly marshalled using
   * java.lang.Number.doubleValue() and toString methods and unmarshalled to
   * java.math.BigDecimal using String constructor
   */
  public Status testNumberMapping() throws Fault {
    return new SimpleMappingTester<>(NumberContainer.class).test(
        new NumberContainer(), "\\{\\s*\"instance\"\\s*:\\s*0[\\.0]?+\\s*}",
        "{ \"instance\" : 0 }", new NumberContainer() {
          {
            setInstance(new BigDecimal("0"));
          }
        });
  }
}
