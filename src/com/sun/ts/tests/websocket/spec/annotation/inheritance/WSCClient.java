/*
 * Copyright (c) 2014, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.spec.annotation.inheritance;

import javax.websocket.DeploymentException;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
/**
 * The Spec is a bit fuzzy about how the annotation should work. But the proper
 * meaning should be that it works the way java se states, i.e. annotation
 * inheritance from java language specification.
 * <p/>
 * <ul>
 * <li>So first if the super class has the annotation then the subclass has it,
 * too.</li>
 * <li>Second, if super class has the annotation and the subclass override the
 * method, the annotation is gone</li>
 * <li>Third, most tricky, if the annotation is on superclass not overridden in
 * subclass and annotation is put to some other method in subclass, the subclass
 * has the annotation twice, first from superclass and second from subclass and
 * the class should not deploy, i.e. DeploymentException is thrown as stated by
 * WSC-4.8.1, Section 4.8 WebSockets and Inheritance, WebSocket
 * Specification</li>
 * </ul>
 * This has proven to be incorrectly understood by various implementing teams,
 * but after several discussions, the common agreement is the java se behavior.
 */
public class WSCClient extends WebSocketCommonClient {

  private static final long serialVersionUID = 3037319902828702665L;

  protected final static String MESSAGE = "message";

  public WSCClient() {
    setContextRoot("wsc_spec_annotation_inheritance_web");
  }

  public static void main(String[] args) {
    new WSCClient().run(args);
  }

  /*
   * @testName: annotationOnMethodIsInheritedTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.8.1;
   * 
   * @test_Strategy: The subclass just inherits @OnMessage
   */
  public void annotationOnMethodIsInheritedTest() throws Fault {
    setAnnotatedClientEndpoint(
        new AnnotatedSubclassEndpointWithoutAnnotations());
    invoke("echo", MESSAGE, MESSAGE);
    logMsg("Annotations have been inherited as expected");
  }

  /*
   * @testName: annotationOnMethodIsOverridenTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.8.1;
   * 
   * @test_Strategy: The subclass overrides @OnMessage
   */
  public void annotationOnMethodIsOverridenTest() throws Fault {
    setAnnotatedClientEndpoint(
        new AnnotatedClientEndpointSubclassWithOverrideAndAnnotations());
    invoke("echo", MESSAGE, MESSAGE);
    logMsg("Annotations have been overriden as expected");
  }

  /*
   * @testName: annotationOnMessageIsTwiceTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.8.1;
   * 
   * @test_Strategy: The subclass defines its own @OnMessage
   * 
   * Implementations should not deploy Java classes that mistakenly mix Java
   * inheritance with websocket annotations in these ways.
   */
  public void annotationOnMessageIsTwiceTest() throws Fault {
    try {
      setAnnotatedClientEndpoint(
          new AnnotatedClientEndpointWithMultipleOnMessageAnnotations());
      logExceptionOnInvocation(false);
      invoke("echo", MESSAGE, MESSAGE);
      fault(
          "No exception has been thrown when multiple @OnMessage annotations defined");
    } catch (Exception e) {
      DeploymentException de = assertCause(e, DeploymentException.class,
          "DeploymentException has not been thrown when multiple @OnMessage annotations");
      logMsg("DeploymentException has been thrown as expected:",
          de.getMessage());
    }
  }

  /*
   * @testName: annotationOnOpenIsTwiceTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.8.1;
   * 
   * @test_Strategy: The subclass defines its own @OnOpen
   * 
   * Implementations should not deploy Java classes that mistakenly mix Java
   * inheritance with websocket annotations in these ways.
   */
  public void annotationOnOpenIsTwiceTest() throws Fault {
    try {
      setAnnotatedClientEndpoint(
          new AnnotatedClientEndpointWithMultipleOnOpenAnnotations());
      logExceptionOnInvocation(false);
      invoke("echo", MESSAGE, MESSAGE);
      fault(
          "No exception has been thrown when multiple @OnOpen annotations defined");
    } catch (Exception e) {
      DeploymentException de = assertCause(e, DeploymentException.class,
          "DeploymentException has not been thrown when multiple @OnOpen annotations");
      logMsg("DeploymentException has been thrown as expected:", de);
    }
  }

  /*
   * @testName: annotationOnCloseIsTwiceTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.8.1;
   * 
   * @test_Strategy: The subclass defines its own @OnClose
   * 
   * Implementations should not deploy Java classes that mistakenly mix Java
   * inheritance with websocket annotations in these ways.
   */
  public void annotationOnCloseIsTwiceTest() throws Fault {
    try {
      setAnnotatedClientEndpoint(
          new AnnotatedClientEndpointWithMultipleOnCloseAnnotations());
      logExceptionOnInvocation(false);
      invoke("echo", MESSAGE, MESSAGE);
      fault(
          "No exception has been thrown when multiple @OnClose annotations defined");
    } catch (Exception e) {
      DeploymentException de = assertCause(e, DeploymentException.class,
          "DeploymentException has not been thrown when multiple @OnClose annotations");
      logMsg("DeploymentException has been thrown as expected:",
          de.getMessage());
    }
  }

  /*
   * @testName: annotationOnErrorIsTwiceTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.8.1;
   * 
   * @test_Strategy: The subclass defines its own @OnError
   * 
   * Implementations should not deploy Java classes that mistakenly mix Java
   * inheritance with websocket annotations in these ways.
   */
  public void annotationOnErrorIsTwiceTest() throws Fault {
    try {
      setAnnotatedClientEndpoint(
          new AnnotatedClientEndpointWithMultipleOnErrorAnnotations());
      logExceptionOnInvocation(false);
      invoke("echo", MESSAGE, MESSAGE);
      fault(
          "No exception has been thrown when multiple @OnError annotations defined");
    } catch (Exception e) {
      DeploymentException de = assertCause(e, DeploymentException.class,
          "DeploymentException has not been thrown when multiple @OnError annotations");
      logMsg("DeploymentException has been thrown as expected:",
          de.getMessage());
    }
  }
}
