/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.common.client;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.ClientEndpointConfig.Configurator;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.EndpointConfig;
import javax.websocket.HandshakeResponse;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.webclient.TestFailureException;
import com.sun.ts.tests.common.webclient.WebTestCase;
import com.sun.ts.tests.common.webclient.http.HttpRequest;
import com.sun.ts.tests.common.webclient.http.HttpResponse;
import com.sun.ts.tests.common.webclient.validation.ValidationFactory;
import com.sun.ts.tests.common.webclient.validation.ValidationStrategy;
import com.sun.ts.tests.websocket.common.client.ClientEndpoint.ClientEndpointData;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient.Property;

/**
 * <p>
 * Possibly there might be different
 * {@link com.sun.ts.tests.common.webclient.TestCase}s for different technology
 * </p>
 * <p>
 * This one uses existing routines to check result of client-server
 * request-response by websocket technology
 * </p>
 * 
 * TODO Create an instance of endpoint class using reflection with zero arg
 * constructor from the given ClientEndpoint class
 * <li>Then ClientEndpoinData do not need to be static, make it accessible from
 * CientEndpoint instance</li>
 * <li>Do not forget to pass a reference of the endpoint instance to each single
 * callback for ClientEndpointData to be accessible from the callback</li>
 * 
 * @author supol
 */
public class WebSocketTestCase extends WebTestCase {

  /**
   * Apache HTTP response mock
   */
  protected HttpResponse _response;

  /**
   * A reference to WebSocketCommonClient, holder of properties needed to build
   * request
   */
  protected WebSocketCommonClient client;

  /**
   * A reference to ClientEndpoint, a websocket endpoint the messages are passed
   * into it
   */
  protected Class<? extends ClientEndpoint<?>> endpoint = StringClientEndpoint.class;

  /**
   * An Instance to ClientEndpoint, a websocket endpoint the messages are passed
   * into it
   */
  protected ClientEndpoint<?> endpointInstance = null;

  /**
   * An annotated endpoint, in this implementation merely a bridge to
   * ClientEndpoint, to be sure the annotated methods are called and also to
   * have all the EndpointCallbacks working there
   */
  protected AnnotatedClientEndpoint<?> annotatedEndpoint = null;

  /**
   * A single master callback to be used in client end point
   */
  protected volatile EndpointCallback clientCallback = null;

  /*
   * Callbacks to be added to a given master callback or to a default
   * SendMessageCallback master callback if no master callback is provided
   */
  protected List<EndpointCallback> slaveClientCallbacks = null;

  /**
   * Upper case, lower case, or exact text matching?
   */
  protected TextCaser textCaser = TextCaser.NONE;

  /**
   * Strategy to use when validating the test case against the server's
   * response.
   */
  protected ValidationStrategy strategy = null;

  /**
   * The WebSocket Session Object to be addressed from test
   */
  protected volatile Session session;

  /**
   * Count Down Latch Count
   */
  protected int countDownLatchCount = 1;

  /**
   * ClientEndpointConfig to be used
   */
  protected ClientEndpointConfig clientEndpointConfig = null;

  protected List<ClientEndpointConfig.Configurator> configurators = null;

  /**
   * Flag to enable client call logging
   */
  protected boolean printClientCall = false;

  // ---------------------------------------------------------------------
  public WebSocketTestCase(WebSocketCommonClient client) {
    super();
    this.client = client;
    clientEndpointConfig = ClientEndpointConfig.Builder.create().build();
    strategy = ValidationFactory.getInstance(TOKENIZED_STRATEGY);
    configurators = new LinkedList<ClientEndpointConfig.Configurator>();
    slaveClientCallbacks = new LinkedList<EndpointCallback>();
    ClientEndpointData.resetData();
    logTrace("A new test case has been created");
  }

