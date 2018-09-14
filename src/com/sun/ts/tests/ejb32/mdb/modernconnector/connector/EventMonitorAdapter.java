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

package com.sun.ts.tests.ejb32.mdb.modernconnector.connector;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;

public class EventMonitorAdapter implements ResourceAdapter {

  private Map<EventMonitorConfig, ActivatedEndpoint> endpoints = new HashMap<EventMonitorConfig, ActivatedEndpoint>();

  public void start(BootstrapContext bootstrapContext)
      throws ResourceAdapterInternalException {
  }

  public void stop() {
  }

  public void endpointActivation(MessageEndpointFactory messageEndpointFactory,
      ActivationSpec activationSpec) throws ResourceException {
    final EventMonitorConfig config = (EventMonitorConfig) activationSpec;
    final ActivatedEndpoint activatedEndpoint = new ActivatedEndpoint(
        messageEndpointFactory, config);
    endpoints.put(config, activatedEndpoint);
    final Thread thread = new Thread(activatedEndpoint);
    thread.setDaemon(true);
    thread.start();
  }

  public void endpointDeactivation(
      MessageEndpointFactory messageEndpointFactory,
      ActivationSpec activationSpec) {
    endpoints.remove(activationSpec);
  }

  public XAResource[] getXAResources(ActivationSpec[] activationSpecs)
      throws ResourceException {
    return new XAResource[0];
  }

  private static class ActivatedEndpoint implements Runnable {

    private final MessageEndpointFactory factory;

    private final EventMonitorConfig config;

    private final Map<String, Method> eventConsumers = new HashMap<String, Method>();

    private ActivatedEndpoint(MessageEndpointFactory factory,
        EventMonitorConfig config) {
      this.factory = factory;
      this.config = config;
      final Method[] methods = factory.getEndpointClass().getMethods();
      for (Method method : methods) {
        if (method.isAnnotationPresent(EventMonitor.class)) {
          eventConsumers
              .put(method.getAnnotation(EventMonitor.class).priority(), method);
        }
      }

    }

    /**
     * Have to wait till the application is fully started
     */
    private static void pause() {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        Thread.interrupted();
      }
    }

    private void produceEvent(MessageEndpoint endpoint, String priority,
        String event) throws Exception {
      Method consumer = eventConsumers.get(priority);
      endpoint.beforeDelivery(consumer);
      try {
        consumer.invoke(endpoint, event);
      } finally {
        endpoint.afterDelivery();
      }

    }

    public void run() {
      pause();
      try {
        final MessageEndpoint endpoint = factory.createEndpoint(null);
        try {
          produceEvent(endpoint, "high",
              "One " + config.getCategory() + " typed high-priority event");
          produceEvent(endpoint, "normal",
              "One " + config.getCategory() + " typed normal-priority event");
          produceEvent(endpoint, "low",
              "One " + config.getCategory() + " typed low-priority event");
        } finally {
          endpoint.release();
        }
      } catch (Throwable e) {
        e.printStackTrace();
        // fail
      }
    }
  }

  /*
   * @name equals
   * 
   * @desc Compares the given object to the ManagedConnectionFactory instance.
   * 
   * @param Object
   * 
   * @return boolean
   */
  public boolean equals(Object obj) {

    if ((obj == null) || !(obj instanceof EventMonitorAdapter)) {
      return false;
    }
    if (obj == this) {
      return true;
    }

    EventMonitorAdapter that = (EventMonitorAdapter) obj;

    return true;
  }

  /*
   * @name hashCode
   * 
   * @desc Gives a hash value to a ManagedConnectionFactory Obejct.
   * 
   * @return int
   */
  public int hashCode() {
    return this.getClass().getName().hashCode();
  }

}
