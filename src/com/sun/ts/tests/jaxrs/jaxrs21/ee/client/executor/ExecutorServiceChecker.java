/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.jaxrs21.ee.client.executor;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.core.Response;

import com.sun.ts.tests.jaxrs.ee.rs.core.request.JAXRSClient;

public interface ExecutorServiceChecker extends Closeable {
  final static String THREADPREFIX = "JAXRS_TCK_THREAD";

  static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
    AtomicInteger ai = new AtomicInteger();

    @Override
    public Thread newThread(Runnable r) {
      return new Thread(r, THREADPREFIX + ai.incrementAndGet());
    }
  };

  final static ExecutorService EXECUTOR_SERVICE = Executors
      .newFixedThreadPool(5, THREAD_FACTORY);

  default Client createClient() {
    Client c = ClientBuilder.newBuilder().executorService(EXECUTOR_SERVICE)
        .build();
    c.register(threadNameChecker());
    return c;
  }

  default ClientRequestFilter threadNameChecker() {
    return new ClientRequestFilter() {
      @Override
      public void filter(ClientRequestContext requestContext)
          throws IOException {
        Thread t = Thread.currentThread();
        if (!t.getName().startsWith(THREADPREFIX))
          requestContext.abortWith(Response.notAcceptable(null)
              .entity("ThreadExecutor check failed").build());
        JAXRSClient.logMsg(
            "[Client EXECUTOR SERVICE check]: running from thread",
            t.getName());
      }
    };
  }

  default void close() throws IOException {
    EXECUTOR_SERVICE.shutdown();
  }
}