  /**
   * Executes the test case.
   * 
   * @throws TestFailureException
   *           if the test fails for any reason.
   * @throws IllegalStateException
   *           if no request was configured or if no Validator is available at
   *           runtime.
   */
  @Override
  public void execute() throws TestFailureException {
    verifyValidationStrategy();
    verifySettings();
    addAllConfiguratorsToClientEndpointConfig();
    try {
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      String path = client.TEST_PROPS.get(Property.REQUEST);
      if (printClientCall)
        logTrace("\n-----------\n", printClientCall(), "-----------\n");
      logMsg("Connecting to", path);
      if (slaveClientCallbacks.size() != 0)
        clientCallback = createMasterEndpointCallback();
      if (client.entity != null) {
        if (clientCallback != null)
          ClientEndpointData.callback = clientCallback;
        else
          ClientEndpointData.callback = new SendMessageCallback(client.entity);
      }
      client.setupWebSocketContainerBeforeConnect(clientContainer);
      newCountDown();
      session = connectToServer(clientContainer, path);
      awaitCountDown();
    } catch (Throwable t) {
      String message = t.getMessage();

      StringBuilder sb = new StringBuilder();
      sb.append("[FATAL] Unexpected failure during test execution.\n");
      // print client call code to report into JIRA when needed
      if (client.logExceptionOnInvoke)
        sb.append(printClientCall().toString());
      // Inherited message
      sb.append((message == null ? t.toString() : message));

      throw new TestFailureException(sb.toString(), t);
    }
  }

  /**
   * Validate this test case instance
   * 
   * @throws TestFailureException
   */
  protected void validate() throws TestFailureException {
    if (!strategy.validate(this)) {
      throw new TestFailureException("Test FAILED!");
    }
  }

  /**
   * Logging method for internal purposes
   */
  protected StringBuilder printClientCall() {
    StringBuilder sb = new StringBuilder();
    if (client.entity != null && client.entity.isInstance(String.class)) {
      sb.append("class ").append(endpoint.getSimpleName());
      sb.append(" extends Endpoint implements MessageHandler.Whole<String>");
      sb.append(" {\n");
      sb.append(
          "  public void onOpen(Session session, EndpointConfig config) {\n");
      sb.append("    session.addMessageHandler(this);\n");
      sb.append(
          "    RemoteEndpoint.Basic endpoint = session.getBasicRemote();\n");
      sb.append("    try {\n");
      sb.append("      endpoint.sendText(\"");
      sb.append(client.entity.getEntityAt(String.class, 0));
      sb.append("\");\n");
      sb.append("    } catch (IOException e) {\n");
      sb.append("      throw new RuntimeException(e);\n");
      sb.append("    }\n");
      sb.append("  }\n");
      sb.append("  public void onMessage(String message) {\n");
      sb.append("    System.out.println(message);\n");
      sb.append("    messageLatch.countDown()");
      sb.append("  }\n");
      sb.append("};\n");
    }
    sb.append("CountDownLatch messageLatch;\n");
    sb.append("public void test() {\n");
    sb.append("  WebSocketContainer clientContainer = ContainerProvider");
    sb.append(".getWebSocketContainer();\n");
    sb.append("  messageLatch = new CountDownLatch(");
    sb.append(countDownLatchCount).append(");\n");
    sb.append("  Session session = clientContainer.connectToServer(");
    sb.append(endpoint.getSimpleName()).append(".class,\n");
    sb.append("                    ");
    sb.append("ClientEndpointConfig.Builder.create()");
    Configurator configurator = clientEndpointConfig.getConfigurator();
    if (configurator != null)
      sb.append(".config(new ").append(configurator.getClass().getName())
          .append("())");
    sb.append(".build(),\n");
    sb.append("                    ");
    sb.append("new URI(\"").append(client.TEST_PROPS.get(Property.REQUEST));
    sb.append("\"));\n");
    sb.append("  messageLatch.await(").append(client._ws_wait);
    sb.append(", TimeUnit.SECONDS);\n");
    sb.append("}\n");

    return sb;
  }

