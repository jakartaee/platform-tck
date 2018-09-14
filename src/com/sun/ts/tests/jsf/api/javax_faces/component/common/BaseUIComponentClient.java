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
 * $Id$
 */
package com.sun.ts.tests.jsf.api.javax_faces.component.common;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.jsf.common.client.AbstractUrlClient;

public abstract class BaseUIComponentClient extends AbstractUrlClient {

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /**
   * testName: uiComponentGetAttributesTest
   * 
   * @assertion_ids: JSF:JAVADOC:596; JSF:JAVADOC:649; JSF:JAVADOC:596
   * @test_Strategy: Assert the following:
   *                 <ul>
   *                 <li>The <code>Map</code> implementation must implement the
   *                 <code>java.io.Serializable</code> interface.</li>
   *                 <li>Any attempt to add a <code>null</code> key or value
   *                 must throw a <code>NullPointerException</code>.</li>
   *                 <li>Any attempt to add a key that is not a String must
   *                 throw a <code>ClassCastException</code>.</li>
   *                 <li>If the attribute name specified as a key matches a
   *                 property of this {@link UIComponent}'s implementation
   *                 class, the following methods will have special behavior:
   *                 <ul>
   *                 <li><code>containsKey</code> - Return <code>false</code>.
   *                 </li>
   *                 <li><code>remove</code> - Throw
   *                 <code>IllegalArgumentException</code>.</li>
   *                 </ul>
   *                 </li>
   *                 </ul>
   * 
   */
  public void uiComponentGetAttributesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetAttributesTest");
    invoke();
  }

  /**
   * testName: uiComponentDecodeNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:583; JSF:JAVADOC:484
   * @test_Strategy: Validate that a NullPointerException is thrown when arg is
   *                 null.
   */
  public void uiComponentDecodeNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentDecodeNPETest");
    invoke();
  }

  /**
   * testName: uiComponentGetClientIdTest
   * 
   * @assertion_ids: JSF:JAVADOC:600; JSF:JAVADOC:649
   * @test_Strategy: Verify getClientId() returns a non-null value.
   */
  public void uiComponentGetClientIdTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetClientIdTest");
    invoke();
  }

  /**
   * testName: uiComponentGetClientIdContextTest
   * 
   * @assertion_ids: JSF:JAVADOC:600; JSF:JAVADOC:649
   * @test_Strategy: Verify getClientId(FacesContext) returns a non-null value.
   */
  public void uiComponentGetClientIdContextTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetClientIdContextTest");
    invoke();
  }

  /**
   * testName: uiComponentGetCurrentComponentTest
   * 
   * @assertion_ids: JSF:JAVADOC:509
   * @test_Strategy: Verify getCurrentComponent() returns current component that
   *                 is processing.
   * 
   * @since 2.0
   */
  public void uiComponentGetCurrentComponentTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetCurrentComponentTest");
    invoke();
  }

  /**
   * testName: uiComponentGetCurrentComponentNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:510
   * @test_Strategy: Verify getCurrentComponent(null) throws a
   *                 NullPointException.
   * 
   * @since 2.0
   */
  public void uiComponentGetCurrentComponentNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetCurrentComponentNPETest");
    invoke();
  }

  /**
   * testName: uiComponentGetCurrentComponentNullTest
   * 
   * @assertion_ids: JSF:JAVADOC:509
   * @test_Strategy: Verify getCurrentComponent() returns null if there is no
   *                 currently processing UIComponent
   * 
   * @since 2.0
   */
  public void uiComponentGetCurrentComponentNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetCurrentComponentNullTest");
    invoke();
  }

  /**
   * testName: uiComponentGetCurrentCompositeComponentNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:512
   * @test_Strategy: Verify getCurrentCompositeComponent(null) throws a
   *                 NullPointException.
   * 
   * @since 2.0
   */
  public void uiComponentGetCurrentCompositeComponentNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "uiComponentGetCurrentCompositeComponentNPETest");
    invoke();
  }

  /**
   * testName: uiComponentGetCurrentCompositeComponentNullTest
   * 
   * @assertion_ids: JSF:JAVADOC:511
   * @test_Strategy: Verify getCurrentCompositeComponent() returns null if there
   *                 is no currently processing UIComponent
   * 
   * @since 2.0
   */
  public void uiComponentGetCurrentCompositeComponentNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "uiComponentGetCurrentCompositeComponentNullTest");
    invoke();
  }

  /**
   * testName: uiComponentIsCurrentCompositeComponentNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:534
   * @test_Strategy: Verify isCompositeComponent(null) throws a
   *                 NullPointException.
   * 
   * @since 2.0
   */
  public void uiComponentIsCompositeComponentNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentIsCompositeComponentNPETest");
    invoke();
  }

  /**
   * testName: uiComponentIsCurrentCompositeComponentNullTest
   * 
   * @assertion_ids: JSF:JAVADOC:535
   * @test_Strategy: Verify isCompositeComponent() returns false if there is no
   *                 currently processing UIComponent
   * 
   * @since 2.0
   */
  public void uiComponentIsCompositeComponentNegTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentIsCompositeComponentNegTest");
  }

  /**
   * testName: uiComponentGetClientIdNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:505; JSF:JAVADOC:601
   * @test_Strategy: Verify getClientId() throws NPE if the <code>context</code>
   *                 argument is null.
   */
  public void uiComponentGetClientIdNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetClientIdNPETest");
    invoke();
  }

  /**
   * testName: uiComponentGetSetIdTest
   * 
   * @assertion_ids: JSF:JAVADOC:504; JSF:JAVADOC:558; JSF:JAVADOC:640;
   *                 JSF:JAVADOC:608; JSF:JAVADOC:641
   * @test_Strategy: Verify {get,set}Id() - if a value is set, the same value is
   *                 returned.
   */
  public void uiComponentGetSetIdTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetSetIdTest");
    invoke();
  }

  /**
   * testName: uiComponentSetIdRestrictionsTest
   * 
   * @assertion_ids: JSF:JAVADOC:558; JSF:JAVADOC:559; JSF:JAVADOC:641
   * @test_Strategy: Verify that component under test enforces the component ID
   *                 restrictions as specified: # Must not be a zero-length
   *                 String. # First character must be a letter or an underscore
   *                 ('_'). # Subsequent characters must be a letter, a digit,
   *                 an underscore ('_'), or a dash ('-').
   */
  public void uiComponentSetIdRestrictionsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentSetIdRestrictionsTest");
    invoke();
  }

  /**
   * testName: uiComponentQueueEventNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:561; JSF:JAVADOC:632; JSF:JAVADOC:557
   * @test_Strategy: Validate that a NullPointerException is thrown when arg is
   *                 null.
   */
  public void uiComponentQueueEventNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentQueueEventNPETest");
    invoke();
  }

  /**
   * testName: uiComponentGetSetParentTest
   * 
   * @assertion_ids: JSF:JAVADOC:561; JSF:JAVADOC:609; JSF:JAVADOC:642
   * @test_Strategy: Verify {get,set}Parent() - if a value is set, the same
   *                 value is returned.
   */
  public void uiComponentGetSetParentTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetSetParentTest");
    invoke();
  }

  /**
   * testName: uiComponentInvokeOnComponentTest
   * 
   * @assertion_ids: JSF:JAVADOC:531; JSF:JAVADOC:614
   * @test_Strategy: Validate that the correct information is returned after the
   *                 InvokeOnComponent has been called on a given UIComponent.
   */
  public void uiComponentInvokeOnComponentTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentInvokeOnComponentTest");
    invoke();
  }

  /**
   * testName: uiComponentInvokeOnComponentNegativeTest
   * 
   * @assertion_ids: JSF:JAVADOC:531; JSF:JAVADOC:532; JSF:JAVADOC:533;
   *                 JSF:JAVADOC:615; JSF:JAVADOC:616
   * @test_Strategy: Validate that the correct Exceptions are thrown when
   *                 calling the InvokeOnComponent method incorrectly.
   */
  public void uiComponentInvokeOnComponentNegativeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentInvokeOnComponentNegativeTest");
    invoke();
  }

  /**
   * testName: uiComponentIsSetRenderedTest
   * 
   * @assertion_ids: JSF:JAVADOC:537; JSF:JAVADOC:562; JSF:JAVADOC:617
   *                 JSF:JAVADOC:643
   * @test_Strategy: Verify {is,set}Rendered() - if a value is set, the same
   *                 value is returned.
   */
  public void uiComponentIsSetRenderedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentIsSetRenderedTest");
    invoke();
  }

  /**
   * testName: uiComponentIsInViewTest
   * 
   * @assertion_ids: JSF:JAVADOC:536
   * @test_Strategy: Verify isInView() Retuns true if the component is in the
   *                 current view.
   * 
   * @since 2.0
   */
  public void uiComponentIsInViewTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentIsInViewTest");
    invoke();
  }

  /**
   * testName: uiComponentIsInViewNegTest
   * 
   * @assertion_ids: JSF:JAVADOC:536
   * @test_Strategy: Verify isInView() Retuns false if the component is BOT in
   *                 the current view.
   * 
   * @since 2.0
   */
  public void uiComponentIsInViewNegTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentIsInViewNegTest");
    invoke();
  }

  /**
   * testName: uiComponentVisitTreeTest
   * 
   * @assertion_ids: JSF:JAVADOC:575
   * @test_Strategy: Verify that we receive false when calling
   *                 UIComponent.visitTree().
   * @since 2.0
   */
  public void uiComponentVisitTreeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentVisitTreeTest");
    invoke();
  }

  /**
   * testName: uiComponentVisitTreeNegTest
   * 
   * @assertion_ids: JSF:JAVADOC:575
   * @test_Strategy: Verify that we receive false when calling
   *                 UIComponent.visitTree().
   * @since 2.0
   */
  public void uiComponentVisitTreeNegTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentVisitTreeNegTest");
    invoke();
  }

  /**
   * testName: uiComponentGetChildrenTest
   * 
   * @assertion_ids: JSF:JAVADOC:502; JSF:JAVADOC:598
   * @test_Strategy: Verify the following: * implementation must support all of
   *                 the standard and optional <code>List</code> methods, plus
   *                 support the following additional requirements:
   *                 </p>
   *                 <ul>
   *                 <li>The <code>List</code> implementation must implement the
   *                 <code>java.io.Serializable</code> interface.</li>
   *                 <li>Any attempt to add a <code>null</code> must throw a
   *                 NullPointerException</li>
   *                 <li>Any attempt to add an object that does not implement
   *                 {@link UIComponent} must throw a ClassCastException.</li>
   * 
   *                 <li>Any attempt to add a child {@link UIComponent} with a
   *                 non-null <code>componentId</code> that contains invalid
   *                 characters, or begins with
   *                 {@link NamingContainer#SEPARATOR_CHAR}, or
   *                 {@link UIViewRoot#UNIQUE_ID_PREFIX} (i.e. other than
   *                 letters, digits, '-', or '_') must throw
   *                 IllegalArgumentException.</li>
   * 
   *                 <li>Whenever a new child component is added:
   *                 <ul>
   *                 <li>The <code>parent</code> property of the child must be
   *                 set to this component instance.</li>
   *                 </ul>
   *                 </li>
   *                 <li>Whenever an existing child component is removed:
   *                 <ul>
   *                 <li>The <code>parent</code> property of the child must be
   *                 set to <code>null</code>.</li>
   *                 </ul>
   *                 </li>
   *                 </ul>
   */
  public void uiComponentGetChildrenTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetChildrenTest");
    invoke();
  }

  /**
   * testName: uiComponentGetSetRendererTypeTest
   * 
   * @assertion_ids: JSF:JAVADOC:523; JSF:JAVADOC:563; JSF:JAVADOC:610;
   *                 JSF:JAVADOC:644
   * @test_Strategy: Verify {get,set}Parent() - if a value is set, the same
   *                 value is returned. Also verify that the default return
   *                 value. If a component is specified as having a specific
   *                 Renderer type, ensure that value is returned, otherwise
   *                 ensure that null is returned.
   */
  public void uiComponentGetSetRendererTypeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetSetRendererTypeTest");
    invoke();
  }

  /**
   * testName: uiComponentGetRendersChildrenTest
   * 
   * @assertion_ids: JSF:JAVADOC:524; JSF:JAVADOC:611
   * @test_Strategy: Verify getRendersChildren() returns the expected value by
   *                 obtaining this components Renderer and calling
   *                 getRendersChildren() in the Renderer, and comparing that
   *                 value with what is returned by calling getRendersChildren()
   *                 on the component.
   */
  public void uiComponentGetRendersChildrenTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetRendersChildrenTest");
    invoke();
  }

  /**
   * testName: uiComponentGetChildCountTest
   * 
   * @assertion_ids: JSF:JAVADOC:501; JSF:JAVADOC:597
   * @test_Strategy: Validate getChildCount() returns the expected value.
   */
  public void uiComponentGetChildCountTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetChildCountTest");
    invoke();
  }

  /**
   * testName: uiComponentGetFacetsTest
   * 
   * @assertion_ids: JSF:JAVADOC:515; JSF:JAVADOC:606
   * @test_Strategy: Assert the following: The returned implementation must
   *                 support all of the standard and optional <code>Map</code>
   *                 methods, plus support the following additional
   *                 requirements:
   *                 </p>
   *                 <ul>
   *                 <li>The <code>Map</code> implementation must implement the
   *                 <code>java.io.Serializable</code> interface.</li>
   *                 <li>Any attempt to add a <code>null</code> key or value
   *                 must throw a NullPointerException.</li>
   *                 <li>Any attempt to add a key that is not a String must
   *                 throw a ClassCastException.</li>
   *                 <li>Any attempt to add a value that is not a
   *                 {@link UIComponent} must throw a ClassCastException.</li>
   *                 <li>Whenever a new facet {@link UIComponent} is added:
   *                 <ul>
   *                 <li>The <code>parent</code> property of the component must
   *                 be set to this component instance.</li>
   *                 </ul>
   *                 </li>
   *                 <li>Whenever an existing facet {@link UIComponent} is
   *                 removed:
   *                 <ul>
   *                 <li>The <code>parent</code> property of the facet must be
   *                 set to <code>null</code>.</li>
   *                 </ul>
   *                 </li>
   *                 </ul>
   */
  public void uiComponentGetFacetsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetFacetsTest");
    invoke();
  }

  /**
   * testName: uiComponentGetFacetCountTest
   * 
   * @assertion_ids: JSF:JAVADOC:514; JSF:JAVADOC:605
   * @test_Strategy: Validate that we receive 1 or more facets.
   */
  public void uiComponentGetFacetCountTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetFacetCountTest");
    invoke();
  }

  /**
   * testName: uiComponentGetFacetTest
   * 
   * @assertion_ids: JSF:JAVADOC:514; JSF:JAVADOC:604
   * @test_Strategy: Ensure a facet of a component can be obtained by name.
   */
  public void uiComponentGetFacetTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetFacetTest");
    invoke();
  }

  /**
   * testName: uiComponentGetFacetsAndChildrenTest
   * 
   * @assertion_ids: JSF:JAVADOC:516; JSF:JAVADOC:607
   * @test_Strategy: Ensure the Iterator returned has the expected values, and
   *                 in the case of the UIComponent instances contained in the
   *                 iterator, ensure the order is correct.
   */
  public void uiComponentGetFacetsAndChildrenTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetFacetsAndChildrenTest");
    invoke();
  }

  /**
   * testName: uiComponentGetFamilyTest
   * 
   * @assertion_ids: JSF:JAVADOC:448; JSF:JAVADOC:517
   * @test_Strategy: Unsure we get the proper family back.
   */
  public void uiComponentGetFamilyTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetFamilyTest");
    invoke();
  }

  /**
   * testName: uiComponentFindComponentTest
   * 
   * @assertion_ids: JSF:JAVADOC:497; JSF:JAVADOC:593
   * @test_Strategy: Validate the behavior of findConponent(): - Identify the
   *                 UIComponent that will be the base for searching, by
   *                 stopping as soon as one of the following conditions is met:
   *                 o If the search expression begins with the the separator
   *                 character (called an "absolute" search expression), the
   *                 base will be the root UIComponent of the component tree.
   *                 The leading separator character will be stripped off, and
   *                 the remainder of the search expression will be treated as a
   *                 "relative" search expression as described below. o
   *                 Otherwise, if this UIComponent is a NamingContainer it will
   *                 serve as the basis. o Otherwise, search up the parents of
   *                 this component. If a NamingContainer is encountered, it
   *                 will be the base. o Otherwise (if no NamingContainer is
   *                 encountered) the root UIComponent will be the base. - The
   *                 search expression (possibly modified in the previous step)
   *                 is now a "relative" search expression that will be used to
   *                 locate the component (if any) that has an id that matches,
   *                 within the scope of the base component. The match is
   *                 performed as follows: o If the search expression is a
   *                 simple identifier, this value is compared to the id
   *                 property, and then recursively through the facets and
   *                 children of the base UIComponent (except that if a
   *                 descendant NamingContainer is found, its own facets and
   *                 children are not searched). o If the search expression
   *                 includes more than one identifier separated by the
   *                 separator character, the first identifier is used to locate
   *                 a NamingContainer by the rules in the previous bullet
   *                 point. Then, the findComponent() method of this
   *                 NamingContainer will be called, passing the remainder of
   *                 the search expression.
   */
  public void uiComponentFindComponentTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentFindComponentTest");
    invoke();
  }

  /**
   * testName: uiComponentProcessSaveRestoreStateTest
   * 
   * @assertion_ids: JSF:JAVADOC:545; JSF:JAVADOC:547; JSF:JAVADOC:624
   * @test_Strategy: Assert the following when process{Save,Restore}State() is
   *                 called: - Call the process{Save,Restore}State() method of
   *                 all facets and children of this UIComponent in the order
   *                 determined by a call to getFacetsAndChildren(). - Call the
   *                 {save,restore}State() method of this component.
   */
  public void uiComponentProcessSaveRestoreStateTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentProcessSaveRestoreStateTest");
    invoke();
  }

  /**
   * testName: uiComponentProcessSaveStateNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:548; JSF:JAVADOC:625
   * @test_Strategy: Validate that a NullPointerException is thrown when context
   *                 is null.
   * 
   * @since 2.0
   */
  public void uiComponentProcessSaveStateNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentProcessSaveStateNPETest");
    invoke();
  }

  /**
   * testName: uiComponentProcessRestoreStateNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:546; JSF:JAVADOC:623
   * @test_Strategy: Validate that a NullPointerException is thrown when context
   *                 is null.
   * 
   * @since 2.0
   */
  public void uiComponentProcessRestoreStateNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentProcessRestoreStateNPETest");
    invoke();
  }

  /**
   * testName: uiComponentProcessSaveStateTransientTest
   * 
   * @assertion_ids: JSF:JAVADOC:547
   * @test_Strategy: Verify that when a component is marked transient, that
   *                 saveState() is not called, nor is
   *                 processSaveState()/saveState() is called on any children of
   *                 the transient component.
   */
  public void uiComponentProcessSaveStateTransientTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentProcessSaveStateTransientTest");
    invoke();
  }

  /**
   * testName: uiComponentProcessUpdatesTest
   * 
   * @assertion_ids: JSF:JAVADOC:549; JSF:JAVADOC:626
   * @test_Strategy: Assert the following when processUpdates() is called: #
   *                 Call the processUpdates() method of all facets and children
   *                 of this UIComponent. # If the current component is an
   *                 UIInput, call its updateModel() method of this component,
   *                 if this component's rendered property is true and it is not
   *                 nested in a parent component whose rendersChildren property
   *                 is true but whose rendered property is false.
   */
  public void uiComponentProcessUpdatesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentProcessUpdatesTest");
    invoke();
  }

  /**
   * testName: uiComponentProcessUpdatesNotRenderedTest
   * 
   * @assertion_ids: JSF:JAVADOC:549; JSF:JAVADOC:626
   * @test_Strategy: Assert the following when processUpdates() is called and
   *                 the component's rendered property is false: - If the
   *                 rendered property of this UIComponent is false, skip
   *                 further processing.
   */
  public void uiComponentProcessUpdatesNotRenderedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentProcessUpdatesNotRenderedTest");
    invoke();
  }

  /**
   * testName: uiComponentProcessUpdatesNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:550; JSF:JAVADOC:627
   * @test_Strategy: Validate a NullPointerException is thrown if context is
   *                 null.
   * 
   * @since 2.0
   */
  public void uiComponentProcessUpdatesNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentProcessUpdatesNPETest");
    invoke();
  }

  /**
   * testName: uiComponentRestoreAttachedStateNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:550; JSF:JAVADOC:635
   * @test_Strategy: Validate a NullPointerException is thrown if context is
   *                 null.
   * 
   */
  public void uiComponentRestoreAttachedStateNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentRestoreAttachedStateNPETest");
    invoke();
  }

  /**
   * testName: uiComponentSaveAttachedStateNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:550; JSF:JAVADOC:638
   * @test_Strategy: Validate a NullPointerException is thrown if context is
   *                 null.
   * 
   */
  public void uiComponentSaveAttachedStateNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentSaveAttachedStateNPETest");
    invoke();
  }

  /**
   * testName: uiComponentProcessUpdatesRenderResponseTest
   * 
   * @assertion_ids: JSF:JAVADOC:549; JSF:JAVADOC:626
   * @test_Strategy: Assert the following when calling processUpdates() and a
   *                 component is marked as invalid: If the valid property of
   *                 this UIComponent is now false, call
   *                 FacesContext.renderResponse() to transfer control at the
   *                 end of the current phase.
   */
  public void uiComponentProcessUpdatesRenderResponseTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "uiComponentProcessUpdatesRenderResponseTest");
    invoke();
  }

  /**
   * testName: uiComponentProcessValidatorsTest
   * 
   * @assertion_ids: JSF:JAVADOC:551; JSF:JAVADOC:628
   * @test_Strategy: Assert the following when processValidators() is called: #
   *                 Call the processValidators() method of all facets and
   *                 children of this UIComponent. # If the current component is
   *                 an UIInput, call its validate() method, if this component's
   *                 rendered property is true and it is not nested in a parent
   *                 component whose rendersChildren property is true but whose
   *                 rendered property is false.
   */
  public void uiComponentProcessValidatorsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentProcessValidatorsTest");
    invoke();
  }

  /**
   * testName: uiComponentProcessValidatorsNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:552; JSF:JAVADOC:629
   * @test_Strategy: Validate a NullPointerException is thrown when argument is
   *                 null.
   */
  public void uiComponentProcessValidatorsNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentProcessValidatorsNPETest");
    invoke();
  }

  /**
   * testName: uiComponentProcessValidatorsNotRenderedTest
   * 
   * @assertion_ids: JSF:JAVADOC:551; JSF:JAVADOC:628
   * @test_Strategy: Assert the following when processValidators() is called and
   *                 the component's rendered property is false: - If the
   *                 rendered property of this UIComponent is false, skip
   *                 further processing.
   */
  public void uiComponentProcessValidatorsNotRenderedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "uiComponentProcessValidatorsNotRenderedTest");
    invoke();
  }

  /**
   * testName: uiComponentProcessValidatorsRenderResponseTest
   * 
   * @assertion_ids: JSF:JAVADOC:551; JSF:JAVADOC:628
   * @test_Strategy: Assert renderResponse() is called if a RuntimeException is
   *                 thrown when calling validate() on a particular component.
   */
  public void uiComponentProcessValidatorsRenderResponseTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "uiComponentProcessValidatorsRenderResponseTest");
    invoke();
  }

  /**
   * testName: uiComponentProcessValidatorsIsValidRenderResponseTest
   * 
   * @assertion_ids: JSF:JAVADOC:551; JSF:JAVADOC:628
   * @test_Strategy: Assert the following when processValidators() is called and
   *                 the component has been marked invalid: - If the isValid()
   *                 method of this component returns false, call the
   *                 renderResponse() method on the FacesContext instance for
   *                 this request.
   */
  public void uiComponentProcessValidatorsIsValidRenderResponseTest()
      throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "uiComponentProcessValidatorsIsValidRenderResponseTest");
    invoke();
  }

  /**
   * testName: uiComponentProcessDecodesTest
   * 
   * @assertion_ids: JSF:JAVADOC:541; JSF:JAVADOC:620; JSF:JAVADOC:483;
   *                 JSF:JAVADOC:582
   * @test_Strategy: Assert the following when processDecodes is called: # Call
   *                 the processDecodes() method of all facets and children of
   *                 this UIComponent.
   * 
   *                 # Call the decode() method of this component, if this
   *                 component's rendered property is true and it is not nested
   *                 in a parent component whose rendersChildren property is
   *                 true but whose rendered property is false.
   */
  public void uiComponentProcessDecodesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentProcessDecodesTest");
    invoke();
  }

  /**
   * @testName: uiComponentProcessDecodesNPETest
   * @assertion_ids: JSF:JAVADOC:542; JSF:JAVADOC:621
   * @test_Strategy: Validate a NullPointerException is thrown when arg is null.
   */
  public void uiComponentProcessDecodesNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentProcessDecodesTest");
    invoke();
  }

  /**
   * testName: uiComponentPushComponentToELTest
   * 
   * @assertion_ids: JSF:JAVADOC:553
   * @test_Strategy: Assert that a call to pushComponentToEL does put a
   *                 UIComponent onto the EL stack, and that another call to the
   *                 method causes the newly added component to be the current
   *                 component.
   * 
   * @since 2.0
   */
  public void uiComponentPushComponentToELTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentPushComponentToELTest");
    invoke();
  }

  /**
   * testName: uiComponentPushComponentToELNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:554
   * @test_Strategy: Validate hat a NullPointerException is thrown when
   *                 FacesContext is null.
   * 
   * @since 2.0
   */
  public void uiComponentPushComponentToELNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentPushComponentToELNPETest");
    invoke();
  }

  /**
   * testName: uiComponentPopComponentFromELTest
   * 
   * @assertion_ids: JSF:JAVADOC:539
   * @test_Strategy: Assert that a call to popComponentToEL does put a the
   *                 previous UIComponent as the current component being
   *                 processed.
   * 
   * @since 2.0
   */
  public void uiComponentPopComponentFromELTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentPopComponentFromELTest");
    invoke();
  }

  /**
   * testName: uiComponentPopComponentFromELNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:540
   * @test_Strategy: Validate that a NullPointerException is thrown if
   *                 FacesContext is null.
   * 
   * @since 2.0
   */
  public void uiComponentPopComponentFromELNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentPopComponentFromELNPETest");
    invoke();
  }

  /**
   * testName: uiComponentProcessDecodesNotRenderedTest
   * 
   * @assertion_ids: JSF:JAVADOC:541
   * @test_Strategy: Assert the following when processDecodes() is called and
   *                 the component's rendered property is false: - If the
   *                 rendered property of this UIComponent is false, skip
   *                 further processing.
   */
  public void uiComponentProcessDecodesNotRenderedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentProcessDecodesNotRenderedTest");
    invoke();
  }

  /**
   * testName: uiComponentProcessDecodesRenderResponseTest
   * 
   * @assertion_ids: JSF:JAVADOC:541
   * @test_Strategy: Assert renderResponse() is called if a RuntimeException is
   *                 thrown when calling decode() on a particular component.
   */
  public void uiComponentProcessDecodesRenderResponseTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "uiComponentProcessDecodesRenderResponseTest");
    invoke();
  }

  /**
   * testName: uiComponentEncodeBeginTest
   * 
   * @assertion_ids: JSF:JAVADOC:488; JSF:JAVADOC:584
   * @test_Strategy: Verify that when encodeBegin() is called on the component
   *                 under test, it delegates this call to the encodeBegin()
   *                 method of the Renderer
   */
  public void uiComponentEncodeBeginTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentEncodeBeginTest");
    invoke();
  }

  /**
   * testName: uiComponentEncodeAllNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:487
   * @test_Strategy: Verify that when encodeAll() is called passing null as the
   *                 context, a NullPointException is thrown.
   * 
   * @since 2.0
   */
  public void uiComponentEncodeAllNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentEncodeAllNPETest");
    invoke();
  }

  /**
   * testName: uiComponentEncodeBeginELTest
   * 
   * @assertion_ids: JSF:JAVADOC:488
   * @test_Strategy: Verify that when encodeBegin() is called that a call is
   *                 made to
   *                 pushComponentToEL(javax.faces.context.FacesContext).
   */

  public void uiComponentEncodeBeginELTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentEncodeBeginELTest");
    invoke();
  }

  /**
   * testName: uiComponentEncodeBeginNotRenderedTest
   * 
   * @assertion_ids: JSF:JAVADOC:488; JSF:JAVADOC:584
   * @test_Strategy: Verify that the component doesn't delegate the
   *                 encodeBegin() call when the rendered property of the
   *                 component is false.
   */
  public void uiComponentEncodeBeginNotRenderedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentEncodeBeginNotRenderedTest");
    invoke();
  }

  /**
   * testName: uiComponentEncodeEndTest
   * 
   * @assertion_ids: JSF:JAVADOC:494; JSF:JAVADOC:590
   * @test_Strategy: Verify that when encodeEnd() is called on the component
   *                 under test, it delegates this call to the encodeEnd()
   *                 method of the Renderer
   */
  public void uiComponentEncodeEndTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentEncodeEndTest");
    invoke();
  }

  /**
   * testName: uiComponentEncodeEndELTest
   * 
   * @assertion_ids: JSF:JAVADOC:494
   * @test_Strategy: Verify that when encodeEnd() is called on the component
   *                 under test, it calls
   *                 popComponentFromEL(javax.faces.context.FacesContext).
   */
  public void uiComponentEncodeEndELTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentEncodeEndELTest");
    invoke();
  }

  /**
   * testName: uiComponentEncodeEndNotRenderedTest
   * 
   * @assertion_ids: JSF:JAVADOC:494
   * @test_Strategy: Verify that the component doesn't delegate the encodeEnd()
   *                 call when the rendered property of the component is false.
   */
  public void uiComponentEncodeEndNotRenderedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentEncodeEndNotRenderedTest");
    invoke();
  }

  /**
   * testName: uiComponentEncodeChildrenTest
   * 
   * @assertion_ids: JSF:JAVADOC:491; JSF:JAVADOC:587
   * @test_Strategy: Verify that when encodeChildren() is called on the
   *                 component under test, it delegates this call to the
   *                 encodeChildren() method of the Renderer
   */
  public void uiComponentEncodeChildrenTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentEncodeChildrenTest");
    invoke();
  }

  /**
   * testName: uiComponentEncodeChildrenNotRenderedTest
   * 
   * @assertion_ids: JSF:JAVADOC:491; JSF:JAVADOC:587
   * @test_Strategy: Verify that the component doesn't delegate the
   *                 encodeChildren() call when the rendered property of the
   *                 component is false.
   */
  public void uiComponentEncodeChildrenNotRenderedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentEncodeChildrenNotRenderedTest");
    invoke();
  }

  /**
   * testName: uiComponentEncodeBeginNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:490; JSF:JAVADOC:586
   * @test_Strategy: Verify an NPE is thrown if the <code>context</code>
   *                 argument is null.
   */
  public void uiComponentEncodeBeginNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentEncodeBeginNPETest");
    invoke();
  }

  /**
   * testName: uiComponentEncodeEndNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:496; JSF:JAVADOC:592
   * @test_Strategy: Verify an NPE is thrown if the <code>context</code>
   *                 argument is null.
   */
  public void uiComponentEncodeEndNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentEncodeEndNPETest");
    invoke();
  }

  /**
   * testName: uiComponentEncodeChildrenNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:493; JSF:JAVADOC:589
   * @test_Strategy: Verify an NPE is thrown if the <code>context</code>
   *                 argument is null.
   */
  public void uiComponentEncodeChildrenNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentEncodeChildrenNPETest");
    invoke();
  }

  /**
   * testName: uiComponentFindComponentIAETest
   * 
   * @assertion_ids: JSF:JAVADOC:498; JSF:JAVADOC:594
   * @test_Strategy: Verify an IllegalArgumentException is thrown by
   *                 findComponent when an intermediate identifier in a search
   *                 expression identifies a UIComponent that is not a
   *                 NamingContainer
   */
  public void uiComponentFindComponentIAETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentFindComponentIAETest");
    invoke();
  }

  /**
   * testName: uiComponentFindComponentNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:499; JSF:JAVADOC:595
   * @test_Strategy: Verify a NullPointerException is thrown if expr is null.
   */
  public void uiComponentFindComponentNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentFindComponentNPETest");
    invoke();
  }

  /**
   * testName: uiComponentGetSetValueExpressionTest
   * 
   * @assertion_ids: JSF:JAVADOC:528; JSF:JAVADOC:567
   * @test_Strategy: Verify the following: - setValueExpression with a
   *                 non-literal ValueExpression results in getValueExpression()
   *                 returning the non-literal ValueExpression -
   *                 setValueExpression with a literal ValueExpression results
   *                 in getValueExpression returning null for that particular
   *                 key and the evaluated value of the ValueExpression is
   *                 stored in the component's attribute map. - Providing a null
   *                 value for the ValueExpression argument of -
   *                 setValueExpression() where a ValueExpression has already
   *                 been stored using the same key, will result in the
   *                 ValueExpression being removed from its internal collection
   *                 - this is verified by calling getValueExpression() and
   *                 ensuring a null return
   * @since 1.2
   */
  public void uiComponentGetSetValueExpressionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetSetValueExpressionTest");
    invoke();
  }

  /**
   * testName: uiComponentGetSetValueExpressionNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:529; JSF:JAVADOC:569
   * @test_Strategy: Verify an NPE is thrown if a null value for the 'name'
   *                 argument is passed to getValueExpression().
   * @since 1.2
   */
  public void uiComponentGetSetValueExpressionNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetSetValueExpressionNPETest");
    invoke();
  }

  /**
   * testName: uiComponentSetValueExpressionNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:569
   * @test_Strategy: Verify an NPE is thrown if a null value for the 'name'
   *                 argument is passed to setValueExpression().
   * @since 1.2
   */
  public void uiComponentSetValueExpressionNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentSetValueExpressionNPETest");
    invoke();
  }

  /**
   * testName: uiComponentSetValueExpressionIAETest
   * 
   * @assertion_ids: JSF:JAVADOC:568
   * @test_Strategy: Verify an IAE is thrown if a null value for the 'name'
   *                 argument is 'id' or 'parent' setValueExpression().
   * @since 1.2
   */
  public void uiComponentSetValueExpressionIAETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentSetValueExpressionIAETest");
    invoke();
  }

  /**
   * testName: uiComponentSubscribeToEventTest
   * 
   * @assertion_ids: JSF:JAVADOC:570
   * @test_Strategy: Ensure UIComponent.subscribeToEvent Does not throw any
   *                 Exceptions when called correctly.
   * 
   *                 Test cases: UIComponent.subscribeToEvent(SystemEvent,
   *                 ComponentSystemEventlistener)
   * @since 2.0
   */
  public void uiComponentSubscribeToEventTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentSubscribeToEventTest");
    invoke();
  }

  /**
   * testName: uiComponentSubscribeToEventNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:2547
   * @test_Strategy: Ensure UIComponent.uiComponentSubscribeToEventNPETest
   *                 throws a NullPointerException in the following test cases:
   * 
   *                 Test cases: UIComponent.subscribeToEvent(null,
   *                 ComponentSystemEventlistener)
   *                 UIComponent.subscribeToEvent(SystemEvent, null)
   * @since 2.0
   */
  public void uiComponentSubscribeToEventNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentSubscribeToEventNPETest");
    invoke();
  }

  // --------------------------------------------------- StateHolder Tests
  /**
   * testName: stateHolderIsSetTransientTest
   * 
   * @assertion_ids: JSF:JAVADOC:442
   * @test_Strategy: Verify {is,set}Transient() - if a value is set, the same
   *                 value is returned.
   */
  public void stateHolderIsSetTransientTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "stateHolderIsSetTransientTest");
    invoke();
  }

  /**
   * testName: stateHolderSaveRestoreStateTest
   * 
   * @assertion_ids: JSF:JAVADOC:445
   * @test_Strategy: Verify saveState returns a serialized object of the
   *                 component's current state and that this state can be
   *                 restored when passing this state back into the
   *                 restoreState() method.
   */
  public void stateHolderSaveRestoreStateTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "stateHolderSaveRestoreStateTest");
    invoke();
  }

  /**
   * testName: stateHolderRestoreStateNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:444
   * @test_Strategy: Verify that restoreState throws a NullpointerException if
   *                 either FacesContext or state is null.
   */
  public void stateHolderRestoreStateNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "stateHolderRestoreStateNPETest");
    invoke();
  }

  /**
   * testName: stateHolderSaveStateNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:446
   * @test_Strategy: Verify that saveState throws a NullpointerException if
   *                 either FacesContext null.
   */
  public void stateHolderSaveStateNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "stateHolderSaveStateNPETest");
    invoke();
  }

  // --------------------------------------------- PartialStateHolder Tests

  /**
   * testName: partialStateHolderMICStateTest
   * 
   * @assertion_ids: JSF:JAVADOC:482; JSF:JAVADOC:530; JSF:JAVADOC:538;
   *                 JSF:JAVADOC:581; JSF:JAVADOC:619; JSF:JAVADOC:431;
   *                 JSF:JAVADOC:432; JSF:JAVADOC:433
   * @test_Strategy: Verify that clearInitialState(), initialStateMarked(), and
   *                 markInitialState() work correctly, by setting and checking
   *                 values.
   */
  public void partialStateHolderMICStateTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "partialStateHolderMICStateTest");
    invoke();
  }

  // -------------------------------------------------------------------------
  /**
   * testName: actionSourceAddGetRemoveActListTest
   * 
   * @assertion_ids: JSF:JAVADOC:392; JSF:JAVADOC:395; JSF:JAVADOC:398
   * @test_Strategy: Verify the following: - If no ActionListeners have been
   *                 added, an empty array is returned. - Verify listeners added
   *                 are present in the array returned by getActionListeners().
   *                 - Verify listeners that have been removed are not present
   *                 in the array returned by getActionListeners().
   */
  public void actionSourceAddGetRemoveActListTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "actionSourceAddGetRemoveActListTest");
    invoke();
  }

  /**
   * testName: actionSourceAddActListNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:393
   * @test_Strategy: Verify an NPE is thrown when attempting to pass a null
   *                 value to addActionListener().
   */
  public void actionSourceAddActListNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "actionSourceAddActListNPETest");
    invoke();
  }

  /**
   * testName: actionSourceRemoveActListNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:399
   * @test_Strategy: Verify a NullPointerException is thrown when attempting to
   *                 pass a null value to removeActionListener().
   */
  public void actionSourceRemoveActListNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "actionSourceRemoveActListNPETest");
    invoke();
  }

  /**
   * testName: actionSourceRemoveActionListenerNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:393
   * @test_Strategy: Verify a NullPointerEception is thrown when attempting to
   *                 pass a null value to removeActionListener().
   */
  public void actionSourceRemoveActionListenerNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "actionSourceRemoveActionListenerNPETest");
    invoke();
  }

  /**
   * testName: actionSourceGetSetActionTest
   * 
   * @assertion_ids: JSF:JAVADOC:394; JSF:JAVADOC:400
   * @test_Strategy: Verify {get,set}Action() - if a value is set, the same
   *                 value is returned.
   */
  public void actionSourceGetSetActionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "actionSourceGetSetActionTest");
    invoke();
  }

  /**
   * testName: actionSourceGetSetActListTest
   * 
   * @assertion_ids: JSF:JAVADOC:396
   * @test_Strategy: Verify {get,set}ActionListener() - if a value is set, the
   *                 same value is returned.
   */
  public void actionSourceGetSetActListTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "actionSourceGetSetActListTest");
    invoke();
  }

  /**
   * testName: actionSourceSetIsImmediateTest
   * 
   * @assertion_ids: JSF:JAVADOC:402
   * @test_Strategy: Verify {is,set}Immediate() - if a value is set, the same
   *                 value is returned.
   */
  public void actionSourceSetIsImmediateTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "actionSourceSetIsImmediateTest");
    invoke();
  }

  // -----------------------------------------------------------------------------
  /**
   * testName: actionSource2GetSetActionExpressionTest assertion_ids:
   * JSF:JAVADOC:403 test_Strategy: Verify {get,set}ActionExpression in
   * ActionSource2 components accept and properly return a provided
   * MethodExpression instance.
   * 
   * @since 1.2
   */
  public void actionSource2GetSetActionExpressionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "actionSource2GetSetActionExpressionTest");
    invoke();
  }

  // -----------------------------------------------------------------------------
  /**
   * testName: valueHolderGetSetValueTest
   * 
   * @assertion_ids: JSF:JAVADOC:966; JSF:JAVADOC:968
   * @test_Strategy: Verify {get,set}Value() - if a value is set, the same value
   *                 is returned.
   */
  public void valueHolderGetSetValueTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valueHolderGetSetValueTest");
    invoke();
  }

  /**
   * testName: valueHolderGetSetConverterTest
   * 
   * @assertion_ids: JSF:JAVADOC:964; JSF:JAVADOC:967
   * @test_Strategy: Verify {get,set}Immediate() - if a value is set, the same
   *                 value is returned.
   */
  public void valueHolderGetSetConverterTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "valueHolderGetSetConverterTest");
    invoke();
  }

  // ---------------------------------------------------------
  // EditableValueHolder
  /**
   * testName: editableValueHolderIsSetValidTest
   * 
   * @assertion_ids: JSF:JAVADOC:416
   * @test_Strategy: Verify {is,set}Valid() - if a value is set, the same value
   *                 is returned.
   */
  public void editableValueHolderIsSetValidTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "editableValueHolderIsSetValidTest");
    invoke();
  }

  /**
   * @testName: editableValueHolderIsSetImmediateTest
   * @assertion_ids: JSF:JAVADOC:415; JSF:JAVADOC:423
   * @test_Strategy: editableValueHolder.{is,set}Immediate(): Validate that the
   *                 default value is false and that if we change the value via
   *                 setImmediate that we get the correct value back.
   */
  public void editableValueHolderIsSetImmediateTest() throws EETest.Fault {
    TEST_PROPS.setProperty(APITEST, "editableValueHolderIsSetImmediateTest");
    invoke();
  }

  /**
   * testName: editableValueHolderAddGetRemoveValidatorTest
   * 
   * @assertion_ids: JSF:JAVADOC:406; JSF:JAVADOC:411; JSF:JAVADOC:419;
   *                 JSF:JAVADOC:412
   * @test_Strategy: Verify the default behavior of addValidator(),
   *                 removeValidator(), and getValidators() by first adding a
   *                 validator to the EditableValueHolder instance, then calling
   *                 getValidators() and ensure the expected length and instance
   *                 contained in the array. Then call removeValidator(). Ensure
   *                 the array size is as expected.
   */
  public void editableValueHolderAddGetRemoveValidatorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "editableValueHolderAddGetRemoveValidatorTest");
    invoke();
  }

  /**
   * testName: editableValueHolderGetSetSubmittedValueTest
   * 
   * @assertion_ids: JSF:JAVADOC:410; JSF:JAVADOC:426
   * @test_Strategy: Verify {get,set}SubmittedValue() - if a value is set, the
   *                 expected value is returned.
   */
  public void editableValueHolderGetSetSubmittedValueTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "editableValueHolderGetSetSubmittedValueTest");
    invoke();
  }

  /**
   * testName: editableValueHolderIsSetRequiredTest
   * 
   * @assertion_ids: JSF:JAVADOC:417
   * @test_Strategy: Verify {is,set}Required - if a value is set, the expected
   *                 value is returned.
   */
  public void editableValueHolderIsSetRequiredTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "editableValueHolderIsSetRequiredTest");
    invoke();
  }

  /**
   * testName: editableValueHolderAddGetRemoveValueChangeListenerTest
   * 
   * @assertion_ids: JSF:JAVADOC:408; JSF:JAVADOC:420; JSF:JAVADOC:414
   * @test_Strategy: Verify the addition and or removal of ValueChangeListeners
   *                 from the component is property reflected in the value
   *                 returned by getValueChangeListeners().
   */
  public void editableValueHolderAddGetRemoveValueChangeListenerTest()
      throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "editableValueHolderAddGetRemoveValueChangeListenerTest");
    invoke();
  }

  /**
   * testName: editableValueHolderGetSetValueChangeListenerTest
   * 
   * @assertion_ids: JSF:JAVADOC:413; JSF:JAVADOC:429
   * @test_Strategy: editableValueHolder.{set,get}ValueChangeListener - if a
   *                 value is set, the expected value is returned.
   */
  public void editableValueHolderGetSetValueChangeListenerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "editableValueHolderGetSetValueChangeListenerTest");
    invoke();
  }

  /**
   * testName: editableValueHolderAddValidatorNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:407
   * @test_Strategy: Validate that addValidator throws a NullPointerException
   *                 when Validator is null
   */
  public void editableValueHolderAddValidatorNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "editableValueHolderAddValidatorNPETest");
    invoke();
  }

  /**
   * testName: editableValueHolderAddValueChangeListenerNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:409
   * @test_Strategy: Validate that addValueChangeListener throws a
   *                 NullPointerException when ValueChangeListener is null
   */
  public void editableValueHolderAddValueChangeListenerNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "editableValueHolderAddValueChangeListenerNPETest");
    invoke();
  }

  /**
   * testName: editableValueHolderRemoveValueChangeListenerNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:421
   * @test_Strategy: Validate that addValueChangeListener throws a
   *                 NullPointerException when ValueChangeListener is null
   */
  public void editableValueHolderRemoveValueChangeListenerNPETest()
      throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "editableValueHolderRemoveValueChangeListenerNPETest");
    invoke();
  }

  /**
   * testName: editableValueHolderIsSetLocalValueSetTest
   * 
   * @assertion_ids: JSF:JAVADOC:416; JSF:JAVADOC:424
   * @test_Strategy: Validate the following statements. isLocalValueSet() -
   *                 Return the "local value set" state for this component.
   *                 Calls to setValue() automatically reset this property to
   *                 true.
   * 
   *                 setLocalValueSet() - Sets the "local value set" state for
   *                 this component.
   */
  public void editableValueHolderIsSetLocalValueSetTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "editableValueHolderIsSetLocalValueSetTest");
    invoke();
  }

  // ------------------------------------ HTML Component Attributes Test
  /**
   * testName: componentAttributesTest
   * 
   * @assertion_ids: JSF:JAVADOC:488
   * @test_Strategy: Validate the values of the HTML component attributes can be
   *                 set and then obtained.
   */
  public void componentAttributesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "componentAttributesTest");
    invoke();
  }

  // --------------------------- Components that Implement NamingContainer

  /**
   * testName: uiComponentGetContainerClientIdNPETest
   * 
   * @assertion_ids: JSF:JAVADOC:508
   * @test_Strategy: Validate a NullPointerException is thrown when arg is null.
   */
  public void uiComponentGetContainerClientIdNPETest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetContainerClientIdNPETest");
    invoke();
  }

  /**
   * testName: uiComponentGetContainerClientIdTest
   * 
   * @assertion_ids: JSF:JAVADOC:507
   * @test_Strategy: Validate a that we get a non null value return.
   */
  public void uiComponentGetContainerClientIdTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "uiComponentGetContainerClientIdTest");
    invoke();
  }

} // end of URLClient
