/*
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
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet {

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

    private static final long serialVersionUID = -7672096092047821010L;

    @Inject
    public void initialize(Sheep sheep) {
        initializerCalled = sheep != null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().endsWith("Servlet")) {
            testServlet(req, resp);
        } else if (req.getRequestURI().endsWith("ServletListener")) {
            testListener(req, resp);
        } else if (req.getRequestURI().endsWith("TagLibraryListener")) {
            testTagLibraryListener(req, resp);
        } else {
            resp.setStatus(404);
        }
    }

    private void testServlet(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getParameter("test").equals("injection")) {
            // Return 200 if injection into Servlet occurred, 500 otherwise
            resp.setStatus(injectionPerformedCorrectly ? 200 : 500);
        } else if (req.getParameter("test").equals("initializer")) {
            // Return 200 if the initializer was called, 500 otherwise
            resp.setStatus(initCalledAfterInitializer ? 200 : 500);
        } else if (req.getParameter("test").equals("resource")) {
            // Return 200 if the resource was injected before init, 500 otherwise
            resp.setStatus(initCalledAfterResourceInjection ? 200 : 500);
        } else if (req.getParameter("test").equals("ejb")) {
            // Return 200 if the resource was injected before init, 500 otherwise
            resp.setStatus(initCalledAfterEJBResourceInjection ? 200 : 500);
        } else if (req.getParameter("test").equals("persistence")) {
            // Return 200 if the resource was injected before init, 500 otherwise
            resp.setStatus(initCalledAfterPersistenceResourceInjection ? 200 : 500);
        } else {
            resp.setStatus(404);
        }
    }

    private void testListener(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getParameter("test").equals("injection")) {
            // Return 200 if injection into Listener occurred, 500 otherwise
            boolean result = (Boolean) req.getSession().getServletContext().getAttribute("listener.injected");
            resp.setStatus((result) ? 200 : 500);
        } else if (req.getParameter("test").equals("initializer")) {
            // Return 200 if the initializer was called, 500 otherwise
            boolean result = (Boolean) req.getSession().getServletContext().getAttribute("listener.initializer.called");
            resp.setStatus((result) ? 200 : 500);
        } else {
            resp.setStatus(404);
        }
    }

    private void testTagLibraryListener(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getParameter("test").equals("injection")) {
            // Return 200 if injection into TagLibrary Listener occurred, 500 otherwise
            boolean result = (Boolean) req.getSession().getServletContext().getAttribute("tag.library.listener.injected");
            resp.setStatus((result) ? 200 : 500);
        } else if (req.getParameter("test").equals("initializer")) {
            // Return 200 if the initializer was called, 500 otherwise
            boolean result = (Boolean) req.getSession().getServletContext()
                    .getAttribute("tag.library.listener.initializer.called");
            resp.setStatus((result) ? 200 : 500);
        } else {
            resp.setStatus(404);
        }
    }

    @Override
    public void init() throws ServletException {
        injectionPerformedCorrectly = sheep != null;
        initCalledAfterInitializer = initializerCalled;
        initCalledAfterResourceInjection = "Hello".equals(greeting) && "poker".equals(game);
        initCalledAfterEJBResourceInjection = sessionBean.ping();
        initCalledAfterPersistenceResourceInjection = (persistenceContext != null) && (persistenceUnit != null);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        init();
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