  protected Session connectToServer(WebSocketContainer clientContainer,
      String path) throws Exception {
    WebSocketCommonClient.assertTrue(
        endpointInstance == null || annotatedEndpoint == null,
        "Either a ClientEndpoint instance or Annotated endpoint can be used, not both");

    if (annotatedEndpoint != null)
      session = clientContainer.connectToServer(annotatedEndpoint,
          new URI(path));
    else if (endpointInstance != null)
      session = clientContainer.connectToServer(endpointInstance,
          clientEndpointConfig, new URI(path));
    else
      session = clientContainer.connectToServer(endpoint, clientEndpointConfig,
          new URI(path));
    logTrace("Connection session id:", session.getId());
    return session;
  }

  protected void addAllConfiguratorsToClientEndpointConfig() {
    if (!configurators.isEmpty()) {
      ClientEndpointConfig.Configurator masterConfigurator = createMasterConfigurator();
      ClientEndpointConfig.Builder builder = cloneConfigWithoutConfigurator();
      builder.configurator(masterConfigurator);
      clientEndpointConfig = builder.build();
    }
  }

  protected ClientEndpointConfig.Builder cloneConfigWithoutConfigurator() {
    ClientEndpointConfig.Builder builder = ClientEndpointConfig.Builder
        .create();
    builder.decoders(clientEndpointConfig.getDecoders());
    builder.encoders(clientEndpointConfig.getEncoders());
    builder.extensions(clientEndpointConfig.getExtensions());
    builder
        .preferredSubprotocols(clientEndpointConfig.getPreferredSubprotocols());
    return builder;
  }

  protected ClientEndpointConfig.Configurator createMasterConfigurator() {
    ClientEndpointConfig.Configurator master = new ClientEndpointConfig.Configurator() {
      final ClientEndpointConfig.Configurator original = clientEndpointConfig
          .getConfigurator();

      @Override
      public void afterResponse(HandshakeResponse hr) {
        if (configurators != null)
          for (ClientEndpointConfig.Configurator configurator : configurators)
            configurator.afterResponse(hr);
        if (original != null)
          original.afterResponse(hr);
      }

      @Override
      public void beforeRequest(Map<String, List<String>> headers) {
        if (configurators != null)
          for (ClientEndpointConfig.Configurator configurator : configurators)
            configurator.beforeRequest(headers);
        if (original != null)
          original.beforeRequest(headers);

        if (true)
          printHeaders(headers);
      }

      void printHeaders(Map<String, List<String>> headers) {
        for (Entry<String, List<String>> set : headers.entrySet())
          logMsg(set.getKey(), ":",
              WebSocketCommonClient.objectsToString(set.getValue().toArray()));
      }
    };
    return master;
  }

  protected EndpointCallback createMasterEndpointCallback() {
    EndpointCallback master = new EndpointCallback() {
      EndpointCallback original = clientCallback == null
          ? new SendMessageCallback(client.entity)
          : clientCallback;

      @Override
      public void onOpen(Session session, EndpointConfig config) {
        original.onOpen(session, config);
        for (EndpointCallback callback : slaveClientCallbacks)
          callback.onOpen(session, config);
      }

      @Override
      public void onMessage(Object o) {
        original.onMessage(o);
        for (EndpointCallback callback : slaveClientCallbacks)
          callback.onMessage(o);
      }

      @Override
      public void onError(Session session, Throwable t) {
        original.onError(session, t);
        for (EndpointCallback callback : slaveClientCallbacks)
          callback.onError(session, t);
      }

      @Override
      public void onClose(Session session, CloseReason closeReason) {
        original.onClose(session, closeReason);
        for (EndpointCallback callback : slaveClientCallbacks)
          callback.onClose(session, closeReason);
      }
    };
    return master;
  }

  protected void verifyValidationStrategy() {
    // If no strategy instance is available (strange, but has happened),
    // fail.
    try {
      getStrategy();
    } catch (NullPointerException e) {
      throw new IllegalStateException("[FATAL] No Validator available.");
    }
  }

  protected void verifySettings() throws TestFailureException {
    if (client.getContextRoot() == null)
      throw new TestFailureException("No resource url request set");
  }

  protected void awaitCountDown() {
    logTrace("Setting CountDownLatch to", client._ws_wait,
        "seconds, should be hit", countDownLatchCount, "times");
    ClientEndpointData.awaitCountDown(client._ws_wait);
  }

