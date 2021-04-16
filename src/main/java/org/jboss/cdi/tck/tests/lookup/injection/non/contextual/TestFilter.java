/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.cdi.tck.tests.lookup.injection.non.contextual;

import java.io.IOException;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

public class TestFilter implements Filter {

    @Inject
    private Sheep sheep;

    @Resource(name = "greeting")
    String greeting;

    String game;

    SessionBean sessionBean;

    EntityManager persistenceContext;

    EntityManagerFactory persistenceUnit;

    private boolean injectionPerformedCorrectly = false;
    private boolean initializerCalled = false;
    private boolean initCalledAfterInitializer = false;
    private boolean initCalledAfterResourceInjection = false;
    private boolean initCalledAfterEJBResourceInjection = false;
    private boolean initCalledAfterPersistenceResourceInjection = false;

    @Inject
    public void initialize(Sheep sheep) {
        initializerCalled = sheep != null;
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;

        if (request.getParameter("test").equals("injection")) {
            // Return 200 if injection into Filter occurred, 500 otherwise
            resp.setStatus(injectionPerformedCorrectly ? 200 : 500);
        } else if (request.getParameter("test").equals("initializer")) {
            // Return 200 if the initializer was called, 500 otherwise
            resp.setStatus(initCalledAfterInitializer ? 200 : 500);
        } else if (request.getParameter("test").equals("resource")) {
            // Return 200 if the resource was injected before init, 500 otherwise
            resp.setStatus(initCalledAfterResourceInjection ? 200 : 500);
        } else if (request.getParameter("test").equals("ejb")) {
            // Return 200 if the resource was injected before init, 500 otherwise
            resp.setStatus(initCalledAfterEJBResourceInjection ? 200 : 500);
        } else if (request.getParameter("test").equals("persistence")) {
            // Return 200 if the resource was injected before init, 500 otherwise
            resp.setStatus(initCalledAfterPersistenceResourceInjection ? 200 : 500);
        } else {
            resp.setStatus(404);
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        injectionPerformedCorrectly = sheep != null;
        initCalledAfterInitializer = initializerCalled;
        initCalledAfterResourceInjection = "Hello".equals(greeting) && "poker".equals(game);
        initCalledAfterEJBResourceInjection = sessionBean.ping();
        initCalledAfterPersistenceResourceInjection = (persistenceContext != null) && (persistenceUnit != null);
    }

    @Resource(name = "game")
    private void setGame(String game) {
        this.game = game;
    }

    @EJB
    private void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    @PersistenceContext
    private void setPersistenceContext(EntityManager persistenceContext) {
        this.persistenceContext = persistenceContext;
    }

    @PersistenceUnit
    private void setPersistenceUnit(EntityManagerFactory persistenceUnit) {
        this.persistenceUnit = persistenceUnit;
    }
}
