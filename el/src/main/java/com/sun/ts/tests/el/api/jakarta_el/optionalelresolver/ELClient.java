/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
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
package com.sun.ts.tests.el.api.jakarta_el.optionalelresolver;

import java.lang.System.Logger;
import java.util.Optional;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import java.util.Properties;

import com.sun.ts.tests.el.common.elcontext.BareBonesELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;

import jakarta.el.BeanELResolver;
import jakarta.el.CompositeELResolver;
import jakarta.el.ELContext;
import jakarta.el.ELResolver;
import jakarta.el.MethodNotFoundException;
import jakarta.el.OptionalELResolver;
import jakarta.el.PropertyNotWritableException;

public class ELClient extends ServiceEETest {

  private Properties testProps;

  public static void main(String[] args) {
    ELClient theTests = new ELClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Exception {
    TestUtil.logTrace("Setup method called");
    this.testProps = p;
  }

  /**
   * @testName: optionalELResolverEmptyNullTest
   *
   * @test_Strategy: Verify that API calls work as expected for: getValue() getType() setValue() isReadOnly()
   * getCommonPropertyType() when the base object is an empty Optional and the property is null
   */
  public void optionalELResolverEmptyNullTest() throws Exception {

    boolean pass = true;
    StringBuffer buf = new StringBuffer();
    Object testObject = Optional.empty();
    
    pass = testOptionalELResolverProperty(buf, testObject, null, null);

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }
  
  /**
   * @testName: optionalELResolverEmptyNonNullTest
   *
   * @test_Strategy: Verify that API calls work as expected for: getValue() getType() setValue() isReadOnly()
   * getCommonPropertyType() when the base object is an empty Optional and the property is non-null
   */
  public void optionalELResolverEmptyNonNullTest() throws Exception {

    boolean pass = true;
    StringBuffer buf = new StringBuffer();
    Object testObject = Optional.empty();
    
    pass = testOptionalELResolverProperty(buf, testObject, "property", null);

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: optionalELResolverObjectNullTest
   *
   * @test_Strategy: Verify that API calls work as expected for: getValue() getType() setValue() isReadOnly()
   * getCommonPropertyType() when the base object is an non-empty Optional and the property is null
   */
  public void optionalELResolverObjectNullTest() throws Exception {

    boolean pass = true;
    StringBuffer buf = new StringBuffer();
    TestBean testBean = new TestBean("data");
    Object testObject = Optional.of(testBean);
    
    pass = testOptionalELResolverProperty(buf, testObject, null, testBean);

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: optionalELResolverObjectNullTest
   *
   * @test_Strategy: Verify that API calls work as expected for: getValue() getType() setValue() isReadOnly()
   * getCommonPropertyType() when the base object is an non-empty Optional and the property is non-null
   */
  public void optionalELResolverObjectNonNullTest() throws Exception {

    boolean pass = true;
    StringBuffer buf = new StringBuffer();
    TestBean testBean = new TestBean("data");
    Object testObject = Optional.of(testBean);
    
    pass = testOptionalELResolverProperty(buf, testObject, "propertyA", "data");

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }

  public static boolean testOptionalELResolverProperty(StringBuffer buf, Object base, Object property, Object expectedValue) {
    boolean pass = true;
    
    BareBonesELContext barebonesContext = new BareBonesELContext();
    
    // OptionalELResolver depends on BeanELResolver to resolve properties of an object wrapped in an Optional
    ELResolver resolver = barebonesContext.getELResolver();
    BeanELResolver beanResolver = new BeanELResolver();
    OptionalELResolver optionalResolver = new OptionalELResolver();
    ((CompositeELResolver) resolver).add(optionalResolver);
    ((CompositeELResolver) resolver).add(beanResolver);
    
    ELContext context = barebonesContext.getELContext();

    // getValue()
    Object result = resolver.getValue(context, base, property);
    if (expectedValue == null && result != null || expectedValue != null && !expectedValue.equals(result)) {
      buf.append("getValue(): Expected [" + expectedValue + "] but got [" + result + "]");
      pass = false;
    }
    
    // getType()
    result = resolver.getType(context, base, property);
    if (result != null) {
      pass = false;
    }
    
    // setValue()
    try {
      resolver.setValue(context, base, property, "anything");
      pass = false;
    } catch (PropertyNotWritableException pnwe) {
      // Expected
    }
    
    // isReadOnly()
    boolean bResult = resolver.isReadOnly(context, base, property);
    if (!bResult) {
      pass = false;
    }
    
    // getCommonPropertyType()
    result = resolver.getCommonPropertyType(context, base);
    if (result != Object.class) {
      pass = false;
    }

    return pass;
  }
  
   /**
   * @testName: optionalELResolverEmptyInvoke
   *
   */
  public void optionalELResolverEmptyInvoke() throws Exception {
    boolean pass = true;
    StringBuffer buf = new StringBuffer();
    Object testObject = Optional.empty();
    
    pass = testOptionalELResolverInvoke(buf, testObject, "doSomething", null);

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }
  
  /**
   * @testName: optionalELResolverEmptyInvokeInvalid
   *
   */
  public void optionalELResolverEmptyInvokeInvalid() throws Exception {
    boolean pass = true;
    StringBuffer buf = new StringBuffer();
    Object testObject = Optional.empty();
    
    pass = testOptionalELResolverInvoke(buf, testObject, "unknownMethod", null);

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }
  
  /**
   * @testName: optionalELResolverObjectInvoke
   *
   */
  public void optionalELResolverObjectInvoke() throws Exception {
    boolean pass = true;
    StringBuffer buf = new StringBuffer();
    TestBean testBean = new TestBean("data");
    Object testObject = Optional.of(testBean);
    
    pass = testOptionalELResolverInvoke(buf, testObject, "doSomething", TestBean.DATA);

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }
  
  /**
   * @testName: optionalELResolverObjectInvokeInvalid
   *
   */
  public void optionalELResolverObjectInvokeInvalid() throws Exception {
    boolean pass = true;
    StringBuffer buf = new StringBuffer();
    TestBean testBean = new TestBean("data");
    Object testObject = Optional.of(testBean);
    
    try {
      testOptionalELResolverInvoke(buf, testObject, "unknownMethod", null);
      pass = false;
      buf.append("invoke(): Expected MethodNotFoundException but no exception was thrown"); 
    } catch (MethodNotFoundException mnfe) {
      // Expected (so pass is not set to false)
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }
  
  public static boolean testOptionalELResolverInvoke(StringBuffer buf, Object base, Object method, Object expectedValue) {
    boolean pass = true;
    
    BareBonesELContext barebonesContext = new BareBonesELContext();
    
    // OptionalELResolver depends on BeanELResolver to invoke methods of an object wrapped in an Optional
    ELResolver resolver = barebonesContext.getELResolver();
    BeanELResolver beanResolver = new BeanELResolver();
    OptionalELResolver optionalResolver = new OptionalELResolver();
    ((CompositeELResolver) resolver).add(optionalResolver);
    ((CompositeELResolver) resolver).add(beanResolver);
    ELContext context = barebonesContext.getELContext();

    // invoke()
    Object result = resolver.invoke(context, base, method, null, null);
    if (expectedValue == null && result != null || expectedValue != null && !expectedValue.equals(result)) {
      buf.append("invoke(): Expected [" + expectedValue + "] but got [" + result + "]");
      pass = false;
    }
    
    return pass;
  }
  
  public static class TestBean {
    private static final String DATA = "interesting data"; 
    private final String propertyA; 
    public TestBean(String propertyA) {
      this.propertyA = propertyA;
    }
    public String getPropertyA() {
      return propertyA;
    }
    public String doSomething() {
      return DATA;
    }
  }
}