  protected void newCountDown() {
    ClientEndpointData.newCountDown(countDownLatchCount);
  }

  // ---------------------------------------------------------------------
  // Apache adaptor methods

  @Override
  public HttpRequest getRequest() {
    if (super.getRequest() == null) {
      super.setRequest(new ApacheRequestAdapter(client.requestProtocol,
          client.TEST_PROPS.get(Property.REQUEST), client._hostname,
          client._port));
    }
    return super.getRequest();
  }

  @Override
  public HttpResponse getResponse() {
    if (_response == null) {
      _response = new ApacheResponseAdapter(client._hostname, client._port,
          textCaser);
    }
    return _response;
  }

  // ----------------------------------------------------------------------
  /**
   * Sets the validation strategy for this test case instance.
   * 
   * @param validator
   *          - the fully qualified class name of the response validator to use.
   */
  @Override
  public void setStrategy(String validator) {
    ValidationStrategy strat = ValidationFactory.getInstance(validator);
    if (strat != null) {
      strategy = strat;
    } else {
      StringBuilder sb = new StringBuilder();
      sb.append("[WebTestCase][WARNING] An attempt was made to use a ");
      sb.append("non-existing validator (");
      sb.append(validator);
      sb.append(").  Falling back to the TokenizedValidator");
      TestUtil.logMsg(sb.toString());
    }
  }

  // --------------------------- Getters & setters --------------------------
  protected TextCaser getTextCaser() {
    return textCaser;
  }

  /**
   * set ClientEndpoint class. This endpoint class can be overriden by client
   * endpoint instance set by {@link #setClientEndpointInstance}
   */
  protected void setClientEndpoint(
      Class<? extends ClientEndpoint<?>> endpoint) {
    this.endpoint = endpoint;
  }

  /**
   * The ClientEndpoint instance. It holds precedence over ClientEndpoint class
   * set by {@link #setClientEndpoint}
   */
  protected void setClientEndpointInstance(ClientEndpoint<?> endpointInstance) {
    this.endpointInstance = endpointInstance;
  }

  /**
   * The annotated client endpoint alternative to ClientEndpoint instance. It
   * has precedence over ClientEndpoint class set by {@link #setClientEndpoint}
   */
  protected void setAnnotatedClientEndpoint(
      AnnotatedClientEndpoint<?> annotatedEndpoint) {
    this.annotatedEndpoint = annotatedEndpoint;
  }

  protected void setCountDownLatchCount(int countDownLatchCount) {
    this.countDownLatchCount = countDownLatchCount;
  }

  protected int getCountDownLatchTotalCount() {
    return countDownLatchCount;
  }

  protected long getCountDownLatchRemainingHits() {
    long hits = ClientEndpointData.getCount();
    return hits;
  }

  protected void setCallback(EndpointCallback callback) {
    this.clientCallback = callback;
  }

  protected void addClientCallback(EndpointCallback callback) {
    this.slaveClientCallbacks.add(callback);
  }

  protected Session getSession() {
    return session;
  }

  protected String getResponseAsString() {
    return ClientEndpoint.getMessageBuilder().toString();
  }

  @SuppressWarnings("unchecked")
  protected <T> T getLastResponse(Class<T> type) {
    return (T) ClientEndpointData.lastMessage;
  }

  protected void //
      setClientEndpointConfig(ClientEndpointConfig clientEndpointConfig) {
    this.clientEndpointConfig = clientEndpointConfig;
  }

  protected void addClientConfigurator(
      ClientEndpointConfig.Configurator config) {
    this.configurators.add(config);
  }

  protected void printClientCall(boolean printClientCall) {
    this.printClientCall = printClientCall;
  }

  static void logMsg(Object... msg) {
    WebSocketCommonClient.logMsg("[WebSocketCommonClient]", msg);
  }

  static void logTrace(Object... msg) {
    WebSocketCommonClient.logTrace("[WebSocketCommonClient]", msg);
  }

  protected ClientEndpointConfig getEndpointConfig() {
    return clientEndpointConfig;
  }
}
