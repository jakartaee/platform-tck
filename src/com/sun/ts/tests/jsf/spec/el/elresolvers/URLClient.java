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

/*
 * @(#)URLClient.java	1.5 04/22/04
 */

package com.sun.ts.tests.jsf.spec.el.elresolvers;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;
import com.sun.ts.lib.util.TestUtil;

public final class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsf_el_resolvers_web";

  private static boolean init = false;

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out, true),
        new PrintWriter(System.err, true));
    s.exit();
  }

  public Status run(String[] args, PrintWriter out, PrintWriter err) {

    setContextRoot(CONTEXT_ROOT);
    setServletName(DEFAULT_SERVLET_NAME);
    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Test Declarations */

  /**
   * @testName: managedBeanELResolverFeatureDescriptorTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate that the feature descriptors for managed beans
   *                 conform to the requirements: - ELResolver.TYPE -> the
   *                 actual class representation of the bean type as defined by
   *                 managed-bean-class - ELResolver.RESOLVABLE_AT_DESIGN_TIME
   *                 -> Boolean.TRUE - Name and displayName properties are the
   *                 value of managed-bean-name - Hidden and Expert properties
   *                 are false - Preferred property is true - shortDescription
   *                 is populated with the description of the managed bean
   *                 otherwise null if no description is present. This test uses
   *                 the ELResolver from both Faces and JSP.
   * @since 1.2
   */
  public void managedBeanELResolverFeatureDescriptorTest() throws Fault {
    init();
    TEST_PROPS.setProperty(APITEST,
        "managedBeanELResolverFeatureDescriptorTest");
    invoke();
  }

  /**
   * @testName: facesImplicitObjectResolverFeatureDescriptorTest
   * @assertion_ids: PENDING
   * @test_Strategy: Validate the JSP and Faces defined implicit objects conform
   *                 to the following requirements. - ELResolver.TYPE -> matches
   *                 the types defined in table 5-9 of the JSF Spec (in the
   *                 non-jsp case). - ELResolver.TYPE -> view ->
   *                 javax.faces.component.UIViewRoot facesContext ->
   *                 javax.faces.component.FacesContext (in the JSP case) -
   *                 ELResolver.RESOLVABLE_AT_DESIGN_TIME -> Boolean.TRUE - Name
   *                 and displayName properties are the name of the implicit
   *                 object - Hidden and Expert properties are false - Preferred
   *                 property is true - shortDescription is non-null. This test
   *                 uses the ELResolver from both Faces and JSP.
   * @since 1.2
   */
  public void facesImplicitObjectResolverFeatureDescriptorTest() throws Fault {
    init();
    TEST_PROPS.setProperty(APITEST,
        "facesImplicitObjectResolverFeatureDescriptorTest");
    invoke();
  }

  /*
   * @testName: facesImplicitObjectResolverGetValueTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure the following: - for each implicit object listed in
   * table 5-9, ensure the property type is returned for each implict object
   * (Faces case) - for the view and facesContext implicit object, a UIViewRoot
   * and FacesContext object is returned (respectively - JSP case) -
   * ELContext.isPropertyResolved() returns true
   * 
   * @since 1.2
   */
  public void facesImplicitObjectResolverGetValueTest() throws Fault {

    init();
    TEST_PROPS.setProperty(APITEST, "facesImplicitObjectResolverGetValueTest");
    invoke();

  } // END facesImplicitObjectResolverGetValueTest

  /*
   * @testName: facesImplicitObjectResolverGetTypeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure when calling ELResolver.getType() (either JSP (or
   * Faces) null is returned and ELContext.isPropertyResolved() returns true for
   * all spec defined implicit objects. This test uses the ELResolvers from both
   * JSP and Faces.
   * 
   * @since 1.2
   */
  public void facesImplicitObjectResolverGetTypeTest() throws Fault {

    init();
    TEST_PROPS.setProperty(APITEST, "facesImplicitObjectResolverGetTypeTest");
    invoke();

  } // END facesImplicitObjectResolverGetTypeTest

  /*
   * @testName: facesImplicitObjectResolverSetValueTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure a PropertyNotFoundException is thrown when calling
   * ELResolver.setValue() when base is null, matches any of the spec defined
   * implicit objects. Also ensure ELContext.isPropertyResolved() returns false.
   * This test uses the ELResolvers from both JSP and Faces.
   * 
   * @since 1.2
   */
  public void facesImplicitObjectResolverSetValueTest() throws Fault {

    init();
    TEST_PROPS.setProperty(APITEST, "facesImplicitObjectResolverSetValueTest");
    invoke();

  } // END facesImplicitObjectResolverSetValueTest

  /*
   * @testName: facesImplicitObjectResolverIsReadOnlyTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure ELResolver.isReadOnly() returns true when base is
   * null and property is any of the spec defined implicit objects. Also ensure
   * ELContext.isPropertyResolved() returns true. This test uses the ELResolvers
   * from both JSP and Faces.
   * 
   * @since 1.2
   */
  public void facesImplicitObjectResolverIsReadOnlyTest() throws Fault {

    init();
    TEST_PROPS.setProperty(APITEST,
        "facesImplicitObjectResolverIsReadOnlyTest");
    invoke();

  } // END facesImplicitObjectResolverIsReadOnlyTest

  /*
   * @testName: facesManagedBeanResolverGetValueTest
   * 
   * @assertion_ids: JSF:SPEC:85
   * 
   * @test_Strategy: Ensure that if base is null, and the property parameter
   * matches the name of a managed bean: - search the request, session, and
   * application scopes for that attribute name, if found, a new managed bean
   * will not be created. - otherwise create and return a new instance of the
   * managed bean and set the propertyResolved property on the ELContext to
   * true. This test uses the ELResolvers from both JSP and Faces.
   * 
   * @since 1.2
   */
  public void facesManagedBeanResolverGetValueTest() throws Fault {

    init();
    TEST_PROPS.setProperty(APITEST, "facesManagedBeanResolverGetValueTest");
    invoke();

  } // END facesManagedBeanResolverGetValueTest

  /*
   * @testName: facesArrayListMapBeanResolverTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure that Arrays, Lists, Maps, and Beans can be properly
   * handled using the ELResolver chain returned by
   * FacesContext.getELContext().getELResolver();
   * 
   * @since 1.2
   */
  public void facesArrayListMapBeanResolverTest() throws Fault {

    init();
    TEST_PROPS.setProperty(APITEST, "facesArrayListMapBeanResolverTest");
    invoke();

  } // END facesArrayListMapBeanResolverTest

  /*
   * @testName: facesScopedAttributeResolverGetValueTest
   * 
   * @assertion_ids: JSF:SPEC:85
   * 
   * @test_Strategy: Ensure the following: - when searching scopes, ensure
   * request, session, and application are searched in that order - when a value
   * is resolved in any of those scopes, ELContext.isPropertyResolved() returns
   * true - If a value isn't resolved, null is returned and
   * ELContext.isPropertyResolved() returns false
   * 
   * @since 1.2
   */
  public void facesScopedAttributeResolverGetValueTest() throws Fault {

    init();
    TEST_PROPS.setProperty(APITEST, "facesScopedAttributeResolverGetValueTest");
    invoke();

  } // END facesScopedAttributeResolverGetValueTest

  /*
   * @testName: facesScopedAttributeResolverSetValueTest
   * 
   * @assertion_ids: JSF:SPEC:85
   * 
   * @test_Strategy: Ensure the following behavior for setValue() - if the value
   * already exists in request, session, or application (searched in that order)
   * replace the existing value with the new value provided to setValue(). - if
   * the attribute doesn't already exist set a new value in the request scope
   * 
   * @since 1.2
   */
  public void facesScopedAttributeResolverSetValueTest() throws Fault {

    init();
    TEST_PROPS.setProperty(APITEST, "facesScopedAttributeResolverSetValueTest");
    invoke();

  } // END facesScopedAttributeResolverSetValueTest

  /*
   * @testName: facesScopedAttributeResolverFeatureDescriptorTest
   * 
   * @assertion_ids: JSF:SPEC:85
   * 
   * @test_Strategy: Validate the following for the feature descriptors returned
   * by the ScopedAttributeResolver: - There is one feature descriptor for each
   * attribute in request, session, and application scopes. - The name and
   * displayName properties are the name of the attribute - The value associated
   * with ELResolver.TYPE is the runtime type of the attribute value - The value
   * associated with ELResolver.RESOLVABLE_AT_DESIGN_TIME is Boolean.TRUE - The
   * hidden and exper properties are false - The preferred property is true -
   * The shortDescription property is non-null
   * 
   * @since 1.2
   */
  public void facesScopedAttributeResolverFeatureDescriptorTest() throws Fault {

    init();
    TEST_PROPS.setProperty(APITEST,
        "facesScopedAttributeResolverFeatureDescriptorTest");
    invoke();

  } // END facesScopedAttributeResolverFeatureDescriptorTest

  /*
   * @testName: facesResourceBundleResolverGetValueTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure the ELResolver.getValue() is able to resolve various
   * ResouceBundles (using the var of 'simplerb' defined in the faces-config)
   * based on the Locale of the UIViewRoot. Additionally ensure that the
   * propertyResolved property of the ELContext is set to true if the property
   * was properly resolved.
   * 
   * @since 1.2
   */
  public void facesResourceBundleResolverGetValueTest() throws Fault {

    init();
    TEST_PROPS.setProperty(APITEST, "facesResourceBundleResolverGetValueTest");
    invoke();

  } // END facesResourceBundleResolverGetValueTest

  /*
   * @testName: facesResourceBundleResolverGetTypeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure ELResolver.getType() returns ResourceBundle.class
   * when calling getType() where the resolved property will be a a
   * ResourceBundle.
   * 
   * @since 1.2
   */
  public void facesResourceBundleResolverGetTypeTest() throws Fault {

    init();
    TEST_PROPS.setProperty(APITEST, "facesResourceBundleResolverGetTypeTest");
    invoke();

  } // END facesResourceBundleResolverGetTypeTest

  /*
   * @testName: facesResourceBundleResolverSetValueTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure a PropertyNotFoundException is thrown when calling
   * setValue() when the resolved property will be a ResourceBundle.
   * 
   * @since 1.2
   */
  public void facesResourceBundleResolverSetValueTest() throws Fault {

    init();
    TEST_PROPS.setProperty(APITEST, "facesResourceBundleResolverSetValueTest");
    invoke();

  } // END facesResourceBundleResolverSetValueTest

  /*
   * @testName: facesResourceBundleResolverIsReadOnlyTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure isReadOnly() returns true and the propertyResolved
   * property is set to true when the resolved property is a ResourceBundle.
   * 
   * @since 1.2
   */
  public void facesResourceBundleResolverIsReadOnlyTest() throws Fault {

    init();
    TEST_PROPS.setProperty(APITEST,
        "facesResourceBundleResolverIsReadOnlyTest");
    invoke();

  } // END facesResourceBundleResolverIsReadOnlyTest

  /*
   * @testName: facesResourceBundleResolverFeatureDescriptorTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure the FeatureDescriptor returned by the
   * ResourceBundleResolver is properly configured.
   * 
   * @since 1.2
   */
  public void facesResourceBundleResolverFeatureDescriptorTest() throws Fault {

    init();
    TEST_PROPS.setProperty(APITEST,
        "facesResourceBundleResolverFeatureDescriptorTest");
    invoke();

  } // END facesResourceBundleResolverFeatureDescriptorTest

  /*
   * @testName: facesConfigELResolverRegistrationTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Ensure ELResolvers specified within a faces-config file are
   * used when performing EL evaluations.
   * 
   * @since 1.2
   */
  public void facesConfigELResolverRegistrationTest() throws Fault {

    init();
    TEST_PROPS.setProperty(APITEST, "facesConfigELResolverRegistrationTest");
    invoke();

  } // END facesConfigELResolverRegistrationTest

  // --------------------------------------------------------- Private Methods

  private void init() {

    if (!init) {
      /*
       * Make a quick request to the FacesServlet not caring about the result
       */
      try {
        TestUtil.logMsg("MAKING REQUEST FOR FACES SERVLET");
        TEST_PROPS.setProperty(REQUEST,
            "GET " + CONTEXT_ROOT + "/faces/test.jsp HTTP/1.0");
        TEST_PROPS.setProperty(IGNORE_STATUS_CODE, "true");
        TEST_PROPS.setProperty(IGNORE_BODY, "true");
        invoke();
      } catch (Exception e) {
        // ignore it
      }
      init = true;
    }
  }

} // end of URLClient
