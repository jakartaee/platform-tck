/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.implementation.simple.ee.components;

import java.io.IOException;
import java.util.UUID;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jboss.cdi.tck.tests.implementation.simple.ee.components.Tame.TameLiteral;
import org.jboss.cdi.tck.tests.implementation.simple.ee.components.Wild.WildLiteral;
import org.jboss.cdi.tck.util.ActionSequence;

/**
 * This servlet is also registered as dependent bean with producer method, field, observer method and disposer method.
 */
@SuppressWarnings("serial")
@WebServlet("/test")
@Dependent
public class IntrospectServlet extends HttpServlet {

    public static final String MODE_INJECT = "inject";

    public static final String MODE_PRODUCER_METHOD = "pm";

    public static final String MODE_PRODUCER_FIELD = "pf";

    public static final String MODE_DISPOSER_METHOD = "dm";

    public static final String MODE_OBSERVER_METHOD = "om";

    private String id = null;

    @Inject
    @Any
    Instance<Ping> pingInstance;

    @Inject
    @Any
    Event<Ping> pingEvent;

    @Produces
    @Wild
    @Dependent
    Ping ping;

    @PostConstruct
    public void init() {
        // Both, servlet component and CDI bean support PostConstruct callback
        id = UUID.randomUUID().toString();
        // Init producer field
        ping = new Ping(id);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/text");
        String mode = req.getParameter("mode");

        if (MODE_INJECT.equals(mode)) {
            resp.getWriter().println(pingInstance.select(Default.Literal.INSTANCE).get().pong() + ":" + getId());
        } else if (MODE_PRODUCER_METHOD.equals(mode)) {
            resp.getWriter().println(pingInstance.select(TameLiteral.INSTANCE).get().getTestServletId() + ":" + getId());
        } else if (MODE_PRODUCER_FIELD.equals(mode)) {
            resp.getWriter().println(pingInstance.select(WildLiteral.INSTANCE).get().getTestServletId() + ":" + getId());
        } else if (MODE_OBSERVER_METHOD.equals(mode)) {
            pingEvent.select(Default.Literal.INSTANCE).fire(new Ping());
            resp.getWriter().println(ActionSequence.getSequenceData().get(0) + ":" + getId());
        } else if (MODE_DISPOSER_METHOD.equals(mode)) {
            pingEvent.select(WildLiteral.INSTANCE).fire(new Ping());
            resp.getWriter().println(ActionSequence.getSequenceData().get(0) + ":" + getId());
        } else {
            throw new ServletException("Unknown mode");
        }
    }

    @Tame
    @Produces
    @Dependent
    public Ping procudeTamePing() {
        return new Ping(getId());
    }

    public void disposeTamePing(@Disposes @Tame Ping ping) {
        storeCdiBeanId();
    }

    public void observePing(@Observes Ping ping) {
        storeCdiBeanId();
    }

    /**
     *
     * @param ping
     * @param tamePing Destroyed after observer method invocation
     */
    public void observeWildPing(@Observes @Wild Ping ping, @Tame Ping tamePing) {
        // Simple auxiliary observer to test the disposer method
    }

    /**
     * @return id of servlet component or CDI bean
     */
    public String getId() {
        return id;
    }

    private void storeCdiBeanId() {
        ActionSequence.reset();
        ActionSequence.addAction(this.id);
    }

}
